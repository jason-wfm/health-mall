package com.wechuang.mallshop.core.web.model.res;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应 VO（自研重写，对齐 core-3.0.27908；注意 records 是页数，不是记录列表）
 *
 * @param <T> 记录类型
 * @since 3.1.0-healthmall
 */
@Data
public class BaseListRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 记录列表
     */
    private List<T> items;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 总页数
     */
    private Integer records;

    public BaseListRes() {
    }

    public BaseListRes(List<T> items) {
        this.items = items;
    }

    public BaseListRes(List<T> items, Integer total, Integer page, Integer size, Integer records) {
        this.items = items;
        this.total = total;
        this.page = page;
        this.size = size;
        this.records = records;
    }
}
