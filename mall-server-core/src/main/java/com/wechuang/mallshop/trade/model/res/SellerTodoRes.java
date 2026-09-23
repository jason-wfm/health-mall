package com.wechuang.mallshop.trade.model.res;


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
@Schema(name = "商家待办事项")
public class SellerTodoRes implements Serializable {

    @Schema(description = "待发货")
    private Long waitShippingNum;

    @Schema(description = "待审核")
    private Long reviewNum;

    @Schema(description = "违规商品")
    private Long offNum;

    @Schema(description = "待补货")
    private int productWarningNum;

    @Schema(description = "待回复")
    private Long waitReplyNum;

    @Schema(description = "待开票")
    private Long invoiceWaitNum;

}
