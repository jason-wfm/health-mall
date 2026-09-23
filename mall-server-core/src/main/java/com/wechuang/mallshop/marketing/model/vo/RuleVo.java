package com.wechuang.mallshop.marketing.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "规则")
public class RuleVo {
    @Schema(description = "总额")
    private BigDecimal amount;

    @Schema(description = "最大总额")
    private BigDecimal maxAmount;

    @Schema(description = "百分比")
    private BigDecimal percent;

    @Schema(description = "总数量")
    private Integer num;

    @Schema(description = "最多数量")
    private Integer maxNum;

    private Integer pointsStandard;

    private Integer pointsDouble;

    @Schema(description = "产品及数量")
    private List<ItemNumVo> item;

    @Schema(description = "满返优惠券名称")
    private String giveVoucherName;

    @Schema(description = "满返优惠券价格")
    private BigDecimal giveVoucherPrice;

    @Schema(description = "满返优惠券消费总额")
    private BigDecimal giveVoucherSubtotal;
}