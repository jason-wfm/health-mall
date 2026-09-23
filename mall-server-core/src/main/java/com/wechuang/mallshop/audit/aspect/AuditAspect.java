package com.wechuang.mallshop.audit.aspect;

import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.audit.annotation.Audited;
import com.wechuang.mallshop.audit.model.entity.AudOperationLog;
import com.wechuang.mallshop.audit.service.AuditService;
import com.wechuang.mallshop.common.datascope.DataScopeContext;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.HttpServletUtils;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * [healthmall-ext] 操作审计切面
 * 请求上下文（用户/IP/URI）必须在请求线程内同步采集，落库走异步，审计失败不影响业务
 */
@Aspect
@Component
@Slf4j
public class AuditAspect {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private static final int VALUE_MAX_LENGTH = 4000;

    @Autowired
    private AuditService auditService;

    @Around("@annotation(audited)")
    public Object around(ProceedingJoinPoint pjp, Audited audited) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                null, method, pjp.getArgs(), new DefaultParameterNameDiscoverer());

        AudOperationLog auditLog = new AudOperationLog();
        auditLog.setAction(audited.action());
        auditLog.setResourceType(audited.resourceType());
        auditLog.setResourceId(truncate(eval(audited.resourceId(), context)));
        auditLog.setOldValue(truncate(eval(audited.oldValue(), context)));

        captureContext(auditLog, method);

        Object result;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            auditLog.setResult(0);
            auditLog.setErrorMsg(truncate(e.getMessage()));
            auditService.record(auditLog);
            throw e;
        }

        auditLog.setResult(1);
        if (StrUtil.isNotBlank(audited.newValue())) {
            context.setVariable("result", result);
            auditLog.setNewValue(truncate(eval(audited.newValue(), context)));
        }
        auditService.record(auditLog);
        return result;
    }

    private void captureContext(AudOperationLog auditLog, Method method) {
        auditLog.setTraceId(MDC.get("traceId"));

        ContextUser user = ContextUtil.getLoginUser();
        if (user != null) {
            auditLog.setUserId(user.getUserId());
            auditLog.setUserAccount(user.getUserAccount());
            auditLog.setRoleId(user.getRoleId());
        }
        auditLog.setMerchantId(DataScopeContext.getMerchantId());
        auditLog.setChainId(DataScopeContext.getChainId());

        try {
            HttpServletRequest request = HttpServletUtils.getRequest();
            if (request != null) {
                auditLog.setClientIp(resolveIp(request));
                auditLog.setHttpMethod(request.getMethod());
                auditLog.setRequestUri(truncate(request.getRequestURI(), 255));
            }
        } catch (Exception e) {
            // 非请求上下文（MQ 消费/定时任务）没有 request，忽略
        }
    }

    private String resolveIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StrUtil.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int comma = ip.indexOf(',');
            return comma > 0 ? ip.substring(0, comma).trim() : ip.trim();
        }
        return request.getRemoteAddr();
    }

    private String eval(String expr, MethodBasedEvaluationContext context) {
        if (StrUtil.isBlank(expr)) {
            return null;
        }
        try {
            Object value = PARSER.parseExpression(expr).getValue(context);
            if (value == null) {
                return null;
            }
            if (value instanceof String str) {
                return str;
            }
            return JSONUtil.toJSONString(value);
        } catch (Exception e) {
            log.warn("审计 SpEL 求值失败, expr={}", expr, e);
            return null;
        }
    }

    private String truncate(String value) {
        return truncate(value, VALUE_MAX_LENGTH);
    }

    private String truncate(String value, int max) {
        if (value == null || value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }
}
