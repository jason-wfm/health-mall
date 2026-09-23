package com.wechuang.mallshop.account.service.impl;

import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.entity.UserSns;
import com.wechuang.mallshop.account.model.req.UserBaseListReq;
import com.wechuang.mallshop.account.model.res.UserBaseMixedRes;
import com.wechuang.mallshop.account.repository.UserBaseRepository;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserSnsRepository;
import com.wechuang.mallshop.account.service.UserBaseService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 用户基本信息表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Service
public class UserBaseServiceImpl extends BaseServiceImpl<UserBaseRepository, UserBase, UserBaseListReq> implements UserBaseService {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserSnsRepository userSnsRepository;
    @Autowired
    private UserBaseRepository userBaseRepository;

    public UserBaseMixedRes getUserBaseInfoSns(Integer user_id) {
        UserBaseMixedRes userBaseMixedReq = new UserBaseMixedRes();

        Optional<UserBase> userBaseOptional = Optional.ofNullable(userBaseRepository.get(user_id));
        userBaseOptional.ifPresent(userBase -> {
            BeanUtils.copyProperties(userBase, userBaseMixedReq);
        });

        Optional<UserInfo> accountInfoOptional = Optional.ofNullable(userInfoRepository.get(user_id));
        accountInfoOptional.ifPresent(userInfo -> {
            BeanUtils.copyProperties(userInfo, userBaseMixedReq);
        });

        Optional<UserSns> accountUserSnsOptional = Optional.ofNullable(userSnsRepository.get(user_id));
        accountUserSnsOptional.ifPresent(accountUserSns -> {
            BeanUtils.copyProperties(accountUserSns, userBaseMixedReq);
        });


        return userBaseMixedReq;
    }


}