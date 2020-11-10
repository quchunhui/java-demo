package normal;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.UUID;

public class CreateTopic {
    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        producer.setNamesrvAddr("192.168.1.80:9876;192.168.1.81:9876");
        producer.setInstanceName(UUID.randomUUID().toString());
        producer.setRetryAnotherBrokerWhenNotStoreOK(true);
        producer.start();
        producer.createTopic("post", "t2", 6, 1);
        producer.shutdown();
    }
}
