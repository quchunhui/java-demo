package com.rexel.tdengine.api;

import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName CreateDatabase
 * @Description CreateDatabase
 * @Author: chunhui.qu
 * @Date: 2020/9/30
 */
public class CreateDatabase {
    public static void main(String[] args) throws SQLException {
        TdUtils tdUtils = TdUtils.getInstance();
        Connection conn = tdUtils.getConnection();
        if (conn == null) {
            return;
        }
        System.out.println("get connection");

        Statement stmt = conn.createStatement();
        if (stmt == null) {
            return;
        }
        stmt.executeUpdate("create database if not exists javatestdb");
        System.out.println("create database");
        stmt.executeUpdate("use javatestdb");
        System.out.println("use database");
        stmt.executeUpdate("create table if not exists javatesttable (ts timestamp, temperature int, humidity float)");
        System.out.println("create table");
    }
}
