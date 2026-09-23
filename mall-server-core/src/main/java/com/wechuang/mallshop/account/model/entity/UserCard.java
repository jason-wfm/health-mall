// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.account.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户会员卡
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("account_user_card")
@Schema(name = "UserCard对象", description = "用户会员卡")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "会员卡编号")
    @TableId(value = "card_id", type = IdType.AUTO)
    private Integer cardId;

    @Schema(description = "会员卡名称")
    @TableField("card_name")
    private String cardName;

    @Schema(description = "会员等级")
    @TableField("user_level_id")
    private Integer userLevelId;

    @Schema(description = "等级名称")
    @TableField(exist = false)
    private String userLevelName;  //user_level_name


    @Schema(description = "状态(BOOL):0-禁用;1-开启")
    @TableField("card_enable")
    private Boolean cardEnable;

    @Schema(description = "会费（元/期）")
    @TableField("card_pirce")
    private BigDecimal cardPirce;

    @Schema(description = "原会费（元/期）")
    @TableField("card_market_price")
    private BigDecimal cardMarketPrice;

    @Schema(description = "预计年省金额")
    @TableField("card_save_amount")
    private BigDecimal cardSaveAmount;

    @Schema(description = "有效期年")
    @TableField("card_year")
    private Integer cardYear;

    @Schema(description = "有效期月")
    @TableField("card_month")
    private Integer cardMonth;

    @Schema(description = "有效期日")
    @TableField("card_day")
    private Integer cardDay;

    @Schema(description = "上线时间")
    @TableField("card_time")
    private Long cardTime;

    @Schema(description = "排序")
    @TableField("card_sort")
    private Integer cardSort;

    @Schema(description = "会员类型(ENUM):1-普通用户;2-扩展用户")
    @TableField("user_type_id")
    private Integer userTypeId;

    @Schema(description = "物流模板编号")
    @TableField("transport_type_id")
    private Integer transportTypeId;

    @Schema(description = "唯一地址(BOOL):0-否;1-是")
    @TableField("card_bind_addr")
    private Boolean cardBindAddr;

    @Schema(description = "体验卡(BOOL):0-否;1-是")
    @TableField("card_is_exp")
    private Boolean cardIsExp;

    @Schema(description = "体验月")
    @TableField("card_exp_month")
    private Integer cardExpMonth;

    @Schema(description = "体验日")
    @TableField("card_exp_day")
    private Integer cardExpDay;

    @Schema(description = "体验费用")
    @TableField("card_exp_fee")
    private BigDecimal cardExpFee;

    @Schema(description = "体验状态(BOOL):0-禁用;1-开启")
    @TableField("card_exp_status")
    private Boolean cardExpStatus;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "活动会员专享折扣编号")
    @TableField("activity_id_discount")
    private Integer activityIdDiscount;

    @Schema(description = "活动会员开卡赠品编号")
    @TableField("activity_id_gitfbag")
    private Integer activityIdGitfbag;

    @Schema(description = "活动会员日（实物）编号")
    @TableField("activity_id_member_day")
    private Integer activityIdMemberDay;

    @Schema(description = "活动生日专享编号")
    @TableField("activity_id_birthday")
    private Integer activityIdBirthday;

    @Schema(description = "允许分销(BOOL):1-启用分销;0-禁用分销")
    @TableField("user_fx_enable")
    private Boolean userFxEnable;

    @Schema(description = "分销佣金比例")
    @TableField("user_fx_rate")
    private BigDecimal userFxRate;

    @Schema(description = "分享标题")
    @TableField("card_title")
    private String cardTitle;

    @Schema(description = "分享描述")
    @TableField("card_desc")
    private String cardDesc;

    @Schema(description = "搜索关键字")
    @TableField("card_keywords")
    private String cardKeywords;

    @Schema(description = "分享图标")
    @TableField("card_img")
    private String cardImg;

    @Schema(description = "开通推广员(BOOL):1-开通;0-不开通")
    @TableField("card_fx_active")
    private Boolean cardFxActive;

    @Schema(description = "背景图片")
    @TableField("card_img_bg")
    private String cardImgBg;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
