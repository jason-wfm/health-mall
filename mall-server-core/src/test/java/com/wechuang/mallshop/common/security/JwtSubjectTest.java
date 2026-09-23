package com.wechuang.mallshop.common.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** [healthmall-ext] JwtSubject 携带 merchantId 的往返测试 */
class JwtSubjectTest {

    @Test
    void jwtSubjectRoundTripKeepsMerchantId() {
        String key = JwtUtil.encodeKey(JwtUtil.randomKey());
        JwtSubject subject = new JwtSubject(10001, "salt", 2, 0, 3, 0, 3, 1); // 8 参：含 merchantId

        String token = JwtUtil.buildToken(subject, 60L, key);
        Claims claims = JwtUtil.parseToken(token, key);
        JwtSubject parsed = JwtUtil.getJwtSubject(claims);

        assertEquals(Integer.valueOf(3), parsed.getMerchantId());
        assertEquals(Integer.valueOf(2), parsed.getRoleId());
    }

    @Test
    void oldTokenWithoutMerchantIdParsesToNull() {
        String key = JwtUtil.encodeKey(JwtUtil.randomKey());
        // 旧 7 参构造已不存在，用 JSON 兼容旧报文：手工构造缺字段 JSON
        String oldJson = "{\"userId\":10001,\"userSalt\":\"salt\",\"roleId\":9,\"siteId\":0,\"storeId\":0,\"chainId\":0,\"clientId\":1}";
        String token = JwtUtil.buildToken(oldJson, 60L, JwtUtil.decodeKey(key));
        JwtSubject parsed = JwtUtil.getJwtSubject(JwtUtil.parseToken(token, key));
        assertNull(parsed.getMerchantId()); // 旧 token 兼容（R3）
    }
}
