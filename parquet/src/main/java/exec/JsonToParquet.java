package exec;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import utils.CommonUtil;
import utils.HdfsFileReader;
import utils.LocalFileReader;
import utils.MyParquetWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * 1、将JSON格式的文件，转换为Parquet格式。
 * 2、检查JSON是否存在嵌套，如果有嵌套则将JSON压平
 *
 * @author QuChunhui 2019/2/12
 */
public class JsonToParquet {
    private static MyParquetWriter myParquetWriter = null;
    private static LocalFileReader localFileReader = null;
    private static HdfsFileReader hdfsFileReader = null;
    private static BufferedReader bufferedReader = null;

    public static void main(String[] args) throws IOException {
        //输入参数检查
        if (args.length != 2) {
            System.out.println("main param count error.");
            return;
        }

        //输入路径（HDFS，包含文件名）
        String inputFullPath = args[0];
        //输入文件名
        String inputFile = CommonUtil.getFileNameNoEx(CommonUtil.getFileName(inputFullPath));
        //输出路径（HDFS，不包含文件名）
        String outPath = CommonUtil.rtrim(args[1], '/');

        try {
            //获取BufferedReader
            bufferedReader = getBufferedReader(inputFullPath);
            if (bufferedReader == null) {
                System.out.println("get BufferedReader error.");
                return;
            }

            //获取ParquetWriter
            myParquetWriter = MyParquetWriter.getInstance(inputFile, outPath);
            if (myParquetWriter == null) {
                System.out.println("get MyParquetWriter error.");
                return;
            }

            //逐行读取文件
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //转换为JSONObject
                JSONObject jsonObject = JSONObject.parseObject(line);
                JSONObject result = new JSONObject();

                //递归压平JSON
                planish(result, "", jsonObject);

                //写入parquet格式文件
                myParquetWriter.parquetWriter(result);
            }
        } finally {
            close();
        }
    }

    /**
     * 释放相关资源
     */
    private static void close() {
        if (myParquetWriter != null) {
            myParquetWriter.close();
        }

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (hdfsFileReader != null) {
            try {
                hdfsFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (localFileReader != null) {
            try {
                localFileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件读取句柄
     * @param inputFullPath 输入文件路径
     * @return BufferedReader
     */
    private static BufferedReader getBufferedReader(String inputFullPath) {
        if (CommonUtil.isHdfsPath(inputFullPath)) {
            hdfsFileReader = HdfsFileReader.getInstance(inputFullPath);
            if (hdfsFileReader != null) {
                return hdfsFileReader.getReader();
            }
        } else {
            localFileReader = LocalFileReader.getInstance(inputFullPath);
            if (localFileReader != null) {
                return localFileReader.getReader();
            }
        }

        return null;
    }

    /**
     * 递归压平JSON
     *
     * @param result [OUT]输出
     * @param parentKey [IN]父节点
     * @param jsonObject [IN]JSON内容
     */
    private static void planish(JSONObject result, String parentKey, JSONObject jsonObject) {
        Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        for (Map.Entry<String, Object> map : set) {
            //获得新的Key
            String key = getNewKey(parentKey, map.getKey());
            Object value = map.getValue();

            //判断是否存在嵌套
            JSONObject json = toJson(value.toString());
            if (json == null) {
                result.put(key, value);
            } else {
                planish(result, key, json);
            }
        }
    }

    /**
     * 格式化新的Key
     *
     * @param parentKey 父节点
     * @param key 当前节点
     * @return 父节点_当前节点
     */
    private static String getNewKey(String parentKey, String key) {
        if (parentKey == null || parentKey.isEmpty()) {
            return key;
        } else {
            return parentKey + "_" + key;
        }
    }

    /**
     * 字符串转换为JSON
     *
     * @param object 字符串
     * @return 转换失败时返回null，正常时返回JSONObject
     */
    private static JSONObject toJson(String object) {
        try {
            return JSONObject.parseObject(object);
        } catch (JSONException | ClassCastException e) {
            return null;
        }
    }
}