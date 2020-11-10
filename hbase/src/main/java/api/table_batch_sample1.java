package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.util.ArrayList;
import java.util.List;

public class table_batch_sample1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        Connection connection = ConnectionFactory.createConnection(conf);
        Table table = connection.getTable(TableName.valueOf(constants.TABLE_NAME));

        List<Row> batch = new ArrayList<>();
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

        Object[] results = new Object[batch.size()];
        try {
            table.batch(batch, results);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        Get get = new Get(row_key);
        Result result = table.get(get);
        String name = getValue(result, constants.COLUMN_FAMILY_DF, "name");
        String sex = getValue(result, constants.COLUMN_FAMILY_DF, "sex");
        String height = getValue(result, constants.COLUMN_FAMILY_EX, "height");
        String weight = getValue(result, constants.COLUMN_FAMILY_EX, "weight");
        System.out.print("[------]name=" + name + "\n");
        System.out.print("[------]sex=" + sex + "\n");
        System.out.print("[------]height=" + height + "\n");
        System.out.print("[------]weight=" + weight + "\n");

        table.close();
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
