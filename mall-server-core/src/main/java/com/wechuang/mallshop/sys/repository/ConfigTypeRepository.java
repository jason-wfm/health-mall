package com.wechuang.mallshop.sys.repository;

import com.wechuang.mallshop.core.web.repository.IBaseRepository;
import com.wechuang.mallshop.sys.model.entity.ConfigType;

import java.io.Serializable;

/**
 * <p>
 * 配置分组表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
public interface ConfigTypeRepository extends IBaseRepository<ConfigType> {
    boolean remove(Serializable id);
}
