package pojo;

import annotation.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Data
@EqualsAndHashCode(callSuper = true)
@Measurement(name = "device_property")
public class AirFilmInfo extends PojoBase {
    @Tag("TEST")
    @Column(name = "AR_TEST_VAR1")
    private String AR_TEST_VAR1;

    @Tag("TEST")
    @Column(name = "AR_TEST_VAR2")
    private String AR_TEST_VAR2;

    @Tag("TEST")
    @Column(name = "AR_TEST_VAR3")
    private String AR_TEST_VAR3;

    @Tag("TEST")
    @Column(name = "AR_TEST_VAR4")
    private String AR_TEST_VAR4;

    @Column(name = "VA_PM2A5_VAL")
    private String VA_PM2A5_VAL;

    @Column(name = "VA_WSPEED_VAL")
    private String VA_WSPEED_VAL;

    @Column(name = "VA_REALTIME_POWER")
    private String VA_REALTIME_POWER;

    @Column(name = "VA_DAY_EQUANTITY")
    private String VA_DAY_EQUANTITY;

    @Column(name = "VA_MONTH_EQUANTITY")
    private String VA_MONTH_EQUANTITY;

    @Tag(deviceName = "YC", deviceNo = "1")
    @Column(name = "VA_1YC_VAL")
    private String VA_1YC_VAL;

    @Tag(deviceName = "YC", deviceNo = "2")
    @Column(name = "VA_2YC_VAL")
    private String VA_2YC_VAL;

    @Tag(deviceName = "YC", deviceNo = "3")
    @Column(name = "VA_3YC_VAL")
    private String VA_3YC_VAL;

