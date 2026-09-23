package com.wechuang.mallshop.core.web.service;

import com.wechuang.mallshop.core.web.model.ReleaseDto;
import com.wechuang.mallshop.core.web.model.SmsDto;

import java.util.Map;

/**
 * 云服务接口（自研重写，方法签名对齐 core-3.0.27908 javap 核验）
 * 本地化决策（见 docs/core重写方案.md §6）：
 * - 短信发送已本地化：请将系统配置 sms_type 切换为本地通道（1-阿里 2-腾讯 3-华为）
 * - 授权/模板/安装包分发/IM 等官方云能力不再回拨，返回空数据
 *
 * @since 3.1.0-healthmall
 */
public interface CloundService {

    boolean send(SmsDto smsDto) throws Exception;

    Map listSmsRecords(SmsDto smsDto, Integer page, Integer rows) throws Exception;

    Map getModuleTpl(Integer userId, String appKey) throws Exception;

    Map getDistrict(Integer userId, String appKey) throws Exception;

    String getMpApp(ReleaseDto releaseDto) throws Exception;

    String getLicence(Integer userId, String appKey) throws Exception;

    void initService(Map map) throws Exception;

    String getMpWeixin(ReleaseDto releaseDto) throws Exception;

    Map listImOnline(Integer userId, String appKey, String puids) throws Exception;

    String getPc(ReleaseDto releaseDto) throws Exception;

    String getWeb(ReleaseDto releaseDto) throws Exception;
}
