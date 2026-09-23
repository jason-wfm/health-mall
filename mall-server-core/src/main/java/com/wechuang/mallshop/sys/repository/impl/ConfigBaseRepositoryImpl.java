package com.wechuang.mallshop.sys.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.sys.dao.ConfigBaseDao;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.repository.ConfigBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 系统参数设置表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Repository
public class ConfigBaseRepositoryImpl extends BaseRepositoryImpl<ConfigBaseDao, ConfigBase> implements ConfigBaseRepository {

    static Map<String, ConfigBase> map;

    @Override
    public List<ConfigBase> gets(List<String> objects) {
        List<ConfigBase> configBaseList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(objects)) {
            for (String object : objects) {

                if (CollectionUtil.isNotEmpty(map)) {
                    ConfigBase base = map.get(object);

                    if (base == null) {
                        base = super.get(object);
                        map.put(object, base);
                    }
                    configBaseList.add(base);
                } else {
                    configBaseList = super.gets(objects);
                    for (ConfigBase configBase : configBaseList) {
                        map.put(configBase.getConfigKey(), configBase);
                    }
                }
            }
        }

        return configBaseList;
    }

    public boolean getConfigValue(String config_key, Boolean defaultValue) {
        String config_value = "";
        ConfigBase configBase = get(config_key);
        if (ObjectUtil.isNull(configBase))
            return defaultValue;

        config_value = configBase.getConfigValue();
        if (StrUtil.isBlank(config_value)) return defaultValue;
        boolean isNumber = NumberUtil.isNumber(config_value);
        if (!isNumber) return defaultValue;
        return "1".equals(config_value);
    }

}
