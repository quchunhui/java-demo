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

/**
 * @ClassName MockData
 * @Description MockData
 * param1: 产品数量→1
 * param2: 设备数量→50
 * param3: 测点数量→100
 * param4: 开始时间→"2015-01-01 00:00:00.000"
 * param5: 结束时间→"2016-01-01 00:00:00.000"
 * param6: 上报频率→5
 * param7: 数据库名→"mock_data"
 * example：nohup java -classpath /home/radmin/package/tdengine-1.0-jar-with-dependencies.jar com.rexel.tdengine.api.MockData 1 50 100 "2015-02-01 00:00:00.000" "2017-12-31 23:59:59.999" 5 "mock_data" >/home/radmin/package/java_mock_thread.log &
 * @Author: chunhui.qu
 * @Date: 2020/11/18
 */
public class MockData extends Thread {
    private final static int SPLIT_LEN = 500;
    private PointUtils pointUtils = PointUtils.getInstance();
    private TdUtils tdUtils = TdUtils.getInstance();
    private Statement statement;
    private int productCount;
    private int deviceCount;
    private int pointCount;
    private Date fromDate;
    private Date toDate;
    private int interval;
    private String database;

    private MockData(int productCount,
        int deviceCount, int pointCount, Date fromDate, Date toDate, int interval, String database) {
        this.productCount = productCount;
        this.deviceCount = deviceCount;
        this.pointCount = pointCount;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.interval = interval;
        this.database = database;
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
        // 7.数据库名称
        String database = args[6];
        System.out.println("database=" + database);

        // 按月生成模拟对象
        List<MockData> mockList = new ArrayList<>();
        while (fromDate.compareTo(toDate) <= 0) {
            Date from = fromDate;
            Date to =  CommonUtils.getNextMonth(fromDate);
            mockList.add(new MockData(productCount, deviceCount, pointCount, from, to, interval, database));
            fromDate = to;
        }

        // 执行线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(mockList.size());
        for(MockData mockData : mockList) {
            fixedThreadPool.execute(mockData);
        }
    }

    @Override
    public void run() {
        try {
            Connection connection = tdUtils.getConnection();
            if (connection == null) {
                return;
            }
            System.out.println("getConnection.");

            statement = connection.createStatement();
            if (statement == null) {
                return;
            }
            System.out.println("createStatement.");

            // 创建数据库
            createDatabase(database);
            System.out.println("createDatabase.");

            // 创建超级表
            createSuperTable(new PointInfo(database));
            System.out.println("createSuperTable.");

            int loopCount = 0;
            while (fromDate.compareTo(toDate) <= 0) {
                // 数据上报时间
                String time = CommonUtils.timeLongToStr(fromDate.getTime());
                // 模拟上报数据
                List<PointInfo> pointList =
                    pointUtils.getMockPointList(database, productCount, deviceCount, pointCount, time);

                // 批量插入数据
                long startTime = System.currentTimeMillis();
                batchInsert(pointList);
                long expend = System.currentTimeMillis() - startTime;

                // 计算下一次时间
                loopCount ++;
                fromDate = CommonUtils.getNextSecond(fromDate, interval);
                System.out.println("loopCount=" + loopCount + ", time=" + time + ", expend=" + expend);
            }

            statement.close();
            connection.close();
            System.out.println("mock end. fromDate=" + fromDate + ", toDate=" + toDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase(String database) throws SQLException {
        String sql = SqlUtils.getCreateDatabaseSql(database);
        statement.executeQuery(sql);
    }

    private void createSuperTable(PointInfo pointInfo) throws SQLException {
        String sql = SqlUtils.getCreateSuperTableSql(pointInfo);
        statement.executeQuery(sql);
    }

    private void batchInsert(List<PointInfo> pointInfoList) throws SQLException {
        List<List<PointInfo>> splitList = CommonUtils.listSplit(pointInfoList, SPLIT_LEN);
        for (List<PointInfo> split : splitList) {
            statement.executeUpdate(SqlUtils.insertBatchUsingSuper(split));
        }
    }
}
