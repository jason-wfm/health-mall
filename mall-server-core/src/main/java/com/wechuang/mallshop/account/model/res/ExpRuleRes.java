package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
@Schema(name = "用户等级规则")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ExpRuleRes {

    @Schema(description = "注册增加")
    private String exp_reg;

    @Schema(description = "评论增加")
    private String exp_evaluate_good;

    @Schema(description = "登录增加")
    private String exp_login;

    @Schema(description = "消费额与增加比例")
    private String exp_consume_rate;

    @Schema(description = "每单最多增加")
    private String exp_consume_max;

}
