package com.wechuang.mallshop.sys.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCenterOutput {
    @Schema(description = "菜单信息")
    private Map menus;

    @Schema(description = "订单信息")
    private Map orders;
}
