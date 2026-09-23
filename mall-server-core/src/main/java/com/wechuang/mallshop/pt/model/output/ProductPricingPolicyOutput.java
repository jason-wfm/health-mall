package com.wechuang.mallshop.pt.model.output;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductPricingPolicy;
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
public class ProductPricingPolicyOutput extends ProductPricingPolicy {

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

    @Schema(description = "商品主图")
    @TableField("product_image")
    private String productImage;

    @Schema(description = "商品单价")
    @TableField(exist = false)
    private BigDecimal itemUnitPrice;

    @Schema(description = "用户账号")
    @TableField("user_account")
    private String userAccount;

}
