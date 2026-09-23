package com.wechuang.mallshop.marketing.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "活动规则列表")
public class ActivityRuleRes implements Serializable {

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "编号")
    private Integer totalId;

    @Schema(description = "商品信息集合")
    private List<ProductItemVo> item;

    @Schema(description = "总额")
    private BigDecimal amount;

    @Schema(description = "最多数量")
    private Integer maxNum;

}
