package com.rexel.tdengine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Tag
 * @Description Tag
 * @Author: chunhui.qu
 * @Date: 2020/10/16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Tag {
    /**
     * tag名称
     */
    String name() default "";
}