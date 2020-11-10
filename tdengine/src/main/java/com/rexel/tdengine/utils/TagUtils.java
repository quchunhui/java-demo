package com.rexel.tdengine.utils;

import com.rexel.tdengine.annotation.Tag;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName TagUtils
 * @Description TagUtils
 * @Author: chunhui.qu
 * @Date: 2020/10/16
 */
public class TagUtils {
    public static String getTagString(Object object) {
        StringBuilder sb = new StringBuilder();
        Class clazz = object.getClass();
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

    public static String getTagValueString(Object object) {
        StringBuilder sb = new StringBuilder();
        Class clazz = object.getClass();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Tag.class)) {
                continue;
            }
            field.setAccessible(true);
            Tag tag = field.getAnnotation(Tag.class);
            sb.append("\"");
            sb.append(getGetMethod(object, tag.name()));
            sb.append("\"");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static Object getGetMethod(Object ob, String name) {
        Method[] methods = ob.getClass().getMethods();
        for (Method method : methods) {
            String getName = "get" + name;
            if (!getName.toLowerCase().equals(method.getName().toLowerCase())) {
                continue;
            }
            try {
                return method.invoke(ob);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
