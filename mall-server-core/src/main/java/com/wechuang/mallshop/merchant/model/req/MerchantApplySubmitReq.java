package com.wechuang.mallshop.merchant.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家入驻提交参数（C 端）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商家入驻提交参数")
public class MerchantApplySubmitReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商家名称")
    private String merchantName;

    @Schema(description = "商家简称")
    private String shortName;

    @Schema(description = "主营类目")
    private String category;

    @Schema(description = "商家LOGO")
    private String logo;

    @Schema(description = "商家简介")
    private String intro;

    @Schema(description = "联系人")
    private String contactName;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "营业执照URL")
    private String businessLicense;

    @Schema(description = "资质图片URL,逗号分隔")
    private String qualificationUrls;
}
