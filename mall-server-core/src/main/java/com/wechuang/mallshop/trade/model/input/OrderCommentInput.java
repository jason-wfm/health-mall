package com.wechuang.mallshop.trade.model.input;

import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.req.OrderCommentItemReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCommentInput {

    private String orderId;

    private OrderBase orderBase;

    private OrderCommentItemReq commentItemReq;

    private List<String> commentImage;

    @Schema(description = "描述相符")
    private BigDecimal storeDesccredit = BigDecimal.ZERO;

    @Schema(description = "服务评价")
    private BigDecimal storeServicecredit = BigDecimal.ZERO;

    @Schema(description = "物流评价")
    private BigDecimal storeDeliverycredit = BigDecimal.ZERO;

}
