package com.wechuang.mallshop.account.model.output;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.account.model.vo.UserSaleVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class UserInfoOutput implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    @Schema(description = "用户账号")
    @TableField("user_account")
    private String userAccount;

    @Schema(description = "用户昵称")
    @TableField("user_nickname")
    private String userNickname;

    @Schema(description = "用户头像")
    @TableField("user_avatar")
    private String userAvatar;

    @Schema(description = "手机号码(mobile)")
    @TableField("user_mobile")
    private String userMobile;

    @Schema(description = "国家编码")
    @TableField("user_intl")
    private String userIntl;

    @Schema(description = "性别(ENUM):0-保密;1-男;  2-女;")
    @TableField("user_gender")
    private Integer userGender;

    @Schema(description = "生日(DATE)")
    @TableField("user_birthday")
    private Date userBirthday;

    @Schema(description = "用户邮箱(email)")
    @TableField("user_email")
    private String userEmail;

    @Schema(description = "微信二维码")
    @TableField("user_wx_card")
    private String userWxCard;

    @Schema(description = "用户等级")
    @TableField("user_level_id")
    private Integer userLevelId;

    @Schema(description = "真实姓名")
    @TableField("user_realname")
    private String userRealname;

    @Schema(description = "身份证")
    @TableField("user_idcard")
    private String userIdcard;

    @Schema(description = "身份证图片(DOT)")
    @TableField("user_idcard_images")
    private String userIdcardImages;

    @Schema(description = "身份证图片(DOT)")
    @TableField(exist = false)
    private List<String> userIdcardImageList = new ArrayList<>();

    @Schema(description = "认证状态(ENUM):0-未认证;1-待审核;2-认证通过;3-认证失败")
    @TableField("user_is_authentication")
    private Integer userIsAuthentication;

    @Schema(description = "状态(ENUM):0-锁定;1-已激活;2-未激活;")
    @TableField("user_state")
    private Integer userState;

    @Schema(description = "是否销售员(BOOL):0-不是;1-是")
    @TableField("user_is_sale")
    private Boolean userIsSale;

    //用户资源
    @Schema(description = "货币编号")
    @TableField("currency_id")
    private Integer currencyId;

    @Schema(description = "左符号")
    @TableField("currency_symbol_left")
    private String currencySymbolLeft;

    @Schema(description = "用户资金")
    @TableField("user_money")
    private BigDecimal userMoney;

    @Schema(description = "冻结资金:待结算余额")
    @TableField("user_money_frozen")
    private BigDecimal userMoneyFrozen;

    @Schema(description = "充值卡余额")
    @TableField("user_recharge_card")
    private BigDecimal userRechargeCard;

    @Schema(description = "冻结充值卡:待结算")
    @TableField("user_recharge_card_frozen")
    private BigDecimal userRechargeCardFrozen;

    @Schema(description = "积分")
    @TableField("user_points")
    private BigDecimal userPoints;

    @Schema(description = "冻结积分")
    @TableField("user_points_frozen")
    private BigDecimal userPointsFrozen;

    @Schema(description = "经验值")
    @TableField("user_exp")
    private Long userExp;

    @Schema(description = "可用信用")
    @TableField("user_credit")
    private BigDecimal userCredit;

    @Schema(description = "冻结额度")
    @TableField("user_credit_frozen")
    private BigDecimal userCreditFrozen;

    @Schema(description = "使用信用")
    @TableField("user_credit_used")
    private BigDecimal userCreditUsed;

    @Schema(description = "信用额度")
    @TableField("user_credit_total")
    private BigDecimal userCreditTotal;

    @Schema(description = "保证金")
    @TableField("user_margin")
    private BigDecimal userMargin;

    @Schema(description = "红包额度")
    @TableField("user_redpack")
    private BigDecimal userRedpack;

    @Schema(description = "红包冻结额度")
    @TableField("user_redpack_frozen")
    private BigDecimal userRedpackFrozen;


    //其它信息
    @Schema(description = "角色列表")
    @TableField(exist = false)
    private List<String> roles;

    @Schema(description = "权限列表")
    @TableField(exist = false)
    private List<String> permissions;

    @Schema(description = "角色编号:0-用户;2-商家;3-门店;8-租户;9-平台;")
    @TableField(exist = false)
    private Integer roleId = 0;

    @Schema(description = "分站编号:0-总站")
    @TableField(exist = false)
    private Integer siteId = 0;

    @Schema(description = "信用支付状态")
    @TableField(exist = false)
    private Integer userCreditStatus = 0;

    @Schema(description = "店铺编号")
    @TableField(exist = false)
    private Integer storeId = 0;

    @Schema(description = "门店编号")
    @TableField(exist = false)
    private Integer chainId = 0;

    @Schema(description = "后台管理:admin=1;移动端front=0")
    @TableField(exist = false)
    private Integer clientId = 0;

    @Schema(description = "详细地址")
    @TableField(exist = false)
    private String udAddress;

    @Schema(description = "等级名称")
    @TableField(exist = false)
    private String userLevelName;

    @Schema(description = "注册时间")
    private Long userRegTime;

    @Schema(description = "登录时间")
    @TableField(exist = false)
    private Long userLoginTime;

    @Schema(description = "标签标题(DOT)")
    @TableField(exist = false)
    private String tagTitles;

    @Schema(description = "标签标题(DOT)")
    @TableField(exist = false)
    private List<String> tagTitleList;

    @Schema(description = "分组名称(DOT)")
    @TableField(exist = false)
    private String tagGroupNames;

    @Schema(description = "用户标签(DOT)")
    @TableField(exist = false)
    private String tagIds;

    @Schema(description = "本月订单")
    @TableField(exist = false)
    private BigDecimal monthOrder;

    @Schema(description = "总计订单")
    @TableField(exist = false)
    private BigDecimal totalOrder;

    @Schema(description = "本月消费金额")
    @TableField(exist = false)
    private BigDecimal monthTrade;

    @Schema(description = "总消费金额")
    @TableField(exist = false)
    private BigDecimal totalTrade;

    @Schema(description = "优惠券数量")
    @TableField(exist = false)
    private Long voucher;

    @Schema(description = "待付款数量")
    @TableField(exist = false)
    private Long waitPayNum = 0L;

    @Schema(description = "收藏数量")
    @TableField(exist = false)
    private Long favoritesGoodsNum = 0L;

    @Schema(description = "关注数量")
    @TableField(exist = false)
    private Long concernNum = 0L;

    @Schema(description = "未读消息数量")
    @TableField(exist = false)
    private Integer unreadNumber = 0;

    @Schema(description = "佣金总额:历史总额度")
    @TableField(exist = false)
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Schema(description = "累计佣金")
    @TableField(exist = false)
    private BigDecimal userCommissionNow = BigDecimal.ZERO;

    @Schema(description = "本月预估收益")
    @TableField(exist = false)
    private BigDecimal monthCommissionBuy = BigDecimal.ZERO;

    @Schema(description = "上级用户编号")
    @TableField(exist = false)
    private Integer userParentId;

    @Schema(description = "IM Config")
    @TableField(exist = false)
    private ImConfigOutput im;

    @Schema(description = "销售员信息")
    @TableField(exist = false)
    private UserSaleVo userSale;

    @Schema(description = "用户默认收货地址")
    @TableField(exist = false)
    private List<Integer> districtList;

    @Schema(description = "PLUS状态")
    @TableField(exist = false)
    private boolean userPlusStatus = false;

    @Schema(description = "是否PLUS会员(BOOL):0-不是;1-是")
    @TableField("user_is_plus")
    private Boolean userIsPlus;

    @Schema(description = "PLUS状态")
    @TableField(exist = false)
    private Integer plusStatus = 0;

    @Schema(description = "PLUS会员开始时间")
    @TableField(exist = false)
    private Date plusStartTime;

    @Schema(description = "PLUS会员到期时间")
    @TableField(exist = false)
    private Date plusEndTime;

}
