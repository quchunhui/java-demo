package examples.express.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class CompanyCountBolt implements IRichBolt {
    private OutputCollector _collector = null;
    private int _taskId = 0;
    private Map<String, Integer> _map = new HashMap<>();

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
        _taskId = context.getThisTaskId();
    }

    @Override
    public void execute(Tuple tuple) {
        String company = tuple.getStringByField("company");
        int count = 0;
        if (_map.containsKey(company)) {
            count = _map.get(company);
        }
        count++;
        _map.put(company, count);

        System.out.print("[---CompanyCount---]" +
                "taskId=" + _taskId + ", company=" + company + ", count=" + count + "\n");
        _collector.fail(tuple);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
