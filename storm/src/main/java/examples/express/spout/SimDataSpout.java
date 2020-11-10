package examples.express.spout;

import com.alibaba.fastjson.JSONObject;
import rexel.common.constants.Constants;
import rexel.common.simulate.DataRandom;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class SimDataSpout implements IRichSpout {
    private SpoutOutputCollector _collector = null;
    private DataRandom _dataRandom = null;
    private int _timeInterval = 1000;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("json"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _dataRandom = DataRandom.getInstance();
        if (conf.containsKey(Constants.SpoutInterval)) {
            _timeInterval = Integer.valueOf((String) conf.get(Constants.SpoutInterval));
        }
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
    public void nextTuple() {
        try {
            Thread.sleep(_timeInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = _dataRandom.getRandomExpressData();
//        long messageID = new Random().nextInt(1000000);
        System.out.print("[---SimDataSpout---]jsonObject=" + jsonObject + "\n");
        _collector.emit(new Values(jsonObject.toJSONString()), jsonObject.toJSONString());
    }

    @Override
    public void ack(Object msgId) {
        System.out.print("[---SimDataSpout---]ack msgID=" + msgId + "\n");
    }

    @Override
    public void fail(Object msgId) {
        System.out.print("[---SimDataSpout---]fail msgID=" + msgId + "\n");
    }
}
