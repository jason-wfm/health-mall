package com.wechuang.mallshop.common.idempotent;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * [healthmall-ext] 幂等切面
 * Redis SETNX 抢锁，抢不到即视为重复请求；锁随 TTL 自动过期，业务结束后不主动释放
 * （回调/资金类操作在锁定期内拒绝重放是预期行为；确需放行的场景应调短 expireSeconds）
 */
@Aspect
@Component
@Slf4j
public class IdempotentAspect {

    private static final String KEY_PREFIX = "healthmall:idem:";

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        String key = buildKey(pjp, idempotent.key());

        Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(key, "1",
                Duration.ofSeconds(idempotent.expireSeconds()));

        if (!Boolean.TRUE.equals(acquired)) {
            log.warn("幂等拦截，key={}", key);
            throw new BusinessException(ResultCode.FAILED, __(idempotent.message()));
        }

        return pjp.proceed();
    }

    private String buildKey(ProceedingJoinPoint pjp, String keyExpr) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String base = method.getDeclaringClass().getName() + ":" + method.getName();

        String keyPart;
        if (StrUtil.isNotBlank(keyExpr)) {
            MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                    null, method, pjp.getArgs(), new DefaultParameterNameDiscoverer());
            Object value = PARSER.parseExpression(keyExpr).getValue(context);
            keyPart = value == null ? "" : String.valueOf(value);
        } else {
            keyPart = DigestUtil.md5Hex(JSONUtil.toJSONString(pjp.getArgs()));
        }

        return KEY_PREFIX + base + ":" + keyPart;
    }
}
