package com.wechuang.mallshop.pt.model.output;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductOutput extends ProductIndex {
    //base
    @Schema(description = "商品卖点:商品广告词")
    private String productTips;

    @Schema(description = "商品主图")
    private String productImage;

    @Schema(description = "产品视频 ")
    private String productVideo;

    @Schema(description = "起订量")
    private Integer productMinimumOrder;

    @Schema(description = "产品文件 ")
    private List<String> productFiles;

    @Schema(description = "品牌名称 ")
    private String brandName;

    @Schema(description = "选择售卖区域:完成售卖区域及运费设置")
    private Integer transportTypeId;

    @Schema(description = "每人限购")
    private Integer productBuyLimit;

    @Schema(description = "平台佣金比率")
    private BigDecimal productCommissionRate;

    //info
    @Schema(description = "规格(JSON)-规格、规格值、goods_id  规格不需要全选就可以添加对应数据[{'id' : spec_id, 'name' : spec_name, 'item':[{'id' : spec_item_id, 'name' : spec_item_name}, {'id' : spec_item_id, 'name' : spec_item_name}]},{'id' : spec_id, 'name' : spec_name, 'item':[{'id' : spec_item_id, 'name' : spec_item_name}, {'id' : spec_item_id, 'name' : spec_item_name}]}]")
    private String productSpec;

    @Schema(description = "商品SKU(JSON):{'uniq_id':[item_id, price, url]}")
    private String productUniqid;


    @Schema(description = "商品SKU全名")
    private String productItemName;

    @Schema(description = "默认SKU")
    private Long itemId;

    @Schema(description = "SKU信息")
    private List<ProductItem> items;

    @Schema(description = "活动信息")
    private Object activity;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "店铺logo")
    private String storeLogo;

    @Schema(description = "所在区域")
    private String storeArea;

    @Schema(description = "店铺详细地址")
    private String storeAddress;

    @Schema(description = "是否收藏")
    private Boolean storeIsFavorites;

    @Schema(description = "店铺统计信息")
    private StoreAnalytics storeAnalytics;

    @Schema(description = "会员专属价")
    private BigDecimal itemPolicyPrice;

    @Schema(description = "SKU价格")
    private BigDecimal itemUnitPrice;

    @Schema(description = "SKU销售价")
    private BigDecimal itemSalePrice;

    @Schema(description = "PLUS会员价")
    private BigDecimal itemPlusPrice;

    @Schema(description = "PLUS节省价格")
    private BigDecimal itemPlusDiscountPrice;

    @Schema(description = "SKU有效库存")
    private Integer itemAvailableQuantity;

    @Schema(description = "节省价格")
    private BigDecimal itemSavePrice;

    @Schema(description = "价格类型")
    private String priceType;

    @Schema(description = "展示标签")
    private String priceTag;

    @Schema(description = "商品规格序列化(JSON):{spec_id:spec_item_id, spec_id:spec_item_id, spec_id:spec_item_id}")
    private String itemSpec;

}
