package com.wechuang.mallshop.core.thread;

import java.util.ArrayList;
import java.util.List;

/**
 * 请求上下文变量（自研重写，对齐 core-3.0.27908）：
 * 登记事务内待失效的缓存键，事务提交后由调用方统一删除并 remove()
 *
 * @since 3.1.0-healthmall
 */
public class RequestContextVariable {

    private static final ThreadLocal<List<String>> CACHE_KEY_LIST = ThreadLocal.withInitial(ArrayList::new);

    public static void addCacheKey(String key) {
        if (key != null && !key.isEmpty()) {
            CACHE_KEY_LIST.get().add(key);
        }
    }

    public static void addCacheKey(List<String> keys) {
        if (keys != null && !keys.isEmpty()) {
            CACHE_KEY_LIST.get().addAll(keys);
        }
    }

    public static List<String> getCacheKeyList() {
        return CACHE_KEY_LIST.get();
    }

    public static void remove() {
        CACHE_KEY_LIST.remove();
    }
}
