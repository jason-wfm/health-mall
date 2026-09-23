package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.entity.OrderReturnReason;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "退货订单商品")
public class OrderReturnItemVo {

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "订单item_id")
    private Long orderItemId;

    @Schema(description = "商品名称")
    private String productItemName;

    @Schema(description = "单位")
    private String unitName;

    @Schema(description = "单价")
    private BigDecimal itemUnitPrice;

    @Schema(description = "退款凭据")
    private List<String> returnItemImageList;

    @Schema(description = "退货商品数量")
    private Integer returnItemNum;

    @Schema(description = "退款总额")
    private BigDecimal returnItemSubtotal;

    @Schema(description = "商品图片")
    private String orderItemImage;

    @Schema(description = "商品实际成交价单价")
    private BigDecimal orderItemSalePrice;

    @Schema(description = "商品数量")
    private Integer orderItemQuantity;

    @Schema(description = "货品编号")
    private Long itemId;

    @Schema(description = "商品名称")
    private String itemName;

    @Schema(description = "产品编号")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "可退金额")
    private BigDecimal canRefundAmount;

    @Schema(description = "可退数量")
    private Integer canRefundNum;

    @Schema(description = "收回优惠券提醒")
    private Boolean withdrawVoucherRemind = false;

    @Schema(description = "退货原因集合")
    List<OrderReturnReason> returnReasonList;

}
