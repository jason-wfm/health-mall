package com.wechuang.mallshop.analytics.model.output;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "商品销售统计", description = "商品销售统计")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnalyticsOrderItemNumOutput {
    @Schema(description = "产品编号")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "货品编号")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "商品名称")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "分类编号")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "成本价")
    @TableField("item_cost_price")
    private BigDecimal itemCostPrice;

    @Schema(description = "商品价格单价")
    @TableField("item_unit_price")
    private BigDecimal itemUnitPrice;

    @Schema(description = "资源1单价")
    @TableField("item_unit_points")
    private BigDecimal itemUnitPoints;

    @Schema(description = "资源2单价")
    @TableField("item_unit_sp")
    private BigDecimal itemUnitSp;

    @Schema(description = "商品实际成交价单价")
    @TableField("order_item_sale_price")
    private BigDecimal orderItemSalePrice;

    @Schema(description = "商品数量")
    @TableField("order_item_quantity")
    private Integer orderItemQuantity;

    @Schema(description = "商品图片")
    @TableField("order_item_image")
    private String orderItemImage;

    @Schema(description = "退货数量")
    @TableField("order_item_return_num")
    private Integer orderItemReturnNum;

    @Schema(description = "退款总额")
    @TableField("order_item_return_subtotal")
    private BigDecimal orderItemReturnSubtotal;

    @Schema(description = "退款金额:同意额度")
    @TableField("order_item_return_agree_amount")
    private BigDecimal orderItemReturnAgreeAmount;

    @Schema(description = "商品实际总金额: order_item_sale_price * order_item_quantity")
    @TableField("order_item_amount")
    private BigDecimal orderItemAmount;

    @Schema(description = "优惠金额:只考虑单品的，订单及店铺总活动优惠不影响")
    @TableField("order_item_discount_amount")
    private BigDecimal orderItemDiscountAmount;

    @Schema(description = "手工调整金额")
    @TableField("order_item_adjust_fee")
    private BigDecimal orderItemAdjustFee;

    @Schema(description = "积分费用")
    @TableField("order_item_points_fee")
    private BigDecimal orderItemPointsFee;

    @Schema(description = "赠送积分")
    @TableField("order_item_points_add")
    private BigDecimal orderItemPointsAdd;

    @Schema(description = "实付金额: order_item_payment_amount =  order_item_amount - order_item_discount_amount - order_item_adjust_fee - order_item_point_fee")
    @TableField("order_item_payment_amount")
    private BigDecimal orderItemPaymentAmount;

    @Schema(description = "评价状态(ENUM): 0-未评价;1-已评价;2-失效评价")
    @TableField("order_item_evaluation_status")
    private Boolean orderItemEvaluationStatus;

    @Schema(description = "活动类型(ENUM):0-默认;1101-加价购=搭配宝;1102-店铺满赠-小礼品;1103-限时折扣;1104-优惠套装;1105-店铺代金券coupon优惠券;1106-拼团;1107-满减送;1108-阶梯价;1109-积分换购")
    @TableField("activity_type_id")
    private Integer activityTypeId;

    @Schema(description = "促销活动ID:与activity_type_id搭配使用, 团购ID/限时折扣ID/优惠套装ID/积分兑换编号")
    @TableField("activity_id")
    private Integer activityId;

    @Schema(description = "礼包活动对应兑换码code")
    @TableField("activity_code")
    private String activityCode;

    @Schema(description = "分佣金比例百分比")
    @TableField("order_item_commission_rate")
    private BigDecimal orderItemCommissionRate;

    @Schema(description = "佣金")
    @TableField("order_item_commission_fee")
    private BigDecimal orderItemCommissionFee;

    @Schema(description = "退款佣金")
    @TableField("order_item_commission_fee_refund")
    private BigDecimal orderItemCommissionFeeRefund;

    @Schema(description = "价格策略折扣率")
    @TableField("policy_discountrate")
    private BigDecimal policyDiscountrate;

    @Schema(description = "分配优惠券额度")
    @TableField("order_item_voucher")
    private BigDecimal orderItemVoucher;

    @Schema(description = "分配满减额度")
    @TableField("order_item_reduce")
    private BigDecimal orderItemReduce;


    @Schema(description = "同意退货数量")
    @TableField("order_item_return_agree_num")
    private Integer orderItemReturnAgreeNum;


    @Schema(description = "统计数量")
    @TableField("order_item_amount_sum")
    private BigDecimal orderItemAmountSum;

    @Schema(description = "统计数量")
    @TableField("num")
    private Long num;

    @Schema(description = "退款件数")
    @TableField("refund_num")
    private Long refundNum;

    @Schema(description = "加购件数")
    @TableField("cart_num")
    private Integer cartNum;

    @Schema(description = "用户数量")
    @TableField("user_num")
    private Integer userNum;

    @Schema(description = "转化率")
    @TableField("conversion_rate")
    private BigDecimal conversionRate;
}

