package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class CopyFromLocalFile {
    public static void main(String[] args) throws IOException {
        Path src = new Path("C:\\test\\test.jpg");
        Path dst = new Path("hdfs://rexel-ids001:9000/test/test.jpg");

        FileSystem fs = FileSystemUtil.getFileSystem();
        System.out.println("getFileSystem");
        fs.copyFromLocalFile(src, dst);
        System.out.println("copyFromLocalFile");
        fs.close();
        System.out.println("end");
    }
}
