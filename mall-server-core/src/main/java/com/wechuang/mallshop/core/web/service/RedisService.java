package com.wechuang.mallshop.core.web.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Redis 操作服务（自研重写，42 个方法签名对齐 core-3.0.27908 javap 核验；
 * 带 long 参数的重载为"附带过期秒数"，execute 为 Lua 脚本执行）
 *
 * @since 3.1.0-healthmall
 */
public interface RedisService {

    void set(String key, Object value);

    void set(String key, Object value, long timeout);

    Object get(String key);

    Boolean hasKey(String key);

    Boolean expire(String key, long timeout);

    Long getExpire(String key);

    Boolean del(String key);

    Long del(List<String> keys);

    Long del(Set<String> keys);

    Set<String> keys(String pattern);

    Long incr(String key, Long delta);

    Long incr(String key, long delta);

    Long decr(String key, Long delta);

    Long decr(String key, long delta);

    List<Object> multiGet(List<String> keys);

    void multiSet(Map<String, Object> map);

    Object hGet(String key, String field);

    void hSet(String key, String field, Object value);

    Boolean hSet(String key, String field, Object value, long timeout);

    Map<Object, Object> hGetAll(String key);

    Boolean hHasKey(String key, String field);

    void hDel(String key, Object... fields);

    Long hIncr(String key, String field, Long delta);

    Long hDecr(String key, String field, Long delta);

    void hSetAll(String key, Map<String, ?> map);

    Boolean hSetAll(String key, Map<String, Object> map, long timeout);

    Long sAdd(String key, Object... values);

    Long sAdd(String key, long timeout, Object... values);

    Set<Object> sMembers(String key);

    Long sSize(String key);

    Boolean sIsMember(String key, Object value);

    Long sRemove(String key, Object... values);

    Long lPush(String key, Object value);

    Long lPush(String key, Object value, long timeout);

    Long lPushAll(String key, Object... values);

    Long lPushAll(String key, Long timeout, Object... values);

    List<Object> lRange(String key, long start, long end);

    Object lIndex(String key, long index);

    Long lSize(String key);

    Long lRemove(String key, long count, Object value);

    Object rPop(String key);

    Long execute(String script, List<String> keys, String arg);
}
