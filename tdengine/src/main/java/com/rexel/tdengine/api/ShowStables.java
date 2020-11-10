package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName ShowStables
 * @Description ShowStables
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class ShowStables {
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

        String sql = "show stables;";
        ResultSet result = stmt.executeQuery(sql);

        JSONArray jsonArray = new JSONArray();
        while(result.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", result.getString("name"));
            jsonObject.put("created_time", result.getTimestamp("created_time"));
            jsonObject.put("columns", result.getInt("columns"));
            jsonObject.put("tags", result.getInt("tags"));
            jsonObject.put("tables", result.getInt("tables"));
            jsonArray.add(jsonObject);
        }
        System.out.println("jsonArray=" + jsonArray.toJSONString());
    }
}
