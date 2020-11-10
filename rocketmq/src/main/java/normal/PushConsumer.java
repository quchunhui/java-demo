package normal;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class PushConsumer {
    public static void main(String[] args) throws MQClientException {
        String instanceName = "instance67";
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("co_group");
        consumer.setNamesrvAddr("192.168.29.100:9876;192.168.29.101:9876");
        consumer.subscribe("t3", "*");
        consumer.registerMessageListener(new MessageListener());
        consumer.setInstanceName(instanceName);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setConsumeMessageBatchMaxSize(1);

        consumer.start();
        System.out.println("[------]%s started." + instanceName);
    }

    static class MessageListener implements MessageListenerConcurrently{
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            for (MessageExt me : list) {
                System.out.println(new String(me.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}