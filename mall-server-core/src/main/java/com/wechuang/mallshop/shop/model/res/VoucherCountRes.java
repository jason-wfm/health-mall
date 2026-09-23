package com.wechuang.mallshop.shop.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "自定义不同优惠券数量")
public class VoucherCountRes implements Serializable {

    @Schema(description = "所有优惠券")
    private Long voucherAllNum;

    @Schema(description = "线下优惠券")
    private Long voucherOfflinedNum;

    @Schema(description = "线上优惠券")
    private Long voucherOnlinedNum;

    @Schema(description = "附加优惠券")
    private Long voucherCarcouponNum;

    @Schema(description = "未使用优惠券")
    private Long voucherUnusedNum;

    @Schema(description = "已使用优惠券")
    private Long voucherUsedNum;

    @Schema(description = "已过期优惠券")
    private Long voucherTimeoutNum;

}
