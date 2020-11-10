package com.rexel.tdengine.utils;

import com.rexel.tdengine.annotation.Tag;
import com.rexel.tdengine.pojo.PointInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName TagUtils
 * @Description TagUtils
 * @Author: chunhui.qu
 * @Date: 2020/10/16
 */
public class AnnotationUtils {
    /**
     * 获取Tag字符串
     *
     * @param pointInfo PointInfo
     * @return tag字符串
     */
    public static String getTagString(PointInfo pointInfo) {
        StringBuilder sb = new StringBuilder();
        Class clazz = pointInfo.getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Tag.class)) {
                continue;
            }
            field.setAccessible(true);
            Tag tag = field.getAnnotation(Tag.class);
            sb.append(tag.name());
            sb.append(" ");
            sb.append("binary(256)");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 获取Tag Value字符串
     *
     * @param pointInfo PointInfo
     * @return tag value字符串
     */
    public static String getTagValueString(PointInfo pointInfo) {
        StringBuilder sb = new StringBuilder();
        Class clazz = pointInfo.getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Tag.class)) {
                continue;
            }
            field.setAccessible(true);
            Tag tag = field.getAnnotation(Tag.class);
            sb.append("\"");
            sb.append(getGetMethod(pointInfo, tag.name()));
            sb.append("\"");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * 通过反射获取Get函数
     *
     * @param pointInfo PointInfo
     * @param name 字段
     * @return 结果
     */
    private static Object getGetMethod(PointInfo pointInfo, String name) {
        Method[] methods = pointInfo.getClass().getMethods();
        for (Method method : methods) {
            String getName = "get" + name;
            if (!getName.toLowerCase().equals(method.getName().toLowerCase())) {
                continue;
            }
            try {
                return method.invoke(pointInfo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
