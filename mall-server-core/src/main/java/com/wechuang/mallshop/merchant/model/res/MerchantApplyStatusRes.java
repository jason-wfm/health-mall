package com.wechuang.mallshop.merchant.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * C 端入驻申请进度
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "C 端入驻申请进度")
public class MerchantApplyStatusRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "申请编号")
    private Integer applyId;

    @Schema(description = "商家名称")
    private String merchantName;

    @Schema(description = "类型:10入驻 20变更 30退驻")
    private Integer applyType;

    @Schema(description = "状态:10待审核 20通过 30驳回")
    private Integer status;

    @Schema(description = "审核备注(驳回原因)")
    private String auditRemark;

    @Schema(description = "审核时间")
    private Date auditTime;

    @Schema(description = "提交时间")
    private Date createTime;
}
