package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.UUID;

public class table_put_sample1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        for (int i = 0; i < 1000; i++) {
            Put put = new Put(("row" + UUID.randomUUID().toString()).getBytes());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes(), random.getName());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes(), random.getSex());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes(), random.getHeight());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes(), random.getWeight());
            table.put(put);
            System.out.print("[------]put i=" + i + "\n");
        }
        table.close();
        connection.close();
    }
}
