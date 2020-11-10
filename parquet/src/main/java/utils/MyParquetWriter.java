package utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * parquet文件写入工具类
 *
 * @author QuChunhui 2019/2/12
 */
public class MyParquetWriter {
    private static final Logger logger = LoggerFactory.getLogger(MyParquetWriter.class);
    private static MyParquetWriter myParquetWriter = null;
    private Map<Integer, ParquetWriter<Group>> writerMap = null;
    private String inputFile = null;
    private String outPath = null;

    private MyParquetWriter() {}

    /**
     * 获取ParquetWriter实例
     *
     * @return ParquetWriter实例
     */
    public static MyParquetWriter getInstance(String inputFile, String outPath) {
        if (myParquetWriter != null) {
            return myParquetWriter;
        }

        myParquetWriter = new MyParquetWriter();
        myParquetWriter.inputFile = inputFile;
        myParquetWriter.outPath = outPath;
        return myParquetWriter;
    }

    /**
     * 向指定HDFS路径写入Parquet格式文件
     *
     * @param jsonObject JSONObject
     * @throws IOException e
     */
    public void parquetWriter(JSONObject jsonObject) throws IOException {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        int keySize = set.size();
        Configuration conf = new Configuration();

        //构建Parquet Schema
        StringBuilder sb = new StringBuilder();
        sb.append("message json {\n");
        for (Map.Entry<String, Object> map : set) {
            sb.append(String.format(" optional binary %s (UTF8);\n", map.getKey()));
        }
        sb.append("}");
        MessageType schema = MessageTypeParser.parseMessageType(sb.toString());

        GroupWriteSupport writeSupport = new GroupWriteSupport();
        GroupWriteSupport.setSchema(schema, conf);

        //构建Parquet Factory
        GroupFactory factory = new SimpleGroupFactory(schema);
        Group group = factory.newGroup();
        for (Map.Entry<String, Object> map : set) {
            group.append(map.getKey(), map.getValue().toString());
        }

        logger.debug("----------------------");
        logger.debug("schema=\n" + schema.toString());
        logger.debug("group=\n" + group.toString());
        logger.debug("----------------------");

        //写入至HDFS
        if (writerMap == null) {
            writerMap = new HashMap<>();
        }

        if (!writerMap.containsKey(keySize)) {
            //输出全路径（HDFS，包含文件名）
            String outFullPath = getOutFullPath(keySize);
            writerMap.put(keySize, new ParquetWriter<>(
                    new Path(outFullPath),
                    ParquetFileWriter.Mode.OVERWRITE,
                    writeSupport,
                    CompressionCodecName.SNAPPY,
                    ParquetWriter.DEFAULT_BLOCK_SIZE,
                    ParquetWriter.DEFAULT_PAGE_SIZE,
                    ParquetWriter.DEFAULT_PAGE_SIZE,
                    ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                    ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                    ParquetWriter.DEFAULT_WRITER_VERSION,
                    conf));
            logger.info("new parquet writer. keySize=" + keySize);
        }
        ParquetWriter<Group> writer = writerMap.get(keySize);
        writer.write(group);
    }

    /**
     * 释放资源
     */
    public void close() {
        if (writerMap != null) {
            writerMap.forEach((k, writer) -> {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 获取输出全路径
     * @param keySize 字段数
     * @return 输出全路径（包括文件名）
     */
    private String getOutFullPath(int keySize) {
        String outFileName = inputFile
                .concat("_").concat(Integer.toString(keySize))
                .concat("_").concat(CommonUtil.getUUId())
                .concat(".parquet");

        String repairOutPath = CommonUtil.rtrim(outPath, '/').concat("/");
        return repairOutPath.concat(outFileName);
    }
}
