package com.wechuang.mallshop.common.consts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * [healthmall-ext] 多商家 P1 白名单登记锚定测试（Task 8 分批灰度）
 * 锁定各批次已登记表与 R7 铁律（平台映射表永不入名单），防止后续误增/误删
 */
@DisplayName("merchant 白名单登记锚定（分批灰度）")
class ConstantDataScopeTest {

    @Test
    @DisplayName("R7 铁律：shop_store_base/shop_store_access 永不入 merchant 名单")
    void platformMappingTablesNeverMerchantScoped() {
        // C 端商家推导经这两张表查库，入名单即拦截器递归死锁（虽有哨兵破环，语义上属 site 豁免）
        assertFalse(ConstantDataScope.MERCHANT_SCOPED_TABLES.contains("shop_store_base"), "shop_store_base 不得进入 merchant 名单");
        assertFalse(ConstantDataScope.MERCHANT_SCOPED_TABLES.contains("shop_store_access"), "shop_store_access 不得进入 merchant 名单");
    }

    @Test
    @DisplayName("批次1：交易域 4 表已登记")
    void batch1TradeDomainRegistered() {
        // 名单随批次增长，历史批次仅锚定成员资格；精确规模由当前批次用例锚定
        assertTrue(ConstantDataScope.MERCHANT_SCOPED_TABLES.containsAll(java.util.Arrays.asList(
                "trade_order_base", "trade_order_item", "trade_order_info", "trade_order_return")));
    }

    @Test
    @DisplayName("批次2：商品域 4 表已登记")
    void batch2ProductDomainRegistered() {
        assertTrue(ConstantDataScope.MERCHANT_SCOPED_TABLES.containsAll(java.util.Arrays.asList(
                "pt_product_base", "pt_product_item", "pt_product_index", "pt_product_comment")));
    }

    @Test
    @DisplayName("批次3：长尾 12 表已登记；pay_store_settlement 条件启用暂不登记；名单合计 20 表")
    void batch3LongTailRegistered() {
        assertTrue(ConstantDataScope.MERCHANT_SCOPED_TABLES.containsAll(java.util.Arrays.asList(
                "pay_consume_record", "pay_consume_deposit", "pay_consume_trade", "pay_consume_withdraw",
                "marketing_activity_base", "marketing_activity_item",
                "sys_material_base", "sys_material_gallery",
                "invoicing_stock_bill", "invoicing_stock_bill_item", "invoicing_warehouse_item",
                "shop_user_voucher")));
        // 本地库不存在该表（仅 sql/upgrade/V0_002__b2b2c_phase1.sql 建表），与 p1_multi_merchant.sql
        // 条件启用块同口径：表落地（含 merchant_id 回填）前不得登记，否则拦截器对该表 SQL 注入 1054
        assertFalse(ConstantDataScope.MERCHANT_SCOPED_TABLES.contains("pay_store_settlement"),
                "pay_store_settlement 表未落地前不得登记");
        // admin_user_admin 虽在 T1 加列 22 表之列，但登录/鉴权查询先于商家上下文执行且平台需跨商家
        // 管理账号，不得登记（其 merchant_id 列仅供登录链路 LoginServiceImpl 读取）
        assertFalse(ConstantDataScope.MERCHANT_SCOPED_TABLES.contains("admin_user_admin"),
                "admin_user_admin 不得进入 merchant 名单");
        assertEquals(20, ConstantDataScope.MERCHANT_SCOPED_TABLES.size(), "三批次合计应恰为 20 表");
    }
}
