package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDeviceDesiredPropertyRequest;
import com.aliyuncs.iot.model.v20180120.QueryDeviceDesiredPropertyResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;
import java.util.ArrayList;
import java.util.List;

public class QueryDeviceDesiredProperty {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        List<String> identifiers = new ArrayList<>();
        identifiers.add("testVar1");
        identifiers.add("testVar2");
        identifiers.add("testVar3");
        identifiers.add("testVar4");

        //请求参数
        QueryDeviceDesiredPropertyRequest request = new QueryDeviceDesiredPropertyRequest();
        request.setProductKey(propertiesBean.getProductKey());//指定要查询的设备隶属的产品Key。
        request.setDeviceName(propertiesBean.getDeviceName());//指定要查询的设备的名称。
        request.setIdentifiers(identifiers);//要查询的属性标识符。

        //发起请求
        try {
            QueryDeviceDesiredPropertyResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
