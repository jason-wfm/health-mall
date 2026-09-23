package com.wechuang.mallshop.account.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.dao.UserBaseDao;
import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.account.repository.UserBaseRepository;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Repository
public class UserBaseRepositoryImpl extends BaseRepositoryImpl<UserBaseDao, UserBase> implements UserBaseRepository {

    @Override
    public UserBase getByUserAccount(String username) {
        if (StrUtil.isBlank(username)) {
            return null;
        }

        UserBase user = findOne(new QueryWrapper<>(new UserBase().setUserAccount(username)));

        //todo fixInfo

        return user;
    }
}
