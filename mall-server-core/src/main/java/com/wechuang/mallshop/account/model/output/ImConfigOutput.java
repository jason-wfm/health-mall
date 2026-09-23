package com.wechuang.mallshop.account.model.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.entity.UserGroup;
import com.wechuang.mallshop.account.model.entity.UserZone;
import com.wechuang.mallshop.account.model.vo.UserInfoVo;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(name = "")
public class ImConfigOutput implements Serializable {

    private Boolean imChat = true;
    private String nodeSiteUrl;
    private Integer suid;
    private String resourceSiteUrl = "https://test.shopsuite.cn/account/static/src/common";
    private UserInfoVo userInfo;
    private Integer puid;
    private UserInfoVo userOtherInfo;
    private ProductItem chatItemRow;

    List<UserGroup> friend;
    List<UserZone> group;
    UserInfoVo mine;
}
