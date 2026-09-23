package com.wechuang.mallshop.account.controller.front;

import com.wechuang.mallshop.account.model.res.LoginRes;
import com.wechuang.mallshop.account.service.WechatService;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Tag(name = "微信操作控制器")
@RestController
@RequestMapping("/front/account/wechat")
public class WechatController extends BaseController {

    @Autowired
    private WechatService wechatService;

    /**
     * <pre>
     * 验证推送过来的消息的正确性
     * </pre>
     *
     * @param signature 消息签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @return the boolean
     */
    private final Logger logger = LoggerFactory.getLogger(WechatController.class);

    @Operation(summary = "验证签名")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String checkSignature(@RequestParam(name = "timestamp") String timestamp,
                                 @RequestParam(name = "signature") String signature,
                                 @RequestParam(name = "nonce") String nonce,
                                 @RequestParam(name = "echostr") String echostr) {
        return wechatService.checkSignature(timestamp, nonce, signature) == true ? echostr : null;
    }

    @Operation(summary = "公众号登录 - 获取code请求")
    @RequestMapping(value = "/redirectToWxCode", method = RequestMethod.GET)
    public void redirectToWxCode(HttpServletRequest request, HttpServletResponse response) {
        wechatService.redirectToWxCode(request, response);
    }

    @Operation(summary = "公众号登录 - 公众号授权回调")
    @RequestMapping(value = "/callbackMp", method = RequestMethod.GET)
    public CommonRes<?> callbackMp(HttpServletResponse response,
                                   @RequestParam(name = "activity_id", required = false) Integer activity_id,
                                   @RequestParam(name = "code") String code) {
        LoginRes loginRes = wechatService.callbackMp(response, activity_id, code);

        return success(loginRes);
    }

    @Operation(summary = "用户登录验证")
    @RequestMapping(value = "/checkAppLogin", method = RequestMethod.GET)
    public CommonRes<?> checkAppLogin(@RequestParam(name = "code") String code) {
        LoginRes checkAppLogin = wechatService.checkAppLogin(code);

        if (checkAppLogin != null) {
            return success(checkAppLogin);
        }

        return fail(__("请先登录"));
    }

    @Operation(summary = "获取微信配置请求")
    @RequestMapping(value = "/wxConfig", method = RequestMethod.GET)
    public CommonRes<?> wxConfig(@RequestParam(name = "href") String url) {
        return success(wechatService.wxConfig(url));
    }

    @Operation(summary = "用户注册")
    @RequestMapping(value = "/jsCode2Session", method = RequestMethod.GET)
    public CommonRes<?> jsCode2Session(@RequestParam(name = "code") String code,
                                       @RequestParam(name = "encryptedData") String encryptedData,
                                       @RequestParam(name = "iv") String iv,
                                       @RequestParam(name = "user_info") String userInfo,
                                       @RequestParam(name = "activity_id", required = false) Integer activityId,
                                       @RequestParam(name = "source_user_id", required = false) Integer sourceUserId) {
        return success(wechatService.jsCode2Session(code, encryptedData, iv, userInfo, activityId, sourceUserId));
    }

    @Operation(summary = "小程序获取手机号")
    @RequestMapping(value = "/getUserPhoneNumber", method = RequestMethod.GET)
    public CommonRes<?> getUserPhoneNumber(@RequestParam(name = "code") String code) {
        return success(wechatService.getUserPhoneNumber(code, ContextUtil.getLoginUser()));
    }

    @Operation(summary = "获取getXcxAccessToken-向外提供")
    @RequestMapping(value = "/getXcxAccessToken", method = RequestMethod.GET)
    public String getXcxAccessToken(@RequestParam(name = "useCacheFlag") boolean useCacheFlag) {
        return wechatService.getXcxAccessToken(useCacheFlag);
    }

    @Operation(summary = "根据code 获取openid")
    @RequestMapping(value = "/getOpenIdByCode", method = RequestMethod.GET)
    public CommonRes<?> getOpenIdByCode(@RequestParam(name = "code") String code) {
        return success(wechatService.getOpenIdByCode(code, ContextUtil.getLoginUser()));
    }

    @Operation(summary = "微信通知消息回调")
    @RequestMapping(value = "/callbackMessage", method = RequestMethod.GET)
    public CommonRes<?> callbackMessage() {
        return success(wechatService.callbackMessage());
    }

    @Operation(summary = "微信网页登录生成二维码")
    @RequestMapping(value = "/getQrCode", method = RequestMethod.GET)
    public CommonRes<?> getQrCode() {
        return success(wechatService.getQrCode());
    }

    @Operation(summary = "二维码登录 - 微信网页回调")
    @RequestMapping(value = "/callbackPc", method = RequestMethod.GET)
    public void callbackPc(String code, HttpServletResponse response) {
        wechatService.callbackPc(code, response);
    }

}
