package tagtest;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TagProducer {
    private static String dateNow = null;

    public static void main(String[] args)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        System.out.print("[------]start.\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateNow = sdf.format(new Date());

        DefaultMQProducer producer = new DefaultMQProducer("pro_a");
        producer.setNamesrvAddr(TagConstants.NAMESRV_ADDR);
        producer.setInstanceName(UUID.randomUUID().toString());
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            producer.shutdown();
            return;
        }
        System.out.print("[------]producer started.\n");

        int count = 5;
        for (int i = 1; i <= count; i++) {
            Message msg =  getMsg(TagConstants.T01, TagConstants.TAG1, String.valueOf(i));
            producer.send(msg);
            System.out.print("[------]msg=" + new String(msg.getBody()) + "\n");
        }

//        for (int i = 1; i <= count; i++) {
//            Message msg =  getMsg(TagConstants.T01, TagConstants.TAG2, String.valueOf(i));
//            producer.send(msg);
//            System.out.print("[------]msg=" + new String(msg.getBody()) + "\n");
//        }
//
//        for (int i = 1; i <= count; i++) {
//            Message msg =  getMsg(TagConstants.T01, TagConstants.TAG3, String.valueOf(i));
//            producer.send(msg);
//            System.out.print("[------]msg=" + new String(msg.getBody()) + "\n");
//        }

        producer.shutdown();
        System.out.print("[------]end.\n");
    }

    private static Message getMsg(String topic, String tag, String ext) {
        String str = dateNow + " | " + topic + " | " + tag + " | " + ext;
        if (tag == null || tag.isEmpty()) {
            return new Message(topic, str.getBytes());
        } else {
            return new Message(topic, tag, str.getBytes());
        }
    }
}
