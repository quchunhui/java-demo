package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 该过滤器允许针对返回给客户端的时间版本进行更细粒度的控制。
 * 使用的时候，可以提供一个返回的时间戳的列表，只有与时间戳匹配的单元才可以返回。
 * 当做多行扫描或者是单行检索时，如果需要一个时间区间，可以在Get或Scan对象上使用setTimeRange()方法来实现这一点
 *
 * shell版：
 * scan 'TEST1', {FILTER => "TimestampsFilter(1539853098209, 1539852981188)"}
 */
public class TimestampsFilterSample {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", Constants.HBASE_ZOOKEEPER_QUORUM);
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection connection = ConnectionFactory.createConnection(conf);

        Scan scan = new Scan();
        scan.setFilter(getFilter());

        Table table = connection.getTable(TableName.valueOf("TEST1"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner){
            String row = Bytes.toString(result.getRow());
            Utils.printCells(row, result.rawCells());
        }
        scanner.close();

        table.close();
        connection.close();
    }

    private static Filter getFilter() {
        List<Long> timestamps = new ArrayList<>();
        timestamps.add(1539853098209L);
        timestamps.add(1539852981188L);
        timestamps.add(1539858393829L);
        return new TimestampsFilter(timestamps);
    }
}
