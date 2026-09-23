package com.wechuang.mallshop.pt.model.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDetailReq {
    @Schema(description = "SKU编号")
    Long itemId;

    @Schema(description = "配送地区")
    private Integer districtId;

    @Schema(description = "拼团活动编号")
    private Integer gbId;

    @Schema(description = "A+B组合套餐活动编号")
    private Integer gifgbagId;

    @Schema(description = "砍价活动编号")
    private Integer cutpriceId;
}
