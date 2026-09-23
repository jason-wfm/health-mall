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
package com.wechuang.mallshop.pay.model.entity;

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
import java.util.Date;

/**
 * <p>
 * 结算账户表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pay_user_bank_card")
@Schema(name = "UserBankCard对象", description = "结算账户表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserBankCard implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "卡编号")
    @TableId(value = "user_bank_id", type = IdType.AUTO)
    private Integer userBankId;

    @Schema(description = "用户编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "银行编号")
    @TableField("bank_id")
    private Integer bankId;

    @Schema(description = "银行名称")
    @TableField("bank_name")
    private String bankName;

    @Schema(description = "账户类别(ENUM):1001-微信;1002-支付宝;1003-现金;1004-银行")
    @TableField("settlement_account_type_id")
    private Integer settlementAccountTypeId;

    @Schema(description = "卡号账户名称")
    @TableField("user_bank_card_name")
    private String userBankCardName;

    @Schema(description = "银行卡卡号")
    @TableField("user_bank_card_code")
    private String userBankCardCode;

    @Schema(description = "开户支行名称")
    @TableField("user_bank_card_address")
    private String userBankCardAddress;

    @Schema(description = "银行卡添加时间")
    @TableField("user_bank_card_time")
    private Date userBankCardTime;

    @Schema(description = "银行预留手机号")
    @TableField("user_bank_card_mobile")
    private String userBankCardMobile;

    @Schema(description = "开户省份编号")
    @TableField("province_id")
    private String provinceId;

    @Schema(description = "开户城市编号")
    @TableField("city_id")
    private String cityId;

    @Schema(description = "卡的类型编号")
    @TableField("type_id")
    private Integer typeId;

    @Schema(description = "用户当前所选默认提现卡")
    @TableField("user_bank_default")
    private Boolean userBankDefault;

    @Schema(description = "是否可用(BOOL):1-启用;0-禁用")
    @TableField("user_bank_enable")
    private Boolean userBankEnable;

    @Schema(description = "余额日期")
    @TableField("user_bank_begin_date")
    private Integer userBankBeginDate;

    @Schema(description = "账户余额")
    @TableField("user_bank_amount_money")
    private BigDecimal userBankAmountMoney;

    @Schema(description = "国家区号")
    @TableField("user_intl")
    private String userIntl;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
