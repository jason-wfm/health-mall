package com.wechuang.mallshop.trade.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.entity.UserDeliveryAddress;
import com.wechuang.mallshop.trade.model.input.CheckoutInput;
import com.wechuang.mallshop.trade.model.vo.StoreItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "CheckoutVo对象", description = "CheckoutVo对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckoutOutput implements Serializable {

    @Schema(description = "店铺信息")
    List<StoreItemVo> items = new ArrayList<>();

    @Schema(description = "商品原价总价")
    BigDecimal orderProductAmount;

    @Schema(description = "单品优惠总价")
    BigDecimal orderItemAmount;

    @Schema(description = "运费总价")
    BigDecimal orderFreightAmount;

    @Schema(description = "优惠总额度=orderProductAmount-orderMoneyAmount")
    BigDecimal orderDiscountAmount;

    @Schema(description = "销售员折扣总额度")
    BigDecimal orderSalePersonDiscount;

    @Schema(description = "商品最终总价：单品优惠总价+运费-代金券")
    BigDecimal orderMoneyAmount;

    @Schema(description = "订单需要总积分")
    BigDecimal orderPointsAmount;

    @Schema(description = "订单需要总积分2")
    BigDecimal orderSpAmount;

    //前端使用跳转判断
    @Schema(description = "是否支付完成")
    Boolean isPaid = false;

    @Schema(description = "买家编号")
    private Integer userId;

    @Schema(description = "会员等级")
    private Integer userLevelId;

    @Schema(description = "地址信息")
    private UserDeliveryAddress userDeliveryAddress;

    @Schema(description = "输入参数")
    private CheckoutInput in;

    @Schema(description = "商户订单编号")
    private String orderId;

    @Schema(description = "实付金额:在线支付金额,此为订单默认需要支付额度。")
    private BigDecimal tradePaymentAmount;
}