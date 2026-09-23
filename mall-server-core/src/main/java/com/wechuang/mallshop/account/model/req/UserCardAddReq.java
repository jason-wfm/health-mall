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
package com.wechuang.mallshop.account.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户会员卡参数")
public class UserCardAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "会员卡名称")
    private String cardName;

    @Schema(description = "会员等级")
    private Integer userLevelId;

    @Schema(description = "状态(BOOL):0-禁用;1-开启")
    private Boolean cardEnable;

    @Schema(description = "会费（元/期）")
    private BigDecimal cardPirce;

    @Schema(description = "原会费（元/期）")
    private BigDecimal cardMarketPrice;

    @Schema(description = "预计年省金额")
    private BigDecimal cardSaveAmount;

    @Schema(description = "有效期年")
    private Integer cardYear;

    @Schema(description = "有效期月")
    private Integer cardMonth;

    @Schema(description = "有效期日")
    private Integer cardDay;

    @Schema(description = "上线时间")
    private Long cardTime;

    @Schema(description = "排序")
    private Integer cardSort;

    @Schema(description = "会员类型(ENUM):1-普通用户;2-扩展用户")
    private Integer userTypeId;

    @Schema(description = "物流模板编号")
    private Integer transportTypeId;

    @Schema(description = "唯一地址(BOOL):0-否;1-是")
    private Boolean cardBindAddr;

    @Schema(description = "体验卡(BOOL):0-否;1-是")
    private Boolean cardIsExp;

    @Schema(description = "体验月")
    private Integer cardExpMonth;

    @Schema(description = "体验日")
    private Integer cardExpDay;

    @Schema(description = "体验费用")
    private BigDecimal cardExpFee;

    @Schema(description = "体验状态(BOOL):0-禁用;1-开启")
    private Boolean cardExpStatus;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "活动会员专享折扣编号")
    private Integer activityIdDiscount;

    @Schema(description = "活动会员开卡赠品编号")
    private Integer activityIdGitfbag;

    @Schema(description = "活动会员日（实物）编号")
    private Integer activityIdMemberDay;

    @Schema(description = "活动生日专享编号")
    private Integer activityIdBirthday;

    @Schema(description = "允许分销(BOOL):1-启用分销;0-禁用分销")
    private Boolean userFxEnable;

    @Schema(description = "分销佣金比例")
    private BigDecimal userFxRate;

    @Schema(description = "分享标题")
    private String cardTitle;

    @Schema(description = "分享描述")
    private String cardDesc;

    @Schema(description = "搜索关键字")
    private String cardKeywords;

    @Schema(description = "分享图标")
    private String cardImg;

    @Schema(description = "开通推广员(BOOL):1-开通;0-不开通")
    private Boolean cardFxActive;

    @Schema(description = "背景图片")
    private String cardImgBg;


}
