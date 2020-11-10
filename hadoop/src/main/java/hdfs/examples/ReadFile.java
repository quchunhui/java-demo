package hdfs.examples;

import hdfs.common.FileSystemUtil;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        Path path = new Path("hdfs://10.11.0.193:9000/err_xml/rn/test1.txt");

        FileSystem fs = FileSystemUtil.getFileSystem();
        if (fs.exists(path)) {
            //方式一：复制到输出流
            FSDataInputStream fi1 = fs.open(path);
            IOUtils.copyBytes(fi1, System.out, 4096, false);
            IOUtils.closeStream(fi1);

            //方式二：文件输出流
            FileStatus stat = fs.getFileStatus(path);
            byte[] buffer = new byte[Integer.parseInt(String.valueOf(stat.getLen()))];
            FSDataInputStream fi2 = fs.open(path);
            fi2.readFully(0, buffer);
            System.out.print("[-----]" + new String(buffer));
            fi2.close();
        }
        fs.close();
    }
}
