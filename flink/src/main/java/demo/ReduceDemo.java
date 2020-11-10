package demo;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

public class ReduceDemo {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境配置信息
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.定义加载或创建数据源（source）,监听端口的socket消息
        DataStream<String> textStream =
            env.socketTextStream("localhost", Constants.PORT, "\n");

        //3.
        DataStream<Tuple2<String, Integer>> result = textStream
            //map是将每一行单词变为一个tuple2
            .map(line -> Tuple2.of(line.trim(), 1))
            //如果要用Lambda表示是，Tuple2是泛型，那就得用returns指定类型。
            .returns(Types.TUPLE(Types.STRING, Types.INT))
            //keyBy进行分区，按照第一列，也就是按照单词进行分区
            .keyBy(0)
            //指定窗口，每10秒个计算一次
            .timeWindow(Time.of(10, TimeUnit.SECONDS))
            //对每一组内的元素进行归并操作，即第一个和第二个归并，结果再与第三个归并...
            .reduce((Tuple2<String, Integer> t1, Tuple2<String, Integer> t2) -> new Tuple2(t1.f0, t1.f1 + t2.f1));

        //4.打印输出sink
        result.print();
        //5.开始执行
        env.execute();
    }
}
