package rexel.iotextension.rest;

import com.alibaba.fastjson.JSONObject;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import rexel.iotextension.ExtensionConst;

public class ServerRestClient {
    public static void main(String[] args) throws Exception {
        // 获取设备ID
        String deviceIdentify = getDeviceIdentify(ExtensionConst.clientId);

        // 获取下发命令
        String command = getCommand(deviceIdentify);

        // 执行命令下发
        postCommand(command);
    }

    private static void postCommand(String command) throws Exception {
        System.out.println("command=" + command);
        String url = ExtensionConst.restUrl + ExtensionConst.devicecontrol_operations;
        CloseableHttpClient httpclient = HttpClient.getHttpClient();

        StringEntity entity = new StringEntity(command, StandardCharsets.UTF_8);
        entity.setContentEncoding(StandardCharsets.UTF_8.toString());
        entity.setContentType("application/json");

        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(entity);
        System.out.println("Executing request " + httppost.getRequestLine());
        CloseableHttpResponse response = httpclient.execute(httppost);
        System.out.println(response.getStatusLine().getStatusCode());

        response.close();
        httpclient.close();
    }

    private static String getDeviceIdentify(String clientId) throws Exception {
        String url = ExtensionConst.restUrl + ExtensionConst.identity_externalIds + "/c8y_Serial/" + clientId;
        CloseableHttpClient httpClient = HttpClient.getHttpClient();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String entityStr = EntityUtils.toString(response.getEntity());
        JSONObject entityJson = JSONObject.parseObject(entityStr);
        System.out.println("entityJson=" + entityJson.toJSONString());

        response.close();
        httpClient.close();

        return entityJson.getJSONObject("managedObject").getString("id");
    }

    private static String getCommand(String deviceIdentify) {
        JSONObject contentJson = new JSONObject();
        contentJson.put("AR_TEST_VAR1", new Random().nextInt(1000));
        contentJson.put("AR_TEST_VAR2", new Random().nextInt(1000));
        contentJson.put("AR_TEST_VAR3", new Random().nextInt(1000));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", deviceIdentify);
        JSONObject command = new JSONObject();
        command.put("text", contentJson.toJSONString());
        jsonObject.put("c8y_Command", command);

        return jsonObject.toJSONString();
    }
}