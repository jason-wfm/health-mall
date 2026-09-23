package com.wechuang.mallshop.common.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wechuang.mallshop.common.config.mybatis.PointTypeHandler;
import com.wechuang.mallshop.common.mybatisplus.SqlLogInterceptor;
import com.wechuang.mallshop.common.utils.ContextUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

/**
 * MybatisPlus配置
 *
 * @author Xinze
 * @since 2018-02-22 11:29:28
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    @Bean
    public SqlLogInterceptor sqlLogInterceptor() {
        return new SqlLogInterceptor();
    }

    @Bean
    public MybatisPlusInterceptor optimisticLockerInnerInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());

        // 3. 防止全表更新/删除插件（生产环境必加）
        BlockAttackInnerInterceptor blockAttackInterceptor =
                new BlockAttackInnerInterceptor();

        interceptor.addInnerInterceptor(blockAttackInterceptor);
        return interceptor;
    }

    @Bean
    public TypeHandlerRegistry typeHandlerRegistry() {
        TypeHandlerRegistry registry = new TypeHandlerRegistry();
        registry.register(Point.class, new PointTypeHandler());
        return registry;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户插件配置
        TenantLineHandler tenantLineHandler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Integer siteId = ContextUtil.getSiteId();
                return new LongValue(siteId);
            }

            @Override
            public String getTenantIdColumn() {
                return "subsite_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                Integer siteId = ContextUtil.getSiteId();

                if (siteId.intValue() == 0) {
                    return true;
                } else {
                    return false || Arrays.asList(
                            "sys_config_type",
                            "sys_config_base",
                            "sys_crontab_base",
                            "sys_district_base",

                            "admin_menu_base",
                            "admin_user_role",
                            "admin_user_admin",

//                            "account_user_base",
//                            "account_user_info",

                            "pay_trade_type",
                            "pt_product_kind",
                            "pt_product_verify",
                            "sys_number_seq",
                            "sys_dictionary",
                            "sys_dictionary_data",
                            // [healthmall-ext] 平台级映射表（Task 6 评审补）：C 端门店解析与商家推导查询必须
                            // 与请求携带的 site_id 参数无关（平台级映射语义），否则推导链路会被任意 site 参数
                            // 打穿/报错；shop_store_access 另因无 subsite_id 列豁免（shop_store_base 有该列，
                            // 仍按平台级语义豁免）
                            "shop_store_access",
                            "shop_store_base"
                    ).contains(tableName);
                }
            }
        };

        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor(tenantLineHandler);
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);

        // 多店铺插件配置 - 店铺
        TenantLineHandler tenantLineHandlerStore = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                Integer storeId = ContextUtil.getStoreId();
                if (storeId == null) {
                    return new LongValue(0);
                } else {
                    return new LongValue(storeId);
                }
            }

            @Override
            public String getTenantIdColumn() {
                return "store_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                Integer storeId = ContextUtil.getStoreId();

                if (storeId == 0) {
                    return true;
                } else {
                    return !Arrays.asList(
                            "shop_store_express_logistics",
                            "shop_store_shipping_address",
                            "shop_store_transport_type",
                            "pt_product_brand",
                            "pt_product_index",
                            "pt_product_spec_item",
                            "pt_product_comment",
                            "pt_product_item",
                            "admin_user_role",
                            "o2o_chain_base",
                            "pt_product_pricing_policy",
                            "marketing_activity_base",
                            "marketing_activity_item",
                            "pay_user_points_history",
                            "pay_consume_record",
                            "pay_consume_deposit",
                            "pay_consume_trade",
                            "sys_material_gallery",
                            "sys_material_base",
                            "pay_consume_withdraw",
                            "trade_order_info",
                            "trade_order_invoice",
                            "trade_order_return",
                            // [b2b2c] 商户工作台数据隔离扩充：以下表均已核验存在 store_id 列（shopsuite-2.0.sql）
                            // 注意：trade_order_data / trade_order_delivery_address / trade_order_logistics 无 store_id 列，
                            // 不可加入，依赖 trade_order_base 先行过滤后按 order_id 主键读取
                            "trade_order_base",
                            "trade_order_item",
                            "shop_user_voucher",
                            "pt_product_base",
                            "invoicing_stock_bill",
                            "invoicing_stock_bill_item",
                            "invoicing_warehouse_item",
                            // [b2b2c] 结算单头表(明细表pay_store_settlement_order无store_id列,经头表归属校验后按settlement_id读取)
                            "pay_store_settlement"
                    ).contains(tableName);
                }
            }
        };

        TenantLineInnerInterceptor tenantLineInnerInterceptorStore = new TenantLineInnerInterceptor(tenantLineHandlerStore);
        interceptor.addInnerInterceptor(tenantLineInnerInterceptorStore);

        // [healthmall-ext] 数据权限插件 - 商家维度（新域表按 merchant_id 隔离，白名单见 ConstantDataScope；
        // 商家管理员身份自动注入 INSERT 填充与 SELECT 过滤，平台/买方/任务上下文不生效）
        TenantLineHandler merchantScopeHandler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(com.wechuang.mallshop.common.datascope.DataScopeContext.getMerchantId());
            }

            @Override
            public String getTenantIdColumn() {
                return "merchant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                Integer merchantId = com.wechuang.mallshop.common.datascope.DataScopeContext.getMerchantId();
                if (merchantId == null) {
                    return true;
                } else {
                    return !com.wechuang.mallshop.common.consts.ConstantDataScope.MERCHANT_SCOPED_TABLES.contains(tableName);
                }
            }
        };
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(merchantScopeHandler));

        // [healthmall-ext] 数据权限插件 - 门店维度（新域表按 chain_id 隔离）
        TenantLineHandler chainScopeHandler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(com.wechuang.mallshop.common.datascope.DataScopeContext.getChainId());
            }

            @Override
            public String getTenantIdColumn() {
                return "chain_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                Integer chainId = com.wechuang.mallshop.common.datascope.DataScopeContext.getChainId();
                if (chainId == null) {
                    return true;
                } else {
                    return !com.wechuang.mallshop.common.consts.ConstantDataScope.CHAIN_SCOPED_TABLES.contains(tableName);
                }
            }
        };
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(chainScopeHandler));

        // 分页插件配置
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);

        return interceptor;
    }
}
