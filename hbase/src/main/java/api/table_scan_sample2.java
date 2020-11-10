package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

public class table_scan_sample2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        Scan scan = new Scan();
        scan.addFamily(constants.COLUMN_FAMILY_DF.getBytes());

        ResultScanner rs = table.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            byte[] row_key = r.getRow();
            byte[] name = r.getValue(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes());
            System.out.print("[------]row_key=" + new String(row_key) + "\n");
            System.out.print("[------]name=" + new String(name) + "\n");
        }

        table.close();
        connection.close();
    }
}
