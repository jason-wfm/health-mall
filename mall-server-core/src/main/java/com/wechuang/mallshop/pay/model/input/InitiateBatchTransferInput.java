package com.wechuang.mallshop.pay.model.input;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "转账到零钱")
public class InitiateBatchTransferInput implements Serializable {

    @Schema(description = "APPID")
    private String appid;

    @Schema(description = "商户单号")
    private String out_batch_no;

    @Schema(description = "转账批次名称")
    private String batch_name;

    @Schema(description = "转账批次备注")
    private String batch_remark;

    @Schema(description = "回调地址")
    private String notify_url;

    @Schema(description = "转账总金额 说明：转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作")
    private Long total_amount;

    @Schema(description = "转账总笔数")
    private Integer total_num;

    @Schema(description = "转账明细列表")
    private List<TransferDetailInput> transfer_detail_list;

    @Schema(description = "转账场景id")
    private String transfer_scene_id;

}
