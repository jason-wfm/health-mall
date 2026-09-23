package com.wechuang.mallshop.trade.model.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wechuang.mallshop.common.api.StateCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class OrderAddReq {
    @Schema(description = "收货地址编号")
    private Integer udId = 0;

    @Schema(description = "下单商品数据:商品编号|数量,商品编号|数量...")
    String cartId = "";

    @Schema(description = "门店编号")
    private Integer chainId = 0;

    @Schema(description = "活动编号")
    private Integer activityId = 0;

    @Schema(description = "拼团编号")
    private Integer gbId = 0;

    @Schema(description = "配送方式")
    private Integer deliveryTypeId = StateCode.DELIVERY_TYPE_EXP;

    @Schema(description = "来源渠道 0:正常下单;1:直播渠道")
    private Integer channelType = 0;

    @Schema(description = "付款方式")
    private Integer paymentTypeId;


    @Schema(description = "消息")
    private String orderMessage;


    @Schema(description = "优惠券")
    private String userVoucherIds;

    @Schema(description = "发票")
    private String userInvoiceIds;

    @Schema(description = "买家编号")
    private Integer userId;

    @Schema(description = "SKU信息")
    private String productItems;

    @Schema(description = "自提日期")
    private Date virtualServiceDate;

    @Schema(description = "自提日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date virtualServiceTime;
}
