package com.wechuang.mallshop.account.model.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "后台通知对象")
public class MessageNoticeRes {

    @Schema(description = "消息数量")
    private Integer total;

    @Schema(description = "站内信数量")
    private List<UserMessageRes> items;

}
