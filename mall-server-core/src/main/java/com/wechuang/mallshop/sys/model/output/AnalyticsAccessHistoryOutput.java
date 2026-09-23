package com.wechuang.mallshop.sys.model.output;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Schema(name = "商品访问日志表", description = "商品访问日志表")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnalyticsAccessHistoryOutput {


    @Schema(description = "商品编号")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "用户数量")
    @TableField("user_num")
    private Integer userNum;
}

