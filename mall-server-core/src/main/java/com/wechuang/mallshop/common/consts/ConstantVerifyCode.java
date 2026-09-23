package com.wechuang.mallshop.common.consts;

import com.wechuang.mallshop.core.consts.ConstantRedis;

/**
 * 注册常量
 */
public class ConstantVerifyCode {
    public static final String KEY_PREFIX = ConstantRedis.Cache_NameSpace + "verify" + ConstantRedis.SEPARATOR;
    public static final long TIME = 60 * 5;
}
