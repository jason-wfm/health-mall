package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FreightVo {
    @Schema(description = "默认运费")
    private Integer freight;

    @Schema(description = "是否可销售")
    private Integer ifStore;
}
