package com.wechuang.mallshop.merchant.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家停启/冻结参数（平台）
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "商家停启参数")
public class MerchantBaseStateEditReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "商家编号")
    private Integer merchantId;

    @Schema(description = "状态:40营业 50冻结")
    private Integer status;
}
