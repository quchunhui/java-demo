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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName InsertSingle
 * @Description InsertSingle
 * @Author: chunhui.qu
 * @Date: 2020/10/12
 */
public class InsertSingleColumnModel {
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

        AtomicInteger count = new AtomicInteger();
        int maxCount = 3;
        for (int i = 0; i < maxCount; i++) {
            System.out.println("count=" + i);
            long startTime = System.currentTimeMillis();
            for (PointInfo pointInfo : pointList) {
                insertIntoTable(pointInfo);
                count.getAndIncrement();
            }
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

    private static void insertIntoTable(PointInfo pointInfo) {
        String sysTime = CommonUtils.timeLongToStr(System.currentTimeMillis());
        String table = pointInfo.getTableName();
        String superTable = pointInfo.getSuperTableName();
        String tag = TagUtils.getTagValueString(pointInfo);
        String value = "\"" + sysTime + "\"" + "," + new Random().nextInt(10000);

        // insert into {table} using {superTable} tags ({tag}) values ({value});
        // insert into d1001 using meters tags ("Beijng.Chaoyang", 2) values (now, 10.2, 219, 0.32);
        String sql = Constants.INSERT_ONE_USING_SUPER;
        sql = sql.replace("{table}", table);
        sql = sql.replace("{superTable}", superTable);
        sql = sql.replace("{tag}", tag);
        sql = sql.replace("{value}", value);
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
