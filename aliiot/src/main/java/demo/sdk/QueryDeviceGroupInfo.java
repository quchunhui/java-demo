package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupInfoRequest;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupInfoResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

/**
 * 名称	类型	是否必需	描述
 * Action	String	是	要执行的操作。取值：QueryDeviceGroupInfo。
 * GroupId	String	是	分组ID，分组的全局唯一标识符。
 * IotInstanceId	String	否	公共实例不传此参数；仅独享实例需传入实例ID。
 * 公共请求参数	-	是	请参见公共参数。
 */
public class QueryDeviceGroupInfo {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //设置参数
        QueryDeviceGroupInfoRequest request = new QueryDeviceGroupInfoRequest();
        request.setGroupId("WYwADnVaiJqXUoqDznRa010200");

        //发起请求
        try {
            QueryDeviceGroupInfoResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
