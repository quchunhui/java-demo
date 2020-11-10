package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class CreateFile {
    public static void main(String[] args) throws IOException {
        Path path = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test.txt");

        FileSystem fs = FileSystemUtil.getFileSystem();
        if (!fs.exists(path)) {
            FSDataOutputStream os = fs.create(path);
            os.write("create test".getBytes());
            os.close();
        }
        fs.close();
    }
}
