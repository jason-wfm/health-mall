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
package com.wechuang.mallshop.pay.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 交易订单表-强调唯一订单-充值则先创建充值订单
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "交易订单表-强调唯一订单-充值则先创建充值订单参数")
public class ConsumeTradeAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "标题")
    private String tradeTitle;

    @Schema(description = "商户订单编号")
    private String orderId;

    @Schema(description = "买家编号")
    private Integer buyerId;

    @Schema(description = "买家是否有店铺")
    private Integer buyerStoreId;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "所属分站:0-总站")
    private Integer subsiteId;

    @Schema(description = "卖家编号")
    private Integer sellerId;

    @Schema(description = "门店编号")
    private Integer chainId;

    @Schema(description = "支付状态")
    private Integer tradeIsPaid;

    @Schema(description = "交易类型(ENUM):1201-购物; 1202-转账; 1203-充值; 1204-提现; 1205-销售; 1206-佣金;")
    private Integer tradeTypeId;

    @Schema(description = "支付渠道")
    private Integer paymentChannelId;

    @Schema(description = "交易模式(ENUM):1-担保交易;  2-直接交易")
    private Integer tradeModeId;

    @Schema(description = "充值编号")
    private Integer rechargeLevelId;

    @Schema(description = "货币编号")
    private Integer currencyId;

    @Schema(description = "左符号")
    private String currencySymbolLeft;

    @Schema(description = "总付款额度: trade_payment_amount + trade_payment_money + trade_payment_recharge_card + trade_payment_points")
    private BigDecimal orderPaymentAmount;

    @Schema(description = "平台交易佣金")
    private BigDecimal orderCommissionFee;

    @Schema(description = "实付金额:在线支付金额,此为订单默认需要支付额度。")
    private BigDecimal tradePaymentAmount;

    @Schema(description = "余额支付")
    private BigDecimal tradePaymentMoney;

    @Schema(description = "充值卡余额支付")
    private BigDecimal tradePaymentRechargeCard;

    @Schema(description = "积分支付")
    private BigDecimal tradePaymentPoints;

    @Schema(description = "众宝支付")
    private BigDecimal tradePaymentSp;

    @Schema(description = "信用支付")
    private BigDecimal tradePaymentCredit;

    @Schema(description = "红包支付")
    private BigDecimal tradePaymentRedpack;

    @Schema(description = "折扣优惠")
    private BigDecimal tradeDiscount;

    @Schema(description = "总额虚拟:trade_order_amount + trade_discount")
    private BigDecimal tradeAmount;

    @Schema(description = "描述")
    private String tradeDesc;

    @Schema(description = "备注")
    private String tradeRemark;

    @Schema(description = "创建时间")
    private Long tradeCreateTime;

    @Schema(description = "付款时间")
    private Date TradePaidTime;

    @Schema(description = "是否删除")
    private Boolean tradeDelete;


}
