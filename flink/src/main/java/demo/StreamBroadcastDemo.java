package demo;

import java.util.concurrent.TimeUnit;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.util.Collector;

public class StreamBroadcastDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度
        env.setParallelism(1);

        //自定义广播流，产生拦截数据的配置信息
        DataStreamSource<String> filterData = env.addSource(new RichSourceFunction<String>() {
            private boolean isRunning = true;

            //测试数据集
            String[] data = new String[]{"java", "python", "scala"};

            /**
             * 模拟数据源，每1分钟产生一次数据，实现数据的跟新
             */
            @Override
            public void run(SourceContext <String> cxt) throws Exception {
                int size = data.length;
                while (isRunning) {
                    TimeUnit.MINUTES.sleep(1);
                    int seed = (int) (Math.random() * size);
                    //在数据集中随机生成一个数据进行发送
                    cxt.collect(data[seed]);
                    System.out.println("发送的关键字是：" + data[seed]);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        //1、定义数据广播的规则：
        MapStateDescriptor<String, String> configFilter = new MapStateDescriptor <>(
            "configFilter",
            BasicTypeInfo.STRING_TYPE_INFO,
            BasicTypeInfo.STRING_TYPE_INFO);

        //2、对filterData进行广播
        BroadcastStream<String> broadcastConfig = filterData.setParallelism(1).broadcast(configFilter);

        //定义数据集
        DataStreamSource <String> dataStream = env.addSource(new RichSourceFunction <String>() {
            private boolean isRunning = true;
            //测试数据集
            String[] data = new String[]{
                "java代码量太大",
                "python代码量少，易学习",
                "php是web开发语言",
                "scala流式处理语言，主要应用于大数据开发场景",
                "go是一种静态强类型、编译型、并发型，并具有垃圾回收功能的编程语言"
            };

            /**
             * 模拟数据源，每3s产生一次
             */
            @Override
            public void run(SourceContext <String> ctx) throws Exception {
                int size = data.length;
                while (isRunning) {
                    TimeUnit.SECONDS.sleep(3);
                    int seed = (int) (Math.random() * size);
                    //在数据集中随机生成一个数据进行发送
                    ctx.collect(data[seed]);
                    System.out.println("上游发送的消息：" + data[seed]);
                }
            }

            @Override
            public void cancel() {
                isRunning = false;
            }
        });

        //3、dataStream对广播的数据进行关联（使用connect进行连接）
        DataStream<String> result = dataStream.connect(broadcastConfig).process(
            new BroadcastProcessFunction<String, String, String>() {

            //拦截的关键字
            private String keyWords = null;

            /**
             * open方法只会执行一次
             * 可以在这实现初始化的功能
             * 4、设置keyWords的初始值，否者会报错：java.lang.NullPointerException
             */
            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                keyWords="java";
                System.out.println("初始化keyWords：java");
            }

            /**
             * 6、处理流中的数据
             */
            @Override
            public void processElement(String value, ReadOnlyContext ctx, Collector<String> out) {
                if (value.contains(keyWords)) {
                    out.collect("拦截消息:" + value + ", 原因:包含拦截关键字：" + keyWords);
                }
            }

            /**
             * 5、对广播变量的获取更新
             */
            @Override
            public void processBroadcastElement(String value, Context ctx, Collector <String> out) {
                keyWords = value;
                System.out.println("更新关键字：" + value);
            }
        });

        result.print();

        env.execute("broadcast test");
    }
}