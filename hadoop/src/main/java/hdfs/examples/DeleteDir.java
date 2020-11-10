package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class DeleteDir {
    public static void main(String[] args) throws IOException {
        Path path = new Path("hdfs://10.11.0.193:9000/err_xml/rn");

        FileSystem fs = FileSystemUtil.getFileSystem();
        fs.delete(path, true);
        fs.close();
    }
}
