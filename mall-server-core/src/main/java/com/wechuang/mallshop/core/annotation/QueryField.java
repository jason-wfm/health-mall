package com.wechuang.mallshop.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ListReq 字段查询条件声明（自研重写，默认值对齐 core-3.0.27908：value 默认空串、type 默认 EQ）
 * value 为空时按字段名驼峰转下划线取列名
 *
 * @since 3.1.0-healthmall
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface QueryField {

    /**
     * 列名，为空时按字段名驼峰转下划线
     */
    String value() default "";

    /**
     * 条件类型
     */
    QueryType type() default QueryType.EQ;
}
