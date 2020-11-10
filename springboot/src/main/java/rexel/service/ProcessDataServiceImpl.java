package rexel.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import java.net.URI;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import rexel.common.Constants;
import rexel.common.Utils;
import rexel.config.PropertiesConfig;
import rexel.entity.AccessTokenEntity;
import rexel.entity.CollectSwitchEntity;
import rexel.entity.ProcessDataEntity;
import rexel.repository.AccessTokenRepository;
import rexel.repository.CollectSwitchRepository;
import rexel.repository.ProcessDataRepository;
import rexel.rest.ProcessBean;
import rexel.rest.ProcessRootBean;
import rexel.rest.ResultBean;
import rexel.rest.TokenBean;

@Transactional
@Service
@Slf4j
public class ProcessDataServiceImpl implements ProcessDataService {
    private AccessTokenRepository accessTokenRepository;
    private ProcessDataRepository processDataRepository;
    private CollectSwitchRepository collectSwitchRepository;
    private RestTemplate restTemplate;
    private PropertiesConfig propertiesConfig;
    private Utils utils;
    private String token = null;
    private String deviceUnique;

    @Autowired
    public void setAccessTokenRepository(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Autowired
    public void setProcessDataRepository(ProcessDataRepository processDataRepository) {
        this.processDataRepository = processDataRepository;
    }

    @Autowired
    public void setCollectSwitchRepository(CollectSwitchRepository collectSwitchRepository) {
        this.collectSwitchRepository = collectSwitchRepository;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setPropertiesConfig(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }

    @Autowired
    public void setUtils(Utils utils) {
        this.utils = utils;
    }

    /**
     * Web接口服务（查询数据）
     * @param jsonObject 查询条件
     * @param page 页号
     * @param size 条数
     * @return 数据集
     */
    @Override
    public JSONObject findProcess(JSONObject jsonObject, int page, int size) {
        //获取统计结果数据
        JSONObject summary = getSummaryJson(jsonObject);

        //获取指定页表单数据
        JSONArray content = getContentArray(jsonObject, page, size);

        //生成应答Json
        JSONObject result = utils.responseJson(Constants.RESPONSE_OK, "succeeded");
        result.put("summary", summary);
        result.put("content", content);
        log.debug(result.toString());

        return result;
    }

    /**
     * Web接口服务（向平台提交数据）
     * @param jsonArray 需要提交的数据集
     * @throws Exception e
     */
    @Override
    public JSONObject commitProcess(JSONArray jsonArray) throws Exception {
        SpecificationForCommit specification = new SpecificationForCommit(jsonArray);
        List<ProcessDataEntity> entityList = processDataRepository.findAll(specification);
        if (entityList == null || entityList.size() <= 0) {
            return utils.responseJson(
                Constants.RESPONSE_OK, "succeeded. but have no data to commit.");
        }

        //取得现在持有的Token
        String token = getHoldToken();
        log.debug("token=" + token);

        //无法获取Token
        if (token == null || token.isEmpty()) {
            throw new Exception("[REXEL]get hold token exception.");
        }

        //尝试提交数据
        ResultBean result = commitData(entityList, token);

        //Token失效则重新尝试提交
        if (Constants.RESPONSE_TOKEN_INVALID == result.getCode()) {
            result = recommitDate(entityList);
        }

        //应答结果异常
        if (Constants.RESPONSE_OK != result.getCode()) {
            throw new Exception("[REXEL]response code is not ok. code=");
        }

        //更新数据状态
        batchUpdate(entityList);

        return utils.responseJson(Constants.RESPONSE_OK, "succeeded.");
    }

    /**
     * Schedule接口服务（删除过期数据）
     */
    @Override
    public JSONObject deleteProcess() {
        //当前日期的前一个月
        String deleteDate = utils.minusMonth(new Date(System.currentTimeMillis()));

        //执行删除处理
        processDataRepository.deleteBy(deleteDate);

        return utils.responseJson(Constants.RESPONSE_OK, "succeeded.");
    }

    /**
     * Schedule接口服务（获取待加工数据）
     */
    @Override
    public JSONObject pullProcess() throws Exception {
        //检查依赖注入
        if (processDataRepository == null || restTemplate == null) {
            throw new Exception("[REXEL]Autowired exception.");
        }

        //获取设备唯一标识
        deviceUnique = utils.getDeviceUnique();

        //检查连接开关是否打开
        if (!checkCollectSwitch()) {
            log.info("[REXEL]The connection has been disconnected through the web client.");
            return utils.responseJson(Constants.RESPONSE_OK, "succeeded.");
        }

        //取得现在持有的Token
        String token = getHoldToken();
        log.debug("token=" + token);

        //无法获取Token
        if (token == null || token.isEmpty()) {
            throw new Exception("[REXEL]get hold token exception.");
        }

        //获取待加工数据
        ProcessRootBean dataBean = getProcessData(token);

        //无效令牌
        if (Constants.RESPONSE_TOKEN_INVALID == dataBean.getCode()) {
            //更新token，重新获取待加工数据
            dataBean = retryProcessData();
        }

        //应答结果异常
        if (Constants.RESPONSE_OK != dataBean.getCode()) {
            throw new Exception("[REXEL]response code is not ok. code=");
        }

        //批量插入数据
        batchInsert(dataBean);

        return utils.responseJson(Constants.RESPONSE_OK, "succeeded.");
    }

    /**
     * 获取Web页面统计区域的数据
     * @param jsonObject 检索条件
     * @return 统计数据
     */
    private JSONObject getSummaryJson(JSONObject jsonObject) {
        SpecificationForPull specification = new SpecificationForPull(jsonObject);
        List<ProcessDataEntity> entityList = processDataRepository.findAll(specification);

        int totalPiece = 0;
        float totalThickness = 0;
        float totalWidth = 0;
        float totalLength = 0;
        int totalFilm = 0;
        int totalTodo = 0;
        int totalDoing = 0;
        int totalDone = 0;
        for (ProcessDataEntity entity : entityList) {
            totalPiece += entity.getCnt();
            totalThickness += entity.getThickness();
            totalWidth += entity.getWidth();
            totalLength += entity.getLength();
            totalFilm += entity.getIsFilm();
            int status = entity.getStatus();
            if (status == 0) {
                totalTodo += 1;
            } else if (status == 1) {
                totalDoing += 1;
            } else {
                totalDone += 1;
            }
        }

        JSONObject summary = new JSONObject();
        summary.put("totalOrders", entityList.size());
        summary.put("totalPiece", totalPiece);
        summary.put("totalThickness", totalThickness);
        summary.put("totalWidth", totalWidth);
        summary.put("totalLength", totalLength);
        summary.put("totalFilm", totalFilm);
        summary.put("totalTodo", totalTodo);
        summary.put("totalDoing", totalDoing);
        summary.put("totalDone", totalDone);

        return summary;
    }

    /**
     * 获取Web页面的表单
     * @param jsonObject 检索条件
     * @param page 页号
     * @param size 条数
     * @return 表单数据
     */
    private JSONArray getContentArray(JSONObject jsonObject, int page, int size) {
        //设置排序及分页
        Order order = new Sort.Order(Sort.Direction.DESC, "insertTime");
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        //设置检索条件
        SpecificationForPull specification = new SpecificationForPull(jsonObject);

        //执行检索处理
        Page<ProcessDataEntity> pages = processDataRepository.findAll(specification, pageable);

        List<ProcessDataEntity> entityList = pages.toList();
        JSONArray content = new JSONArray();
        for (ProcessDataEntity entity : entityList) {
            content.add(JSONObject.toJSON(entity));
        }

        return content;
    }

    /**
     * 重新提交数据处理
     * @param entityList 待加工数据列表
     * @return 应答结果
     * @throws Exception e
     */
    private ResultBean recommitDate(List<ProcessDataEntity> entityList) throws Exception {
        String token = refreshToken();
        return commitData(entityList, token);
    }

    /**
     * 提交数据处理
     * @param entityList 待加工数据列表
     * @param token 访问密钥
     * @return 应答结果
     * @throws Exception e
     */
    private ResultBean commitData(
        List<ProcessDataEntity> entityList, String token) throws Exception {
        //设置请求Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, token);

        //设置请求Body
        JSONArray jsonArray = new JSONArray();
        for(ProcessDataEntity entity : entityList) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", entity.getId());
            jsonObj.put("cnt", entity.getCnt());
            jsonObj.put("aluGrade", entity.getAluGrade());
            jsonObj.put("aluState", entity.getAluState());
            jsonObj.put("thickness", entity.getThickness());
            jsonObj.put("width", entity.getWidth());
            jsonObj.put("length", entity.getLength());
            jsonObj.put("isFilm", entity.getIsFilm());
            jsonArray.add(jsonObj);
        }

        //调用接口获取新的Token
        URI uri = makeUri(Constants.OPEN_API_POST_STATE);
        JSONObject result = exchange(uri, new HttpEntity<>(jsonArray.toString(), headers));
        return new ResultBean(result);
    }

    /**
     * 检查连接开关是否打开
     * @return false：关闭、true：打开
     */
    private boolean checkCollectSwitch() {
        List<CollectSwitchEntity> list = collectSwitchRepository.findAll();
        if (list == null || list.size() != 1) {
            return false;
        }
        return list.get(0).getCollect() != 0;
    }

    /**
     * 获取目前持有的访问令牌
     * @return 访问令牌
     * @throws Exception e
     */
    private String getHoldToken() throws Exception {
        if (this.token != null) {
            return this.token;
        }

        AccessTokenEntity entity = new AccessTokenEntity();
        entity.setAccessId(propertiesConfig.getAccessId());
        entity.setAccessKey(propertiesConfig.getAccessKey());
        entity.setDeviceId(deviceUnique);

        //从数据库中取得持有的Token
        List<String> tokenList = accessTokenRepository.selectByKey(entity);

        //如果能够获取Token
        if (tokenList.size() >= 1) {
            this.token = tokenList.get(0);
            return this.token;
        }

        //获取一个新的Token
        return refreshToken();
    }

    /**
     * 刷新访问令牌
     * @return 访问令牌
     * @throws Exception e
     */
    private String refreshToken() throws Exception {
        String accessId = propertiesConfig.getAccessId();
        String accessKey = propertiesConfig.getAccessKey();
        String deviceId = deviceUnique;

        //设置请求Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //设置请求Body
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("accessId", accessId);
        jsonObj.put("accessKey", accessKey);
        jsonObj.put("deviceId", deviceId);
        String body = jsonObj.toString();

        //调用接口获取新的Token
        URI uri = makeUri(Constants.OPEN_API_GET_TOKEN);
        JSONObject jsonObject = exchange(uri, new HttpEntity<>(body, headers));
        if (jsonObject == null) {
            return null;
        }

        //转换为Bean形式
        TokenBean tokenBean = new TokenBean(jsonObject);
        if (Constants.RESPONSE_OK != tokenBean.getCode()) {
            throw new Exception(String.format(
                "[REXEL]get tocken exception. code=%s, msg=%s, json=%s",
                tokenBean.getCode(), tokenBean.getMsg(), jsonObj.toString()));
        }
        String token = tokenBean.getToken();

        //将新的Token持久化至数据库
        AccessTokenEntity saveEntity = new AccessTokenEntity();
        saveEntity.setAccessId(accessId);
        saveEntity.setAccessKey(accessKey);
        saveEntity.setDeviceId(deviceId);
        saveEntity.setToken(token);
        saveEntity.setInsertTime(Timestamp.valueOf(LocalDateTime.now()));
        AccessTokenEntity result = accessTokenRepository.save(saveEntity);
        if (result == null) {
            throw new Exception(String.format(
                "[REXEL]Save token to database exception. entity=%s", saveEntity.toString()));
        }

        log.info("[REXEL]token is refreshed.");
        this.token = token;

        return token;
    }

    /**
     * 获取待加工数据
     * @param token 访问令牌
     * @return 待加工数据
     * @throws Exception e
     */
    private ProcessRootBean getProcessData(String token) throws Exception {
        //设置请求Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add(HttpHeaders.AUTHORIZATION, token);

        //调用接口并将结果转换为JSON格式
        URI uri = makeUri(Constants.OPEN_API_GET_QUERY);
        JSONObject jsonObject = exchange(uri, new HttpEntity<>(null, headers));

        //转换为Bean格式
        return new ProcessRootBean(jsonObject);
    }

    /**
     * 重新获取待加工数据
     * @return 待加工数据
     * @throws Exception e
     */
    private ProcessRootBean retryProcessData() throws Exception {
        //获取新的Token
        String token = refreshToken();
        //重新尝试获取待加工数据
        return getProcessData(token);
    }

    /**
     * 批量插入数据库
     * @param bean 待加工数据
     * @throws Exception e
     */
    private void batchInsert(ProcessRootBean bean) throws Exception {
        //取得待加工数据
        List<ProcessBean> processBeans = bean.getData();
        if (processBeans == null) {
            throw new Exception("[REXEL]Response data is null.");
        }

        //转换为插入用的Entity
        for(ProcessBean processBean : processBeans) {
            ProcessDataEntity processedData = makeInsertEntity(processBean);
            processDataRepository.insert(processedData);
        }
    }

    /**
     * 批量插入数据库
     * @param entityList 待加工数据
     */
    private void batchUpdate(List<ProcessDataEntity> entityList) {
        int count = 0;
        for(ProcessDataEntity entity : entityList) {
            count += processDataRepository.updateBy(entity.getAutoId(), entity.getId());
        }
        log.info(String.valueOf(count));
    }

    /**
     * 生成数据库用的Entity
     * @param bean 待加工数据
     * @return ProcessDataEntity
     */
    private ProcessDataEntity makeInsertEntity(ProcessBean bean) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());

