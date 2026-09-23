package com.wechuang.mallshop.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.shop.service.StoreAccessService;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Locale;

import static com.wechuang.mallshop.common.consts.ConstantJwt.TOKEN_EXPIRED_CODE;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Component
@Slf4j
public class ContextUtil {
    @Resource
    private ConfigBaseService configBaseService;
    private static ConfigBaseService staticConfigBaseService;

    @Resource
    private CurrencyBaseRepository currencyBaseRepository;

    private static CurrencyBaseRepository staticCurrencyBaseRepository;

    // [healthmall-ext] 门店 → 商家推导委托（静态注入，与 configBaseService 同型；Task 6 接管 C 端定位语义）
    @Resource
    private com.wechuang.mallshop.shop.service.StoreAccessService storeAccessService;
    private static com.wechuang.mallshop.shop.service.StoreAccessService staticStoreAccessService;

    @PostConstruct
    public void init() {
        ContextUtil.staticConfigBaseService = configBaseService;
        ContextUtil.staticCurrencyBaseRepository = currencyBaseRepository;
        ContextUtil.staticStoreAccessService = storeAccessService;
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static ContextUser getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Object object = authentication.getPrincipal();
                if (object instanceof UserBase) {
                    ContextUser contextUser = BeanUtil.copyProperties(object, ContextUser.class);
                    return contextUser;
                }
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * 获取当前登录的userId
     *
     * @return userId
     */
    public static ContextUser checkLoginUser() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        return loginUser == null ? null : loginUser;
    }

    /**
     * 获取当前登录的userId
     *
     * @return userId
     */
    public static Integer getLoginUserId() {
        ContextUser loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * 获取当前登录的userId
     *
     * @return userId
     */
    public static Integer checkLoginUserId() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(TOKEN_EXPIRED_CODE, __("请重新登录"));
        } else {

        }

