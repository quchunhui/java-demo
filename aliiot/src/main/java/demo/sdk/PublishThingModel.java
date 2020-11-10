package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.PublishThingModelRequest;
import com.aliyuncs.iot.model.v20180120.PublishThingModelResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class PublishThingModel {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        // 发布物模型
        PublishThingModelRequest request1 = new PublishThingModelRequest();
        request1.setProductKey("a1H35nAkyfz");
        request1.setModelVersion("1.2");

        //发起请求
        try {
            PublishThingModelResponse response = client.getAcsResponse(request1);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }
}
