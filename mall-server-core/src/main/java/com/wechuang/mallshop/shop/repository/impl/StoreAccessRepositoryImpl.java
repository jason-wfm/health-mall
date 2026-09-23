package com.wechuang.mallshop.shop.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.shop.dao.StoreAccessDao;
import com.wechuang.mallshop.shop.model.entity.StoreAccess;
import com.wechuang.mallshop.shop.repository.StoreAccessRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * [healthmall-ext] C端门店接入定位 服务实现类（spec §3.3）
 */
@Repository
public class StoreAccessRepositoryImpl extends BaseRepositoryImpl<StoreAccessDao, StoreAccess> implements StoreAccessRepository {

    @Override
    public StoreAccess getByTypeAndKey(String accessType, String accessKey) {
        LambdaQueryWrapper<StoreAccess> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StoreAccess::getAccessType, accessType)
                .eq(StoreAccess::getAccessKey, accessKey)
                .eq(StoreAccess::getAccessStatus, 1);

        // 取第一条（唯一键 uk_type_key 约束下正常仅一条，兜底防脏数据）
        List<StoreAccess> list = list(queryWrapper);
        return list.isEmpty() ? null : list.get(0);
    }
}
