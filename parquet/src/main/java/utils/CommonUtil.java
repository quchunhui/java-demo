package utils;

import java.util.UUID;

public class CommonUtil {
    /**
     * 获取一个UUID
     * @return UUID
     */
    public static String getUUId() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }

    /**
     * 从一个全路径中截取文件名
     * @param fullPath 全路径
     * @return 文件名
     */
    public static String getFileName(String fullPath) {
        return fullPath.substring(fullPath.lastIndexOf("/") + 1);
    }

    /**
     * 获取不带扩展名的文件名
     * @param filename 文件名称（包含扩展名）
     * @return 文件名
     */
    public static String getFileNameNoEx(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length()) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * trim指定字符
     * @param str 字符串
     * @param c 字符
     * @return trim后字符串
     */
    public static String rtrim(String str, char c) {
        char[] chars = str.toCharArray();
        int len = chars.length;
        int st = 0;
        while ((st < len) && (chars[len-1] == c)) {
            len --;
        }
        return (len < chars.length) ? str.substring(st, len) : str;
    }

    /**
     * 检查是否为HDFS路径
     * @param inputPath 输入路径
     * @return true：HDFS路径、false：本地路径
     */
    public static boolean isHdfsPath(String inputPath) {
        return inputPath.startsWith("hdfs://");
    }
}