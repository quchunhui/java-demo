package com.rexel.tdengine.pojo;

import com.rexel.tdengine.annotation.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @ClassName PointInfo
 * @Description PointInfo
 * @Author: chunhui.qu
 * @Date: 2020/10/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class PointInfo extends AbstractPojo {
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
     * 属性名称
     */
    @Tag(name = "name")
    private String name;

    /**
     * 数据类型
     */
    @Tag(name = "dataType")
    private String dataType;

    /**
     * 最小值
     */
    @Tag(name = "minimum")
    private String minimum;

    /**
     * 最大值
     */
    @Tag(name = "maximum")
    private String maximum;

    /**
     * 步长
     */
    @Tag(name = "step")
    private String step;

    /**
     * 单位
     */
    @Tag(name = "unit")
    private String unit;

    /**
     * 描述
     */
    @Tag(name = "description")
    private String description;

    /**
     * 测点表名
     *
     * @return 表名
     */
    public String getTableName() {
        return productKey + "_" + deviceName + "_" + pointId;
    }
}