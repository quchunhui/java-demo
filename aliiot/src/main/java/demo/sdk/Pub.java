package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.PubRequest;
import com.aliyuncs.iot.model.v20180120.PubResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class Pub {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);


        //请求参数
        String productKey = propertiesBean.getProductKey();
        String deviceName = propertiesBean.getDeviceName();
        String topic = "/" + productKey + "/" + deviceName + "/user/update";
        PubRequest request = new PubRequest();
        request.setQos(0);
        request.setTopicFullName(topic);
        request.setProductKey(productKey);

        //发起请求
        try {
            PubResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }
}
