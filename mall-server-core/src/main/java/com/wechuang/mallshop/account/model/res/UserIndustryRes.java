package com.wechuang.mallshop.account.model.res;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.account.model.entity.UserIndustry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "文章前端对象")
public class UserIndustryRes extends UserIndustry {

    @Schema(description = "企业子集")
    private List<UserIndustryRes> children;
}
