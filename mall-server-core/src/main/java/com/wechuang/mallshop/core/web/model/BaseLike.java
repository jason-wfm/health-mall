package com.wechuang.mallshop.core.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 泛用模糊搜索项（自研重写，对齐 core-3.0.27908）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class BaseLike implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 列名（蛇形）
     */
    private String column;

    /**
     * 模糊匹配值
     */
    private String like;
}
