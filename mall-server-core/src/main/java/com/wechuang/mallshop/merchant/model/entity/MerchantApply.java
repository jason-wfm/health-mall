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
 * 商家申请单（入驻/变更/退驻）
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mch_merchant_apply")
@Schema(name = "MerchantApply对象", description = "商家申请单")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantApply implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 类型:10入驻 20变更 30退驻 */
    public static final int TYPE_ENTER = 10;
    public static final int TYPE_CHANGE = 20;
    public static final int TYPE_QUIT = 30;

    /** 状态:10待审核 20通过 30驳回 */
    public static final int STATUS_TO_AUDIT = 10;
    public static final int STATUS_PASSED = 20;
    public static final int STATUS_REJECTED = 30;

    @Schema(description = "申请编号")
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;

    @Schema(description = "商家编号(入驻单审核前为0)")
    @TableField("merchant_id")
    private Integer merchantId = 0;

    @Schema(description = "类型:10入驻 20变更 30退驻")
    @TableField("apply_type")
    private Integer applyType;

    @Schema(description = "申请人(user_base.user_id)")
    @TableField("apply_user_id")
    private Integer applyUserId;

    @Schema(description = "申请人姓名")
    @TableField("apply_name")
    private String applyName;

    @Schema(description = "申请人电话")
    @TableField("apply_phone")
    private String applyPhone;

    @Schema(description = "申请资料快照JSON")
    @TableField("snapshot_json")
    private String snapshotJson;

    @Schema(description = "状态:10待审核 20通过 30驳回")
    @TableField("status")
    private Integer status;

    @Schema(description = "是否在途:1是 0已完结(释放唯一键)")
    @TableField("apply_active")
    private Boolean applyActive = true;

    @Schema(description = "审核人")
    @TableField("audit_user_id")
    private Integer auditUserId;

    @Schema(description = "审核时间")
    @TableField("audit_time")
    private Date auditTime;

    @Schema(description = "审核备注(驳回原因)")
    @TableField("audit_remark")
    private String auditRemark;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
