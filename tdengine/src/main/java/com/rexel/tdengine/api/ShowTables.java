package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName ShowTables
 * @Description ShowTables
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class ShowTables {
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

        String sql = "show tables;";
        ResultSet result = stmt.executeQuery(sql);

        JSONArray jsonArray = new JSONArray();
        while(result.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("table_name", result.getString("table_name"));
            jsonObject.put("created_time", result.getTimestamp("created_time"));
            jsonObject.put("columns", result.getInt("columns"));
            jsonObject.put("stable_name", result.getString("stable_name"));
            jsonArray.add(jsonObject);
        }
        System.out.println("jsonArray=" + jsonArray.toJSONString());
    }
}
