package com.wechuang.mallshop.account.repository.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.dao.UserInfoDao;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.common.consts.AuthConstant;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户详细信息表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Repository
public class UserInfoRepositoryImpl extends BaseRepositoryImpl<UserInfoDao, UserInfo> implements UserInfoRepository {

    @Autowired
    private UserLevelRepository userLevelRepository;

    public Map<Integer, UserInfo> getUserInfoMap(List<Integer> userIds) {
        List<UserInfo> userInfos = gets(userIds);
        Map<Integer, UserInfo> userInfoMap = new HashMap<>();
        List<UserLevel> userLevels = userLevelRepository.find(new QueryWrapper<>());
        Map<Integer, String> levelNameMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(userLevels)) {
            levelNameMap = userLevels.stream().collect(Collectors.toMap(UserLevel::getUserLevelId, UserLevel::getUserLevelName));
        }

        if (CollectionUtil.isNotEmpty(userInfos)) {
            for (UserInfo userInfo : userInfos) {

                if (levelNameMap.containsKey(userInfo.getUserLevelId())) {
                    userInfo.setUserLevelName(levelNameMap.get(userInfo.getUserLevelId()));
                }
            }

            userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getUserId, UserInfo -> UserInfo, (k1, k2) -> k1));
        }

        return userInfoMap;
    }

    @Override
    public List<UserInfo> gets(Collection<? extends Serializable> a) {
        List<UserInfo> list = super.gets(a);
        ContextUser loginUser = ContextUtil.getLoginUser();

        if ((loginUser != null && loginUser.getClientId().equals(AuthConstant.ADMIN_CLIENT_ID)) && ConstantConfig.URL_BASE.equals("https://demo.modulithshop.cn")) {
            for (UserInfo it : list) {
                if (ObjectUtil.isNotEmpty(it)) {
                    String phoneNumber = it.getUserMobile();
                    if (phoneNumber.length() == 11) {
                        phoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
                        it.setUserMobile(phoneNumber);
                    }

                    String userAccount = it.getUserAccount();

                    if (userAccount.contains("+") && userAccount.length() == 14) {
                        userAccount = userAccount.substring(0, 6) + "****" + userAccount.substring(10);
                        it.setUserAccount(userAccount);
                    }

                    String userNickname = it.getUserNickname();
                    if (userNickname.contains("+") && userNickname.length() == 14) {
                        userNickname = userNickname.substring(0, 6) + "****" + userNickname.substring(10);
                        it.setUserNickname(userNickname);
                    }
                }
            }
        }

        return list;
    }
}
