package mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ICRISEnglish {
    private static Path srcPath = new Path("hdfs:///cr/tbwj");
    private static Path dstPath = new Path("hdfs:///cr/tbwj/result");
    private static Path localPth = new Path("/home/qch/work/wordcount");

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        initDstPath(conf);

        Job job = Job.getInstance(conf);
        job.setJobName("ICRISEnglish");
        job.setJarByClass(ICRISEnglish.class);
        job.setMapperClass(WordSplitMapper.class);
        job.setReducerClass(WordCountReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, srcPath);
        FileOutputFormat.setOutputPath(job, dstPath);

        if (job.waitForCompletion(true)) {
            copyToLocal(conf);
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    private static void initDstPath(Configuration conf) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(dstPath)) {
            fs.delete(dstPath, true);
        }
    }

    private static void copyToLocal(Configuration conf) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        fs.copyToLocalFile(false, dstPath, localPth);
    }

    static class WordSplitMapper extends Mapper<Object, Text, Text, IntWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            String word;
            while (itr.hasMoreTokens()){
                word = itr.nextToken();
                if (word == null || word.isEmpty()) {
                    continue;
                }
                if (word.length() <= 1 || word.length() >= 20) {
                    continue;
                }
                if (isContainsNumbers(word)) {
                    continue;
                }
                if (!isWord(word)) {
                    continue;
                }
                word = replaceSpecialWord(word);
                context.write(new Text(word), new IntWritable(1));
            }
        }

        private boolean isWord(String word) {
            Pattern p = Pattern.compile("[a-zA-z]");
            return p.matcher(word).find();
        }

        private boolean isContainsNumbers(String word) {
            Pattern p = Pattern.compile(".*\\d+.*");
            return p.matcher(word).find();
        }

        private String replaceSpecialWord(String word) {
            return word.replaceAll("�|\\(|\\)|\\?|<|>|\\*", "");
        }
    }

    static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values){
                sum += val.get();
            }
            IntWritable result = new IntWritable();
            result.set(sum);
            context.write(key, result);
        }
    }
}
