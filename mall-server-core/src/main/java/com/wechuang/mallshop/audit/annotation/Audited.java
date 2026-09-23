package com.wechuang.mallshop.audit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [healthmall-ext] 操作审计注解
 * 标注在 Service 方法上，由 AuditAspect 自动记录操作人/上下文/新旧值/结果
 * 重点场景（对齐《健康商城重构方案》6.5）：健康数据查看/导出、退款、强制退款、改价、
 * 商品下架、派单、提现、结算、权限变更、商家审核
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audited {

    /**
     * 操作动作编码，如 ORDER_REFUND / HEALTH_PROFILE_VIEW / WITHDRAWAL_AUDIT
     */
    String action();

    /**
     * 资源类型，如 Order / HealthProfile / Withdrawal
     */
    String resourceType() default "";

    /**
     * 资源编号，支持 SpEL 引用方法参数，如 "#orderId"
     */
    String resourceId() default "";

    /**
     * 变更前值，SpEL，方法执行前求值，如 "#order.snapshot()"
     */
    String oldValue() default "";

    /**
     * 变更后值，SpEL，方法执行后求值，可用 "#result" 引用返回值
     */
    String newValue() default "";
}
