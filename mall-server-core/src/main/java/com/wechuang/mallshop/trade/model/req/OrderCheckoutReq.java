package com.wechuang.mallshop.trade.model.req;

import com.wechuang.mallshop.common.api.StateCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCheckoutReq {
    @Schema(description = "收货地址编号")
    private Integer udId = 0;

    @Schema(description = "下单商品数据:商品编号|数量,商品编号|数量...")
    String cartId = "";

    @Schema(description = "门店编号")
    private Integer chainId = 0;

    @Schema(description = "活动编号")
    private Integer activityId = 0;

    @Schema(description = "配送方式")
    private Integer deliveryTypeId = StateCode.DELIVERY_TYPE_EXP;

    @Schema(description = "来源渠道 0:正常下单;1:直播渠道")
    private Integer channelType = 0;

    @Schema(description = "优惠券")
    private String userVoucherIds;
}
