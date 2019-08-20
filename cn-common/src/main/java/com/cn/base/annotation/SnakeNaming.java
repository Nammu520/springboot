package com.cn.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * desc 接受参数时，用来下划线转驼峰注解，用于DTO
 *
 * @author LiLei
 * Date  2018/7/24
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SnakeNaming {
}
