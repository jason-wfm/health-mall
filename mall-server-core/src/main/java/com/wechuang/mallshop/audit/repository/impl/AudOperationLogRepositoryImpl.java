package com.wechuang.mallshop.audit.repository.impl;

import com.wechuang.mallshop.audit.dao.AudOperationLogDao;
import com.wechuang.mallshop.audit.model.entity.AudOperationLog;
import com.wechuang.mallshop.audit.repository.AudOperationLogRepository;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * [healthmall-ext] 审计操作日志 数据访问实现
 */
@Repository
public class AudOperationLogRepositoryImpl extends BaseRepositoryImpl<AudOperationLogDao, AudOperationLog> implements AudOperationLogRepository {

}
