package com.wechuang.mallshop.marketing.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "活动规则")
public class PageDataVo {

    @Schema(description = "主键")
    private Long itemId;

    @Schema(description = "标题")
    private String itemName;

    @Schema(description = "产品图片")
    private String productImage;

    @Schema(description = "销售价")
    private Integer groupSalePrice;

    @Schema(description = "市场价")
    private Integer itemUnitPrice;

    @Schema(description = "开团人数")
    private Integer groupQuantity;

    @Schema(description = "参团人数")
    private Integer groupUserAmount;

    @Schema(description = "优惠券图片")
    private String voucherImage;

    @Schema(description = "优惠券价格")
    private Integer voucherPrice;

    @Schema(description = "活动开始时间")
    private String activityStarttime;

    @Schema(description = "活动结束时间")
    private String activityEndtime;

    @Schema(description = "优惠券数量")
    private Integer voucherPreQuantity;

    @Schema(description = "售出数量")
    private Integer productSaleNum;


}
