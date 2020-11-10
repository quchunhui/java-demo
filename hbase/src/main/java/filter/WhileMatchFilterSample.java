package filter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 这个过滤器的应用场景也很简单，如果你想要在遇到某种条件数据之前的数据时，就可以使用这个过滤器；
 * 当遇到不符合设定条件的数据的时候，整个扫描也就结束了。
 *
 * shell版：不支持
 */
public class WhileMatchFilterSample {
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
        //row01 column=df:name, timestamp=1532059727796, value=lisi
        //row01 column=df:sex, timestamp=1532059770595, value=men
        //row01 column=ex:height, timestamp=1539853065621, value=166
        //row01 column=ex:weight, timestamp=1539853080223, value=70
        //row02 column=df:name, timestamp=1539853098209, value=zhangsan
        //row02 column=df:sex, timestamp=1539853116629, value=wemon
        //row02 column=ex:height, timestamp=1539852981188, value=175
        //row02 column=ex:weight, timestamp=1539853001889, value=68
        //----------------这以后的就不会显示了-----------------
        //row04 column=df:name, timestamp=1539858361965, value=zhangsan
        //row04 column=df:sex, timestamp=1539858393829, value=men
        ValueFilter valueFilter = new ValueFilter(
                CompareFilter.CompareOp.NOT_EQUAL,
                new BinaryComparator(Bytes.toBytes("68")));
        return new WhileMatchFilter(valueFilter);
    }
}