        ProcessDataEntity entity = new ProcessDataEntity();
        //订单标识
        entity.setId(bean.getId());
        //设备唯一标识
        entity.setDeviceUnique(deviceUnique);
        //接入时间
        entity.setCollectDate(utils.dateToStr(new Date(System.currentTimeMillis())));
        //板的牌号
        entity.setAluGrade(bean.getAluGrade());
        //板的合金状态
        entity.setAluState(bean.getAluState());
        //板的厚度
        entity.setThickness(bean.getThickness());
        //板的宽度
        entity.setWidth(bean.getWidth());
        //板的长度
        entity.setLength(bean.getLength());
        //板是否覆膜（1:覆膜、0:不覆膜）
        entity.setIsFilm(bean.getIsFilm());
        //板的张数
        entity.setCnt(bean.getCnt());
        //加工状态（0:待加工、1：加工中、2：已完成）
        entity.setStatus(Constants.PROCESS_STATUS_TODO);
        //插入时间
        entity.setInsertTime(timestamp);
        //插入用户
        entity.setInsertUser(Constants.REXEL_USER_ID);
        //最后一次更新时间
        entity.setUpdateTime(timestamp);
        //最后一次更新用户
        entity.setUpdateUser(Constants.REXEL_USER_ID);

        return entity;
    }

    /**
     * 生成URI
     * @param api 接口
     * @return URI
     */
    private URI makeUri(String api) {
        String uriStr = utils.makeUriString(api);
        return UriComponentsBuilder.fromUriString(uriStr).build(false).toUri();
    }

    /**
     * 发起REST请求
     * @param uri URI
     * @param requestEntity HttpEntity
     * @return 应答结果
     * @throws Exception e
     */
    private JSONObject exchange(URI uri, HttpEntity<String> requestEntity) throws Exception {
        //发起请求
        ResponseEntity<String> results =
            restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        //获取应答数据
        String responseBody = results.getBody();
        if (responseBody == null || responseBody.isEmpty()) {
            throw new Exception("[REXEL]Response body is null.");
        }

        //转换为JSON格式
        JSONObject jsonObject = JSON.parseObject(results.getBody());
        if (jsonObject == null) {
            throw new Exception("[REXEL]JSONObject parse exception.");
        }

        return jsonObject;
    }

    /**
     * 提交数据的检索条件
     */
    private class SpecificationForCommit implements Specification<ProcessDataEntity> {
        private JSONArray idList;

        SpecificationForCommit(JSONArray idList) {
            this.idList = idList;
        }

        @Override
        public Predicate toPredicate(Root<ProcessDataEntity> root, CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            //and id in (?, ?, ?)
            Path<Object> path = root.get("id");
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
            for (int i = 0; i < idList.size(); i++) {
                in.value(idList.getString(i));
            }
            predicates.add(in);

            Predicate[] p = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(p));
        }
    }

    /**
     * 获取待加工数据的检索条件
     */
    private class SpecificationForPull implements Specification<ProcessDataEntity> {
        private JSONObject jsonObject;

        SpecificationForPull(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        public Predicate toPredicate(Root<ProcessDataEntity> root, CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder) {
            String orderId = jsonObject.getString("id");
            String startDate = utils.toSampleDate(jsonObject.getString("startDate"));
            String endDate = utils.toSampleDate(jsonObject.getString("endDate"));
            JSONArray status = jsonObject.getJSONArray("status");

            List<Predicate> predicates = new ArrayList<>();
            //id = orderId
            if (StringUtils.isNotEmpty(orderId)) {
                predicates.add(criteriaBuilder.equal(root.get("id").as(String.class), orderId));
            }

            //and collectDate >= startDate
            if (StringUtils.isNotEmpty(startDate)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("collectDate").as(String.class), startDate));
            }

            //and collectDate <= endDate
            if (StringUtils.isNotEmpty(endDate)) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("collectDate").as(String.class), endDate));
            }

            //and status in (?, ?, ?)
            if (status != null && status.size() > 0) {
                Path<Object> path = root.get("status");
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(path);
                for (int i = 0; i < status.size(); i++) {
                    in.value(status.getInteger(i));
                }
                predicates.add(in);
            }

            Predicate[] p = new Predicate[predicates.size()];
            return criteriaBuilder.and(predicates.toArray(p));
        }
    }
}