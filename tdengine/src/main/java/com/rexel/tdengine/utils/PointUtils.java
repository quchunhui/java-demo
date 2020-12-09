package com.rexel.tdengine.utils;

import com.rexel.tdengine.pojo.PointInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName PointUtils
 * @Description PointUtils
 * @Author: chunhui.qu
 * @Date: 2020/10/15
 */
public class PointUtils {
    /**
     * 构造函数
     */
    private PointUtils() {
        // do nothing
    }

    /**
     * 单例模式
     */
    private static class SingletonInstance {
        private static final PointUtils INSTANCE = new PointUtils();
    }

    /**
     * 获取对象句柄
     */
    public static PointUtils getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * 生成模拟数据
     *
     * @param database 数据库名
     * @param productCount 产品数量
     * @param deviceCount 设备数量
     * @param pointCount 测点数量
     * @param time 上报事件
     * @return 结果集
     */
    public List<PointInfo> getMockPointList(String database,
        int productCount, int deviceCount, int pointCount, String time) {
        List<PointInfo> list = new ArrayList<>();
        for (int i = 1; i <= productCount; i++) {
            for (int j = 1; j <= deviceCount; j++) {
                for (int k = 1; k <= pointCount; k++) {
                    PointInfo pointInfo = new PointInfo(database);
                    pointInfo.setProductKey("Product" + i);
                    pointInfo.setDeviceName("Device" + j);
                    pointInfo.setPointId("AI_POINT" + k);
                    pointInfo.setTime(getTime(time));
                    pointInfo.setValue(new Random().nextInt(10000));
                    list.add(pointInfo);
                }
            }
        }
        System.out.println("mock count=" + list.size());
        return list;
    }

    private String getTime(String time) {
        if (time == null || "".equals(time)) {
            return CommonUtils.timeLongToStr(System.currentTimeMillis());
        } else {
            return time;
        }
    }
}
