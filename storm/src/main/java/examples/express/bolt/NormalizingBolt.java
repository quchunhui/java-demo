package examples.express.bolt;

import com.alibaba.fastjson.JSONObject;
import rexel.common.constants.Constants;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class NormalizingBolt implements IRichBolt {
    private OutputCollector _collector = null;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        System.out.print("[---Normalizing---]MessageId=" + input.getMessageId() + "\n");

        String str = input.getStringByField("json");
        JSONObject jsonObject = JSONObject.parseObject(str);
        normallizing(jsonObject);

        _collector.emit(input, new Values(
                jsonObject.getString(Constants.EnterpriseCode),
                jsonObject.getString(Constants.ProvCode)));
        _collector.ack(input);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("company", "prov"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    private void normallizing(JSONObject jsonObject) {
        String provCode = jsonObject.getString(Constants.ProvCode);
        if (provCode == null) {
            provCode = "990000";
        } else {
            provCode = provCode.substring(0, 2) + "0000";
        }
        jsonObject.put(Constants.ProvCode, provCode);

        String cityCode = jsonObject.getString(Constants.CityCode);
        if (cityCode == null) {
            cityCode = "999900";
        } else {
            cityCode = cityCode.substring(0, 4) + "00";
        }
        jsonObject.put(Constants.CityCode, cityCode);
    }
}