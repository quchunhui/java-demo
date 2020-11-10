package api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class table_batch_sample3 {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.1.80,192.168.1.81,192.168.1.82");
        conf.set("hbase.client.write.buffer", "1048576");//1M
        Connection connection = ConnectionFactory.createConnection(conf);

        TableName tableName = TableName.valueOf(constants.TABLE_NAME);
        final BufferedMutator.ExceptionListener listener = (e, mutator) -> {
            for (int i = 0; i < e.getNumExceptions(); i++) {
                System.out.println("Failed to sent put <<" + e.getRow(i) + ">> to HBase.");
            }
        };
        BufferedMutatorParams params = new BufferedMutatorParams(tableName).listener(listener);
        params.writeBufferSize(2 * 1024 * 1024);    //2M
        BufferedMutator mutator = connection.getBufferedMutator(params);

        List<Mutation> batch = new ArrayList<>();
        Put put = new Put(random.getRowKey());
        put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "name".getBytes(), random.getName());
        put.addColumn(constants.COLUMN_FAMILY_DF.getBytes(), "sex".getBytes(), random.getSex());
        put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "height".getBytes(), random.getHeight());
        put.addColumn(constants.COLUMN_FAMILY_EX.getBytes(), "weight".getBytes(), random.getWeight());
        batch.add(put);
        mutator.mutate(batch);
        mutator.flush();

        mutator.close();
        connection.close();
    }
}
