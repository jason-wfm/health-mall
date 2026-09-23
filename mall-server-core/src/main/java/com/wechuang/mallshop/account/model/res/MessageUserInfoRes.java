package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "站内信userInfo")
public class MessageUserInfoRes {

    @Schema(description = "用户昵称")
    private String username;

    @Schema(description = "uid")
    private Integer uid;

    @Schema(description = "uid")
    private String face;

}