package demo.sdk;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.iot.model.v20180120.QueryDeviceByTagsRequest;
import com.aliyuncs.iot.model.v20180120.QueryDeviceByTagsRequest.Tag;
import com.aliyuncs.iot.model.v20180120.QueryDeviceByTagsResponse;
import demo.bean.PropertiesBean;
import demo.utils.IotClientUtils;
import demo.utils.PropertiesUtils;
import java.util.ArrayList;
import java.util.List;

public class QueryDeviceByTags {
    private static PropertiesUtils propertiesUtils = PropertiesUtils.getInstance();

    public static void main(String[] args) {
        PropertiesBean propertiesBean = propertiesUtils.readProperties();
        if (propertiesBean == null) {
            return;
        }

        //初始化SDK客户端
        DefaultAcsClient client = IotClientUtils.getIotClient(propertiesBean);

        //请求参数
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setTagKey("factory");
        tag.setTagValue("factory1");
        tags.add(tag);
        QueryDeviceByTagsRequest request = new QueryDeviceByTagsRequest();
        request.setPageSize(10);
        request.setCurrentPage(1);
        request.setTags(tags);

        //发起请求
        try {
            QueryDeviceByTagsResponse response = client.getAcsResponse(request);
            System.out.println(JSON.toJSONString(response));
        } catch (ClientException e) {
            e.printStackTrace();
            System.out.println("异常");
        }
    }
}
