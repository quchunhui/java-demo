package examples.express.topology;

import examples.express.bolt.CompanyCountBolt;
import examples.express.bolt.NormalizingBolt;
import examples.express.bolt.ProvinceCountBolt;
import examples.express.spout.SimDataSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class ExpressTopology {
    public static void main(String[] args)
            throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("SimDataSpout", new SimDataSpout(), 1);
        builder.setBolt("Normalizing", new NormalizingBolt(), 1)
                .shuffleGrouping("SimDataSpout");
        builder.setBolt("CompanyCount", new CompanyCountBolt(), 1)
                .fieldsGrouping("Normalizing", new Fields("company"));
        builder.setBolt("ProvinceCount", new ProvinceCountBolt(), 1)
                .fieldsGrouping("Normalizing", new Fields("prov"));

        Config config = new Config();
        config.setNumWorkers(1);
        config.setMessageTimeoutSecs(60);
        config.setMaxSpoutPending(100);
        config.setNumAckers(1);
        config.put(rexel.common.constants.Constants.SpoutInterval, args[1]);

        if (Boolean.valueOf(args[0])) {
            StormSubmitter.submitTopology("ExpressTopology", config, builder.createTopology());
        } else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("ExpressTopology", config, builder.createTopology());
        }
    }
}