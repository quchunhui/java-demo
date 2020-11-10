package rexel.service.impl;

import com.alibaba.fastjson.JSONArray;
import rexel.pojo.ResultBean;
import rexel.constants.URLS;
import rexel.service.ITimeSeriesAggregates;
import rexel.utils.CommonUtils;
import java.net.URI;
import java.net.URISyntaxException;
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
public class TimeSeriesAggregatesImpl implements ITimeSeriesAggregates {
    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResultBean aggregates(String token,
        String entityId, String propertySetName,
        String from, String to,
        String intervalUnit, Integer intervalValue, String select) throws URISyntaxException {

        ResultBean resultBean = new ResultBean();
        // 请求头
        HttpHeaders headers = CommonUtils.initHeader(token);
        URI uri = makeUri(entityId, propertySetName, from, to, intervalUnit, intervalValue, select);
        HttpEntity httpEntity = new HttpEntity<>(null, headers);

        // 发起请求
        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
        HttpStatus httpStatus = entity.getStatusCode();
        if (HttpStatus.OK.value() != httpStatus.value()) {
            log.error("HttpStatus is not ok. HttpStatus=" + httpStatus);
            return resultBean;
        }

        // 获取应答数据
        String body = entity.getBody();
        JSONArray jsonArray = JSONArray.parseArray(body);
        if (jsonArray == null) {
            log.error("JSONArray parse exception.");
            return resultBean;
        }

        log.info("jsonArray=" + jsonArray.toString());
        log.info("TimeSeriesData get end.");
        resultBean.setObject(jsonArray);

        return resultBean;
    }

    private URI makeUri(String entityId, String propertySetName,
        String from, String to,String intervalUnit, Integer intervalValue, String select)
        throws URISyntaxException {

        // 地址样例：
        // http://localhost:6789/rexel/mindsphere/aggregates
        // /115e0e26f5ac4b09bbfb84b5479eb015/QchIoTExtension?
        // from=2020-04-16T08:00:00Z
        // &to=2020-04-16T10:00:00Z
        // &intervalUnit=minute/hour/day
        // &intervalValue=5
        // &select=AR01_0001,AR01_0003
        String url = URLS.AGGREGATES + "/" + entityId + "/" + propertySetName;
        URIBuilder uriBuilder = new URIBuilder(url);
        if (from != null) {
            uriBuilder.addParameter("from", from);
        }
        if (to != null) {
            uriBuilder.addParameter("to", to);
        }
        if (intervalUnit != null) {
            uriBuilder.addParameter("intervalUnit", intervalUnit);
        }
        if (intervalValue != null) {
            uriBuilder.addParameter("intervalValue", intervalValue.toString());
        }
        if (select != null) {
            uriBuilder.addParameter("select", select);
        }

        return uriBuilder.build();
    }
}
