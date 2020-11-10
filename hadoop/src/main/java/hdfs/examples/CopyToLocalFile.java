package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class CopyToLocalFile {
    public static void main(String[] args) throws IOException {
        Path src = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test1.txt");
        Path dst = new Path("C:/");

        FileSystem fs = FileSystemUtil.getFileSystem();
        if (fs.exists(src)) {
            fs.copyToLocalFile(false, src, dst, true);
        }
        fs.close();
    }
}
