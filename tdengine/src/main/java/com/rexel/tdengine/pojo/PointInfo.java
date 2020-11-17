package com.rexel.tdengine.pojo;

import com.rexel.tdengine.annotation.Tag;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName PointInfo
 * @Description PointInfo
 * @Author: chunhui.qu
 * @Date: 2020/11/12
 */
@Data
@ToString
public class PointInfo {
    /**
     * 产品标识
     */
    @Tag(name = "productKey")
    private String productKey;

    /**
     * 设备标识
     */
    @Tag(name = "deviceName")
    private String deviceName;

    /**
     * 属性标识
     */
    @Tag(name = "pointId")
    private String pointId;

    /**
     * 上报时间
     */
    private String time;

    /**
     * 测点值
     */
    private double value;

    /**
     * 子表名
     *
     * @return 子表名
     */
    public String getTableName() {
        return productKey + "_" + deviceName + "_" + pointId;
    }

    /**
     * 超级表名
     *
     * @return 超级表名
     */
    public String getSuperTable() {
        return "st_device_data_up";
    };
}