package normal;

import java.util.Random;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import java.util.UUID;

public class Producer {
    public static void main(String[] args) {
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        producer.setNamesrvAddr("192.168.29.100:9876;192.168.29.101:9876");
        producer.setInstanceName(UUID.randomUUID().toString());

        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return;
        }

        String topic = "t3";
        String body = String.valueOf(new Random().nextInt(1000));
        Message msg = new Message(topic, body.getBytes());
        try {
            SendResult sendResult = producer.send(msg);
            System.out.println("[------]sendResult=" + sendResult);
        } catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

        producer.shutdown();
    }
}
