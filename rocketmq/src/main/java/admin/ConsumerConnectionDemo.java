package admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.HashSet;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.protocol.body.Connection;
import org.apache.rocketmq.common.protocol.body.ConsumerConnection;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;

public class ConsumerConnectionDemo {
    public static void main(String[] args)
        throws MQClientException, InterruptedException, MQBrokerException, RemotingException {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt(getAclRPCHook(), 300000);
        defaultMQAdminExt.setNamesrvAddr("101.132.242.90:9876;47.116.50.192:9876");
        defaultMQAdminExt.start();

        ConsumerConnection cc = defaultMQAdminExt.examineConsumerConnectionInfo("device_cg_notice_down");
        HashSet<Connection> set = cc.getConnectionSet();
        JSONArray jsonArray = new JSONArray();
        for(Connection connection : set) {
            JSONObject jsonObject = (JSONObject)JSONObject.toJSON(connection);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray.toJSONString());

        defaultMQAdminExt.shutdown();
    }

    static RPCHook getAclRPCHook() {
        return new AclClientRPCHook(new SessionCredentials("rexel_developer","19@ljWo2iUow"));
    }
}
