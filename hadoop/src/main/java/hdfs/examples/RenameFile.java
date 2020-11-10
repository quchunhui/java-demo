package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class RenameFile {
    public static void main(String[] args) throws IOException {
        //文件或者路径均可
        Path oldPath = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test.txt");
        Path newPath = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test1.txt");

        FileSystem fs = FileSystemUtil.getFileSystem();
        fs.rename(oldPath, newPath);
        fs.close();
    }
}
