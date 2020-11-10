package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class ListFiles {
    public static void main(String[] args) throws IOException {
        Path path = new Path("hdfs://10.11.0.193:9000/err_xml/rn/");

        FileSystem fs = FileSystemUtil.getFileSystem();
        FileStatus[] status = fs.listStatus(path);
        for (FileStatus statu : status) {
            System.out.println(statu.getPath().toString());
        }
    }
}
