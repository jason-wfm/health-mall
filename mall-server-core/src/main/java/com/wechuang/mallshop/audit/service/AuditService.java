package com.wechuang.mallshop.audit.service;

import com.wechuang.mallshop.audit.model.entity.AudOperationLog;
import com.wechuang.mallshop.audit.model.req.AudOperationLogListReq;
import com.wechuang.mallshop.core.web.service.IBaseService;

/**
 * [healthmall-ext] 审计服务
 */
public interface AuditService extends IBaseService<AudOperationLog, AudOperationLogListReq> {

    /**
     * 异步记录审计日志（失败只记日志，不影响业务）
     */
    void record(AudOperationLog auditLog);
}
