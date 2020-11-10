package dingtalk.workrecord;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserGetByMobileRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest;
import com.dingtalk.api.request.OapiWorkrecordAddRequest.FormItemVo;
import com.dingtalk.api.request.OapiWorkrecordGetbyuseridRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserGetByMobileResponse;
import com.dingtalk.api.response.OapiWorkrecordAddResponse;
import com.dingtalk.api.response.OapiWorkrecordGetbyuseridResponse;
import com.taobao.api.ApiException;
import java.util.ArrayList;
import java.util.List;

public class AddWorkRecord {

    public static void main(String[] args) throws ApiException {
        // 获取Token
        DefaultDingTalkClient client1 = new DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
        OapiGettokenRequest request1 = new OapiGettokenRequest();
        request1.setAppkey("dingicngczswkvp3ekyc");
        request1.setAppsecret("C8jCZHeLJMrLAKnKCbYN2iofB0X-Kiq_8L6FUwiW1KQbnxif63Rd17ll-Bzl1Eoa");
        request1.setHttpMethod("GET");
        OapiGettokenResponse response1 = client1.execute(request1);
        String accessToken = response1.getAccessToken();

        // 获取userId
        DingTalkClient client2 = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/get_by_mobile");
        OapiUserGetByMobileRequest request2 = new OapiUserGetByMobileRequest();
        request2.setMobile("13651166565");
        OapiUserGetByMobileResponse response2 = client2.execute(request2, accessToken);
        String userId = response2.getUserid();

        // 创建待办
        DingTalkClient client3 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/add");
        OapiWorkrecordAddRequest request3 = new OapiWorkrecordAddRequest();
        request3.setUserid(userId);
        request3.setCreateTime(System.currentTimeMillis());
        request3.setTitle("DingTalkTest");
        request3.setUrl("http://iot.rexelouneng.com/#/login");
        List<FormItemVo> list3 = new ArrayList<>();
        FormItemVo obj3 = new FormItemVo();
        list3.add(obj3);
        obj3.setTitle("标题Test");
        obj3.setContent("内容Test");
        request3.setFormItemList(list3);
        OapiWorkrecordAddResponse response3 = client3.execute(request3, accessToken);
        System.out.println(response3.getBody());

        // 获取待办事项列表
        DingTalkClient client4 = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/workrecord/getbyuserid");
        OapiWorkrecordGetbyuseridRequest req4 = new OapiWorkrecordGetbyuseridRequest();
        req4.setUserid(userId);
        req4.setOffset(0L);
        req4.setLimit(50L);
        req4.setStatus(1L);
        OapiWorkrecordGetbyuseridResponse response4 = client4.execute(req4, accessToken);
        System.out.println(response4.getBody());
    }
}
