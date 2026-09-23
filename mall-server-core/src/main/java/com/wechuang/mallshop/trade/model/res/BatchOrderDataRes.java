package com.wechuang.mallshop.trade.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "批量下单")
public class BatchOrderDataRes {

    @Schema(description = "全部")
    private Integer allNum = 0;

    @Schema(description = "完全匹配")
    private Integer exactMatchNum = 0;

    @Schema(description = "待询价")
    private Integer forInquiryNum = 0;

    @Schema(description = "无法匹配")
    private Integer noMatchNum = 0;

    @Schema(description = "批量下单集合")
    private List<BatchOrderRes> items = new ArrayList<>();

}