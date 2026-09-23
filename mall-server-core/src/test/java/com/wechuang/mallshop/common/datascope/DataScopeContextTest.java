package com.wechuang.mallshop.common.datascope;

import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.common.consts.ConstantRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/** [healthmall-ext] 商家身份数据范围语义测试（spec §4.2） */
class DataScopeContextTest {

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    private void loginAs(int roleId, Integer merchantId, Integer storeId) {
        UserBase user = new UserBase();
        user.setRoleId(roleId);
        user.setMerchantId(merchantId);
        user.setStoreId(storeId);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    void sellerReturnsOwnMerchantIdNotStoreId() {
        // 回归锚点：merchant_id=5/store_id=7 的商家，必须返回 5（旧语义返回 7）
        loginAs(ConstantRole.ROLE_SELLER, 5, 7);
        assertEquals(Integer.valueOf(5), DataScopeContext.getMerchantId());
    }

    @Test
    void chainRoleReturnsDerivedMerchantId() {
        loginAs(ConstantRole.ROLE_CHAIN, 5, 7);
        assertEquals(Integer.valueOf(5), DataScopeContext.getMerchantId());
    }

    @Test
    void platformAndAnonymousReturnNull() {
        loginAs(ConstantRole.ROLE_ADMIN, 0, 0);
        assertNull(DataScopeContext.getMerchantId());
        SecurityContextHolder.clearContext();
        assertNull(DataScopeContext.getMerchantId());
    }
}
