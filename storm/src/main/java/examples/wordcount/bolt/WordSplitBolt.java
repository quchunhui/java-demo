package examples.wordcount.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.StringTokenizer;

public class WordSplitBolt extends BaseRichBolt {
    private OutputCollector _collector = null;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        _collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        if (tuple.getSourceComponent().equals("__system")
                && tuple.getSourceStreamId().equals("__tick")) {
            System.out.println("===========Split============");
            return;
        }

        if (!tuple.contains("sentence")) {
            System.out.println("[---Split---]field isn't exist.");
            return;
        }

        // 接收到一个句子
        String sentence = tuple.getStringByField("sentence");
        // 把句子切割为单词
        StringTokenizer iter = new StringTokenizer(sentence);

        // 发送每一个单词
        while(iter.hasMoreElements()){
            _collector.emit(new Values(iter.nextToken()));
        }
    }
}
