package com.wechuang.mallshop.pt.model.res;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.vo.ProductItemLevelDiscountVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商品折扣", description = "商品折扣")
public class ProductItemLevelDiscountRes implements Serializable {

    @Schema(description = "等级会员(ENUM):1-默认；2-自定义")
    private Integer productLevelMembership;

    @Schema(description = "等级会员折扣")
    private List<ProductItemLevelDiscountVo> itemLevelDiscounts;
}
