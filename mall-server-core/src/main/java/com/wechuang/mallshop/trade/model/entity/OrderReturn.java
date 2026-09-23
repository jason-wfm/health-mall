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
package com.wechuang.mallshop.trade.model.entity;

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
 * 退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("trade_order_return")
@Schema(name = "OrderReturn对象", description = "退款退货表-发货退货,卖家也可以决定不退货退款，买家申请退款不支持。卖家可以主动退款。")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderReturn implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "退单号")
    @TableId(value = "return_id", type = IdType.AUTO)
    private String returnId;

    @Schema(description = "服务类型(ENUM):1-退款;2-退货;3-换货;4-维修")
    @TableField("service_type_id")
    private Integer serviceTypeId;

    @Schema(description = "订单编号")
    @TableField("order_id")
    private String orderId;

    @Schema(description = "退款金额")
    @TableField("return_refund_amount")
    private BigDecimal returnRefundAmount;

    @Schema(description = "积分部分")
    @TableField("return_refund_point")
    private BigDecimal returnRefundPoint;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "门店编号")
    @TableField("chain_id")
    private Integer chainId;

    @Schema(description = "买家编号")
    @TableField("buyer_user_id")
    private Integer buyerUserId;

    @Schema(description = "买家是否有店铺")
    @TableField("buyer_store_id")
    private Integer buyerStoreId;

    @Schema(description = "添加时间")
    @TableField("return_add_time")
    private Long returnAddTime;

    @Schema(description = "退款理由编号")
    @TableField("return_reason_id")
    private Integer returnReasonId;

    @Schema(description = "买家退货手机号")
    @TableField("return_buyer_mobile")
    private String returnBuyerMobile;

    @Schema(description = "买家退货备注")
    @TableField("return_buyer_message")
    private String returnBuyerMessage;

    @Schema(description = "收货人")
    @TableField("return_addr_contacter")
    private String returnAddrContacter;

    @Schema(description = "联系电话")
    @TableField("return_tel")
    private String returnTel;

    @Schema(description = "收货地址详情")
    @TableField("return_addr")
    private String returnAddr;

    @Schema(description = "邮编")
    @TableField("return_post_code")
    private Integer returnPostCode;

    @Schema(description = "物流公司编号")
    @TableField("express_id")
    private Integer expressId;

    @Schema(description = "物流名称")
    @TableField("return_tracking_name")
    private String returnTrackingName;

    @Schema(description = "物流单号")
    @TableField("return_tracking_number")
    private String returnTrackingNumber;

    @Schema(description = "申请状态平台(ENUM):3180-未申请;3181-待处理;3182-为已完成")
    @TableField("plantform_return_state_id")
    private Integer plantformReturnStateId;

    @Schema(description = "卖家处理状态(ENUM): 3100-【客户】提交退单;3105-退单审核;3110-收货确认;3115-退款确认;3120-客户】收款确认;3125-完成")
    @TableField("return_state_id")
    private Integer returnStateId;

    @Schema(description = "退款完成")
    @TableField("return_is_paid")
    private Boolean returnIsPaid;

    @Schema(description = "退货类型(BOOL): 0-退款单;1-退运费单")
    @TableField("return_is_shipping_fee")
    private Integer returnIsShippingFee;

    @Schema(description = "退运费额度")
    @TableField("return_shipping_fee")
    private BigDecimal returnShippingFee;

    @Schema(description = "退货类型(ENUM): 0-不用退货;1-需要退货")
    @TableField("return_flag")
    private Integer returnFlag;

    @Schema(description = "申请类型(ENUM): 1-退款申请; 2-退货申请; 3-虚拟退款  ")
    @TableField("return_type")
    private Integer returnType;

    @Schema(description = "订单锁定类型(BOOL):1-不用锁定;2-需要锁定")
    @TableField("return_order_lock")
    private Integer returnOrderLock;

    @Schema(description = "物流状态(LIST):2030-待发货;2040-已发货/待收货确认;2060-已完成/已签收;2070-已取消/已作废;")
    @TableField("return_item_state_id")
    private Integer returnItemStateId;

    @Schema(description = "商家处理时间")
    @TableField("return_store_time")
    private Date returnStoreTime;

    @Schema(description = "商家备注")
    @TableField("return_store_message")
    private String returnStoreMessage;

    @Schema(description = "退还佣金")
    @TableField("return_commision_fee")
    private BigDecimal returnCommisionFee;

    @Schema(description = "退款完成时间")
    @TableField("return_finish_time")
    private Date returnFinishTime;

    @Schema(description = "平台留言")
    @TableField("return_platform_message")
    private String returnPlatformMessage;

    @Schema(description = "订单是否结算(BOOL): 0-未结算; 1-已结算")
    @TableField("return_is_settlemented")
    private Integer returnIsSettlemented;

    @Schema(description = "订单结算时间")
    @TableField("return_settlement_time")
    private Date returnSettlementTime;

    @Schema(description = "退款渠道(ENUM):money-余额;alipay-支付宝;wx_native-微信")
    @TableField("return_channel_code")
    private String returnChannelCode;

    @Schema(description = "渠道是否退款(ENUM): 0-待退; 1-已退; 2-异常")
    @TableField("return_channel_flag")
    private Integer returnChannelFlag;

    @Schema(description = "渠道退款时间")
    @TableField("return_channel_time")
    private Date returnChannelTime;

    @Schema(description = "渠道退款单号")
    @TableField("return_channel_trans_id")
    private String returnChannelTransId;

    @Schema(description = "交易号")
    @TableField("deposit_trade_no")
    private String depositTradeNo;

    @Schema(description = "支付渠道")
    @TableField("payment_channel_id")
    private Integer paymentChannelId;

    @Schema(description = "实付金额:在线支付金额")
    @TableField("trade_payment_amount")
    private BigDecimal tradePaymentAmount;

    @Schema(description = "联系人")
    @TableField("return_contact_name")
    private String returnContactName;

    @Schema(description = "审核人员id")
    @TableField("return_store_user_id")
    private Integer returnStoreUserId;

    @Schema(description = "提现审核(BOOL):0-未审核; 1-已审核")
    @TableField("return_withdraw_confirm")
    private Boolean returnWithdrawConfirm;

    @Schema(description = "退款财务确认(BOOL):0-未确认; 1-已确认")
    @TableField("return_financial_confirm")
    private Boolean returnFinancialConfirm;

    @Schema(description = "退款财务确认时间")
    @TableField("return_financial_confirm_time")
    private Date returnFinancialConfirmTime;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "取消退货备注")
    @TableField("return_purchase_remark")
    private String returnPurchaseRemark;

}
