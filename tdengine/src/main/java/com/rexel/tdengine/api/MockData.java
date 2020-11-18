package com.rexel.tdengine.api;

import com.rexel.tdengine.pojo.PointInfo;
import com.rexel.tdengine.utils.CommonUtils;
import com.rexel.tdengine.utils.PointUtils;
import com.rexel.tdengine.utils.SqlUtils;
import com.rexel.tdengine.utils.TdUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.SneakyThrows;

/**
 * @ClassName MockData
 * @Description MockData
 * 启动命令：nohup java -classpath /home/radmin/package/tdengine-1.0-jar-with-dependencies.jar com.rexel.tdengine.api.MockData 1 50 100 "2015-02-01 00:00:00.000" "2017-12-31 23:59:59.999" 5 >/home/radmin/package/java_mock_thread.log &
 * @Author: chunhui.qu
 * @Date: 2020/11/18
 */
public class MockData extends Thread {
    private Statement statement;
    private int productCount;
    private int deviceCount;
    private int pointCount;
    private Date fromDate;
    private Date toDate;
    private int interval;

    private MockData(int productCount,
        int deviceCount, int pointCount, Date fromDate, Date toDate, int interval) {
        this.productCount = productCount;
        this.deviceCount = deviceCount;
        this.pointCount = pointCount;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.interval = interval;
    }

    public static void main(String[] args) {
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
        // 4.模拟开始时间(2015-01-01 00:00:00.000)
        Date fromDate = CommonUtils.timeStrToDate(args[3]);
        System.out.println("fromDate=" + fromDate);
        // 5.模拟结束时间(2017-12-31 23:59:59.999)
        Date toDate = CommonUtils.timeStrToDate(args[4]);
        System.out.println("toDate=" + toDate);
        // 6.时间间隔(秒)
        int interval = Integer.valueOf(args[5]);
        System.out.println("interval=" + interval);

        // 按月生成模拟对象
        List<MockData> mockList = new ArrayList<>();
        while (fromDate.compareTo(toDate) <= 0) {
            Date from = fromDate;
            Date to =  CommonUtils.getNextMonth(fromDate);
            mockList.add(new MockData(productCount, deviceCount, pointCount, from, to, interval));
            fromDate = to;
        }

        // 执行线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(mockList.size());
        for(MockData mockData : mockList) {
            fixedThreadPool.execute(mockData);
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        TdUtils tdUtils = TdUtils.getInstance();
        Connection connection = tdUtils.getConnection();
        if (connection == null) {
            return;
        }
        System.out.println("getConnection");

        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
            fromDate = CommonUtils.getNextTime(fromDate, interval);
            System.out.println("次数=" + loopCount + ", 时间=" + time + ", 耗时=" + expend);
        }

        statement.close();
        connection.close();
        System.out.println("mock end. fromDate=" + fromDate + ", toDate=" + toDate);
    }

    private void createSuperTable(PointInfo pointInfo) {
        String sql = SqlUtils.getCreateSuperTableSql(pointInfo);
        try {
            statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void batchInsert(List<PointInfo> pointInfoList) {
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
