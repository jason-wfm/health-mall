package com.wechuang.mallshop.common.datascope;

import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;

/**
 * [healthmall-ext] 数据权限校验（Service 层，用于未纳入拦截器白名单的存量表）
 * 拦截器负责新域表的 SQL 自动注入；存量表在 Service 显式调用本工具校验归属
 * 禁止以前端传入的 merchantId/chainId 作为鉴权依据——只能取 DataScopeContext
 * 端别限制：仅 manage 侧调用（spec §4.2 管理端行）。C 端（front）禁止调用——
 * DataScopeContext.getMerchantId() 在 C 端按定位门店推导商家并在此强制相等校验，跨店浏览会被一律 403；
 * C 端的门店范围由 spec §4.2 C 端行（定位门店过滤）承担，不走本工具
 */
public class DataScopeChecker {

    /**
     * [healthmall-ext] 校验数据归属商家对当前身份可见（spec §4.2；仅 manage 侧，见类注释端别限制）
     * 禁止以前端传入的 merchantId 作为鉴权依据——只能取 DataScopeContext
     * @param merchantId 数据归属商家编号（商家主键，非 store_id）
     */
    public static void assertMerchantVisible(Integer merchantId) {
        Integer current = DataScopeContext.getMerchantId();
        if (current == null) {
            // 平台/门店/无商家范围身份，细粒度归属由业务逻辑判断
            return;
        }
        if (merchantId == null || !current.equals(merchantId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    /**
     * 校验门店数据可见性：门店管理员只能访问自己门店的数据
     *
     * @param chainId 数据归属门店
     */
    public static void assertChainVisible(Integer chainId) {
        Integer current = DataScopeContext.getChainId();
        if (current == null) {
            return;
        }
        if (chainId == null || !current.equals(chainId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}
