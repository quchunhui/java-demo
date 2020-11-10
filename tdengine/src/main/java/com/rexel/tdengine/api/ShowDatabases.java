package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName ShowDatabases
 * @Description ShowDatabases
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class ShowDatabases {
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

        String sql = "show databases;";
        ResultSet result = stmt.executeQuery(sql);

        JSONArray jsonArray = new JSONArray();
        while(result.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", result.getString("name"));
            jsonObject.put("created_time", result.getTimestamp("created_time"));
            jsonObject.put("ntables", result.getInt("ntables"));
            jsonObject.put("vgroups", result.getInt("vgroups"));
            jsonObject.put("replica", result.getInt("replica"));
            jsonObject.put("quorum", result.getInt("quorum"));
            jsonObject.put("days", result.getInt("days"));
            jsonObject.put("keep1,keep2,keep(D)", result.getString("keep1,keep2,keep(D)"));
            jsonObject.put("cache(MB)", result.getInt("cache(MB)"));
            jsonObject.put("blocks", result.getInt("blocks"));
            jsonObject.put("minrows", result.getInt("minrows"));
            jsonObject.put("maxrows", result.getInt("maxrows"));
            jsonObject.put("wallevel", result.getInt("wallevel"));
            jsonObject.put("fsync", result.getInt("fsync"));
            jsonObject.put("comp", result.getInt("comp"));
            jsonObject.put("precision", result.getString("precision"));
            jsonObject.put("status", result.getString("status"));
            jsonArray.add(jsonObject);
        }
        System.out.println("jsonArray=" + jsonArray.toJSONString());
    }
}
