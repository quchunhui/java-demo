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
 * 用于综合使用多个过滤器。其有两种关系：
 * FilterList.Operator.MUST_PASS_ONE
 * FilterList.Operator.MUST_PASS_ALL（默认），
 * 顾名思义，它们分别是AND和OR的关系，
 * 并且FilterList可以嵌套使用FilterList，使我们能够表达更多的需求
 *
 * shell版：不支持
 */
public class FilterListSample {
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
        SingleColumnValueFilter filter1 = new SingleColumnValueFilter(
                Bytes.toBytes("df"), Bytes.toBytes("name"), CompareFilter.CompareOp.NOT_EQUAL, Bytes.toBytes("zhangsan"));
        SingleColumnValueFilter filter2 = new SingleColumnValueFilter(
                Bytes.toBytes("df"), Bytes.toBytes("name"), CompareFilter.CompareOp.NOT_EQUAL, Bytes.toBytes("lisi"));
        List<Filter> filters = new ArrayList<>();
        filters.add(filter1);
        filters.add(filter2);
        return new FilterList(FilterList.Operator.MUST_PASS_ALL, filters);
    }
}
