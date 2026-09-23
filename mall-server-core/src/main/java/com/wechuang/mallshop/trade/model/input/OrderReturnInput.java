package com.wechuang.mallshop.trade.model.input;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.trade.model.vo.OrderReturnItemInputVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "退货申请对象", description = "退货申请")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderReturnInput {

    @Schema(description = "退单号")
    private String returnId;

    @Schema(description = "订单编号")
    private String orderId;

    @Schema(description = "订单商品表编号")
    private List<OrderReturnItemInputVo> returnItems = new ArrayList<>();

    @Schema(description = "买家退货手机号")
    private String returnBuyerMobile;

    @Schema(description = "买家退货备注")
    private String returnBuyerMessage;

    @Schema(description = "联系电话")
    private String returnTel;

    @Schema(description = "退款理由编号")
    private Integer returnReasonId = 0;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "退款凭据(DOT)")
    private String returnItemImage;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "退货标记(BOOL):true-全退， false-单品退")
    private Boolean returnAllFlag = false;

    @Schema(description = "退货类型(ENUM): 0-不用退货;1-需要退货")
    private Integer returnFlag = 1;

    @Schema(description = "自动审核(BOOL): 0-不审核;1-自动审核")
    private Boolean reviewFlag = false;
}
