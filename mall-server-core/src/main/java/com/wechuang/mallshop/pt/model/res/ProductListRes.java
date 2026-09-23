package com.wechuang.mallshop.pt.model.res;

import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.model.output.ProductAssistOutput;
import com.wechuang.mallshop.pt.model.output.ProductOutput;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductListRes extends BaseListRes<ProductOutput> {
    @Schema(description = "分类辅助属性")
    List<ProductAssistOutput> assists = new ArrayList<>();
}