    @Tag(deviceName = "PFF", deviceNo = "1")
    @Column(name = "VA_1PFF_OPDE")
    private String VA_1PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "2")
    @Column(name = "VA_2PFF_OPDE")
    private String VA_2PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "3")
    @Column(name = "VA_3PFF_OPDE")
    private String VA_3PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "4")
    @Column(name = "VA_4PFF_OPDE")
    private String VA_4PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "5")
    @Column(name = "VA_5PFF_OPDE")
    private String VA_5PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "6")
    @Column(name = "VA_6PFF_OPDE")
    private String VA_6PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "7")
    @Column(name = "VA_7PFF_OPDE")
    private String VA_7PFF_OPDE;

    @Tag(deviceName = "PFF", deviceNo = "8")
    @Column(name = "VA_8PFF_OPDE")
    private String VA_8PFF_OPDE;

    @Tag(deviceName = "FJ", deviceNo = "1")
    @Column(name = "VA_1FJ_FREQUENCY")
    private String VA_1FJ_FREQUENCY;

    @Tag(deviceName = "FJ", deviceNo = "2")
    @Column(name = "VA_2FJ_FREQUENCY")
    private String VA_2FJ_FREQUENCY;

    @Tag(deviceName = "QM")
    @Column(name = "VD_QM_CMOD")
    private String VD_QM_CMOD;

    @Tag(deviceName = "FJ", deviceNo = "1")
    @Column(name = "VD_1FJ_AUTO")
    private String VD_1FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "2")
    @Column(name = "VD_2FJ_AUTO")
    private String VD_2FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "3")
    @Column(name = "VD_3FJ_AUTO")
    private String VD_3FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "4")
    @Column(name = "VD_4FJ_AUTO")
    private String VD_4FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "5")
    @Column(name = "VD_5FJ_AUTO")
    private String VD_5FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "6")
    @Column(name = "VD_6FJ_AUTO")
    private String VD_6FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "7")
    @Column(name = "VD_7FJ_AUTO")
    private String VD_7FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "8")
    @Column(name = "VD_8FJ_AUTO")
    private String VD_8FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "9")
    @Column(name = "VD_9FJ_AUTO")
    private String VD_9FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "10")
    @Column(name = "VD_10FJ_AUTO")
    private String VD_10FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "11")
    @Column(name = "VD_11FJ_AUTO")
    private String VD_11FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "12")
    @Column(name = "VD_12FJ_AUTO")
    private String VD_12FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "13")
    @Column(name = "VD_13FJ_AUTO")
    private String VD_13FJ_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "14")
    @Column(name = "VD_14FJ_AUTO")
    private String VD_14FJ_AUTO;

    @Tag(deviceName = "GPFJ", deviceNo = "1")
    @Column(name = "VD_1GPFJ_STA")
    private String VD_1GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "2")
    @Column(name = "VD_2GPFJ_STA")
    private String VD_2GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "3")
    @Column(name = "VD_3GPFJ_STA")
    private String VD_3GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "4")
    @Column(name = "VD_4GPFJ_STA")
    private String VD_4GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "5")
    @Column(name = "VD_5GPFJ_STA")
    private String VD_5GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "6")
    @Column(name = "VD_6GPFJ_STA")
    private String VD_6GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "7")
    @Column(name = "VD_7GPFJ_STA")
    private String VD_7GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "8")
    @Column(name = "VD_8GPFJ_STA")
    private String VD_8GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "9")
    @Column(name = "VD_9GPFJ_STA")
    private String VD_9GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "10")
    @Column(name = "VD_10GPFJ_STA")
    private String VD_10GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "11")
    @Column(name = "VD_11GPFJ_STA")
    private String VD_11GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "12")
    @Column(name = "VD_12GPFJ_STA")
    private String VD_12GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "13")
    @Column(name = "VD_13GPFJ_STA")
    private String VD_13GPFJ_STA;

    @Tag(deviceName = "GPFJ", deviceNo = "14")
    @Column(name = "VD_14GPFJ_STA")
    private String VD_14GPFJ_STA;

    @Tag(deviceName = "YCCGQ", deviceNo = "1")
    @Column(name = "VD_1YCCGQ_FAL")
    private String VD_1YCCGQ_FAL;

    @Tag(deviceName = "YCCGQ", deviceNo = "2")
    @Column(name = "VD_2YCCGQ_FAL")
    private String VD_2YCCGQ_FAL;

    @Tag(deviceName = "YCCGQ", deviceNo = "3")
    @Column(name = "VD_3YCCGQ_FAL")
    private String VD_3YCCGQ_FAL;

    @Tag(deviceName = "PFF", deviceNo = "1")
    @Column(name = "VD_1PFF_AUTO")
    private String VD_1PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "2")
    @Column(name = "VD_2PFF_AUTO")
    private String VD_2PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "3")
    @Column(name = "VD_3PFF_AUTO")
    private String VD_3PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "4")
    @Column(name = "VD_4PFF_AUTO")
    private String VD_4PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "5")
    @Column(name = "VD_5PFF_AUTO")
    private String VD_5PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "6")
    @Column(name = "VD_6PFF_AUTO")
    private String VD_6PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "7")
    @Column(name = "VD_7PFF_AUTO")
    private String VD_7PFF_AUTO;

    @Tag(deviceName = "PFF", deviceNo = "8")
    @Column(name = "VD_8PFF_AUTO")
    private String VD_8PFF_AUTO;

    @Tag(deviceName = "FJ", deviceNo = "1")
    @Column(name = "VD_1FJ_FAL")
    private String VD_1FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "2")
    @Column(name = "VD_2FJ_FAL")
    private String VD_2FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "3")
    @Column(name = "VD_3FJ_FAL")
    private String VD_3FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "4")
    @Column(name = "VD_4FJ_FAL")
    private String VD_4FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "5")
    @Column(name = "VD_5FJ_FAL")
    private String VD_5FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "6")
    @Column(name = "VD_6FJ_FAL")
    private String VD_6FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "7")
    @Column(name = "VD_7FJ_FAL")
    private String VD_7FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "8")
    @Column(name = "VD_8FJ_FAL")
    private String VD_8FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "9")
    @Column(name = "VD_9FJ_FAL")
    private String VD_9FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "10")
    @Column(name = "VD_10FJ_FAL")
    private String VD_10FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "11")
    @Column(name = "VD_11FJ_FAL")
    private String VD_11FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "12")
    @Column(name = "VD_12FJ_FAL")
    private String VD_12FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "13")
    @Column(name = "VD_13FJ_FAL")
    private String VD_13FJ_FAL;

    @Tag(deviceName = "FJ", deviceNo = "14")
    @Column(name = "VD_14FJ_FAL")
    private String VD_14FJ_FAL;

//    public static void main(String ...args) throws NoSuchFieldException, IllegalAccessException {
//        //获取Bar的val字段
//        Field field = AirFilmInfo.class.getDeclaredField("AR_TEST_VAR1");
//        //获取val字段上的Foo注解实例
//        Column column = field.getAnnotation(Column.class);
//        //获取 foo 这个代理实例所持有的 InvocationHandler
//        InvocationHandler h = Proxy.getInvocationHandler(column);
//        // 获取 AnnotationInvocationHandler 的 memberValues 字段
//        Field hField = h.getClass().getDeclaredField("memberValues");
//        // 因为这个字段事 private final 修饰，所以要打开权限
//        hField.setAccessible(true);
//        // 获取 memberValues
//        Map memberValues = (Map) hField.get(h);
//        // 修改 value 属性值
//        memberValues.put("name", "last_" + column.name());
//        // 获取 foo 的 value 属性值
//        String value = column.name();
//        System.out.println(value); // ddd
//    }
}