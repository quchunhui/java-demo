package examples.rocketmq.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import examples.rocketmq.bolt.CountBolt;
import examples.rocketmq.spout.RocketMQSpout;

public class RocketMQTopology {
    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("RocketMQSpout", new RocketMQSpout(), 1);
        builder.setBolt("CountBolt", new CountBolt(), 1).fieldsGrouping("RocketMQSpout", new Fields("json"));

        Config config = new Config();
        config.setNumWorkers(1);
        config.setMessageTimeoutSecs(60);
        config.setMaxSpoutPending(100);
        config.setNumAckers(1);

        if (Boolean.valueOf(args[0])) {
            StormSubmitter.submitTopology("RocketMQTopology", config, builder.createTopology());
        } else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("RocketMQTopology", config, builder.createTopology());
        }
    }
}