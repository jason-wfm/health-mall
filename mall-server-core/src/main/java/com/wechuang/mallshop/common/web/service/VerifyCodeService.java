package com.wechuang.mallshop.common.web.service;

public interface VerifyCodeService {
    boolean checkVerifyCode(String verifyKey, String verifyCode);

    boolean setVerifyCode(String verifyKey, String verifyCode);

    String getVerifyCode(String verifyKey);
}
