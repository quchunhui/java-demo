package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class major_compact {
    public static void main(String[] args) throws IOException {
        String[] tableList = {
                "ANE",
                "APEX",
                "BSHT",
                "CAE",
                "DEPPON",
                "DHL",
                "EMS",
                "FEDEX",
                "GTO",
                "JBD",
                "KJ",
                "KYE",
                "PJ",
                "SF",
                "STO",
                "SUR",
                "TNT",
                "TTKD",
                "UC",
                "UPS",
                "YTO",
                "YUNDA",
                "ZGYZ",
                "ZJS",
                "ZTO",
        };

        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        for (String table : tableList) {
            TableName table_name = TableName.valueOf("DBN_" + table);
            if (admin.tableExists(table_name)) {
                admin.majorCompact(table_name);
                System.out.println("[------]major compact " + table_name.getNameAsString());
            }
        }

        admin.close();
        connection.close();
    }
}
