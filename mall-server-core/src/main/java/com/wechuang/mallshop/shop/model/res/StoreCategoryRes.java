package com.wechuang.mallshop.shop.model.res;

import com.wechuang.mallshop.shop.model.entity.StoreCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class StoreCategoryRes extends StoreCategory {


    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "索引")
    private String index = "store_category_id";

    @Schema(description = "分类编号")
    private Integer value;

    @Schema(description = "子节点列表")
    private List<StoreCategoryRes> children;

}
