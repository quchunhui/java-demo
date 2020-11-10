package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 这个与SingleColumnValueFilter唯一的区别就是，作为筛选条件的列的不会包含在返回的结果中。
 *
 * shell版：
 * scan 'TEST1', {FILTER => "SingleColumnValueExcludeFilter('df','name',=,'substring:z')"}
 */
public class SingleColumnValueExcludeFilterSample {
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
        SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(
                Bytes.toBytes("ex"),
                Bytes.toBytes("address"),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("Beijing"));
        filter.setFilterIfMissing(true);
        return filter;
    }
}
