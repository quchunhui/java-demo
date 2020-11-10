package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.cons.Constants;
import com.rexel.tdengine.utils.PointUtils;
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
        PointUtils pointUtils = PointUtils.getInstance();

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
            jsonObject.put(Constants.TIME, result.getTimestamp(Constants.TIME));
            pointUtils.getMockPointList().forEach(pointInfo -> {
                String pointId = pointInfo.getPointId();
                try {
                    jsonObject.put(pointId, result.getDouble(pointId));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            jsonArray.add(jsonObject);
        }
        System.out.println("jsonArray=" + jsonArray.toJSONString());
    }
}
