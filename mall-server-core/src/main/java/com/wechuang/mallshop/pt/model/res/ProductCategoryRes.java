package com.wechuang.mallshop.pt.model.res;

import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductCategoryRes extends ProductCategory {

    @Schema(description = "子节点列表")
    private List<ProductCategoryRes> children;

}
