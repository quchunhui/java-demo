package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName Select
 * @Description Select
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class Select {
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

        String sql = "select * from t_device_data_up order by time desc limit 3;";
        ResultSet result = stmt.executeQuery(sql);

        JSONArray jsonArray = new JSONArray();
        while(result.next()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("time", result.getTimestamp("time"));
            jsonArray.add(jsonObject);
        }
        System.out.println("jsonArray=" + jsonArray.toJSONString());
    }
}
