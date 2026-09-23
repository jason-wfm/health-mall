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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家账户表（支付/结算 + 服务商进件信息）
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mch_merchant_account")
@Schema(name = "MerchantAccount对象", description = "商家账户表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 结算账户类型:10对公银行 20对私银行 */
    public static final int ACCOUNT_TYPE_CORPORATE = 10;
    public static final int ACCOUNT_TYPE_PRIVATE = 20;

    /** 微信进件状态:0未进件 10审核中 20已开通 30驳回 */
    public static final int WX_APPLY_NONE = 0;
    public static final int WX_APPLY_AUDITING = 10;
    public static final int WX_APPLY_OPENED = 20;
    public static final int WX_APPLY_REJECTED = 30;

    @Schema(description = "商家编号")
    @TableId(value = "merchant_id", type = IdType.INPUT)
    private Integer merchantId;

    @Schema(description = "结算账户类型:10对公银行 20对私银行")
    @TableField("settle_account_type")
    private Integer settleAccountType;

    @Schema(description = "结算账号")
    @TableField("settle_account_no")
    private String settleAccountNo;

    @Schema(description = "结算户名")
    @TableField("settle_account_name")
    private String settleAccountName;

    @Schema(description = "开户银行")
    @TableField("settle_bank_name")
    private String settleBankName;

    @Schema(description = "微信特约商户号(服务商进件返回)")
    @TableField("wx_sub_mchid")
    private String wxSubMchid;

    @Schema(description = "微信进件状态:0未进件 10审核中 20已开通 30驳回")
    @TableField("wx_apply_status")
    private Integer wxApplyStatus = 0;

    @Schema(description = "微信进件单号(幂等键)")
    @TableField("wx_apply_id")
    private String wxApplyId;

    @Schema(description = "微信分账比例(%)")
    @TableField("wx_profit_sharing_ratio")
    private BigDecimal wxProfitSharingRatio = BigDecimal.ZERO;

    @Schema(description = "支付宝ISV授权token")
    @TableField("alipay_app_auth_token")
    private String alipayAppAuthToken;

    @Schema(description = "支付宝token过期时间")
    @TableField("alipay_token_expire")
    private Date alipayTokenExpire;

    @Schema(description = "支付宝商家PID")
    @TableField("alipay_pid")
    private String alipayPid;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
