package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupListRequest;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupListResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;

/**
 * 名称	类型	是否必需	描述
 * Action	String	是	要执行的操作。取值：QueryDeviceGroupList。
 * GroupName	String	否	分组名称。
 *      传入分组名称，则根据名称进行查询。支持分组名称模糊查询。
 *      若不传入此参数，则进行全量分组查询。
 * SuperGroupId	String	否	父组ID。查询某父组下的子分组列表时，需传入此参数。
 * PageSize	Integer	否	每页记录数。最大值是200。默认值是10。
 * CurrentPage	Integer	否	指定从返回结果中的第几页开始显示。默认值为1。
 * IotInstanceId	String	否	公共实例不传此参数；仅独享实例需传入实例ID。
 * 公共请求参数	-	是	请参见公共参数。
 */
public class QueryDeviceGroupList {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //设置参数
        QueryDeviceGroupListRequest request = new QueryDeviceGroupListRequest();
//        request.setGroupName("集团1");

        //发起请求
        try {
            QueryDeviceGroupListResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
