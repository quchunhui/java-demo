package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 根据列的值来决定这一行数据是否返回，落脚点在行，而不是列。
 * 可以设置filter.setFilterIfMissing(true);
 * 如果为true，当这一列不存在时，不会返回，
 * 如果为false，当这一列不存在时，会返回所有的列信息
 *
 * shell版：
 * scan 'TEST1', {FILTER => "SingleColumnValueFilter('df','name',=,'substring:zhangsan')"}
 */
public class SingleColumnValueFilterSample {
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
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                Bytes.toBytes("ex"),
                Bytes.toBytes("address"),
                CompareFilter.CompareOp.NOT_EQUAL,
                new SubstringComparator("Beijing"));
        filter.setFilterIfMissing(false);
        return filter;
    }
}