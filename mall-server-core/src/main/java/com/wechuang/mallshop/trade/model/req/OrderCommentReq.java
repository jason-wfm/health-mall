package com.wechuang.mallshop.trade.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "添加评论请求数据")
public class OrderCommentReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "评论详细 -> 对应OrderCommentItemReq")
    private String item;

    @Schema(description = "评论内容")
    private String commentContent;

    @Schema(description = "描述相符")
    private BigDecimal storeDesccredit = BigDecimal.ZERO;

    @Schema(description = "服务评价")
    private BigDecimal storeServicecredit = BigDecimal.ZERO;

    @Schema(description = "物流评价")
    private BigDecimal storeDeliverycredit = BigDecimal.ZERO;

    @Schema(description = "匿名评价")
    private Boolean commentIsAnonymous;

}
