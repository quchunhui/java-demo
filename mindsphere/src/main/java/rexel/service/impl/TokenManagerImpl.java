package rexel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rexel.config.MindSphereConfig;
import rexel.constants.URLS;
import rexel.service.ITokenManager;

/**
 * @Author: quchunhui
 * @Date: 2020/1/4
 * @Description:
 */
@Service
@Slf4j
public class TokenManagerImpl implements ITokenManager {
    private MindSphereConfig mindSphereConfig;
    private RestTemplate restTemplate;
    private String holdToken;

    @Autowired
    public void setMindSphereConfig(MindSphereConfig mindSphereConfig) {
        this.mindSphereConfig = mindSphereConfig;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getToken(boolean bRefresh) throws URISyntaxException {
        if (bRefresh) {
            refreshHoldToken();
        }

        if (holdToken == null || holdToken.isEmpty()) {
            refreshHoldToken();
        }

        return holdToken;
    }

    private void refreshHoldToken() throws URISyntaxException {
        log.info("token refresh.");

        // 使用base64加密客户ID及客户密钥
        String src = mindSphereConfig.getAccessId() + ":" + mindSphereConfig.getSecretkey();
        String encodeStr = Base64.getEncoder().encodeToString(src.getBytes(StandardCharsets.UTF_8));
        String authKey = "Bearer " + encodeStr;

        // 设置请求Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-space-auth-key", authKey);

        // 设置请求Body
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("appName", mindSphereConfig.getAppName());
        jsonObj.put("appVersion", mindSphereConfig.getAppVersion());
        jsonObj.put("hostTenant", mindSphereConfig.getHostTenant());
        jsonObj.put("userTenant", mindSphereConfig.getUserTenant());
        HttpEntity httpEntity = new HttpEntity<>(jsonObj.toString(), headers);

        // 发起请求
        URI uri = new URIBuilder(URLS.OAUTH_TOKEN).build();
        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, String.class);

        // 获取应答数据
        String body = entity.getBody();
        JSONObject jsonResult = JSON.parseObject(body);
        if (jsonResult == null) {
            log.error("JSONObject parse exception.");
            return;
        }

        holdToken = jsonResult.getString("access_token");
        log.info("new holdToken=" + holdToken);
    }
}