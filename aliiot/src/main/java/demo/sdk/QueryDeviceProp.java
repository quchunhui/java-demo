package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropRequest;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class QueryDeviceProp {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //请求参数
        QueryDevicePropRequest request = new QueryDevicePropRequest();
        request.setProductKey(propertiesBean.getProductKey());
        request.setDeviceName(propertiesBean.getDeviceName());

        //发起请求
        try {
            QueryDevicePropResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }
}
