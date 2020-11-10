package examples.wordcount.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

public class RandomSentencesSpout extends BaseRichSpout {
    // 句子数组
    private String[] sentences = new String[] {
            "Hello LaoNiu Bye LaoNiu",
            "Hello Storm Word Count",
            "Bye Storm Hello Storm",
            "Hello LaoNiu Count"
    };

    private SpoutOutputCollector _collector = null;
    private Random _rand = null;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 定义一个字段sentences
        declarer.declare(new Fields("sentence"));
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        _collector = collector;
        _rand = new Random();
    }

    @Override
    public void nextTuple() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // 随机选择一个句子
        String sentence = sentences[_rand.nextInt(sentences.length)];

        // 发射该句子给Bolt
        System.out.print("[--------------]Spout emit=" + sentence + "\n");
        _collector.emit(new Values(sentence));
    }
}