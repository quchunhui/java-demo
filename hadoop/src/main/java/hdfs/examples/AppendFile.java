package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class AppendFile {
    public static void main(String[] args) throws IOException {
        Path path = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test1.txt");

        FileSystem fs = FileSystemUtil.getFileSystem();
        FSDataOutputStream os = fs.append(path);
        os.writeChars("append file test.\n");
        os.writeChars("append file test.\n");
        os.close();
        fs.close();
    }
}
