package examples.wordcount.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class WordCountBolt extends BaseRichBolt {
    private Map<String, Integer> counts = new HashMap<>();

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public void execute(Tuple tuple) {
        if (tuple.getSourceComponent().equals("__system")
                && tuple.getSourceStreamId().equals("__tick")) {
            System.out.println("===========Count============");
            return;
        }

        if (!tuple.contains("word")) {
            System.out.println("[---Count---]field isn't exist.");
            return;
        }

        // 接收一个单词
        String word = tuple.getStringByField("word");
        // 获取该单词对应的计数
        Integer count = counts.get(word);

        // 计数增加
        if(count == null) {
            count = 0;
        } else {
            count++;
        }

        // 将单词和对应的计数加入map中
        counts.put(word, count);
        System.out.println(word + ":" + count);
    }
}
