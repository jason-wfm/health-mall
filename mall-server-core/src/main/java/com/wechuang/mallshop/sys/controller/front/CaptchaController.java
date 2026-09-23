package com.wechuang.mallshop.sys.controller.front;

import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.EmailDto;
import com.wechuang.mallshop.common.pojo.dto.SmsDto;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.EmailUtil;
import com.wechuang.mallshop.common.utils.ThirdUtil;
import com.wechuang.mallshop.common.utils.phone.PhoneModel;
import com.wechuang.mallshop.common.utils.phone.PhoneNumberUtils;
import com.wechuang.mallshop.common.web.service.VerifyCodeService;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.service.CaptchaService;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.entity.MessageTemplate;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MessageTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户基本信息表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Tag(name = "用户基本信息表")
@RestController
@RequestMapping("/front/sys/captcha")
public class CaptchaController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private VerifyCodeService verifyCodeService;

    @Autowired
    private MessageTemplateService messageTemplateService;
    @Autowired
    private ConfigBaseService configBaseService;

    @Operation(summary = "生成图像验证码", description = "返回的是图片二进制内容")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void index(@RequestParam(name = "verify_key") String verifyKey, HttpServletResponse response) throws IOException {
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");

        LineCaptcha image = captchaService.image(verifyKey);

        verifyCodeService.setVerifyCode(verifyKey, image.getCode());

        /*
        //把图形验证码凭证放入cookie中
        String tokenId = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("imgCodeToken",tokenId);
        cookie.setPath("/");
        response.addCookie(cookie);
         */

        //输出浏览器
        OutputStream out = response.getOutputStream();
        image.write(out);
        out.flush();
        out.close();
    }

    @Operation(summary = "发送手机验证码", description = "发送手机验证码")
    @RequestMapping(value = "/mobile", method = RequestMethod.GET)
    public CommonRes<?> sendMobileVerifyCode(@RequestParam(name = "mobile") String verifyKey) {
        String verifyCode = RandomUtil.randomNumbers(4);

        if (!PhoneNumberUtils.isValidNumber(verifyKey)) {
            throw new BusinessException(__("手机号码不准确！"));
        }

        PhoneModel phoneModelWithCountry = PhoneNumberUtils.getPhoneModelWithCountry(verifyKey);
        MessageTemplate messageTemplate = messageTemplateService.get("verifycode");

        SmsDto smsDto = new SmsDto();
        smsDto.setSmsType(configBaseService.getConfig("sms_type", 0));

        smsDto.setMobile(Convert.toStr(phoneModelWithCountry.getNationalNumber()));
        smsDto.setTemplateCode(messageTemplate.getMessageTplId());
        smsDto.setTengxunTemplateId(Convert.toInt(messageTemplate.getMessageTplId()));

        smsDto.setContent(messageTemplate.getMessageSms());

        Map msgArgs = new HashMap();
        msgArgs.put("yzm", verifyCode);
        msgArgs.put("code", verifyCode); //重复

        msgArgs.put("minutes", 5);
        msgArgs.put("time", 5); //重复兼容
        smsDto.setParamMap(msgArgs); //传入模板对应参数

        try {
            ThirdUtil.send(smsDto);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        verifyCodeService.setVerifyCode(verifyKey, verifyCode);

        return success();
    }

    @Operation(summary = "发送邮件验证码", description = "发送邮件验证码")
    @RequestMapping(value = "/email", method = RequestMethod.GET)
    public CommonRes<?> sendEmailVerifyCode(@RequestParam(name = "email") String email) {
        boolean flag = true;
        String configBaseUserId = configBaseService.getConfig("service_user_id", "");
        String configBaseAppKey = configBaseService.getConfig("service_app_key", "");
        String verifyCode = RandomUtil.randomNumbers(4);

        if (!CheckUtil.isEmail(email)) {
            throw new BusinessException(__("Email不准确！"));
        }

        QueryWrapper<ConfigBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("config_type", "email");

        // 获取emailConfig 对象
        List<ConfigBase> configs = configBaseService.find(queryWrapper);
        Map<String, String> configMap = configs.stream().collect(Collectors.toMap(ConfigBase::getConfigKey, ConfigBase::getConfigValue, (k1, k2) -> k2));
        EmailDto emailDto = Convert.convert(EmailDto.class, configMap);
        emailDto.setEmailToAddress(email);
        emailDto.setSubject(String.format(__("%s 注册验证码"), configBaseService.getConfig("site_name", "MallSuite")));
        emailDto.setContent(String.format(__("您的验证码: [%s] 5分钟内有效"), verifyCode));
        try {
            flag = EmailUtil.send(emailDto);
        } catch (Exception e) {
            throw new BusinessException(__("邮件发送失败！"));
        }

        verifyCodeService.setVerifyCode(email, verifyCode);

        if (flag) {
            return success();
        } else {
            return fail();
        }
    }
}

