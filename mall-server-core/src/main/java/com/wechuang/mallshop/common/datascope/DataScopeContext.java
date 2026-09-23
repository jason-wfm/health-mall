package com.wechuang.mallshop.common.datascope;

import com.wechuang.mallshop.common.consts.ConstantDataScope;
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;

/**
 * [healthmall-ext] 数据权限上下文
 * 解析当前登录身份的数据范围；无请求上下文（定时任务/MQ 消费）或平台身份时返回 null（=不加过滤）
 * /work 通道（医护/管家，Phase 3）接入后在此扩展 STAFF 范围与 JWT 声明的 staffId
 */
public class DataScopeContext {

    /**
     * 当前身份的数据范围
     */
    public static String currentScope() {
        ContextUser user = ContextUtil.getLoginUser();
        if (user == null || user.getRoleId() == null) {
            return ConstantDataScope.SCOPE_SELF;
        }

        int roleId = user.getRoleId();
        if (roleId == ConstantRole.ROLE_ADMIN || roleId == ConstantRole.ROLE_SITE) {
            return ConstantDataScope.SCOPE_PLATFORM;
        }
        if (roleId == ConstantRole.ROLE_SELLER) {
            return ConstantDataScope.SCOPE_MERCHANT;
        }
        if (roleId == ConstantRole.ROLE_CHAIN) {
            return ConstantDataScope.SCOPE_CHAIN;
        }
        return ConstantDataScope.SCOPE_SELF;
    }

    /**
     * [healthmall-ext] 当前商家编号（真实归属语义，spec §4.2）
     * 商家角色(2)直取；门店角色(3)取登录时推导值；平台/匿名/无归属返回 null（=不加过滤）
     */
    public static Integer getMerchantId() {
        ContextUser user = ContextUtil.getLoginUser();
        if (user != null && user.getRoleId() != null) {
            if ((user.getRoleId() == ConstantRole.ROLE_SELLER || user.getRoleId() == ConstantRole.ROLE_CHAIN)
                    && user.getMerchantId() != null && user.getMerchantId() > 0) {
                return user.getMerchantId();
            }
        }
        // C 端（front）：经定位门店推导（spec §4.2 C 端行）
        if (!ContextUtil.isManage()) {
            Integer storeId = ContextUtil.getStoreId();
            if (storeId != null && storeId > 0) {
                return ContextUtil.getStoreMerchantId(storeId);
            }
        }
        return null;
    }

    /**
     * 当前门店编号（仅门店管理员身份返回非空）
     */
    public static Integer getChainId() {
        ContextUser user = ContextUtil.getLoginUser();
        if (user != null && user.getRoleId() != null
                && user.getRoleId() == ConstantRole.ROLE_CHAIN
                && user.getChainId() != null) {
            return user.getChainId();
        }
        return null;
    }
}
