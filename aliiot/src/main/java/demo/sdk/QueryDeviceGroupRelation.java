package demo.sdk;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupListRequest;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupListResponse;
import com.aliyuncs.iot.model.v20180120.QueryDeviceGroupListResponse.GroupInfo;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;
import java.util.List;

public class QueryDeviceGroupRelation {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        queryGroupInfoList(client, "");
    }

    private static List<GroupInfo> queryGroupInfoList(DefaultAcsClient client, String groupName) {
        QueryDeviceGroupListRequest request = new QueryDeviceGroupListRequest();
        if (groupName != null && !groupName.isEmpty()) {
            request.setGroupName(groupName);
        }

        try {
            QueryDeviceGroupListResponse response = client.getAcsResponse(request);
            return response.getData();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}
