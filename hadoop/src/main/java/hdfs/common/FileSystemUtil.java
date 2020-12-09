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
        conf.set("dfs.client.use.datanode.hostname", "true");
        Path path = new Path("hdfs://rexel-ids001:9000");
        try {
            fileSystem = FileSystem.get(path.toUri(), conf, "zk_admin");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return fileSystem;
    }
}
