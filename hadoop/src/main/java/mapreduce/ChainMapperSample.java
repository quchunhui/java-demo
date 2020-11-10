package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

public class ChainMapperSample {
    static class Mapper1 extends Mapper<Text, Text, Text, Text> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String kval = key.toString() + ":map1k";
            String dt = value.toString() + ":map1v";
            context.write(new Text(kval), new Text(dt));
        }
    }

    static class Mapper2 extends Mapper<Text, Text, Text, Text> {
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String kval = key.toString() + ":map2k";
            String dt = value.toString() + ":map2v";
            context.write(new Text(kval), new Text(dt));
        }
    }

    static class Reducer1 extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values){
                String kval = key.toString() + ":reduce1k";
                String dt = value.toString() + ":reduce1v";
                context.write(new Text(kval), new Text(dt));
            }
        }
    }

    static class Mapper3 extends Mapper<Text, Text, Text, Text> {
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String kval = key.toString() + ":map3k";
            String dt = value.toString() + ":map3v";
            context.write(new Text(kval), new Text(dt));
        }
    }

    private void run(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        Configuration configuration = new Configuration();
        String[] otherArgs = new GenericOptionsParser(configuration, args).getRemainingArgs();
        if (otherArgs.length != 2) {
            return;
        }

        Job job = Job.getInstance(configuration, "ChainMapperJob");
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setJarByClass(ChainMapperSample.class);
        ChainMapper.addMapper(job, Mapper1.class, Text.class, Text.class, Text.class, Text.class, configuration);
        ChainMapper.addMapper(job, Mapper2.class, Text.class, Text.class, Text.class, Text.class, configuration);
        ChainReducer.setReducer(job, Reducer1.class, Text.class, Text.class, Text.class, Text.class, configuration);
        ChainReducer.addMapper(job, Mapper3.class, Text.class, Text.class, Text.class, Text.class, configuration);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        if (job.waitForCompletion(true)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    public static void main(String args[]) throws IOException,
            InterruptedException, ClassNotFoundException {
        ChainMapperSample chainMapper = new ChainMapperSample();
        chainMapper.run(args);
    }
}