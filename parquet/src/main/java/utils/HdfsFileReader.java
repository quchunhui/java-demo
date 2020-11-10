package utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HDFS文件读取工具类
 *
 * @author QuChunhui 2019/2/12
 */
public class HdfsFileReader {
    private static HdfsFileReader fileReader = null;
    private FileSystem fileSystem = null;
    private Path path = null;
    private BufferedReader reader = null;

    private HdfsFileReader(){}

    /**
     * 获取BufferedReader
     *
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * 初始化FileReader实例
     *
     * @param filePath HDFS路径
     * @return FileReader实例
     */
    public static HdfsFileReader getInstance(String filePath) {
        if (fileReader != null) {
            return fileReader;
        }

        //初始化FileReader
        fileReader = new HdfsFileReader();

        //设置文件路径
        fileReader.initPath(filePath);

        //初始化FileSystem
        try {
            fileReader.initFileSystem();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //初始化BufferReader
        try {
            fileReader.initReader();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return fileReader;
    }

    /**
     * 释放相关资源
     *
     * @throws IOException e
     */
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
        }
        if (fileSystem != null) {
            fileSystem.close();
        }
    }

    /**
     * 初始化文件路径
     *
     * @param filePath 路径
     */
    private void initPath(String filePath) {
        path = new Path(filePath);
    }

    /**
     * 初始化FileSystem
     *
     * @throws IOException e
     */
    private void initFileSystem() throws IOException {
        if (path == null) {
            return;
        }

        Configuration conf = new Configuration();
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");

        FileSystem fs = FileSystem.get(path.toUri(), conf);
        if (fs.exists(path)) {
            fileSystem = fs;
        } else {
            throw  new IOException("path is not exist.");
        }
    }

    /**
     * 初始化BufferedReader实例
     *
     * @throws IOException e
     */
    private void initReader() throws IOException {
        if (path != null && fileSystem != null) {
            reader = new BufferedReader(new InputStreamReader(fileSystem.open(path)));
        }
    }
}
