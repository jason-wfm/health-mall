package com.wechuang.mallshop.core.web.model.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Service 层分页查询出参基类（自研重写，对齐 core-3.0.27908）
 *
 * @param <T> 记录类型
 * @since 3.1.0-healthmall
 */
@Data
public class BaseListOutput<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T> items;

    private Integer total;

    private Integer page;

    private Integer size;

    /**
     * 总页数
     */
    private Integer records;
}
