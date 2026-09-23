package com.wechuang.mallshop.merchant.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 商家审核留痕
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mch_merchant_audit_log")
@Schema(name = "MerchantAuditLog对象", description = "商家审核留痕")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantAuditLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "日志编号")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @Schema(description = "申请编号")
    @TableField("apply_id")
    private Integer applyId;

    @Schema(description = "商家编号")
    @TableField("merchant_id")
    private Integer merchantId = 0;

    @Schema(description = "变更前状态")
    @TableField("before_status")
    private Integer beforeStatus;

    @Schema(description = "变更后状态")
    @TableField("after_status")
    private Integer afterStatus;

    @Schema(description = "操作人")
    @TableField("audit_user_id")
    private Integer auditUserId;

    @Schema(description = "操作时间")
    @TableField("audit_time")
    private Date auditTime;

    @Schema(description = "操作备注")
    @TableField("audit_remark")
    private String auditRemark;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
