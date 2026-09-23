package com.wechuang.mallshop.merchant.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 入驻/变更单审核参数（平台）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "入驻/变更单审核参数")
public class MerchantApplyAuditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "申请编号")
    private Integer applyId;

    @Schema(description = "是否通过(BOOL):true-通过;false-驳回")
    private Boolean passed;

    @Schema(description = "审核备注(驳回必填)")
    private String auditRemark;
}
