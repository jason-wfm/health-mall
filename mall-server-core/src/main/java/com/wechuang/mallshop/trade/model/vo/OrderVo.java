package com.wechuang.mallshop.trade.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.invoicing.model.entity.WarehouseItem;
import com.wechuang.mallshop.invoicing.model.vo.StockBillVo;
import com.wechuang.mallshop.pay.model.entity.ConsumeDeposit;
import com.wechuang.mallshop.pay.model.entity.ConsumeRecord;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.trade.model.entity.OrderDeliveryAddress;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import com.wechuang.mallshop.trade.model.entity.OrderLogistics;
import com.wechuang.mallshop.trade.model.entity.OrderStateLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderVo implements Serializable {

    //order_info
    @Schema(description = "订单编号")
    @TableId(value = "order_id", type = IdType.AUTO)
    private String orderId;

    @Schema(description = "订单标题")
    @TableField("order_title")
    private String orderTitle;

    @Schema(description = "卖家店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "所属分站:0-总站")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "买家编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "订单销售员")
    @TableField("order_sale_id")
    private Integer orderSaleId;

    @Schema(description = "订单种类(ENUM): 1201-实物 ; 1202-教育类 ; 1203-电子卡券  ; 1204-其它")
    @TableField("kind_id")
    private Integer kindId;

    @Schema(description = "锁定状态(BOOL):0-正常;1-锁定,退款退货")
    @TableField("order_lock_status")
    private Boolean orderLockStatus;

    @Schema(description = "订单是否结算(BOOL):0-未结算; 1-已结算")
    @TableField("order_is_settlemented")
    private Integer orderIsSettlemented;

    @Schema(description = "订单结算时间")
    @TableField("order_settlement_time")
    private Date orderSettlementTime;

    @Schema(description = "买家针对订单对店铺评价(ENUM): 0-未评价;1-已评价;  2-已过期未评价")
    @TableField("order_buyer_evaluation_status")
    private Integer orderBuyerEvaluationStatus;

    @Schema(description = "卖家评价状态(ENUM):0-未评价;1-已评价;  2-已过期未评价")
    @TableField("order_seller_evaluation_status")
    private Boolean orderSellerEvaluationStatus;

    @Schema(description = "买家删除(BOOL): 1-是; 0-否;")
    @TableField("order_buyer_hidden")
    private Boolean orderBuyerHidden;

    @Schema(description = "店铺删除(BOOL): 1-是; 0-否;")
    @TableField("order_shop_hidden")
    private Boolean orderShopHidden;

    @Schema(description = "支付方式(ENUM): 1301-货到付款; 1302-在线支付; 1303-白条支付; 1304-现金支付; 1305-线下支付; ")
    @TableField("payment_type_id")
    private Integer paymentTypeId;

    @Schema(description = "付款时间")
    @TableField("payment_time")
    private Date paymentTime;

    @Schema(description = "订单状态(LIST):2011-待订单审核;2013-待财务审核;2020-待配货/待出库审核;2030-待发货;2040-已发货/待收货确认;2060-已完成/已签收;2070-已取消/已作废;")
    @TableField("order_state_id")
    private Integer orderStateId;

    @Schema(description = "订单审核(ENUM):0-未审核;1-已审核;2-审核驳回;")
    @TableField("order_is_review")
    private Integer orderIsReview;

    @Schema(description = "财务状态(BOOL):0-未审核;1-已审核")
    @TableField("order_finance_review")
    private Boolean orderFinanceReview;

    @Schema(description = "付款状态(ENUM):3010-未付款;3011-付款待审核;3012-部分付款;3013-已付款")
    @TableField("order_is_paid")
    private Integer orderIsPaid;

    @Schema(description = "出库状态(ENUM):3020-未出库;3021-部分出库通过拆单解决这种问题;3022-已出库")
    @TableField("order_is_out")
    private Integer orderIsOut;

    @Schema(description = "发货状态(ENUM):3030-未发货;3032-已发货;3031-部分发货")
    @TableField("order_is_shipped")
    private Integer orderIsShipped;

    @Schema(description = "收货状态(BOOL):0-未收货;1-已收货;")
    @TableField("order_is_received")
    private Boolean orderIsReceived;

    @Schema(description = "订单签收时间")
    @TableField("order_received_time")
    private Date orderReceivedTime;

    @Schema(description = "门店编号")
    @TableField("chain_id")
    private Integer chainId;

    @Schema(description = "配送方式")
    @TableField("delivery_type_id")
    private Integer deliveryTypeId;

    @Schema(description = "线下订单(BOOL):0-线上;1-线下")
    @TableField("order_is_offline")
    private Boolean orderIsOffline;

    @Schema(description = "是否打印(BOOL):0-未打印;1-已打印")
    @TableField("order_express_print")
    private Boolean orderExpressPrint;

    @Schema(description = "活动编号(DOT)")
    @TableField("activity_id")
    private String activityId;

    @Schema(description = "活动类型(DOT)")
    @TableField("activity_type_id")
    private String activityTypeId;

    @Schema(description = "销售员编号:用户编号")
    @TableField("salesperson_id")
    private Integer salespersonId;

    @Schema(description = "是否ERP同步(BOOL):0-未同步;1-已同步;2-同步失败;3-无需同步")
    @TableField("order_is_sync")
    private Integer orderIsSync;

    @Schema(description = "是否自营(ENUM): 1-自营;0-非自营")
    @TableField("store_is_selfsupport")
    private Boolean storeIsSelfsupport;

    @Schema(description = "店铺类型(ENUM): 1-卖家店铺; 2-供应商店铺")
    @TableField("store_type")
    private Integer storeType;

    @Schema(description = "ERP订单编号")
    @TableField("order_erp_id")
    private String orderErpId;

    @Schema(description = "分销商编号:用户编号")
    @TableField("distributor_user_id")
    private Integer distributorUserId;

    @Schema(description = "跨境订单(BOOL):0-否; 1-是")
    @TableField("order_is_cb")
    private Boolean orderIsCb;

    @Schema(description = "是否报关(BOOL):0-否; 1-是")
    @TableField("order_is_cb_sync")
    private Boolean orderIsCbSync;

    @Schema(description = "来源订单")
    @TableField("src_order_id")
    private String srcOrderId;

    @Schema(description = "是否代发(BOOL):0-否; 1-是")
    @TableField("order_is_transfer")
    private Boolean orderIsTransfer;

    @Schema(description = "转单执行结果")
    @TableField("order_is_transfer_note")
    private String orderIsTransferNote;

    @Schema(description = "佣金是否发放(ENUM):0 -未发放;1 -已发放; 2-待发放; 3-停止发放")
    @TableField("order_fx_is_settlemented")
    private Integer orderFxIsSettlemented;

    @Schema(description = "佣金结算时间")
    @TableField("order_fx_settlement_time")
    private Date orderFxSettlementTime;

    @Schema(description = "订单类型(ENUM)")
    @TableField("order_type")
    private Integer orderType;

    @Schema(description = "提现审核(BOOL):0-未审核; 1-已审核")
    @TableField("order_withdraw_confirm")
    private Boolean orderWithdrawConfirm;

    @Schema(description = "支付方式(BOOL):1-先预约后支付;0-先支付后预约")
    @TableField("payment_form_id")
    private Boolean paymentFormId;

    @Schema(description = "购买类型(ENUM):1-购买; 2-积分兑换; 3-赠品; 4-活动促销")
    @TableField("cart_type_id")
    private Boolean cartTypeId;

    @Schema(description = "商品绑定卡片类型(ENUM): 1001-次卡商品; 1002-优惠券商品;1003-券码商品;")
    @TableField("card_kind_id")
    private Integer cardKindId;

    //order_data

    @Schema(description = "订单描述")
    @TableField("order_desc")
    private String orderDesc;

    @Schema(description = "延迟时间,默认为0 - 收货确认")
    @TableField("order_delay_time")
    private Integer orderDelayTime;

    @Schema(description = "配送时间:要求，不限、周一~周五、周末等等")
    @TableField("delivery_time_id")
    private Integer deliveryTimeId;

    @Schema(description = "配送日期")
    @TableField("delivery_time")
    private Long deliveryTime;

    @Schema(description = "是否定时配送(BOOL):0-不定时;1-定时")
    @TableField("delivery_istimer")
    private Boolean deliveryIstimer;

    @Schema(description = "买家订单留言")
    @TableField("order_message")
    private String orderMessage;

    @Schema(description = "商品总价格/商品金额, 不包含运费")
    @TableField("order_item_amount")
    private BigDecimal orderItemAmount;

    @Schema(description = "折扣价格/优惠总金额")
    @TableField("order_discount_amount")
    private BigDecimal orderDiscountAmount;

    @Schema(description = "手工调整费用店铺优惠")
    @TableField("order_adjust_fee")
    private BigDecimal orderAdjustFee;

    @Schema(description = "积分抵扣费用")
    @TableField("order_points_fee")
    private BigDecimal orderPointsFee;

    @Schema(description = "销售员折扣额度")
    @TableField("order_sale_person_discount")
    private BigDecimal orderSalePersonDiscount;

    @Schema(description = "运费价格/运费金额")
    @TableField("order_shipping_fee_amount")
    private BigDecimal orderShippingFeeAmount;

    @Schema(description = "实际运费金额-卖家可修改")
    @TableField("order_shipping_fee")
    private BigDecimal orderShippingFee;

    @Schema(description = "代金券id/优惠券/返现:发放选择使用")
    @TableField("voucher_id")
    private Integer voucherId;

    @Schema(description = "代金券编码")
    @TableField("voucher_number")
    private String voucherNumber;

    @Schema(description = "代金券面额")
    @TableField("voucher_price")
    private BigDecimal voucherPrice;

    @Schema(description = "红包id-平台代金券")
    @TableField("redpacket_id")
    private Integer redpacketId;

    @Schema(description = "红包编码")
    @TableField("redpacket_number")
    private String redpacketNumber;

    @Schema(description = "红包面额")
    @TableField("redpacket_price")
    private BigDecimal redpacketPrice;

    @Schema(description = "红包抵扣订单金额")
    @TableField("order_redpacket_price")
    private BigDecimal orderRedpacketPrice;

    @Schema(description = "第二需要支付资源例如积分")
    @TableField("order_resource_ext1")
    private BigDecimal orderResourceExt1;

    @Schema(description = "众宝")
    @TableField("order_resource_ext2")
    private BigDecimal orderResourceExt2;

    @Schema(description = "金宝")
    @TableField("order_resource_ext3")
    private BigDecimal orderResourceExt3;

    @Schema(description = "余额支付")
    @TableField("trade_payment_money")
    private BigDecimal tradePaymentMoney;

    @Schema(description = "充值卡支付")
    @TableField("trade_payment_recharge_card")
    private BigDecimal tradePaymentRechargeCard;

    @Schema(description = "信用支付")
    @TableField("trade_payment_credit")
    private BigDecimal tradePaymentCredit;

    @Schema(description = "退款状态:0-是无退款;1-是部分退款;2-是全部退款")
    @TableField("order_refund_status")
    private Integer orderRefundStatus;

    @Schema(description = "退款金额:申请额度")
    @TableField("order_refund_amount")
    private BigDecimal orderRefundAmount;

    @Schema(description = "退款金额:同意额度")
    @TableField("order_refund_agree_amount")
    private BigDecimal orderRefundAgreeAmount;

    @Schema(description = "已同意退的现金")
    @TableField("order_refund_agree_cash")
    private BigDecimal orderRefundAgreeCash;

    @Schema(description = "已退的积分额度")
    @TableField("order_refund_agree_points")
    private BigDecimal orderRefundAgreePoints;

    @Schema(description = "退货状态(ENUM):0-是无退货;1-是部分退货;2-是全部退货")
    @TableField("order_return_status")
    private Integer orderReturnStatus;

    @Schema(description = "退货数量")
    @TableField("order_return_num")
    private Integer orderReturnNum;

    @Schema(description = "退货单编号s(DOT):冗余")
    @TableField("order_return_ids")
    private String orderReturnIds;

    @Schema(description = "平台交易佣金")
    @TableField("order_commission_fee")
    private BigDecimal orderCommissionFee;

    @Schema(description = "交易佣金-退款")
    @TableField("order_commission_fee_refund")
    private BigDecimal orderCommissionFeeRefund;

    @Schema(description = "订单赠送积分")
    @TableField("order_points_add")
    private BigDecimal orderPointsAdd;

    @Schema(description = "促销信息")
    @TableField("order_activity_data")
    private String orderActivityData;

    @Schema(description = "订单取消者身份(ENUM):1-买家; 2-卖家; 3-系统")
    @TableField("order_cancel_identity")
    private Boolean orderCancelIdentity;

    @Schema(description = "订单取消原因")
    @TableField("order_cancel_reason")
    private String orderCancelReason;

    @Schema(description = "订单取消时间")
    @TableField("order_cancel_time")
    private Date orderCancelTime;

    @Schema(description = "赠送资源2")
    @TableField("order_bp_add")
    private Integer orderBpAdd;

    @Schema(description = "订单返利")
    @TableField("order_rebate")
    private BigDecimal orderRebate;

    @Schema(description = "手机号码")
    @TableField("buyer_mobile")
    private Integer buyerMobile;

    @Schema(description = "联系人")
    @TableField("buyer_contacter")
    private String buyerContacter;

    @Schema(description = "满返优惠券活动id(DOT)")
    @TableField("activity_manhui_id")
    private String activityManhuiId;

    @Schema(description = "活动-多倍积分id")
    @TableField("activity_double_points_id")
    private Integer activityDoublePointsId;

    @Schema(description = "活动-多倍积分")
    @TableField("order_double_points_add")
    private BigDecimal orderDoublePointsAdd;

    @Schema(description = "满返用户优惠券id(DOT)")
    @TableField("activity_voucher_id")
    private String activityVoucherId;

    @Schema(description = "满返优惠券发放状态(ENUM):1000-无需发放;1001-待发放; 1002-已发放; 1003-发放异常")
    @TableField("order_activity_manhui_state")
    private Integer orderActivityManhuiState;

    //order_base

    @Schema(description = "订单编号")
    @TableField("order_number")
    private String orderNumber;

    @Schema(description = "下单时间")
    @TableField("order_time")
    private Date orderTime;

    @Schema(description = "商品原价总和:商品发布原价")
    @TableField("order_product_amount")
    private BigDecimal orderProductAmount;

    @Schema(description = "应付金额:order_product_amount - order_discount_amount + order_shipping_fee - order_voucher_price - order_points_fee - order_adjust_fee")
    @TableField("order_payment_amount")
    private BigDecimal orderPaymentAmount;

    @Schema(description = "货币编号")
    @TableField("currency_id")
    private Integer currencyId;

    @Schema(description = "左符号")
    @TableField("currency_symbol_left")
    private String currencySymbolLeft;


    @Schema(description = "店铺名称")
    @TableField("store_name")
    private String storeName;


    @Schema(description = "买家昵称")
    @TableField("user_nickname")
    private String userNickname;

    //trade
    @Schema(description = "实付金额:在线支付金额,此为订单默认需要支付额度。")
    @TableField("trade_payment_amount")
    private BigDecimal tradePaymentAmount;

    @Schema(description = "商品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "订单SKU")
    private List<OrderItem> items;

    @Schema(description = "虚拟码:pickupcode")
    @TableField("chain_code")
    private String chainCode;

    @Schema(description = "预约时间")
    @TableField("virtual_service_time")
    private Date virtualServiceTime;

    @Schema(description = "配送地址")
    private OrderDeliveryAddress delivery;

    @Schema(description = "订单物流")
    private List<OrderLogistics> logistics;

    @Schema(description = "订单记录")
    private List<OrderStateLog> logItems;

    @Schema(description = "出库单")
    private List<StockBillVo> stockBill;

    @Schema(description = "支付记录")
    private List<ConsumeRecord> consumeRecord;

    @Schema(description = "交易数据")
    private ConsumeTrade consumeTrade;

    @Schema(description = "支付信息")
    private ConsumeDeposit consumeDeposit;

    @Schema(description = "库存数据")
    private List<WarehouseItem> warehouseItems;

    @Schema(description = "取消订单")
    private Boolean ifBuyerCancel;

    @Schema(description = "退款标记")
    private Boolean returnFlag;

    @Schema(description = "发票标记")
    private Boolean invoiceIsApply;

    @Schema(description = "剩余支付时间")
    private Long remainPayTime;

    @Schema(description = "自提日期")
    private Date virtualServiceDate;

    @Schema(description = "门店电话")
    private String chainMobile;

    @Schema(description = "门店地址")
    private String chainAddress;

    @Schema(description = "门店名称")
    private String chainName;

    @Schema(description = "是否审核订单")
    private Boolean isReviewOrder;

    @Schema(description = "审核失败原因")
    private String stateFailedNote;

    @Schema(description = "不是本人")
    private Boolean notOneself;

    @Schema(description = "销售员")
    private String saleName;

    @Schema(description = "退货按钮隐藏(BOOL): 1-是; 0-否")
    private Boolean orderReturnHidden;

    @Schema(description = "开票按钮隐藏(BOOL): 1-是; 0-否")
    private Boolean orderInvoiceHidden;

    @Schema(description = "订单PLUS优惠金额")
    @TableField("order_plus_discount_amount")
    private BigDecimal orderPlusDiscountAmount;

    @Schema(description = "会员等级优惠金额")
    @TableField("order_level_discount_amount")
    private BigDecimal orderLevelDiscountAmount;

    @Schema(description = "订单PLUS优惠金额")
    @TableField("plus_shipping_discount")
    private BigDecimal plusShippingDiscount;

}
