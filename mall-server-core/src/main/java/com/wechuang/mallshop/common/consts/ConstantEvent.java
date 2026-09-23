package com.wechuang.mallshop.common.consts;

/**
 * [healthmall-ext] 健康商城领域事件常量
 * 事件类型即路由键，命名规范：{域}.{聚合}.{动作}，如 order.paid
 * 事件目录对齐《健康商城重构方案》6.2 节与解决方案文档 §35/§44
 */
public class ConstantEvent {

    /**
     * 领域事件总线交换机（与存量 shop/account/pay 交换机隔离）
     */
    public static final String EVENT_EXCHANGE = "healthmall.event";

    // ===== 订单域 =====
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_PAID = "order.paid";
    public static final String ORDER_CANCELLED = "order.cancelled";
    public static final String ORDER_REFUNDED = "order.refunded";
    public static final String ORDER_SHIPPED = "order.shipped";
    public static final String ORDER_COMPLETED = "order.completed";

    // ===== 库存域 =====
    public static final String INVENTORY_LOCKED = "inventory.locked";
    public static final String INVENTORY_RELEASED = "inventory.released";
    public static final String INVENTORY_SHORTAGE = "inventory.shortage";

    // ===== 预约/履约域 =====
    public static final String APPOINTMENT_CREATED = "appointment.created";
    public static final String APPOINTMENT_CHANGED = "appointment.changed";
    public static final String VERIFICATION_SUCCEEDED = "verification.succeeded";
    public static final String SERVICE_RECORD_COMPLETED = "service.record.completed";

    // ===== 派单域 =====
    public static final String DISPATCH_CREATED = "dispatch.created";
    public static final String DISPATCH_ACCEPTED = "dispatch.accepted";
    public static final String DISPATCH_REJECTED = "dispatch.rejected";
    public static final String DISPATCH_TIMEOUT = "dispatch.timeout";
    public static final String DISPATCH_MANUAL = "dispatch.manual";

    // ===== 资金结算域 =====
    public static final String SETTLEMENT_CREATED = "settlement.created";
    public static final String SETTLEMENT_COMPLETED = "settlement.completed";
    public static final String WITHDRAWAL_CREATED = "withdrawal.created";
    public static final String WITHDRAWAL_COMPLETED = "withdrawal.completed";
    public static final String RECONCILIATION_EXCEPTION = "reconciliation.exception";

    // ===== 健康域 =====
    public static final String HEALTH_DATA_UPDATED = "health.data.updated";
    public static final String HEALTH_ASSESSMENT_COMPLETED = "health.assessment.completed";

    // ===== 商家/门店域 =====
    public static final String MERCHANT_ACTIVATED = "merchant.activated";
    public static final String MERCHANT_SUSPENDED = "merchant.suspended";
}
