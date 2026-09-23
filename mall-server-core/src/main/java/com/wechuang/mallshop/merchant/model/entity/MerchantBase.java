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

/**
 * 商家主表
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mch_merchant_base")
@Schema(name = "MerchantBase对象", description = "商家主表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantBase implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态:10待提交 20待审核 30驳回 40营业 50冻结 60退驻 */
    public static final int STATUS_TO_SUBMIT = 10;
    public static final int STATUS_TO_AUDIT = 20;
    public static final int STATUS_REJECTED = 30;
    public static final int STATUS_OPEN = 40;
    public static final int STATUS_FROZEN = 50;
    public static final int STATUS_QUIT = 60;

    @Schema(description = "商家编号")
    @TableId(value = "merchant_id", type = IdType.AUTO)
    private Integer merchantId;

    @Schema(description = "商家名称")
    @TableField("merchant_name")
    private String merchantName;

    @Schema(description = "商家简称")
    @TableField("short_name")
    private String shortName;

    @Schema(description = "状态:10待提交 20待审核 30驳回 40营业 50冻结 60退驻")
    @TableField("status")
    private Integer status;

    @Schema(description = "主营类目")
    @TableField("category")
    private String category;

    @Schema(description = "商家LOGO")
    @TableField("logo")
    private String logo;

    @Schema(description = "商家简介")
    @TableField("intro")
    private String intro;

    @Schema(description = "联系人")
    @TableField("contact_name")
    private String contactName;

    @Schema(description = "联系电话")
    @TableField("contact_phone")
    private String contactPhone;

    @Schema(description = "营业执照URL")
    @TableField("business_license")
    private String businessLicense;

    @Schema(description = "资质图片URL,逗号分隔")
    @TableField("qualification_urls")
    private String qualificationUrls;

    @Schema(description = "入驻申请人(user_base.user_id)")
    @TableField("applicant_user_id")
    private Integer applicantUserId;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
