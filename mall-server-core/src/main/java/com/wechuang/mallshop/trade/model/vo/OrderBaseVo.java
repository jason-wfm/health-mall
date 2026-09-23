package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单详细信息
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "订单详细信息")
public class OrderBaseVo implements Serializable {

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "下单时间")
    private Date orderTime;

}
