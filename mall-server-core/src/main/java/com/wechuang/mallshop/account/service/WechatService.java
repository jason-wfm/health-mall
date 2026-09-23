package com.wechuang.mallshop.account.service;

import com.wechuang.mallshop.account.model.entity.UserBindConnect;
import com.wechuang.mallshop.account.model.res.LoginRes;
import com.wechuang.mallshop.common.web.ContextUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

/**
 * <p>
 * 微信操作服务类
 * </p>
 *
 * @author Xinze
 * @since 2021-04-14
 */
public interface WechatService {

    boolean checkSignature(String timestamp, String nonce, String signature);

    Map<String, String> parseRequest(InputStream is);

    void redirectToWxCode(HttpServletRequest request, HttpServletResponse response);

    LoginRes callbackMp(HttpServletResponse response, Integer activity_id, String code);

    LoginRes checkAppLogin(String code);

    LoginRes jsCode2Session(String code, String encryptedData, String iv, String userInfo, Integer activityId, Integer sourceUserId);

    LoginRes getUserPhoneNumber(String code, ContextUser user);

    Map wxConfig(String url);

    String getMpAccessToken(boolean useCacheFlag);

    String getMpToken();

    String getXcxAccessToken(boolean useCacheFlag);

    Map getOpenIdByCode(String code, ContextUser user);

    UserBindConnect getVxMiniAppUserBindConnect(String code, String encryptedData, String iv, String user_info);

    boolean callbackMessage();

    String getQrCode();

    void callbackPc(String code, HttpServletResponse response);

}
