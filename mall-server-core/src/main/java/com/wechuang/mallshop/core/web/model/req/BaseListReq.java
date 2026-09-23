package com.wechuang.mallshop.core.web.model.req;

import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.BaseLike;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询请求基类（自研重写，字段对齐 core-3.0.27908 javap 核验）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class BaseListReq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer size;

    /**
     * 排序字段（蛇形列名）
     */
    private String sidx;

    /**
     * 排序方向：asc / desc
     */
    private String sort;

    /**
     * 附加排序列表
     */
    private List<BaseOrder> order;

    /**
     * 泛用模糊搜索列表
     */
    private List<BaseLike> like;

    /**
     * 创建时间起（字符串，如 2024-01-01 00:00:00）
     */
    private String createTimeStart;

    /**
     * 创建时间止
     */
    private String createTimeEnd;

    /**
     * 来源语言
     */
    private String sourceLang;

    public void setAsc() {
        this.sort = Constants.ORDER_BY_ASC;
    }

    public void setDesc() {
        this.sort = Constants.ORDER_BY_DESC;
    }
}
