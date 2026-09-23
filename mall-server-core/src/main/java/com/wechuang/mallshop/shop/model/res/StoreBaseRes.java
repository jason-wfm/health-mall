package com.wechuang.mallshop.shop.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.output.ProductOutput;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "店铺列表")
public class StoreBaseRes extends StoreBase {

    @Schema(description = "国家编码")
    private String storeIntl;

    @Schema(description = "卖家电话")
    private String storeTel;

    @Schema(description = "营业时间")
    private String storeOpeningHours;

    @Schema(description = "打烊时间")
    private String storeCloseHours;

    @Schema(description = "所在区域")
    private String storeArea;

    @Schema(description = "所属地区(DOT)")
    private String storeDistrictId;

    @Schema(description = "店铺详细地址")
    private String storeAddress;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "商品列表")
    private List<ProductOutput> products = new ArrayList<>();

    @Schema(description = "是否收藏")
    private Integer storeIsFavorites;

    @Schema(description = "店铺分类名称")
    private String storeCategoryName;

    @Schema(description = "收藏数量")
    private Integer storeFavoriteNum;

    @Schema(description = "店铺销量")
    private Integer storeSalesNum;

    @Schema(description = "好评率:store_servicecredit/store_evaluation_num/5,使用综合选项服务评价")
    private BigDecimal storeEvaluationRate;

    @Schema(description = "距离")
    private Double distance;
}
