package examples.wordcount.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import examples.wordcount.bolt.WordCountBolt;
import examples.wordcount.bolt.WordSplitBolt;
import examples.wordcount.spout.RandomSentencesSpout;

public class WordCountTopology {
    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", new RandomSentencesSpout());
        builder.setBolt("split", new WordSplitBolt()).shuffleGrouping("spout");
        builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"));

        Config conf = new Config();
        conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, 5);
        conf.setDebug(false);

        if (Boolean.valueOf(args[0])) {
            conf.setNumWorkers(1);
            StormSubmitter.submitTopology("WordCountTopology", conf, builder.createTopology());
        }else{
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("WordCountTopology", conf, builder.createTopology());
        }
    }
}
