package com.wechuang.mallshop.shop.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.shop.model.entity.StoreTransportItem;
import com.wechuang.mallshop.shop.model.entity.StoreTransportType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreTransportItemVo extends StoreTransportType {
    @Schema(description = "运费项目")
    private StoreTransportItem item;
}
