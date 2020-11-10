package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 当需要根据行键特征查找一个范围的行数据时，使用Scan的startRow和stopRow会更高效，
 * 但是，startRow和stopRow只能匹配行键的开始字符，而不能匹配中间包含的字符：
 * 当需要针对行键进行更复杂的过滤时，可以使用RowFilter。
 *
 * shell版：
 * scan 'TEST1', {FILTER => "RowFilter(=,'substring:row')"}
 */
public class RowFilterSample {
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
        return new RowFilter(
                CompareFilter.CompareOp.EQUAL,
                new SubstringComparator("row"));
    }
}
