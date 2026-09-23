package com.wechuang.mallshop.sys.repository;

import com.wechuang.mallshop.core.web.repository.IBaseRepository;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;

import java.util.List;

/**
 * <p>
 * 系统参数设置表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
public interface ConfigBaseRepository extends IBaseRepository<ConfigBase> {

    List<ConfigBase> gets(List<String> objects);

    boolean getConfigValue(String config_key, Boolean defaultValue);

}
