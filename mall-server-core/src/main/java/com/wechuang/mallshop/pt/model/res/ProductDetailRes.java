package com.wechuang.mallshop.pt.model.res;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.model.entity.ProductComment;
import com.wechuang.mallshop.pt.model.entity.ProductImage;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.pt.model.output.ProductOutput;
import com.wechuang.mallshop.sys.model.entity.ContractType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailRes extends ProductOutput {
    @Schema(description = "商品描述")
    @TableField("product_detail")
    private String productDetail;

    @Schema(description = "SKU")
    ProductItem itemRow;

    @Schema(description = "Image")
    ProductImage image;

    @Schema(description = "默认运费")
    private BigDecimal freight;

    @Schema(description = "默认区域")
    private List<Integer> districtList;

    @Schema(description = "是否可销售")
    private Boolean ifStore;

    @Schema(description = "是否收藏")
    private Boolean isFavorite;

    @Schema(description = "是否对比")
    private Boolean isCompare;

    @Schema(description = "最后几条评论")
    private List<ProductComment> lastComments;

    @Schema(description = "最后一条评论")
    private ProductComment lastComment;

    @Schema(description = "分类辅助属性")
    List<ProductAssistOutput> assists = new ArrayList<>();

    @Schema(description = "服务")
    List<ContractType> contracts = new ArrayList<>();

    @Schema(description = "商圈")
    List<Object> markets = new ArrayList<>();

    @Schema(description = "辅助属性")
    List<Map<String, Object>> productAssist = new ArrayList<>();

    @Schema(description = "关联商品")
    List<ItemOutput> associationItems = new ArrayList<>();

    @Schema(description = "关联商品分类")
    private List<ProductCategory> associationCategorys = new ArrayList<>();


    @Schema(description = "商品分类")
    private List<ProductCategory> productCategorys = new ArrayList<>();
}
