package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "站内信content")
public class MessageContentRes {

    @Schema(description = "消息内容")
    private String text;

    @Schema(description = "消息长度")
    private Integer messageLength;

    @Schema(description = "图片宽度")
    private Integer messageW;

    @Schema(description = "图片高度")
    private Integer messageH;

    @Schema(description = "商品价格单价")
    private BigDecimal itemUnitPrice;

    @Schema(description = "商品名称")
    private String productItemName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "商品编号")
    private Long itemId;
}
