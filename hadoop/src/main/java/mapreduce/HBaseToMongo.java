package mapreduce;

import com.mongodb.BasicDBObject;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.util.UUID;

public class HBaseToMongo {
    public static void main(String[] args) throws Exception {
        Long st = System.currentTimeMillis();

        Configuration config = new Configuration();
        config.set("dfs.socket.timeout", "180000");
        config.set("hbase.zookeeper.property.clientPort", "2181");
        config.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");

        // The format of the URI is:
        // mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
        String uri = "mongodb://10.11.2.15:27017,10.11.2.16:27017,10.11.2.17:27017/postal.qch_test";
        MongoConfigUtil.setOutputURI(config, uri);

        Job job = Job.getInstance(config);
        job.setJobName("HBaseToMongo");
        job.setJarByClass(FilterMapper.class);
        job.setOutputFormatClass(MongoOutputFormat.class);
        job.setNumReduceTasks(0);
        TableMapReduceUtil.initTableMapperJob("qch_t1", new Scan(),
                FilterMapper.class, ImmutableBytesWritable.class, BSONWritable.class, job);

        System.exit( job.waitForCompletion( true ) ? 0 : 1 );
        System.out.println("HBaseToMongo:" + (System.currentTimeMillis() - st));
    }

    static class FilterMapper extends TableMapper<Text, BSONWritable> {
        @Override
        protected void map(ImmutableBytesWritable key,
                           Result value, Context context) throws IOException, InterruptedException {
            String col = getStrByByte(value.getValue("if".getBytes(), "col1".getBytes()));
            BSONWritable bsonWritable = new BSONWritable();
            BasicDBObject doc = new BasicDBObject();
            doc.put("_id", UUID.randomUUID().toString());
            doc.put("col", col);
            bsonWritable.setDoc(doc);
            context.write(new Text(key.toString()), bsonWritable);
        }

        private String getStrByByte(byte[] by) {
            String str = "";
            if (by != null && by.length > 0) {
                str = Bytes.toString(by);
            }
            return str;
        }
    }
}