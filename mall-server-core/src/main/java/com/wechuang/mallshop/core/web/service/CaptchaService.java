package com.wechuang.mallshop.core.web.service;

import cn.hutool.captcha.LineCaptcha;

/**
 * 验证码服务（自研重写，对齐 core-3.0.27908）
 *
 * @since 3.1.0-healthmall
 */
public interface CaptchaService {

    /**
     * 生成图形验证码（验证码的持久化由调用方负责）
     */
    LineCaptcha image(String key);
}
