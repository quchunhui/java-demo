package grouptest;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;
import java.util.UUID;

public class GroupConsumer {
    public static void main(String[] args) {
        //1. 消费组相同，集群模式，消费相同Topic
        Group0Topic0Cluster();
        //2. 消费组相同，广播模式，消费相同Topic
        Group0Topic0Broad();
        //3. 消费组不同，集群模式，消费相同Topic
        Group1Topic0Cluster();
        //4. 消费组不同，广播模式，消费相同Topic
        Group1Topic0Broad();

        //5. 消费组相同，集群模式，消费不同Topic
        Group0Topic1Cluster();
        //6. 消费组相同，广播模式，消费不同Topic
        Group0Topic1Broad();
        //7. 消费组不同，集群模式，消费不同Topic
        Group1Topic1Cluster();
        //8. 消费组不同，广播模式，消费不同Topic
        Group1Topic1Broad();
    }

    /**
     * 1.消费组相同，集群模式，消费相同Topic
     */
    private static void Group0Topic0Cluster() {
        StartPushConsumer("consumer11", "con_a", MessageModel.CLUSTERING, GroupConstants.T01);
        StartPushConsumer("consumer12", "con_a", MessageModel.CLUSTERING, GroupConstants.T01);
    }

    /**
     * 2.消费组相同，广播模式，消费相同Topic
     */
    private static void Group0Topic0Broad() {
        StartPushConsumer("consumer21", "con_b", MessageModel.BROADCASTING, GroupConstants.T02);
        StartPushConsumer("consumer22", "con_b", MessageModel.BROADCASTING, GroupConstants.T02);
    }

    /*
     * 3.消费组不同，集群模式，消费相同Topic
     */
    private static void Group1Topic0Cluster() {
        StartPushConsumer("consumer31", "con_c1", MessageModel.CLUSTERING, GroupConstants.T03);
        StartPushConsumer("consumer32", "con_c2", MessageModel.CLUSTERING, GroupConstants.T03);
    }

    /*
     * 4.消费组不同，广播模式，消费相同Topic
     */
    private static void Group1Topic0Broad() {
        StartPushConsumer("consumer41", "con_d1", MessageModel.BROADCASTING, GroupConstants.T04);
        StartPushConsumer("consumer42", "con_d2", MessageModel.BROADCASTING, GroupConstants.T04);
    }

    /**
     * 5.消费组相同，集群模式，消费不同Topic
     */
    private static void Group0Topic1Cluster() {
        StartPushConsumer("consumer51", "con_e", MessageModel.CLUSTERING, GroupConstants.T05);
        StartPushConsumer("consumer52", "con_e", MessageModel.CLUSTERING, GroupConstants.T06);
    }

    /**
     * 6.消费组相同，广播模式，消费不同Topic
     */
    private static void Group0Topic1Broad() {
        StartPushConsumer("consumer61", "con_f", MessageModel.BROADCASTING, GroupConstants.T07);
        StartPushConsumer("consumer62", "con_f", MessageModel.BROADCASTING, GroupConstants.T08);
    }

    /*
     * 7.消费组不同，集群模式，消费不同Topic
     */
    private static void Group1Topic1Cluster() {
        StartPushConsumer("consumer71", "con_g1", MessageModel.CLUSTERING, GroupConstants.T09);
        StartPushConsumer("consumer72", "con_g2", MessageModel.CLUSTERING, GroupConstants.T10);
    }

    /*
     * 8.消费组不同，广播模式，消费不同Topic
     */
    private static void Group1Topic1Broad() {
        StartPushConsumer("consumer81", "con_h1", MessageModel.BROADCASTING, GroupConstants.T11);
        StartPushConsumer("consumer82", "con_h2", MessageModel.BROADCASTING, GroupConstants.T12);
    }

    private static void StartPushConsumer(String name, String group, MessageModel model, String topic) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(GroupConstants.NAMESRV_ADDR);
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setMessageModel(model);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new MessageListener(name));

        try {
            consumer.subscribe(topic, "*");
            consumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return;
        }
        System.out.printf("[------]%s started.\n", name);
    }

    private static class MessageListener implements MessageListenerConcurrently {
        private String _consumerName = null;

        MessageListener(String consumerName) {
            _consumerName = consumerName;
        }

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            for(MessageExt me : list) {
                System.out.printf("[---%s---]msg=%s\n", _consumerName, new String(me.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
