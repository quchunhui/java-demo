package rexel.service.impl;

import com.alibaba.fastjson.JSONObject;
import rexel.constants.URLS;
import rexel.pojo.ResultBean;
import rexel.service.IDeviceManager;
import rexel.utils.CommonUtils;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DeviceManagerImpl implements IDeviceManager {
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResultBean getDeviceTypes(String token) throws Exception {
        ResultBean resultBean = new ResultBean();

        // 请求头
        HttpHeaders headers = CommonUtils.initHeader(token);
        URI uri = new URIBuilder(URLS.DEVICE_TYPES).build();
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        // 发起请求
        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        HttpStatus httpStatus = entity.getStatusCode();

        // 其他异常
        if (HttpStatus.OK.value() != httpStatus.value()) {
            log.error("HttpStatus is not ok. HttpStatus=" + httpStatus);
            return resultBean;
        }

        // 获取应答数据
        String body = entity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        resultBean.setObject(jsonObject);

        log.info("getAssetTypes. jsonObject=" + jsonObject.toJSONString());
        return resultBean;
    }

    @Override
    public ResultBean getDevices(String token) throws Exception {
        ResultBean resultBean = new ResultBean();

        // 请求头
        HttpHeaders headers = CommonUtils.initHeader(token);
        URI uri = new URIBuilder(URLS.DEVICES).build();
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        // 发起请求
        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        HttpStatus httpStatus = entity.getStatusCode();

        // 其他异常
        if (HttpStatus.OK.value() != httpStatus.value()) {
            log.error("HttpStatus is not ok. HttpStatus=" + httpStatus);
            return resultBean;
        }

        // 获取应答数据
        String body = entity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(body);
        resultBean.setObject(jsonObject);

        log.info("getAssetTypes. jsonObject=" + jsonObject.toJSONString());
        return resultBean;
    }
}
