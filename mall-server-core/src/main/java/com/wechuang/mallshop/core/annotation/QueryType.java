package com.wechuang.mallshop.core.annotation;

/**
 * 查询条件类型（自研重写，18 个枚举值对齐 core-3.0.27908 javap 核验）
 *
 * @since 3.1.0-healthmall
 */
public enum QueryType {
    EQ,
    NE,
    GT,
    GE,
    LT,
    LE,
    LIKE,
    NOT_LIKE,
    LIKE_LEFT,
    LIKE_RIGHT,
    IS_NULL,
    IS_NOT_NULL,
    IN,
    NOT_IN,
    IN_STR,
    NOT_IN_STR,
    FIND_IN_SET,
    FIND_IN_SET_STR
}
