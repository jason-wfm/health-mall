package com.wechuang.mallshop.common.idempotent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * [healthmall-ext] 幂等注解
 * 强制用于：支付/退款回调、分账、提现、库存扣减、订单取消、结算、对账、核销等关键操作
 * 实现方式：Redis SETNX + TTL，重复请求在锁定期内直接抛 BusinessException
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等键，支持 SpEL（可引用方法参数），如 "#orderId"、"#callbackBody.transactionId"
     * 为空时按 方法签名 + 全参数摘要 生成
     */
    String key() default "";

    /**
     * 锁定时长（秒）：该窗口内的重复请求视为重复提交
     */
    int expireSeconds() default 10;

    /**
     * 命中重复时的提示文案
     */
    String message() default "请勿重复提交";
}
