package com.wechuang.mallshop.sys.controller.manage;

import com.wechuang.mallshop.common.config.ConfigProperties;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.RSAUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.ReleaseDto;
import com.wechuang.mallshop.core.web.service.CloundService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 打包发布代码
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "打包发布代码")
@RestController
@RequestMapping("/manage/sys/release")
public class ReleaseController extends BaseController {
    @Autowired
    private ConfigBaseService configBaseService;


    @Resource
    private ConfigProperties configProperties;

    @Autowired
    private CloundService cloundService;

    @PreAuthorize("hasAuthority('/manage/sys/release/download')")
    @Operation(summary = "打包发布代码", description = "打包发布代码")
    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public CommonRes<?> download(@RequestParam("release_type") String releaseType, @RequestParam("url_api") String urlApi, @RequestParam("primary_color") String primaryColor) {
        ReleaseDto releaseDto = new ReleaseDto();
        String configBaseUserId = configBaseService.getConfig("service_user_id", "");
        String configBaseAppKey = configBaseService.getConfig("service_app_key", "");

        releaseDto.setServiceUserId(configBaseUserId);
        releaseDto.setServiceAppKey(configBaseAppKey);

        releaseDto.setVersion("1.0.2");
        releaseDto.setUrl(ConstantConfig.URL_BASE);
        releaseDto.setUrl(urlApi);
        releaseDto.setPrimaryColor(primaryColor);
        releaseDto.setAppName(configBaseService.getConfig("site_name"));


        try {
            String downloadUrl = "";
            if (releaseType.equals("mp")) {
                String appId = configBaseService.getConfig("wechat_xcx_app_id");
                releaseDto.setAppId(appId);
                downloadUrl = cloundService.getMpWeixin(releaseDto);

            } else {
                downloadUrl = cloundService.getWeb(releaseDto);
            }

            return success(downloadUrl, downloadUrl);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }


    @Operation(summary = "授权信息", description = "授权信息")
    @RequestMapping(value = "/getLicence", method = RequestMethod.GET)
    public CommonRes<Map> getLicence() throws Exception {
        String licence = configBaseService.getLk();
        String licencePublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5Fa6zAE5sJ9y1qzjhGAFWggKbMAS82xNG8wKglG0k6XojKBTw/7evSwC0aEgYU/BkIPzxNb7j6Oap5iZ43YgFgLI1dGalZJnvmLTmRK4+MqkKI6LlQQyCTZtSDkPNr62jYzSya+pPt0hgBHgk2x6YAns83SYmZf+7NT3qB+uxIgVIJTcO6m+SX3MyQU6cRKlt46+A9GwYiPx6davGxiX4TeeS5/sWiV1+yBb1xovNPjGK9d6N/3ObvSPtNXLnFn5jtwT1UanZPdZMR+oYIlltR9QGE3jnaTxlYTUhkY63GMek94dWbJTBQqpaA6t221iCwh8uawX4sbm4ZoRTy8SRwIDAQAB";

        String s = RSAUtil.decryptByPublicKey(licence, licencePublicKey);
        Map res = new HashMap();
        res.put("licence_str", s);
        res.put("is_authorized", true);

        return success(res);
    }
}

