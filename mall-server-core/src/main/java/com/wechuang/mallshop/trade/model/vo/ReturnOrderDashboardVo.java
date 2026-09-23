package com.wechuang.mallshop.trade.model.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "商家统计信息")
public class ReturnOrderDashboardVo implements Serializable {


    @Schema(description = "已完成")
    private Long finNum;

    @Schema(description = "待审核")
    private Long reviewNum;

    @Schema(description = "未完成")
    private Long unFinNum;

}
