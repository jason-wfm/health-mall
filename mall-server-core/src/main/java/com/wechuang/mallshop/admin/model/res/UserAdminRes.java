package com.wechuang.mallshop.admin.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "UserAdmin对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserAdminRes extends UserAdmin {

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户昵称")
    private String userNickname;
}
