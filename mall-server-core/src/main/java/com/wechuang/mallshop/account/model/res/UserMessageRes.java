package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.entity.UserMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "站内信")
public class UserMessageRes extends UserMessage {

    @Schema(description = "站内信数量")
    private Integer num;

    @Schema(description = "聊天网址")
    private UserMessage msgRow;

    @Schema(description = "消息编号")
    private Integer messageOtherId;

    @Schema(description = "站内信userInfo")
    private MessageUserInfoRes userinfo;

    @Schema(description = "站内信content")
    private MessageContentRes content;

    @Schema(description = "已读信息数")
    private Integer redNumber = 0;

    @Schema(description = "未读信息数")
    private Integer unreadNumber = 0;

    @Schema(description = "type")
    private String type;

}
