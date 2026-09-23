package com.wechuang.mallshop.trade.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.vo.OrderItemVo;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "评论显示接口响应")
public class OrderCommentRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<OrderItemVo> items;

    private Integer no;

    private Object orderEvaluation;

    private StoreInfoVo storeInfo;

    private Integer yes;

}
