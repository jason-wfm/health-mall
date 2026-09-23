package com.wechuang.mallshop.account.service.impl;

import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.model.req.UserLevelListReq;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.account.service.UserLevelService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户等级表-平台 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Service
public class UserLevelServiceImpl extends BaseServiceImpl<UserLevelRepository, UserLevel, UserLevelListReq> implements UserLevelService {
}
