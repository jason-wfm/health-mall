package com.wechuang.mallshop.sys.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.wechuang.mallshop.account.service.UserLevelService;
import com.wechuang.mallshop.common.config.ConfigProperties;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MqMessageService;
import com.wechuang.mallshop.sys.service.PageBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 系统参数设置表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "系统参数设置表")
@RestController
@RequestMapping("/front/sys/config")
public class ConfigController extends BaseController {
    private static final Set<String> FRONT_GET_CONFIGS_ALLOW_KEYS = new HashSet<>(Arrays.asList(
            "joinin_investment_direction",
            "joinin_standard",
            "joinin_condition",
            "joinin_cooperation_details",
            "joinin_expenses_details",
            "account_login_bg"
    ));

    @Autowired
    private ConfigBaseService configBaseService;

    @Resource
    private ConfigProperties configProperties;

    @Resource
    private UserLevelService userLevelService;

    @Autowired
    private PageBaseService pageBaseService;

    @Autowired
    private MqMessageService mqMessageService;

    @Operation(summary = "站点配置信息", description = "站点配置信息")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonRes<Map<String, Object>> info(@RequestParam(name = "source_lang", required = false) String sourceLang) {
        Map<String, Object> res = configBaseService.getSiteInfo(sourceLang);

        return success(res);
    }

    @Operation(summary = "获取公钥", description = "站点配置信息")
    @RequestMapping(value = "/publicKey", method = RequestMethod.GET)
    public CommonRes<Map<String, Object>> getPublicKey() {
        Map<String, Object> res = new HashMap<>();

        res.put("public_key", configBaseService.getConfig("public_key", configProperties.getPublicKey()));

        return success(res);
    }

    @Operation(summary = "读取移动端语言包数据", description = "读取移动端语言包数据")
    @RequestMapping(value = "/listTranslateLang", method = RequestMethod.GET)
    public CommonRes<?> listTranslateLang() {
        return success(configBaseService.listTranslateLang());
    }

    @Operation(summary = "根据提货码获取二维码", description = "根据提货码获取二维码")
    @RequestMapping(value = "/getQrcode", method = RequestMethod.GET)
    public void getQrcode(HttpServletResponse response,
                          @RequestParam(name = "code") String code,
                          @RequestParam(name = "w", defaultValue = "450") Integer width,
                          @RequestParam(name = "h", defaultValue = "450") Integer high) {

        response.setContentType("image/png");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        BufferedImage image = QrCodeUtil.generate(code, width, high);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            LogUtil.error("根据提货码获取二维码信息异常！" + e.getMessage(), e);
        } finally {
            try {
                assert os != null;
                os.close();
            } catch (IOException e) {
                LogUtil.error("根据提货码获取二维码异常！" + e.getMessage(), e);
            }
        }
    }

    @Operation(summary = "加载广告导航", description = "加载广告导航")
    @RequestMapping(value = "/guide", method = RequestMethod.GET)
    public CommonRes<?> guide() {
        String advertisementImage = configBaseService.getConfig("advertisement_image");
        Integer advertisementOpen = configBaseService.getConfig("advertisement_open", 0);
        String logo = configBaseService.getConfig("site_logo");
        List<String> advertisementImages = Convert.toList(String.class, advertisementImage);


        Map data = new HashMap();
        data.put("items", advertisementImages);
        data.put("advertisement_open", advertisementOpen);
        data.put("logo", logo);

        return success(data);
    }


    @Operation(summary = "站点帮助", description = "站点帮助")
    @RequestMapping(value = "/getPcHelp", method = RequestMethod.GET)
    public CommonRes<Map<String, Object>> getPcHelp() {
        String keys = "page_pc_help";

        String pagePcHelp = configBaseService.getConfig(keys);
        Map<String, Object> res = new HashMap<>();
        res.put("page_pc_help", pagePcHelp);

        return success(res);
    }

    @Operation(summary = "获取配置信息", description = "获取配置信息")
    @RequestMapping(value = "/getConfigs", method = RequestMethod.GET)
    public CommonRes<List<ConfigBase>> getConfigs(@RequestParam(name = "config_keys") String config_keys) {
        List<String> keys = Convert.toList(String.class, config_keys);
        keys.replaceAll(key -> key == null ? "" : key.trim());
        keys.removeIf(String::isEmpty);

        if (keys.isEmpty()) {
            throw new BusinessException("配置键不能为空！");
        }

        for (String key : keys) {
            if (!FRONT_GET_CONFIGS_ALLOW_KEYS.contains(key)) {
                throw new BusinessException("包含不允许读取的配置项！");
            }
        }

        return success(configBaseService.gets(keys));
    }
}

