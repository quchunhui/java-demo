package examples.express.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class ProvinceCountBolt implements IRichBolt {
    private OutputCollector _collector = null;
    private int _taskId = 0;
    private Map<String, Integer> _map = new HashMap<>();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector collector) {
        _collector = collector;
        _taskId = topologyContext.getThisTaskId();
    }

    @Override
    public void execute(Tuple tuple) {
        String prov = tuple.getStringByField("prov");

        int count = 0;
        if (_map.containsKey(prov)) {
            count = _map.get(prov);
        }
        count++;
        _map.put(prov, count);

        System.out.print("[---ProvinceCount---]" +
                "taskId=" + _taskId + ", prov=" + prov + ", count=" + count + "\n");
        _collector.ack(tuple);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
