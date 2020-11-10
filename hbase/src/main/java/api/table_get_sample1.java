package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class table_get_sample1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.0.193,10.11.0.194,10.11.0.195");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf("TBDS_201806"));

        Get get = new Get(("row01").getBytes());
        get.setCacheBlocks(true);

        Result result = table.get(get);
        for  (Cell cell : result.rawCells()) {
            System. out .println(
                    "RowKey:" + Bytes.toString (result.getRow()) +
                    "Familiy:Quilifier:" + Bytes.toString(CellUtil.cloneQualifier(cell)) +
                    "Value:" + Bytes.toString(CellUtil.cloneValue (cell)) + "\n");
        }
        byte[] name = result.getValue(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes());
        byte[] weight = result.getValue(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes());
        System.out.print("[------]name=" + new String(name) + "\n");
        System.out.print("[------]name=" + new String(weight) + "\n");

        table.close();
        connection.close();
    }
}
