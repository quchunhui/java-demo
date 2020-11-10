package demo;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class JoinDemo {
    public static void main(String[] args) throws Exception {
        //1.获取执行环境配置信息
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.定义加载或创建数据源（source）,监听9000端口的socket消息
        DataStream<String> textStream9000 = env.socketTextStream("localhost", 9000, "\n");
        DataStream<String> textStream9001 = env.socketTextStream("localhost", 9001, "\n");
        //将输入处理一下，变为tuple2
        DataStream<Tuple2<String,String>> mapStream9000 = textStream9000
            .map(new MapFunction<String, Tuple2<String,String>>() {
                @Override
                public Tuple2<String, String> map(String s) throws Exception {
                    return Tuple2.of(s, "来自9000端口：" + s);
                }
            });

        DataStream<Tuple2<String,String>> mapStream9001 = textStream9001
            .map(new MapFunction<String, Tuple2<String,String>>() {
                @Override
                public Tuple2<String, String> map(String s) throws Exception {
                    return Tuple2.of(s, "来自9001端口：" + s);
                }
            });

        //3.两个流进行join操作，是inner join，关联上的才能保留下来
        DataStream<String> result =  mapStream9000.join(mapStream9001)
            //关联条件，以第0列关联（两个source输入的字符串）
            .where(t1->t1.getField(0)).equalTo(t2->t2.getField(0))
            //以处理时间，每10秒一个滚动窗口
            .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
            //关联后输出
            .apply((t1,t2)->t1.getField(1)+"|"+t2.getField(1));

        //4.打印输出sink
        result.print();

        //5.开始执行
        env.execute();
    }
}
