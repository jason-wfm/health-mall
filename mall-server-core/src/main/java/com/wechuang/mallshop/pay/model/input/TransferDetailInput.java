package com.wechuang.mallshop.pay.model.input;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "转账明细")
public class TransferDetailInput implements Serializable {

    @Schema(description = "商家明细单号")
    private String out_detail_no;

    @Schema(description = "转账金额 转账金额单位为“分”")
    private Long transfer_amount;

    @Schema(description = "转账备注")
    private String transfer_remark;

    @Schema(description = "收款用户OpenID")
    private String openid;

    @Schema(description = "收款用户姓名")
    private String user_name;

}
