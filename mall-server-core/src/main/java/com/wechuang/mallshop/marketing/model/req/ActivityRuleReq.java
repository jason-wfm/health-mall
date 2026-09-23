package com.wechuang.mallshop.marketing.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "活动规则参数")
public class ActivityRuleReq implements Serializable {

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "编号")
    private Integer totalId;

    @Schema(description = "总额")
    private BigDecimal amount;

    @Schema(description = "最多数量")
    private Integer maxNum;

    @Schema(description = "换购商品价格")
    private BigDecimal itemReplacePrice;

    @Schema(description = "换购商品价格的编号")
    private Long itemId;

}
