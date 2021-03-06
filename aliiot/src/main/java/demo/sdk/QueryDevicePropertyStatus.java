package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusRequest;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyStatusResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class QueryDevicePropertyStatus {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //请求参数
        QueryDevicePropertyStatusRequest request = new QueryDevicePropertyStatusRequest();
        request.setProductKey(propertiesBean.getProductKey());
        request.setDeviceName(propertiesBean.getDeviceName());

        //发起请求
        try {
            QueryDevicePropertyStatusResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
