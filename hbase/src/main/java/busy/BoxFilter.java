package busy;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class BoxFilter {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "10.11.2.4,10.11.2.5,10.11.2.6");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        Connection connection = ConnectionFactory.createConnection(conf);

        Scan scan = new Scan();
        scan.setCacheBlocks(false);
        scan.setFilter(getFilter());

        Table table = connection.getTable(TableName.valueOf("BOX"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner){
            //是丰巢
            String enterpriseId = getStrByByte(result.getValue( Bytes.toBytes("if"), Bytes.toBytes("enterpriseId")));
            if (!"FC".equals(enterpriseId)) {
                continue;
            }

            //有入柜时间
            String inCabinetTime = getStrByByte(result.getValue( Bytes.toBytes("if"), Bytes.toBytes("InCabinetTime")));
            if (!"".equals(inCabinetTime)) {
                continue;
            }

            //无取走时间
            String takeAwayTime = getStrByByte(result.getValue( Bytes.toBytes("if"), Bytes.toBytes("takeAwayTime")));
            if ("".equals(takeAwayTime)) {
                String trackNumber = getStrByByte(result.getValue( Bytes.toBytes("if"), Bytes.toBytes("trackNumber")));
                System.out.println("[-----]" + trackNumber);
                break;
            }
        }

        scanner.close();
        table.close();
        connection.close();
    }

    private static Filter getFilter() {
        return new SingleColumnValueFilter(
                Bytes.toBytes("if"), Bytes.toBytes("InCabinetTime"), CompareFilter.CompareOp.EQUAL, new SubstringComparator("20181108"));
    }

    private static String getStrByByte(byte[] by) {
        String str = "";
        if (by != null && by.length > 0) {
            str = Bytes.toString(by);
        }
        return str;
    }
}
