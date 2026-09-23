package com.wechuang.mallshop.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评价Vo
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "评价Vo")
public class EvaluationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "评价状态")
    private List<Integer> orderItemEvaluationStatus;

    @Schema(description = "订单编号")
    private String orderId;

}
