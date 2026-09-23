package com.wechuang.mallshop.audit.service.impl;

import com.wechuang.mallshop.audit.model.entity.AudOperationLog;
import com.wechuang.mallshop.audit.model.req.AudOperationLogListReq;
import com.wechuang.mallshop.audit.repository.AudOperationLogRepository;
import com.wechuang.mallshop.audit.service.AuditService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * [healthmall-ext] 审计服务实现
 */
@Service
@Slf4j
public class AuditServiceImpl extends BaseServiceImpl<AudOperationLogRepository, AudOperationLog, AudOperationLogListReq> implements AuditService {

    @Async("taskExecutor")
    @Override
    public void record(AudOperationLog auditLog) {
        try {
            save(auditLog);
        } catch (Exception e) {
            log.error("审计日志记录失败, action={}, resourceId={}", auditLog.getAction(), auditLog.getResourceId(), e);
        }
    }
}
