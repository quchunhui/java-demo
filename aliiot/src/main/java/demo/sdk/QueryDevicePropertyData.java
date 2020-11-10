package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyDataRequest;
import com.aliyuncs.iot.model.v20180120.QueryDevicePropertyDataResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

public class QueryDevicePropertyData {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //请求参数
        QueryDevicePropertyDataRequest request = new QueryDevicePropertyDataRequest();
        request.setProductKey(propertiesBean.getProductKey());//指定要查询的设备隶属的产品Key。
        request.setDeviceName(propertiesBean.getDeviceName());//指定要查询的设备的名称。
        request.setIdentifier("testVar1");//要查询的属性标识符。
        request.setStartTime(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000 + 1);//要查询的属性记录的开始时间。取值为毫秒值时间戳。
        request.setEndTime(System.currentTimeMillis());//要查询的属性记录的结束时间。取值为毫秒值时间戳。
        request.setPageSize(100);//返回结果中每页显示的记录数。
        request.setAsc(0);//返回结果中属性记录的排序方式，取值：0：倒序，1：正序。

        //发起请求
        try {
            QueryDevicePropertyDataResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
