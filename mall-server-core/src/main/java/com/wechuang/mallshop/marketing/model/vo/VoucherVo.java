package com.wechuang.mallshop.marketing.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "优惠券")
public class VoucherVo {

    @Schema(description = "优惠券开始时间")
    private Long voucherStartDate;

    @Schema(description = "优惠券失效日期")
    private Long voucherEndDate;

    @Schema(description = "优惠券价格")
    private BigDecimal voucherPrice;

    @Schema(description = "已领取张数")
    private Integer voucherQuantityUse;

    @Schema(description = "优惠券图片")
    private String voucherImage;

    @Schema(description = "优惠券数量")
    private Integer voucherQuantity;

    @Schema(description = "可领数量")
    private Integer voucherQuantityFree;

    @Schema(description = "优惠券限制")
    private Integer voucherPreQuantity;

    @Schema(description = "适用商品(ENUM):1-全部商品可用;2-指定商品可用")
    private Integer voucherProductLimit;
}
