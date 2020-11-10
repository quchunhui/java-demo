package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.CreateThingModelRequest;
import com.aliyuncs.iot.model.v20180120.CreateThingModelResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class CreateThingModel {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        // 物模型JSON
        String tslStr = "{"
            + "  \"properties\": [{"
            + "    \"identifier\": \"AI_TEST_001\","
            + "    \"dataSpecs\": {"
            + "      \"max\": \"100000\","
            + "      \"dataType\": \"DOUBLE\","
            + "      \"min\": \"0\","
            + "      \"step\": \"1\""
            + "    },"
            + "    \"std\": false,"
            + "    \"custom\": true,"
            + "    \"dataType\": \"DOUBLE\","
            + "    \"rwFlag\": \"READ_WRITE\","
            + "    \"productKey\": \"a1H35nAkyfz\","
            + "    \"required\": false,"
            + "    \"customFlag\": true,"
            + "    \"name\": \"物模型测试属性1\""
            + "  }]"
            + "}";
        JSONObject jsonObject = JSONObject.parseObject(tslStr);

        //请求参数
        CreateThingModelRequest request = new CreateThingModelRequest();
        request.setProductKey("a1H35nAkyfz");
        request.setThingModelJson(jsonObject.toJSONString());

        //发起请求
        try {
            CreateThingModelResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }
}
