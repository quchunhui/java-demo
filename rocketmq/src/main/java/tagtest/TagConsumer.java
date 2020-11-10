package tagtest;

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

public class TagConsumer {
    public static void main(String[] args) {
        //消费组相同，tag相同（集群消费）
        Topic0Tag0Cluster();
        //消费组相同，tag相同（广播消费）
        Topic0Tag0Broad();
        //消费组相同，tag不同（集群消费）
        Topic0Tag1Cluster();
        //消费组相同，tag不同（广播消费）
        Topic0Tag1Broad();
    }

    /**
     * 消费组相同，tag相同（集群消费）
     */
    private static void Topic0Tag0Cluster() {
        StartPushConsumer("consumer11", "con_a", MessageModel.CLUSTERING, TagConstants.T01, "tag1 || tag2");
        StartPushConsumer("consumer12", "con_a", MessageModel.CLUSTERING, TagConstants.T01, "tag1 || tag2");
    }

    /**
     * 消费组相同，tag相同（广播消费）
     */
    private static void Topic0Tag0Broad() {
        StartPushConsumer("consumer21", "con_b", MessageModel.BROADCASTING, TagConstants.T01, "tag1 || tag3");
        StartPushConsumer("consumer22", "con_b", MessageModel.BROADCASTING, TagConstants.T01, "tag1 || tag3");
    }

    /**
     * 消费组相同，tag不同（集群消费）
     */
    private static void Topic0Tag1Cluster() {
        StartPushConsumer("consumer31", "con_c", MessageModel.CLUSTERING, TagConstants.T01, "tag1 || tag2");
        StartPushConsumer("consumer32", "con_c", MessageModel.CLUSTERING, TagConstants.T01, "tag2 || tag3");
    }

    /**
     * 消费组相同，tag不同（广播消费）
     */
    private static void Topic0Tag1Broad() {
        StartPushConsumer("consumer41", "con_d", MessageModel.BROADCASTING, TagConstants.T01, "tag1 || tag2");
        StartPushConsumer("consumer42", "con_d", MessageModel.BROADCASTING, TagConstants.T01, "tag2 || tag3");
    }

    private static void StartPushConsumer(String name, String group, MessageModel model, String topic, String tag) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
        consumer.setNamesrvAddr(TagConstants.NAMESRV_ADDR);
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setMessageModel(model);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.registerMessageListener(new TagConsumer.MessageListener(name));

        try {
            consumer.subscribe(topic, tag);
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
