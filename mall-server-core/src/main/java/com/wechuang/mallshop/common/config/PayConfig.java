package com.wechuang.mallshop.common.config;

import com.alipay.api.AlipayApiException;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.pay.model.vo.AliPayVo;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.*;

/**
 * 支付配置类
 */
@Configuration
@Slf4j
public class PayConfig {

    @Autowired
    private ConfigBaseService configBaseService;

    /**
     * 支付宝配置
     *
     * @return
     * @throws AlipayApiException
     */
    // [healthmall-ext] 本地环境未配置支付宝密钥，nacos 监听器会强制预实例化本 bean，
    // 因此配置缺失时返回占位配置而非抛异常，避免阻塞启动（支付功能本地不可用）
    @Lazy
    @Bean
    public AliPayApiConfig aliPayApiConfig() throws AlipayApiException {
        AliPayVo aliPayVo;
        try {
            aliPayVo = configBaseService.getAliPayVo();
        } catch (Exception e) {
            LogUtil.error(ConstantLog.PAY, e);
            return buildPlaceholderConfig();
        }

        String aliPayCertPath = null;
        String appCertPath = null;
        String aliPayRootCertPath = null;
        try {
            aliPayCertPath = streamToString(new FileInputStream(aliPayVo.getAliPayCertPath()));
            appCertPath = streamToString(new FileInputStream(aliPayVo.getAppCertPath()));
            aliPayRootCertPath = streamToString(new FileInputStream(aliPayVo.getAliPayRootCertPath()));
        } catch (FileNotFoundException e) {
            LogUtil.error(ConstantLog.PAY, e);
            return buildPlaceholderConfig();
        }

        if (isBlank(aliPayVo.getAppId()) || isBlank(aliPayVo.getPrivateKey())
                || isBlank(aliPayCertPath) || isBlank(appCertPath) || isBlank(aliPayRootCertPath)) {
            log.warn("[PayConfig] 支付宝配置不完整（appId/privateKey/证书为空），使用占位配置启动，支付功能不可用");
            return buildPlaceholderConfig();
        }

        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliPayVo.getAppId())
                .setAliPayPublicKey(aliPayVo.getPublicKey())

                // 配置密钥相对路径启用
                .setAliPayCertContent(aliPayCertPath)
                .setAppCertContent(appCertPath)
                .setAliPayRootCertContent(aliPayRootCertPath)
                // 配置密钥相对路径启用

                .setCharset("UTF-8")
                .setPrivateKey(aliPayVo.getPrivateKey())
                .setServiceUrl(aliPayVo.getServerUrl())
                .setSignType("RSA2")
                .buildByCertContent(); // 配置密钥相对路径启用
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
        return aliPayApiConfig;
    }

    private AliPayApiConfig buildPlaceholderConfig() {
        return AliPayApiConfig.builder()
                .setAppId("local-placeholder")
                .setPrivateKey("local-placeholder")
                .setServiceUrl("https://openapi.alipay.com/gateway.do")
                .setCharset("UTF-8")
                .setSignType("RSA2")
                .build();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        //new一个StringBuffer用于字符串拼接
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            //当输入流内容读取完毕时
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            //记得关闭流数据 节约内存消耗
            is.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            log.error("输入流内容读取异常！" + e.getMessage(), e);
        }
        return "";
    }

}
