package com.wechuang.mallshop.common.consts;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * [healthmall-ext] 数据权限（DataScope）常量
 * 新域表统一携带 merchant_id（=店铺 store_id 语义）/ chain_id 隔离列，
 * 并在对应白名单登记后由 MybatisPlusConfig 的拦截器自动注入过滤条件
 */
public class ConstantDataScope {

    /** 平台：全量数据 */
    public static final String SCOPE_PLATFORM = "PLATFORM";
    /** 商家：merchant_id = 当前商家 */
    public static final String SCOPE_MERCHANT = "MERCHANT";
    /** 门店：chain_id = 当前门店 */
    public static final String SCOPE_CHAIN = "CHAIN";
    /** 服务人员：staff_id = 当前人员（Phase 3 /work 通道启用） */
    public static final String SCOPE_STAFF = "STAFF";
    /** 本人：user_id = 当前用户（买方 front） */
    public static final String SCOPE_SELF = "SELF";

    /**
     * 按 merchant_id 自动过滤的表白名单 [healthmall-ext]
     * 前提：表必须有 merchant_id 列（T1 迁移已回填，见 sql/p1_multi_merchant.sql）
     * 激活后由 MybatisPlusConfig merchantScopeHandler 注入：
     * 商家身份（manage role2/role3，或 C 端定位门店推导出商家）SELECT 追加 AND merchant_id=N，INSERT 自动填充列；
     * 平台（role9/8）/买方未定位/任务上下文不加过滤。
     * R7 铁律：shop_store_base 与 shop_store_access 永不入此名单
     * （C 端商家推导经它们查库，入名单即拦截器递归死锁；且它们是平台映射表，属 site 豁免而非商家作用域）
     */
    public static final Set<String> MERCHANT_SCOPED_TABLES = new HashSet<>(Arrays.asList(
            // [healthmall-ext] 多商家 P1 白名单，分批启用（spec §12 灰度：先交易，再商品，再长尾）
            // 批次1：交易域
            "trade_order_base", "trade_order_item", "trade_order_info", "trade_order_return",
            // 批次2：商品域（4 表本地均已核验存在且带 merchant_id 列，Task 1 Step 1 口径）
            "pt_product_base", "pt_product_item", "pt_product_index", "pt_product_comment",
            // 批次3：长尾（12 表均已核验存在且带 merchant_id 列）
            "pay_consume_record", "pay_consume_deposit", "pay_consume_trade", "pay_consume_withdraw",
            "marketing_activity_base", "marketing_activity_item",
            "sys_material_base", "sys_material_gallery",
            "invoicing_stock_bill", "invoicing_stock_bill_item", "invoicing_warehouse_item",
            "shop_user_voucher"
            // 注意：pay_store_settlement 暂不登记——本地 dev 库该表不存在（仅存在于
            // sql/upgrade/V0_002__b2b2c_phase1.sql），与 sql/p1_multi_merchant.sql 的条件启用块同口径：
            // 待该表（连同 merchant_id 回填）落地后再行登记
            // 注意：admin_user_admin 虽在 p1_multi_merchant.sql 加列回填之列，但不入本名单——
            // 登录/鉴权查询先于商家上下文执行，平台需跨商家管理账号，其 merchant_id 列仅供登录链路（LoginServiceImpl）读取
    ));

    /**
     * 按 chain_id 自动过滤的新域表白名单
     */
    public static final Set<String> CHAIN_SCOPED_TABLES = new HashSet<>(Arrays.asList(
            // 示例：Phase 1 登记 "chain_base"
    ));
}
