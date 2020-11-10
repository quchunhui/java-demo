package demo;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * 注意：
 * Side-Output是从Flink 1.3.0开始提供的功能，支持了更灵活的多路输出。
 * Side-Output可以以侧流的形式，以不同于主流的数据类型，向下游输出指定条件的数据、异常数据、迟到数据等等。
 * Side-Output通过ProcessFunction将数据发送到侧路OutputTag。
 */
public class SplitStreamBySideOutput {
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

        //1、定义OutputTag
        OutputTag<Tuple3<String, String, String>>
            sideOutputTag = new OutputTag<Tuple3<String, String, String>>("side-output-tag"){};

        //2、在ProcessFunction中处理主流和分流
        SingleOutputStreamOperator<Tuple3<String, String, String>> processedStream =
            source.process(new ProcessFunction<Tuple3<String, String, String>, Tuple3<String, String, String>>() {
            @Override
            public void processElement(Tuple3<String, String, String> value, Context ctx,
                Collector<Tuple3<String, String, String>> out) throws Exception {
                //侧流-只输出特定数据
                if (value.f0.equals("productID1")) {
                    System.out.println("1");
                    ctx.output(sideOutputTag, value);
                //主流
                }else {
                    System.out.println("2");
                    out.collect(value);
                }
            }
        });

        //获取主流
        processedStream.print();
        //获取侧流
        processedStream.getSideOutput(sideOutputTag).print();
        //开始执行
        env.execute();
    }
}
