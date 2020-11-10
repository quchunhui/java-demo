package examples.rocketmq.bolt;

import com.alibaba.fastjson.JSONObject;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class CountBolt implements IRichBolt {
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
        String json = tuple.getStringByField("json");
        JSONObject jsonObject = JSONObject.parseObject(json);
        String company = jsonObject.getString("ENTERPRISE_CODE");

        if (company == null) {
            _collector.fail(tuple);
            return;
        }

        int count = 0;
        if (_map.containsKey(company)) {
            count = _map.get(company);
        }
        count++;
        _map.put(company, count);

        System.out.print("[---CountBolt---]" +
                "taskId=" + _taskId + ", company=" + company + ", count=" + count + "\n");
        _collector.ack(tuple);
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
