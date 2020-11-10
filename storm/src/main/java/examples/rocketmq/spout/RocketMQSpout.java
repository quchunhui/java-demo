package examples.rocketmq.spout;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class RocketMQSpout implements IRichSpout, MessageListenerConcurrently {
    private LinkedBlockingQueue<byte[]> queue;
    private SpoutOutputCollector _collector = null;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        queue = new LinkedBlockingQueue<>(100);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("con_group_0125_1");
        consumer.setInstanceName(UUID.randomUUID().toString());
        consumer.setConsumeMessageBatchMaxSize(32);
        consumer.setNamesrvAddr("192.168.1.80:9876;192.168.1.81:9876");
        consumer.registerMessageListener(this);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        try {
            consumer.subscribe("sf_md_0123", "*");
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextTuple() {
        try {
            String json = new String(queue.take());
            _collector.emit(new Values(json), UUID.randomUUID().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }

    /*
    {
    "CONTENT_TYPE": "C201",
    "ENTERPRISE_CODE": "SF",
    "INSERT_TIME": 1425139200000,
    "MAIL_CODE": "T4",
    "MAIL_DATETIME": 1425139200000,
    "MAIL_NO": "134803003873",
    "MAIL_TYPE": "1",
    "NEW_COMP_TIME": 1425139200000,
    "NEW_CONTENT_TYPE": "1",
    "NEW_DEST_CITY": "440500",
    "NEW_DEST_COUNTRY": "CN",
    "NEW_ENTERPRISE_CODE": "SF",
    "NEW_MAIL_TYPE": "1",
    "NEW_REC_CITY": "440500",
    "NEW_REC_COUNTY": "440500",
    "NEW_REC_PROV": "440000",
    "NEW_SEN_CITY": "440600",
    "NEW_SEN_COUNTY": "440600",
    "NEW_SEN_PROV": "440000",
    "NEW_SOURCE_CITY": "440600",
    "REC_ADDRESS": "潮汕路",
    "REC_CITY": "754",
    "REC_MOBILE": "13556397524",
    "REC_NAME": "陈俊生",
    "REC_PHONE": "0",
    "SEN_ADDRESS": "顺德区",
    "SEN_CITY": "757EL",
    "SEN_MOBILE": "18924882171",
    "SEN_NAME": "倪娟",
    "SEN_PHONE": "0",
    "WEIGHT": "1.0"
    }
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for(MessageExt me : list) {
                queue.put(me.getBody());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
