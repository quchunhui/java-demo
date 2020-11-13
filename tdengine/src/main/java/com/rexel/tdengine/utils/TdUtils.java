package com.rexel.tdengine.utils;

import com.taosdata.jdbc.TSDBDriver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @ClassName TdUtils
 * @Description TDengine共通类
 * @Author: chunhui.qu
 * @Date: 2020/9/30
 */
public class TdUtils {
    private Connection connection = null;

    /**
     * 构造函数
     */
    private TdUtils() {
        // do nothing
    }

    /**
     * 单例模式
     */
    private static class SingletonInstance {
        private static final TdUtils INSTANCE = new TdUtils();
    }

    /**
     * 获取对象句柄
     */
    public static TdUtils getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            Class.forName("com.taosdata.jdbc.TSDBDriver");
            String jdbcUrl = "jdbc:TAOS://rexel-ids001:6030/rexel_online";
            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_USER, "root");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_PASSWORD, "RexelRoot!@#");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "Asia/Shanghai");
            connection = DriverManager.getConnection(jdbcUrl, connProps);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
