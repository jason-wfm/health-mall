package com.wechuang.mallshop.core.web.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.wechuang.mallshop.core.web.service.CaptchaService;
import org.springframework.stereotype.Service;

/**
 * 验证码服务实现（自研重写；生成后由控制器将 code 写入 VerifyCodeService，
 * 与 CaptchaController.index 的调用契约一致）
 *
 * @since 3.1.0-healthmall
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public LineCaptcha image(String key) {
        return CaptchaUtil.createLineCaptcha(200, 100, 4, 30);
    }
}
