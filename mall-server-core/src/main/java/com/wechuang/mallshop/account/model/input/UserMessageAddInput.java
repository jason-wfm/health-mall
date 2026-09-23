package com.wechuang.mallshop.account.model.input;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "站内信添加")
public class UserMessageAddInput {

    @Schema(description = "消息类型")
    private String messageCat;

    @Schema(description = "相关昵称:发送者或者接收者")
    private String userNickname;

    @Schema(description = "相关用户:发送者或者接收者")
    private Integer userOtherId;

    @Schema(description = "json对象")
    private String to;

    @Schema(description = "消息内容")
    private String messageContent;

    @Schema(description = "消息标题")
    private String messageTitle;

    @Schema(description = "json对象")
    private String mine;

    @Schema(description = "消息长度")
    private Integer messageLength;

    @Schema(description = "图片宽度")
    private Integer messageW;

    @Schema(description = "图片高度")
    private Integer messageH;
}
