package demo;

import java.util.ArrayList;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * 注意：
 * Split...Select...中Split只是对流中的数据打上标记,并没有将流真正拆分。可通过Select算子将流真正拆分出来。
 * Split...Select...不能连续分流。即不能Split...Select...Split，但可以如Split...Select...Filter...Split。
 * Split...Select...已经过时，推荐使用更灵活的侧路输出(Side-Output)，如下。
 */
public class SplitStreamBySplit {
    public static void main(String[] args) throws Exception{
        //运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //输入数据源
        DataStreamSource<Tuple3<String, String, String>> source = env.fromElements(
            new Tuple3<>("productID1", "click", "user_1"),
            new Tuple3<>("productID1", "click", "user_2"),
            new Tuple3<>("productID1", "browse", "user_1"),
            new Tuple3<>("productID2", "browse", "user_1"),
            new Tuple3<>("productID2", "click", "user_2"),
            new Tuple3<>("productID2", "click", "user_1")
        );

        //1、定义拆分逻辑
        SplitStream<Tuple3<String, String, String>>
            splitStream = source.split(new OutputSelector<Tuple3<String, String, String>>() {
            @Override
            public Iterable<String> select(Tuple3<String, String, String> value) {
                ArrayList<String> output = new ArrayList<>();
                if (value.f0.equals("productID1")) {
                    output.add("productID1");
                } else if (value.f0.equals("productID2")) {
                    output.add("productID2");
                }
                return output;
            }
        });

        //2、将流真正拆分出来
        splitStream.select("productID1").print();
        //开始执行
        env.execute();
    }
}
