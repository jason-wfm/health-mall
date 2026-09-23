package com.wechuang.mallshop.core.web.model.input;

import com.wechuang.mallshop.core.web.model.BaseLike;
import com.wechuang.mallshop.core.web.model.BaseOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Service 层分页查询入参基类（自研重写，对齐 core-3.0.27908；与 BaseListReq 同构但无 sourceLang）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class BaseListInput implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer page;

    private Integer size;

    private String sidx;

    private String sort;

    private List<BaseOrder> order;

    private List<BaseLike> like;

    private String createTimeStart;

    private String createTimeEnd;
}
