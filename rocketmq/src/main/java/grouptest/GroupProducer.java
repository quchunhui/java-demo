package grouptest;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.UUID;

public class GroupProducer {
    public static void main(String[] args)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        System.out.print("[------]start.\n");
        DefaultMQProducer producer = new DefaultMQProducer("pro_a");
        producer.setNamesrvAddr(GroupConstants.NAMESRV_ADDR);
        producer.setInstanceName(UUID.randomUUID().toString());
        try {
            producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            return;
        }
        System.out.print("[------]producer started.\n");

        int count = 10;
        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T01, (GroupConstants.T01 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T01---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T02, (GroupConstants.T02 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T02---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T03, (GroupConstants.T03 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T03---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T04, (GroupConstants.T04 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T04---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T05, (GroupConstants.T05 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T05---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T06, (GroupConstants.T06 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T06---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T07, (GroupConstants.T07 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T07---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T08, (GroupConstants.T08 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T08--]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T09, (GroupConstants.T09 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T09---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T10, (GroupConstants.T10 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T10---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T11, (GroupConstants.T11 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T11---]" + new String(msg.getBody()) + "\n");
        }

        for (int i = 1; i <= count; i++) {
            Message msg = new Message(GroupConstants.T12, (GroupConstants.T12 + "+" + i).getBytes());
            producer.send(msg);
            System.out.print("[---T12---]" + new String(msg.getBody()) + "\n");
        }

        producer.shutdown();
        System.out.print("[------]end.\n");
    }
}