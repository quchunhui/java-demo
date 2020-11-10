package hdfs.common;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class FileSystemUtil {
    private static FileSystem fileSystem = null;

    public static FileSystem getFileSystem() {
        if (fileSystem != null) {
            return fileSystem;
        }

        Configuration conf = new Configuration();
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
        Path path = new Path("hdfs://10.11.0.193:9000/");
        try {
            fileSystem = FileSystem.get(path.toUri(), conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileSystem;
    }
}
