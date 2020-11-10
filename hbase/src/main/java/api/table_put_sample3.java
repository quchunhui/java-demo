package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.ArrayList;
import java.util.List;

public class table_put_sample3 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        List<Row> batch = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            Put put = new Put(random.getRowKey());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes(), random.getName());
            put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes(), random.getSex());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes(), random.getHeight());
            put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes(), random.getWeight());
            batch.add(put);
        }

        Object[] results = new Object[batch.size()];
        try {
            table.batch(batch, results);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        batch.clear();
        table.close();
        connection.close();
    }
}
