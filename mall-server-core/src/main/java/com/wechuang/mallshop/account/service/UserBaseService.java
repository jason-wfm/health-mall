package com.wechuang.mallshop.account.service;

import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.account.model.req.UserBaseListReq;
import com.wechuang.mallshop.account.model.res.UserBaseMixedRes;
import com.wechuang.mallshop.core.web.service.IBaseService;

/**
 * <p>
 * 用户基本信息表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
public interface UserBaseService extends IBaseService<UserBase, UserBaseListReq> {
    public UserBaseMixedRes getUserBaseInfoSns(Integer user_id);
}
