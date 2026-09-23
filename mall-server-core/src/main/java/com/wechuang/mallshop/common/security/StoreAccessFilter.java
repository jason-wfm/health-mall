package com.wechuang.mallshop.common.security;

import com.wechuang.mallshop.common.config.ConfigProperties;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.RequestUtil;
import com.wechuang.mallshop.shop.service.StoreAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import java.io.IOException;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * [healthmall-ext] C端门店定位过滤器（spec §5.3）
 * 1. /front/** 请求先解析 Host/appid 定位门店，写入请求属性供 ContextUtil 使用
 * 2. strict 模式：已定位门店时，query 传 store_id 越界直接 403；未定位（聚合页）不拦
 */
@Component
@Order(3) // 位于 JwtAuthenticationFilter(@Order(2)) 之后
public class StoreAccessFilter extends OncePerRequestFilter {

    @Resource
    private ConfigProperties configProperties;
    @Resource
    private StoreAccessService storeAccessService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path == null || !path.startsWith("/front");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // lenient 也解析：解析结果写请求属性 STORE_ACCESS_STORE_ID（由 StoreAccessServiceImpl 落），
        // T6 ContextUtil front "属性优先"分支依赖本过滤器触发
        Integer resolved = storeAccessService.resolveStoreId();

        if (Boolean.TRUE.equals(configProperties.getStoreAccessStrict()) && resolved != null && resolved > 0) {
            Integer queryStoreId = RequestUtil.getParameter("store_id", 0);
            if (queryStoreId != null && queryStoreId > 0 && !queryStoreId.equals(resolved)) {
                // 调用形态同 JwtAuthenticationFilter.java:100 现有 5 参用法
                CommonUtil.responseError(response, 250, __("无权访问该门店"), 0, "");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