        return loginUser.getUserId();
    }

    /**
     * 取得需要翻译的语言
     *
     * @return null 不翻译
     */
    public static String getToLang() {
        if (!staticConfigBaseService.getConfig("auto_translate_enable", false)) {
            return null;
        }

        return getCurrentLang();
    }

    /**
     * 取得当前语言
     *
     * @return null 不翻译
     */
    public static String getCurrentLang() {

        Locale locale = LocaleContextHolder.getLocale();
        if (ObjectUtil.isEmpty(locale)) {
            return null;
        }

        //判断有效后台设置语言包  staticCurrencyBaseRepository
        if (!staticCurrencyBaseRepository.ifEffective(locale.getLanguage() + "-" + locale.getCountry())) {
            return null;
        }

        ContextUser user = ContextUtil.getLoginUser();

        if (locale.equals(Locale.SIMPLIFIED_CHINESE) || (user != null && user.getClientId().equals(1))) {
            return null;
        }

        //locale.toLanguageTag().replace("-", "_");
        String to = locale.getLanguage() + "_" + locale.getCountry();
        return to;
    }

    /**
     * 获取当前登录用户的租户id
     *
     * @return Integer
     */
    public static Integer getSiteId() {
        Integer siteId = 0;

        try {
            HttpServletRequest request = HttpServletUtils.getRequest();
            String requestURI = request.getRequestURI();

            //后台根据 用户
            if (requestURI.startsWith("/manage")) {
                ContextUser loginUser = ContextUtil.getLoginUser();
                // [healthmall-ext] 空/异常身份降级返回 0（与旧降级一致）：鉴权 fail-fast 职责已上移 Web 层
                // ManageIdentityFailFastInterceptor（Task 6 评审 Critical 修复——本工具在 JWT 预认证 SQL 窗口内
                // 会被租户拦截器回调，此处抛异常会击穿 JwtAuthenticationFilter 被吞成 250，导致 manage 全挂）
                if (loginUser != null && loginUser.getRoleId() != null) {
                    //管理员 放行
                    if (loginUser.getRoleId().intValue() == ConstantRole.ROLE_ADMIN) {

                    } else if (loginUser.getRoleId().intValue() == ConstantRole.ROLE_SITE) {
                        siteId = loginUser.getSiteId();
                    } else {

                    }
                }
            } else if (requestURI.startsWith("/front")) {
                // site_id 为地域维度，front 侧有意保留（R6 决策，不做属性优先）
                siteId = RequestUtil.getParameter("site_id", 0);
            } else {
            }
        } catch (Exception e) {
        }

        return siteId;
    }

    /**
     * 获取当前店铺编号 [healthmall-ext] spec §4.2 过滤矩阵
     * manage：商家(2)维持本店（P1 双保险，P2 移除）；门店(3)收窄为本店（越权修复）；平台/租户(9/8)放行；
     * 空身份降级返回 0（鉴权 fail-fast 已上移 Web 层拦截器，本工具须在 JWT 预认证 SQL 窗口内可安全回调）
     * front：Host/appid 解析结果优先（请求属性），query 参数仅兼容期兜底（strict 由 StoreAccessFilter 拦截）
     *
     * @return Integer
     */
    public static Integer getStoreId() {
        Integer storeId = 0;

        try {
            HttpServletRequest request = HttpServletUtils.getRequest();
            String requestURI = request.getRequestURI();

            if (requestURI.startsWith("/manage")) {
                ContextUser loginUser = ContextUtil.getLoginUser();
                // [healthmall-ext] 空/异常身份降级返回 0（与旧降级一致）：鉴权 fail-fast 职责已上移 Web 层
                // ManageIdentityFailFastInterceptor；本工具在 JWT 预认证 SQL 窗口内被租户拦截器回调，不可抛异常
                if (loginUser != null && loginUser.getRoleId() != null
                        && (loginUser.getRoleId().intValue() == ConstantRole.ROLE_SELLER
                        || loginUser.getRoleId().intValue() == ConstantRole.ROLE_CHAIN)) {
                    storeId = loginUser.getStoreId() != null ? loginUser.getStoreId() : 0;
                }
            } else if (requestURI.startsWith("/front")) {
                Object resolved = request.getAttribute(StoreAccessService.ATTR_STORE_ID);
                if (resolved instanceof Integer resolvedId && resolvedId > 0) {
                    storeId = resolvedId;
                } else {
                    // [healthmall-ext] 属性类型漂移防护：非 Integer 不再强转（原 CCE 被兜底 catch 吞掉后
                    // 静默返回 0 全表），落 warn 日志走 query 参数兜底，防 T7 写入侧类型漂移时静默 fail-open
                    if (resolved != null && !(resolved instanceof Integer)) {
                        log.warn("[healthmall-ext] STORE_ACCESS_STORE_ID 属性类型异常（{}），走 query 参数兜底",
                                resolved.getClass().getName());
                    }
                    storeId = RequestUtil.getParameter("store_id", 0);
                }
            }
        } catch (Exception e) {
            // 非 Web 上下文（MQ/定时任务）维持 0 放行，由任务侧自行设置租户上下文（spec §8，P2 落地）
        }

        return storeId;
    }

    /**
     * [healthmall-ext] 门店 → 商家推导（委托 shop 域 StoreAccessService，请求级缓存）
     * 未注入与运行期异常均降级返回 null（=不加商家过滤），以 warn 日志区分两类原因（T5 评审移交项）
     */
    public static Integer getStoreMerchantId(Integer storeId) {
        // 未注入（静态字段 null）：早于 Spring 初始化的静态调用/非 Spring 环境，非运行期故障
        if (staticStoreAccessService == null) {
            log.warn("[healthmall-ext] StoreAccessService 未注入，门店{}的商家推导降级返回 null", storeId);
            return null;
        }
        try {
            return staticStoreAccessService.getMerchantIdByStore(storeId);
        } catch (Exception e) {
            log.warn("[healthmall-ext] 门店{}的商家推导异常，降级返回 null：{}", storeId, e.getMessage());
            return null;
        }
    }


    /**
     * 判断是否管理端后端请求
     *
     * @return Boolean
     */
    public static Boolean isManage() {
        Boolean flag = false;

        try {
            HttpServletRequest request = HttpServletUtils.getRequest();
            String requestURI = request.getRequestURI();

            //后台根据 用户
            if (requestURI.startsWith("/manage")) {
                flag = true;
            } else if (requestURI.startsWith("/front")) {
                flag = false;
            } else {
            }
        } catch (Exception e) {
        }

        return flag;
    }
}
