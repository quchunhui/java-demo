package tools;

import filter.Constants;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

public class map_filter {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", Constants.HBASE_ZOOKEEPER_QUORUM);
        conf.set("hbase.zookeeper.property.clientPort", "2181");

        Job job = Job.getInstance(conf);
        //设置作业名称
        job.setJobName("map_filter");
        //设置作业执行类
        job.setJarByClass(map_filter.class);
        //设置输出类型
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);
        //设置Reduce数
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(MultiTableOutputFormat.class);

        Scan scan = new Scan();
        if (args.length >= 1 && "SingleColumnValueFilter".equals(args[0])) {
            //设置hbase作为map的输入条件
            SingleColumnValueFilter filter = new SingleColumnValueFilter(
                    Bytes.toBytes("df"),
                    Bytes.toBytes("name"),
                    CompareFilter.CompareOp.NOT_EQUAL,
                    Bytes.toBytes("zhangsan")
            );
            scan.setFilter(filter);
        }
        scan.setCacheBlocks(false);
        TableMapReduceUtil.initTableMapperJob("TEST1", scan,
                Mapper.class, ImmutableBytesWritable.class, Put.class, job);

        if (job.waitForCompletion(true)) {
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    static class Mapper extends TableMapper<ImmutableBytesWritable, Put> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context)
                throws IOException, InterruptedException {

        }
    }
}
