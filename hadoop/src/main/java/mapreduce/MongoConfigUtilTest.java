package mapreduce;

import com.mongodb.MongoClientURI;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;

public class MongoConfigUtilTest {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        String uri = "mongodb://10.11.2.15:27017,10.11.2.16:27017,10.11.2.17:27017/postal.qch_test";
        MongoConfigUtil.setOutputURI(config, uri);

        MongoClientURI mongoClientURI = MongoConfigUtil.getOutputURI(config);
        System.out.println("hosts=" + mongoClientURI.getHosts());
        System.out.println("database=" + mongoClientURI.getDatabase());
        System.out.println("collection=" + mongoClientURI.getCollection());
    }
}
