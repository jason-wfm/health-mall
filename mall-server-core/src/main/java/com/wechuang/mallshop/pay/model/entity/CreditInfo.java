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
 *
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("pay_credit_info")
@Schema(name = "CreditInfo对象", description = "")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreditInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "账单编号")
    @TableId(value = "credit_id", type = IdType.AUTO)
    private String creditId;

    @Schema(description = "账单开始时间")
    @TableField("credit_start_date")
    private Date creditStartDate;

    @Schema(description = "账单结束时间")
    @TableField("credit_end_date")
    private Date creditEndDate;

    @Schema(description = "账单使用额度")
    @TableField("credit_use_amount")
    private BigDecimal creditUseAmount;

    @Schema(description = "账单剩余待还额度")
    @TableField("credit_surplus_amount")
    private BigDecimal creditSurplusAmount;

    @Schema(description = "账单退货额度")
    @TableField("credit_return_amount")
    private BigDecimal creditReturnAmount;

    @Schema(description = "还款状态：0-待还款；1-还款中；2-已结清；3-已逾期")
    @TableField("credit_state")
    private Integer creditState;

    @Schema(description = "还款日")
    @TableField("credit_repayment_date")
    private Integer creditRepaymentDate;

    @Schema(description = "用户编号:管理员店铺主账号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "添加时间")
    @TableField("credit_add_time")
    private Date creditAddTime;

    @Schema(description = "订单年份-索引查询")
    @TableField("credit_year")
    private Integer creditYear;

    @Schema(description = "订单月份-索引查询")
    @TableField("credit_month")
    private Integer creditMonth;

    @Schema(description = "订单日-索引查询")
    @TableField("credit_day")
    private Integer creditDay;

    @Schema(description = "账单所属月份")
    @TableField("credit_time")
    private String creditTime;

    @Schema(description = "逾期状态：1-未逾期；2-已逾期；")
    @TableField("credit_be_overdue")
    private Integer creditBeOverdue;

    @Schema(description = "支付凭证")
    @TableField("credit_pay_img")
    private String creditPayImg;

    @Schema(description = "支付单号")
    @TableField("credit_pay_num")
    private String creditPayNum;

    @Schema(description = "支付审核状态：0-未审核；1-待审核；2-审核成功；3-审核失败")
    @TableField("credit_pay_verify")
    private Integer creditPayVerify;

    @Schema(description = "用户名称")
    @TableField(exist = false)
    private String userNickname;
}
