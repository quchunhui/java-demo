package demo;

import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class CoGroupDemo {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境配置信息
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.定义加载或创建数据源（source）,监听9000端口的socket消息
        DataStream<String> textStream9000 = env.socketTextStream("localhost", 9000, "\n");
        DataStream<String> textStream9001 = env.socketTextStream("localhost", 9001, "\n");
        //将输入处理一下，变为tuple2
        DataStream<Tuple2<String, String>> mapStream9000 = textStream9000
            .map(new MapFunction<String, Tuple2<String, String>>() {
                @Override
                public Tuple2<String, String> map(String s) throws Exception {
                    return Tuple2.of(s, "来自9000端口：" + s);
                }
            });

        DataStream<Tuple2<String, String>> mapStream9001 = textStream9001
            .map(new MapFunction<String, Tuple2<String, String>>() {
                @Override
                public Tuple2<String, String> map(String s) throws Exception {
                    return Tuple2.of(s, "来自9001端口：" + s);
                }
            });

        //3.两个流进行coGroup操作,没有关联上的也保留下来，功能更强大
        DataStream<String> result = mapStream9000.coGroup(mapStream9001)
            //关联条件，以第0列关联（两个source输入的字符串）
            .where(t1 -> t1.getField(0)).equalTo(t2 -> t2.getField(0))
            //以处理时间，每10秒一个滚动窗口
            .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
            //关联后输出
            .apply(new CoGroupFunction<Tuple2<String, String>, Tuple2<String, String>, String>() {
                @Override
                public void coGroup(Iterable<Tuple2<String, String>> iterable, Iterable<Tuple2<String, String>> iterable1, Collector<String> collector) throws Exception {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("来自9000的stream:");
                    for (Tuple2<String, String> item : iterable) {
                        stringBuffer.append(item.f1 + ",");
                    }
                    stringBuffer.append("来自9001的stream:");
                    for (Tuple2<String, String> item : iterable1) {
                        stringBuffer.append(item.f1 + ",");
                    }
                    collector.collect(stringBuffer.toString());
                }
            });

        //4.打印输出sink
        result.print();
        //5.开始执行
        env.execute();
    }
}
