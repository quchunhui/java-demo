package demo;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * 注意:
 * Connect可以用来合并两种不同类型的流。
 * Connect合并后，可用map中的CoMapFunction或flatMap中的CoFlatMapFunction来对合并流中的每个流进行处理。
 */
public class UnionStreamByConnect {
    public static void main(String[] args) throws Exception{
        //运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //输入数据源source1
        DataStreamSource<Tuple3<String, String, String>> source1 = env.fromElements(
            new Tuple3<>("productID1", "click", "user_1")
        );

        //输入数据源source2
        DataStreamSource<String> source2 = env.fromElements(
            "productID3:click:user_1",
            "productID3:browse:user_2"
        );

        //1、合并流
        ConnectedStreams<Tuple3<String, String, String>, String> connectedStream = source1.connect(source2);

        //2、用CoMap处理合并后的流
        SingleOutputStreamOperator<Tuple2<String, String>> resultStream =
            connectedStream.map(new CoMapFunction<Tuple3<String, String, String>, String, Tuple2<String, String>>() {
            //定义第一个流的处理逻辑
            @Override
            public Tuple2<String, String> map1(Tuple3<String, String, String> value) throws Exception {
                return new Tuple2<>(value.f1, value.f2);
            }

            //定义第二个流的处理逻辑
            @Override
            public Tuple2<String, String> map2(String value) throws Exception {
                String[] valueSplit = value.split(":");
                return new Tuple2<>(valueSplit[1], valueSplit[2]);
            }
        });

        resultStream.print();
        env.execute();
    }
}
