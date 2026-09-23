package com.wechuang.mallshop.pt.model.output;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商品展示使用", description = "商品展示使用")
public class ItemOutput extends ProductItem {

    @Schema(description = "商品状态(ENUM):1001-正常;1002-下架仓库中;1003-待审核; 1000-违规禁售")
    private Integer productStateId;

    @Schema(description = "产品编号")
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    @Schema(description = "SPU货号:货号")
    @TableField("product_number")
    private String productNumber;

    @Schema(description = "产品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "Spec名称")
    private String itemSpecName;

    @Schema(description = "商品卖点:商品广告词")
    @TableField("product_tips")
    private String productTips;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "商品主图")
    @TableField("product_image")
    private String productImage;

    @Schema(description = "产品视频 ")
    @TableField("product_video")
    private String productVideo;

    @Schema(description = "选择售卖区域:完成售卖区域及运费设置")
    @TableField("transport_type_id")
    private Integer transportTypeId;

    @Schema(description = "每人限购")
    @TableField("product_buy_limit")
    private Integer productBuyLimit;

    @Schema(description = "活动产品数量")
    @TableField(exist = false)
    private Integer activityItemNum;


    @Schema(description = "门店商品编号")
    @TableField(exist = false)
    private Long chainItemId;

    @Schema(description = "商品商品库存")
    @TableField(exist = false)
    private Integer chainItemQuantity;

    @Schema(description = "门店单价")
    @TableField(exist = false)
    private BigDecimal chainItemUnitPrice;

    @Schema(description = "是否启用(BOOL):0-禁用;1-启用")
    @TableField(exist = false)
    private Boolean chainItemEnable;

    @Schema(description = "店铺名称")
    private String storeName;

    @Schema(description = "产品编号:定为SPU编号")
    private Integer associationId;

}
