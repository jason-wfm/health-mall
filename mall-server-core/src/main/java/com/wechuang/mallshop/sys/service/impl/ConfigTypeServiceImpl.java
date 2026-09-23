package com.wechuang.mallshop.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.entity.ConfigType;
import com.wechuang.mallshop.sys.model.req.ConfigTypeListReq;
import com.wechuang.mallshop.sys.repository.ConfigBaseRepository;
import com.wechuang.mallshop.sys.repository.ConfigTypeRepository;
import com.wechuang.mallshop.sys.service.ConfigTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 配置分组表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Service
public class ConfigTypeServiceImpl extends BaseServiceImpl<ConfigTypeRepository, ConfigType, ConfigTypeListReq> implements ConfigTypeService {
    @Autowired
    private ConfigBaseRepository configBaseRepository;

    @Override
    public boolean remove(Serializable configTypeId) {
        ConfigType configType = get(configTypeId);

        if (configType.getConfigTypeBuildin()) {
            throw new BusinessException(__("系统内置，不可删除"));
        }

        QueryWrapper<ConfigBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_type_id", configTypeId);
        long count = configBaseRepository.count(queryWrapper);

        if (count > 0) {
            throw new BusinessException(String.format(__("有 %d 条配置使用，不可删除"), count));
        }

        return super.remove(configTypeId);
    }
}

