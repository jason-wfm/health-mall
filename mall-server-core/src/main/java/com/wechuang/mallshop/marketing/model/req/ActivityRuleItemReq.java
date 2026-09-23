package com.wechuang.mallshop.marketing.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "活动规则商品参数")
public class ActivityRuleItemReq implements Serializable {

    @Schema(description = "活动编号")
    private Integer activityId;

    @Schema(description = "编号")
    private Integer totalId;

    @Schema(description = "商品编号(DOT)")
    private String itemIds;

    @Schema(description = "商品编号")
    private Long itemId;
}
