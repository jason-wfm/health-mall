package com.wechuang.mallshop.trade.model.res;


import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "平台待办事项")
public class AdminTodoRes implements Serializable {

    @Schema(description = "待审核商户入驻")
    private Long storeCertificationNum;

    @Schema(description = "待审商品")
    private Long productVerifyNum;

    @Schema(description = "待审核提现")
    private Long withdrawNum;

    @Schema(description = "待审核实名")
    private Long userCertificationNum;

    @Schema(description = "待审核社区内容")
    private Long storyVerifyNum;

    @Schema(description = "待处理用户反馈")
    private Long feedbackTodoNum;

}
