package com.wechuang.mallshop.merchant.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家结算账户维护参数（商家）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商家结算账户维护参数")
public class MerchantAccountEditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "结算账户类型:10对公银行 20对私银行")
    private Integer settleAccountType;

    @Schema(description = "结算账号")
    private String settleAccountNo;

    @Schema(description = "结算户名")
    private String settleAccountName;

    @Schema(description = "开户银行")
    private String settleBankName;
}
