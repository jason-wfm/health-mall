package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wechuang.mallshop.core.annotation.QueryField;
import com.wechuang.mallshop.core.annotation.QueryType;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * <p>
 * 用户详细信息表
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户详细信息表分页查询")
public class UserInfoListReq extends BaseListReq {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户账号")
    @QueryField(type = QueryType.LIKE)
    private String userAccount;

    @Schema(description = "用户昵称")
    @QueryField(type = QueryType.LIKE)
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "状态(ENUM):0-锁定;1-已激活;2-未激活;")
    private Integer userState;

    @Schema(description = "性别(ENUM):0-保密;1-男;  2-女;")
    private Integer userGender;

    @Schema(description = "生日(DATE)")
    private LocalDate userBirthday;

    @Schema(description = "国家编码")
    private String userIntl;

    @Schema(description = "手机号码(mobile)")
    private String userMobile;

    @Schema(description = "用户邮箱(email)")
    private String userEmail;

    @Schema(description = "用户等级")
    private Integer userLevelId;

    @Schema(description = "用户标签")
    @QueryField(type = QueryType.FIND_IN_SET_STR)
    private String tagIds;

    @Schema(description = "认证状态(ENUM):0-未认证;1-待审核;2-认证通过;3-认证失败")
    @QueryField(type = QueryType.EQ)
    private Integer userIsAuthentication;

    @Schema(description = "是否销售员(BOOL):0-不是;1-是")
    private Boolean userIsSale;

    @Schema(description = "销售员id")
    private Integer userSaleId;

    @Schema(description = "是否PLUS会员(BOOL):0-不是;1-是")
    private Boolean userIsPlus;

    public UserInfoListReq() {
        setSidx("user_id");
        setSort(Constants.ORDER_BY_DESC);
    }
}
