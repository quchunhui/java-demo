package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.ArrayList;
import java.util.List;

public class table_put_sample4 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        conf.set("hbase.client.write.buffer", "1048576");//1M
        Connection connection = ConnectionFactory.createConnection(conf);
        BufferedMutator table = connection.getBufferedMutator(TableName.valueOf(constants.TABLE_NAME));

        List<Put> puts = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Put put = new Put(random.getRowKey());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes(), random.getName());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes(), random.getSex());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes(), random.getHeight());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes(), random.getWeight());
            puts.add(put);
        }

        table.mutate(puts);
        table.flush();
        table.close();
        connection.close();
    }
}