package demo.thingmodel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.CreateThingModelRequest;
import com.aliyuncs.iot.model.v20180120.CreateThingModelResponse;
import com.aliyuncs.iot.model.v20180120.PublishThingModelRequest;
import com.aliyuncs.iot.model.v20180120.PublishThingModelResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;
import java.util.ArrayList;
import java.util.List;

public class ThingModelDemo {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        String productKey = "a1H35nAkyfz";
        JSONArray varArray = new JSONArray();
        varArray.add("AI_Q2_001");
        varArray.add("AR_Q2_002");
        varArray.add("AO_Q2_003");
        varArray.add("DI_Q2_004");
        varArray.add("DO_Q2_005");
        varArray.add("DR_Q2_006");
        varArray.add("VT_Q2_007");
        varArray.add("AI_Q2_008");
        varArray.add("AR_Q2_009");
        varArray.add("AR_Q2_010");
        varArray.add("AR_Q2_011");
        varArray.add("AR_Q2_012");

        List<JSONObject> thingModelList = new ArrayList<>();
        for (int i = 0; i < varArray.size(); i++) {
            String varName = varArray.getString(i);
            JSONObject thingModelJson = makeOneThingModelJson(productKey, varName);
            System.out.println("thingModelJson=" + thingModelJson);
            thingModelList.add(thingModelJson);
        }

        List<List<JSONObject>> split = listSplit(thingModelList, 10);
        split.forEach(jsonObjects -> {
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(jsonObjects);
            JSONObject thingsJson = new JSONObject();
            thingsJson.put("properties", jsonArray);
            publishThingModel(productKey, thingsJson);
        });
    }

    /**
     * 按照指定长度对List进行切分
     *
     * @param valueList 切分目标List
     * @return 切分结果
     */
    private static List<List<JSONObject>> listSplit(List<JSONObject> valueList, int splitLen) {
        List<List<JSONObject>> result = new ArrayList<>();

        for (int fromIndex = 0; fromIndex < valueList.size(); ) {
            int remainCount = valueList.size() - fromIndex;
            if (remainCount >= splitLen) {
                result.add(valueList.subList(fromIndex, fromIndex + splitLen));
            } else {
                result.add(valueList.subList(fromIndex, fromIndex + remainCount));
            }
            fromIndex += splitLen;
        }

        return result;
    }

    private static void publishThingModel(String productKey, JSONObject thingsJson) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //请求参数
        CreateThingModelRequest request = new CreateThingModelRequest();
        request.setProductKey(productKey);
        request.setThingModelJson(thingsJson.toJSONString());

        //发起请求
        try {
            CreateThingModelResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }

        // 发布物模型
        PublishThingModelRequest request1 = new PublishThingModelRequest();
        request1.setProductKey(productKey);
        request1.setModelVersion(String.valueOf(System.currentTimeMillis()));

        //发起请求
        try {
            PublishThingModelResponse response = client.getAcsResponse(request1);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }

    private static JSONObject makeOneThingModelJson(String productKey, String varName) {
        JSONObject thingModelJson = new JSONObject();
        thingModelJson.put("productKey", productKey);
        thingModelJson.put("identifier", varName);
        thingModelJson.put("name", varName);
        thingModelJson.put("rwFlag", "READ_WRITE");
        thingModelJson.put("required", false);
        thingModelJson.put("custom", true);
        thingModelJson.put("customFlag", true);
        JSONObject dataSpecs = new JSONObject();
        if (varName.startsWith("AI".toUpperCase()) || varName.startsWith("AO".toUpperCase())
            || varName.startsWith("AR".toUpperCase()) || varName.startsWith("DI".toUpperCase())
            || varName.startsWith("DO".toUpperCase()) || varName.startsWith("DR".toUpperCase())) {
            dataSpecs.put("dataType", "DOUBLE");
            dataSpecs.put("max", "1000000000");
            dataSpecs.put("min", "0");
            dataSpecs.put("step", "1");
        } else {
            dataSpecs.put("dataType", "TEXT");
            dataSpecs.put("length", 2048);
            thingModelJson.put("custom", true);
            thingModelJson.put("dataType", "TEXT");
        }
        thingModelJson.put("dataSpecs", dataSpecs);
        thingModelJson.put("dataType", dataSpecs.getString("dataType"));

        return thingModelJson;
    }
}
