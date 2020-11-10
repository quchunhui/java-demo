package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

public class table_scan_sample1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.0.193,10.11.0.194,10.11.0.195");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("TBDS_201806"));

        Scan scan = new Scan();

        ResultScanner rs = table.getScanner(scan);
        for (Result r = rs.next(); r != null; r = rs.next()) {
            byte[] row_key = r.getRow();
            byte[] receiver_city = r.getValue("if".getBytes(), "receiver_city".getBytes());
            byte[] receiver_prov = r.getValue("if".getBytes(), "receiver_prov".getBytes());
            System.out.print("[------]row_key=" + new String(row_key) + "\n");
            if (receiver_city != null) {
                System.out.print("[------]receiver_city=" + new String(receiver_city) + "\n");
            }
            if (receiver_prov != null) {
                System.out.print("[------]receiver_prov=" + new String(receiver_prov) + "\n");
            }
        }

        table.close();
        connection.close();
    }
}
