package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class CopyFromLocalFile {
    public static void main(String[] args) throws IOException {
        Path src = new Path("C:/sm_test1.txt");
        Path dst = new Path("hdfs://10.11.0.193:9000/err_xml/rn/sm_test1.txt");

        FileSystem fs = FileSystemUtil.getFileSystem();
        fs.copyFromLocalFile(src, dst);
        fs.close();
    }
}
