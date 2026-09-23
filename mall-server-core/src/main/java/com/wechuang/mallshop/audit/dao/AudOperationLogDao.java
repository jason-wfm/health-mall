package com.wechuang.mallshop.audit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechuang.mallshop.audit.model.entity.AudOperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * [healthmall-ext] 审计操作日志 Mapper 接口
 */
@Mapper
public interface AudOperationLogDao extends BaseMapper<AudOperationLog> {

}
