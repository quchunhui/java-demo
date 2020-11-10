package com.rexel.tdengine.api;

import com.rexel.tdengine.pojo.PointInfo;
import com.rexel.tdengine.utils.CommonUtils;
import com.rexel.tdengine.utils.PointUtils;
import com.rexel.tdengine.utils.SqlUtils;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @ClassName InsertBatch
 * @Description InsertBatch
 * @Author: chunhui.qu
 * @Date: 2020/11/9
 */
public class InsertBatch {
    private static Statement stmt;

    public static void main(String[] args) throws SQLException {
        int argsLength = 5;
        if (args.length < argsLength) {
            System.out.println("parameter error.");
            return;
        }

        int productCount = Integer.valueOf(args[0]);
        int deviceCount = Integer.valueOf(args[1]);
        int pointCount = Integer.valueOf(args[2]);
        int testCount = Integer.valueOf(args[3]);
        int batchCount = Integer.valueOf(args[4]);
        System.out.println("productCount=" + productCount);
        System.out.println("deviceCount=" + deviceCount);
        System.out.println("pointCount=" + pointCount);
        System.out.println("testCount=" + testCount);
        System.out.println("batchCount=" + batchCount);

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

        List<PointInfo> pointList =
            pointUtils.getMockPointList(productCount, deviceCount, pointCount);

        // 创建超级表
        createSuperTable(pointList.get(0));
        System.out.println("createSuperTable");

        for (int i = 0; i < testCount; i++) {
            System.out.println("count=" + i);
            long startTime = System.currentTimeMillis();
            batchInsert(pointList, batchCount);
            long endTime = System.currentTimeMillis();
            System.out.println("time=" + (endTime - startTime));
        }

        System.out.println("test end");
    }

    private static void createSuperTable(PointInfo pointInfo) {
        String sql = SqlUtils.getCreateSuperTableSql(pointInfo);
        try {
            stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void batchInsert(List<PointInfo> pointInfoList, int batchCount) {
        List<List<PointInfo>> splitList = CommonUtils.listSplit(pointInfoList, batchCount);
        for (List<PointInfo> split : splitList) {
            try {
                stmt.executeUpdate(SqlUtils.insertBatchUsingSuper(split));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
