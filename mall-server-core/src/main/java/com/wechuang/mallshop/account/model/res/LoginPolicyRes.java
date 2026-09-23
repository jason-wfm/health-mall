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
@Schema(name = "隐私政策")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginPolicyRes {

    @Schema(description = "文档内容")
    private String document;

    @Schema(description = "提现协议状态")
    private Integer state;
}
