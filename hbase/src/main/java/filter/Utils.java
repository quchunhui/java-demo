package filter;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.util.Bytes;

public class Utils {
    public static void printCells(String row, Cell[] cells) {
        for  (Cell cell : cells) {
            String family = Bytes.toString(CellUtil.cloneFamily(cell));
            String qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            System.out.println("[row:"+row+"],[family:"+family+"],[qualifier:"+qualifier+"]"+ ",[value:"+value+"],[time:"+cell.getTimestamp()+"]");
        }
    }
}
