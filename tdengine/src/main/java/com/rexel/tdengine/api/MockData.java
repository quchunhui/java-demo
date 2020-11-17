package com.rexel.tdengine.api;

import com.rexel.tdengine.pojo.PointInfo;
import com.rexel.tdengine.utils.CommonUtils;
import com.rexel.tdengine.utils.PointUtils;
import com.rexel.tdengine.utils.SqlUtils;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName InsertBatch
 * @Description InsertBatch
 * @Author: chunhui.qu
 * @Date: 2020/11/9
 */
public class MockData {
    private static Statement statement;

    public static void main(String[] args) throws SQLException {
        int argsLength = 6;
        if (args.length < argsLength) {
            System.out.println("parameter error.");
            return;
        }

        // 1.产品数量
        int productCount = Integer.valueOf(args[0]);
        System.out.println("productCount=" + productCount);
        // 2.设备数量
        int deviceCount = Integer.valueOf(args[1]);
        System.out.println("deviceCount=" + deviceCount);
        // 3.测点数量
        int pointCount = Integer.valueOf(args[2]);
        System.out.println("pointCount=" + pointCount);
        // 4.模拟开始时间(yyyy-MM-dd HH:mm:ss.SSS)
        Date fromDate = CommonUtils.timeStrToDate(args[3]);
        System.out.println("fromDate=" + fromDate);
        // 5.模拟结束时间(yyyy-MM-dd HH:mm:ss.SSS)
        Date toDate = CommonUtils.timeStrToDate(args[4]);
        System.out.println("toDate=" + toDate);
        // 6.时间间隔(秒)
        int interval = Integer.valueOf(args[5]);
        System.out.println("interval=" + interval);

        TdUtils tdUtils = TdUtils.getInstance();
        Connection connection = tdUtils.getConnection();
        if (connection == null) {
            return;
        }
        System.out.println("getConnection");

        statement = connection.createStatement();
        if (statement == null) {
            return;
        }
        System.out.println("createStatement");

        // 创建超级表
        createSuperTable(new PointInfo());
        System.out.println("createSuperTable");

        PointUtils pointUtils = PointUtils.getInstance();
        int loopCount = 0;
        while (fromDate.compareTo(toDate) <= 0) {
            // 上报时间
            String time = CommonUtils.timeLongToStr(fromDate.getTime());
            // 上报数据
            List<PointInfo> pointList =
                pointUtils.getMockPointList(productCount, deviceCount, pointCount, time);

            // 批量插入
            long startTime = System.currentTimeMillis();
            batchInsert(pointList);
            long expend = System.currentTimeMillis() - startTime;

            // 下一次时间
            loopCount ++;
            fromDate = getNextTime(fromDate, interval);
            System.out.println("次数=" + loopCount + ", 时间=" + time + ", 耗时=" + expend);
        }

        statement.close();
        connection.close();
        System.out.println("test end");
    }

    private static Date getNextTime(Date date, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, interval);
        return calendar.getTime();
    }

    private static void createSuperTable(PointInfo pointInfo) {
        String sql = SqlUtils.getCreateSuperTableSql(pointInfo);
        try {
            statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void batchInsert(List<PointInfo> pointInfoList) {
        List<List<PointInfo>> splitList = CommonUtils.listSplit(pointInfoList, 500);
        for (List<PointInfo> split : splitList) {
            try {
                statement.executeUpdate(SqlUtils.insertBatchUsingSuper(split));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
