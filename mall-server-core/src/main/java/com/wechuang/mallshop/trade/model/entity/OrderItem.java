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
import java.util.List;

/**
 * <p>
 * 订单商品表
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("trade_order_item")
@Schema(name = "OrderItem对象", description = "订单商品表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "编号")
    @TableId(value = "order_item_id", type = IdType.AUTO)
    private Long orderItemId;

    @Schema(description = "订单编号")
    @TableField("order_id")
    private String orderId;

    @Schema(description = "买家编号")
    @TableField("user_id")
    private Integer userId;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "产品编号")
    @TableField("product_id")
    private Long productId;

    @Schema(description = "商品名称")
    @TableField("product_name")
    private String productName;

    @Schema(description = "货品编号")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "商品名称")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "分类编号")
    @TableField("category_id")
    private Integer categoryId;

    @Schema(description = "成本价")
    @TableField("item_cost_price")
    private BigDecimal itemCostPrice;

    @Schema(description = "商品价格单价")
    @TableField("item_unit_price")
    private BigDecimal itemUnitPrice;

    @Schema(description = "资源1单价")
    @TableField("item_unit_points")
    private BigDecimal itemUnitPoints;

    @Schema(description = "资源2单价")
    @TableField("item_unit_sp")
    private BigDecimal itemUnitSp;

    @Schema(description = "商品实际成交价单价")
    @TableField("order_item_sale_price")
    private BigDecimal orderItemSalePrice;

    @Schema(description = "商品数量")
    @TableField("order_item_quantity")
    private Integer orderItemQuantity;

    @Schema(description = "库存锁定(ENUM):1001-下单锁定;1002-支付锁定;")
    @TableField("order_item_inventory_lock")
    private Integer orderItemInventoryLock;

    @Schema(description = "商品图片")
    @TableField("order_item_image")
    private String orderItemImage;

    @Schema(description = "退货数量")
    @TableField("order_item_return_num")
    private Integer orderItemReturnNum;

    @Schema(description = "退款总额")
    @TableField("order_item_return_subtotal")
    private BigDecimal orderItemReturnSubtotal;

    @Schema(description = "退款金额:同意额度")
    @TableField("order_item_return_agree_amount")
    private BigDecimal orderItemReturnAgreeAmount;

    @Schema(description = "商品实际总金额: order_item_sale_price * order_item_quantity")
    @TableField("order_item_amount")
    private BigDecimal orderItemAmount;

    @Schema(description = "优惠金额:只考虑单品的，订单及店铺总活动优惠不影响")
    @TableField("order_item_discount_amount")
    private BigDecimal orderItemDiscountAmount;

    @Schema(description = "手工调整金额")
    @TableField("order_item_adjust_fee")
    private BigDecimal orderItemAdjustFee;

    @Schema(description = "积分费用")
    @TableField("order_item_points_fee")
    private BigDecimal orderItemPointsFee;

    @Schema(description = "赠送积分")
    @TableField("order_item_points_add")
    private BigDecimal orderItemPointsAdd;

    @Schema(description = "实付金额: order_item_payment_amount =  order_item_amount - order_item_discount_amount - order_item_adjust_fee - order_item_point_fee")
    @TableField("order_item_payment_amount")
    private BigDecimal orderItemPaymentAmount;

    @Schema(description = "最终在线支付金额(退款金额):order_item_payment_amount - order_item_voucher - order_item_reduce")
    @TableField(exist = false)
    private BigDecimal orderItemCanRefundAmount;

    @Schema(description = "评价状态(ENUM): 0-未评价;1-已评价;2-失效评价")
    @TableField("order_item_evaluation_status")
    private Boolean orderItemEvaluationStatus;

    @Schema(description = "活动类型(ENUM):0-默认;1101-加价购=搭配宝;1102-店铺满赠-小礼品;1103-限时折扣;1104-优惠套装;1105-店铺代金券coupon优惠券;1106-拼团;1107-满减送;1108-阶梯价;1109-积分换购")
    @TableField("activity_type_id")
    private Integer activityTypeId;

    @Schema(description = "促销活动ID:与activity_type_id搭配使用, 团购ID/限时折扣ID/优惠套装ID/积分兑换编号")
    @TableField("activity_id")
    private Integer activityId;

    @Schema(description = "礼包活动对应兑换码code")
    @TableField("activity_code")
    private String activityCode;

    @Schema(description = "分佣金比例百分比")
    @TableField("order_item_commission_rate")
    private BigDecimal orderItemCommissionRate;

    @Schema(description = "佣金")
    @TableField("order_item_commission_fee")
    private BigDecimal orderItemCommissionFee;

    @Schema(description = "退款佣金")
    @TableField("order_item_commission_fee_refund")
    private BigDecimal orderItemCommissionFeeRefund;

    @Schema(description = "价格策略折扣率")
    @TableField("policy_discountrate")
    private BigDecimal policyDiscountrate;

    @Schema(description = "分配优惠券额度")
    @TableField("order_item_voucher")
    private BigDecimal orderItemVoucher;

    @Schema(description = "分配满减额度")
    @TableField("order_item_reduce")
    private BigDecimal orderItemReduce;

    @Schema(description = "分配销售员折扣额度")
    @TableField("order_item_sale_person_discount")
    private BigDecimal orderItemSalePersonDiscount;

    @Schema(description = "备注")
    @TableField("order_item_note")
    private String orderItemNote;

    @Schema(description = "订单附件")
    @TableField("order_item_file")
    private String orderItemFile;

    @Schema(description = "商家附件")
    @TableField("order_item_confirm_file")
    private String orderItemConfirmFile;

    @Schema(description = "买家确认状态(BOOL):0-为确认;1-已确认")
    @TableField("order_item_confirm_status")
    private Boolean orderItemConfirmStatus;

    @Schema(description = "单品分销者编号")
    @TableField("order_item_saler_id")
    private Integer orderItemSalerId;

    @Schema(description = "分销商品编号")
    @TableField("item_src_id")
    private Long itemSrcId;

    @Schema(description = "拆单同步状态(BOOL):0-未同步;1-已同步")
    @TableField("order_item_supplier_sync")
    private Boolean orderItemSupplierSync;

    @Schema(description = "来源订单")
    @TableField("src_order_id")
    private String srcOrderId;

    @Schema(description = "同意退货数量")
    @TableField("order_item_return_agree_num")
    private Integer orderItemReturnAgreeNum;

    @Schema(description = "满返优惠券活动id")
    @TableField("order_give_id")
    private Integer orderGiveId;


    @Schema(description = "节省单价")
    @TableField(exist = false)
    private BigDecimal itemSavePrice;


    @Schema(description = "是否可退货")
    @TableField(exist = false)
    private Boolean ifReturn;

    @Schema(description = "已经学习")
    @TableField(exist = false)
    private Boolean haveLearned = false;

    @Schema(description = "服务编号")
    @TableField(exist = false)
    private List<String> returnIds;

    @Schema(description = "Spec名称")
    @TableField(exist = false)
    private String productItemName;

    @Schema(description = "商品是否发货")
    @TableField(exist = false)
    private Integer itemShipping = 0;

    public BigDecimal getItemSavePrice() {
        return itemUnitPrice.subtract(orderItemSalePrice);
    }

    public BigDecimal getOrderItemCanRefundAmount() {
        return orderItemPaymentAmount;
    }

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;

    @Schema(description = "会员专属价")
    @TableField("item_policy_price")
    private BigDecimal itemPolicyPrice;

    @Schema(description = "PLUS优惠金额")
    @TableField("item_plus_discount_amount")
    private BigDecimal itemPlusDiscountAmount;

    @Schema(description = "会员等级优惠金额")
    @TableField("item_level_discount_amount")
    private BigDecimal itemLevelDiscountAmount;

}
