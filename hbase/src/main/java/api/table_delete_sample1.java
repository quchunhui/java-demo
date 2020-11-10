package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

public class table_delete_sample1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        Delete delete = new Delete("row02".getBytes());
        delete.addFamily(constants.COLUMN_FAMILY_DF.getBytes());
        delete.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes());

        table.delete(delete);
        table.close();
        connection.close();
    }
}
