package com.rexel.tdengine.api;

import com.rexel.tdengine.cons.Constants;
import com.rexel.tdengine.pojo.PointInfo;
import com.rexel.tdengine.utils.CommonUtils;
import com.rexel.tdengine.utils.PointUtils;
import com.rexel.tdengine.utils.TagUtils;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;

/**
 * @ClassName InsertSingleColumnModelBatch
 * @Description InsertSingleColumnModelBatch
 * @Author: chunhui.qu
 * @Date: 2020/11/9
 */
public class InsertSingleColumnModelBatch {
    private static Statement stmt;

    public static void main(String[] args) throws SQLException {
        TdUtils tdUtils = TdUtils.getInstance();
        PointUtils pointUtils = PointUtils.getInstance();

        Connection conn = tdUtils.getConnection();
        if (conn == null) {
            return;
        }
        System.out.println("getConnection");

        stmt = conn.createStatement();
        if (stmt == null) {
            return;
        }
        System.out.println("createStatement");

        List<PointInfo> pointList = pointUtils.getMockPointList();
        System.out.println("getMockPointList");

        // 创建超级表
        createSuperTable(pointList.get(0));
        System.out.println("createSuperTable");

        int maxCount = Integer.valueOf(args[0]);
        for (int i = 0; i < maxCount; i++) {
            System.out.println("count=" + i);
            long startTime = System.currentTimeMillis();
            insertIntoTable(pointList);
            long endTime = System.currentTimeMillis();
            System.out.println("time=" + (endTime - startTime));
        }

        System.out.println("test end");
    }

    private static void createSuperTable(PointInfo pointInfo) {
        String field = "time timestamp," + Constants.VALUE + " double";
        String tag = TagUtils.getTagString(pointInfo);

        // create table if not exists {table} ({field}) tags ({tag});
        String sql = Constants.CREATE_TABLE;
        sql = sql.replace("{table}", pointInfo.getSuperTableName());
        sql = sql.replace("{field}", field);
        sql = sql.replace("{tag}", tag);

        try {
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertIntoTable(List<PointInfo> pointInfoList) {
        List<List<PointInfo>> splitList = CommonUtils.listSplit(pointInfoList, 1000);

        for (List<PointInfo> split : splitList) {
            StringBuilder sql = new StringBuilder(Constants.INSERT_INTO);
            for (PointInfo pointInfo : split) {
                String sysTime = CommonUtils.timeLongToStr(System.currentTimeMillis());
                String table = pointInfo.getTableName();
                String field = Constants.TIME + "," + Constants.VALUE;
                String value = "\"" + sysTime + "\"" + "," + new Random().nextInt(10000);
                // insert into tb_name (field1_name, ...) values(field1_value, ...)
                // insert into {table} ({field}) values ({value})
                String sqlBody = Constants.INSERT_BODY;
                sqlBody = sqlBody.replace("{table}", table);
                sqlBody = sqlBody.replace("{field}", field);
                sqlBody = sqlBody.replace("{value}", value);
                sql.append(sqlBody).append(" ");
            }
            sql.append(";");
            System.out.println(sql.toString().getBytes().length);

            try {
                stmt.executeUpdate(sql.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
