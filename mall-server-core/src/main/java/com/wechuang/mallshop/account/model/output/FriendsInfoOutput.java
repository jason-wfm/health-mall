package com.wechuang.mallshop.account.model.output;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.entity.UserGroup;
import com.wechuang.mallshop.account.model.entity.UserZone;
import com.wechuang.mallshop.account.model.vo.UserInfoVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Data
@Schema(name = "初始化用户对象", description = "初始化用户对象")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendsInfoOutput {
    List<UserGroup> friend;
    List<UserZone> group;
    UserInfoVo mine;
}

