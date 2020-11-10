package oss;

import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static Properties loadProps(String filePath) {
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getResourceAsStream(filePath);
            props.load(in);
        } catch (Exception e) {
            // 此处可根据你的日志框架进行记录
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return props;
    }

    public static String getValue(String fileName, String key) {
        Properties props = loadProps("/" + fileName + ".properties");
        String value = props.getProperty(key);
        return value == null ? "" : value;
    }

    public static String getValue(String fileName, String key, String defaultValue) {
        Properties props = loadProps("/" + fileName + ".properties");
        String value = props.getProperty(key);
        return value == null ? defaultValue : value;
    }
}
