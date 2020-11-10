package demo.sdk;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.NotifyAddThingTopoRequest;
import com.aliyuncs.iot.model.v20180120.NotifyAddThingTopoResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class NotifyAddThingTopo {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        JSONArray jsonArray = new JSONArray();
        JSONObject device1 = new JSONObject();
        device1.put("productKey", "a1pc5j9z8Zv");
        device1.put("deviceName", "QSubDevice1");
        jsonArray.add(device1);

//        JSONObject device2 = new JSONObject();
//        device2.put("productKey", "a1XN2iAK6DM");
//        device2.put("deviceName", "QSubDevice2");
//        jsonArray.add(device2);
//
//        JSONObject device3 = new JSONObject();
//        device3.put("productKey", "a1XN2iAK6DM");
//        device3.put("deviceName", "QSubDevice3");
//        jsonArray.add(device3);

        NotifyAddThingTopoRequest request = new NotifyAddThingTopoRequest();
        request.setGwProductKey("a10IkCI7B1r");
        request.setGwDeviceName("QDView1");
        request.setDeviceListStr(jsonArray.toJSONString());

        try {
            NotifyAddThingTopoResponse response = client.getAcsResponse(request);
            System.out.println(response.toString());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
