package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(name = "店铺及商品信息", description = "店铺及商品信息")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActivitysVo implements Serializable {
    @Schema(description = "店铺赠品")
    private List<ProductItemVo> gift = new ArrayList<>();

    @Schema(description = "店铺满减")
    List reduction = new ArrayList<>();

    @Schema(description = "店铺多件折")
    List multple = new ArrayList<>();

    @Schema(description = "加价购")
    List bargains = new ArrayList<>();

    @Schema(description = "满返")
    List manhui = new ArrayList<>();
}
