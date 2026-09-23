package com.wechuang.mallshop.marketing.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "活动规则")
public class ActivityRuleVo {

    @Schema(description = "规则")
    private List<RuleVo> rule;

    @Schema(description = "前置条件")
    private RequirementVo requirement;

    @Schema(description = "优惠券对象")
    private VoucherVo voucher;

    @Schema(description = "弹窗")
    private PopupVo popup;
}
