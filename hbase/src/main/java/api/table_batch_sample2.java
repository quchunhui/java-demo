package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.ArrayList;
import java.util.List;

public class table_batch_sample2 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        conf.set("hbase.client.write.buffer", "1048576");//1M
        Connection connection = ConnectionFactory.createConnection(conf);
        BufferedMutator mutator = connection.getBufferedMutator(TableName.valueOf(constants.TABLE_NAME));

        List<Mutation> batch = new ArrayList<>();
        byte[] row_key = random.getRowKey();

        Put put = new Put(row_key);
        put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes(), random.getName());
        put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes(), random.getSex());
        put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes(), random.getHeight());
        put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes(), random.getWeight());
        batch.add(put);
        Delete delete = new Delete(row_key);
        delete.addFamily(constants.COLUMN_FAMILY_DF.getBytes());
        delete.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes());
        batch.add(delete);
        mutator.mutate(batch);

        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));
        Get get = new Get(row_key);
        Result result1 = table.get(get);
        System.out.print("[------]name=" + getValue(result1, constants.COLUMN_FAMILY_DF, "name") + "\n");
        System.out.print("[------]sex=" + getValue(result1, constants.COLUMN_FAMILY_DF, "sex") + "\n");
        System.out.print("[------]height=" + getValue(result1, constants.COLUMN_FAMILY_EX, "height") + "\n");
        System.out.print("[------]weight=" + getValue(result1, constants.COLUMN_FAMILY_EX, "weight") + "\n");

        mutator.flush();
        Result result2 = table.get(get);
        System.out.print("[------]name=" + getValue(result2, constants.COLUMN_FAMILY_DF, "name") + "\n");
        System.out.print("[------]sex=" + getValue(result2, constants.COLUMN_FAMILY_DF, "sex") + "\n");
        System.out.print("[------]height=" + getValue(result2, constants.COLUMN_FAMILY_EX, "height") + "\n");
        System.out.print("[------]weight=" + getValue(result2, constants.COLUMN_FAMILY_EX, "weight") + "\n");

        table.close();
        mutator.close();
        connection.close();
    }

    private static String getValue(Result rs, String family, String column) {
        byte[] value = rs.getValue(family.getBytes(), column.getBytes());
        if (value == null) {
            return "";
        } else {
            return new String(value);
        }
    }
}
