package com.wechuang.mallshop.core.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 排序项（自研重写，对齐 core-3.0.27908）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class BaseOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 排序列（蛇形列名）
     */
    private String sidx;

    /**
     * 排序方向：asc / desc（见 Constants.ORDER_BY_ASC/DESC）
     */
    private String sort;
}
