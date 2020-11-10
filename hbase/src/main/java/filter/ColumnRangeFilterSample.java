package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 基于列范围过滤数据ColumnRangeFilter
 * minColumn - 列范围的最小值，如果为空，则没有下限
 * minColumnInclusive - 列范围是否包含minColumn
 * maxColumn - 列范围最大值，如果为空，则没有上限
 * maxColumnInclusive - 列范围是否包含maxColumn
 *
 * shell版：
 * scan 'TEST1', {FILTER => "ColumnRangeFilter('d',true,'n',true)"}
 */
public class ColumnRangeFilterSample {
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
        byte[] startColumn = Bytes.toBytes("b");
        byte[] endColumn = Bytes.toBytes("t");
        return new ColumnRangeFilter(startColumn, true, endColumn, true);
    }
}
