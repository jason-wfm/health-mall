package com.wechuang.mallshop.core.consts;

import lombok.ToString;

/**
 * Redis 常量（自研重写，静态默认值对齐 core-3.0.27908 字节码核验：
 * ID="1001"、SEPARATOR=":"、Cache_NameSpace=":1001:"、CAPTCHA_PREFIX=":1001::"、EXPIRE_TIME=7200）
 * 注意：业务侧在类加载期即拼接 Cache_NameSpace，保持静态初始化顺序不可调整
 *
 * @since 3.1.0-healthmall
 */
@ToString
public class ConstantRedis {

    public static String ID = "1001";

    public static String SEPARATOR = ":";

    public static String Cache_NameSpace = SEPARATOR + ID + SEPARATOR;

    public static String CAPTCHA_PREFIX = Cache_NameSpace + SEPARATOR;

    public static long EXPIRE_TIME = 7200L;

    public static final String VERSION = "1.0.1";

    public void setId(String id) {
        ID = id;
    }

    public void setSeparator(String separator) {
        SEPARATOR = separator;
    }
}
