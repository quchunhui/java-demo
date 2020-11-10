package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class truncate_preserve {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();

        String[] tableList = {
                "SJYB_ANE",
                "SJYB_APEX",
                "SJYB_BSHT",
                "SJYB_CAE",
                "SJYB_DEPPON",
                "SJYB_DHL",
                "SJYB_EMS",
                "SJYB_FEDEX",
                "SJYB_GTO",
                "SJYB_JBD",
                "SJYB_KJ",
                "SJYB_KYE",
                "SJYB_PJ",
                "SJYB_SF",
                "SJYB_STO",
                "SJYB_SUR",
                "SJYB_TNT",
                "SJYB_TTKD",
                "SJYB_UC",
                "SJYB_UPS",
                "SJYB_YTO",
                "SJYB_YUNDA",
                "SJYB_ZGYZ",
                "SJYB_ZJS",
                "SJYB_ZTO",
        };

        for (String table : tableList) {
            TableName table_name = TableName.valueOf(table);
            if (admin.tableExists(table_name)) {
                admin.disableTable(table_name);
                admin.truncateTable(table_name, true);
            }
        }
        admin.close();
        connection.close();
    }
}
