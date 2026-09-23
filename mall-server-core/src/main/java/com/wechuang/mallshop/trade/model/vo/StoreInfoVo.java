package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.StoreInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(name = "店铺及商品信息", description = "店铺及商品信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreInfoVo extends StoreInfo implements Serializable {

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺等级")
    private Integer storeGradeId;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "纬度")
    private Double storeLatitude;

    @Schema(description = "经度")
    private Double storeLongitude;

    @Schema(description = "配送区域(DOT)")
    private String storeDeliverDistrictId;

    @Schema(description = "是否自营(ENUM): 1-自营;0-非自营")
    private Boolean storeIsSelfsupport;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    private Integer storeType;

    @Schema(description = "店铺状态(BOOL):0-关闭;  1-运营中")
    private Boolean storeIsOpen;

    @Schema(description = "店铺分类编号")
    private Integer storeCategoryId;

    @Schema(description = "免费服务(DOT)")
    private String storeO2oTags;

    @Schema(description = "是否O2O(BOOL):0-否;1-是")
    private Boolean storeO2oFlag;

    @Schema(description = "所属商圈(DOT)")
    private String storeCircle;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

}
