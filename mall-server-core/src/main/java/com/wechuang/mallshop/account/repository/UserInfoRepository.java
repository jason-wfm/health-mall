package com.wechuang.mallshop.account.repository;

import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.core.web.repository.IBaseRepository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详细信息表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
public interface UserInfoRepository extends IBaseRepository<UserInfo> {
    Map<Integer, UserInfo> getUserInfoMap(List<Integer> userIds);
}
