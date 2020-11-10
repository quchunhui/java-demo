package com.rexel.tdengine.api;

import com.alibaba.fastjson.JSONObject;
import com.rexel.tdengine.cons.Constants;
import com.rexel.tdengine.pojo.PointInfo;
import com.rexel.tdengine.utils.CommonUtils;
import com.rexel.tdengine.utils.PointUtils;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * @ClassName CreateDatabase
 * @Description CreateDatabase
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class InsertMultiColumnModel {
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

        List<PointInfo> pointList = pointUtils.getMockPointList();

        int count = 0;
        while (true) {
            JSONObject tagJson = new JSONObject();
            tagJson.put("productKey", "a1B6t6ZG6oR");
            tagJson.put("deviceName", "QCHTestDevice1");

            Random intRandom = new Random();
            JSONObject dataJson = new JSONObject();
            pointList.forEach(pointInfo -> dataJson.put(pointInfo.getPointId(), intRandom.nextInt(10000)));

            JSONObject demoJson = new JSONObject();
            demoJson.put(Constants.TABLE, "device_data_up");
            demoJson.put(Constants.TIME, System.currentTimeMillis());
            demoJson.put(Constants.TAG, tagJson);
            demoJson.put(Constants.VALUE, dataJson);

            String sql = jsonToSql(demoJson);

            stmt.executeUpdate(sql);
            count ++;
            System.out.println("executeUpdate. count=" + count + ", sql=" + sql);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static String jsonToSql(JSONObject jsonObject) {
        // insert into {table} ({field}) values ({value});
        String sql = Constants.INSERT_ONE;
        String table = "t_" + jsonObject.getString(Constants.TABLE);
        String field = getFields(jsonObject);
        String value = getValues(jsonObject);

        sql = sql.replace("{table}", table);
        sql = sql.replace("{field}", field);
        return sql.replace("{value}", value);
    }

    private static String getFields(JSONObject jsonObject) {
        JSONObject dataJson = jsonObject.getJSONObject(Constants.VALUE);
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.TIME).append(",");
        dataJson.forEach((key, value) -> sb.append(key).append(","));
        return sb.substring(0, sb.length() - 1);
    }

    private static String getValues(JSONObject jsonObject) {
        JSONObject dataJson = jsonObject.getJSONObject(Constants.VALUE);
        String time = CommonUtils.timeLongToStr(jsonObject.getLong(Constants.TIME));

        StringBuilder sb = new StringBuilder();
        sb.append("\"").append(time).append("\"").append(",");
        dataJson.forEach((key, value) -> sb.append(value).append(","));
        return sb.substring(0, sb.length() - 1);
    }
}
