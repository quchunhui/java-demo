package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.ArrayList;
import java.util.List;

public class table_get_sample4 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        List<Get> gets = new ArrayList<>();
        Get get1 = new Get(("row01").getBytes());
        get1.addFamily(constants.COLUMN_FAMILY_DF.getBytes());
        get1.addFamily(constants.COLUMN_FAMILY_EX.getBytes());
        gets.add(get1);
        Get get2 = new Get(("row02").getBytes());
        get2.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes());
        get2.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes());
        get2.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes());
        get2.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes());
        gets.add(get2);

        Result[] results = table.get(gets);
        for ( Result result : results) {
            byte[] name = result.getValue(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes());
            byte[] sex = result.getValue(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes());
            byte[] height = result.getValue(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes());
            byte[] weight = result.getValue(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes());
            System.out.print("[------]name=" + new String(name) + "\n");
            System.out.print("[------]sex=" + new String(sex) + "\n");
            System.out.print("[------]height=" + new String(height) + "\n");
            System.out.print("[------]weight=" + new String(weight) + "\n");
        }

        table.close();
        connection.close();
    }
}
