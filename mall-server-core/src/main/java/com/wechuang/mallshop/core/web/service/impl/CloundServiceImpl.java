package com.wechuang.mallshop.core.web.service.impl;

import com.wechuang.mallshop.core.web.model.ReleaseDto;
import com.wechuang.mallshop.core.web.model.SmsDto;
import com.wechuang.mallshop.core.web.service.CloundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 云服务本地化实现（自研重写，替换原官方云回拨）：
 *
 * 1. send()：官方云短信通道已停用。系统已有本地直发能力——
 *    ThirdUtil 按 sys_config 的 sms_type 路由（1-AliServiceImpl 阿里直发、
 *    2-TencentServiceImpl 腾讯直发、3-HuaweiServiceImpl 华为直发），
 *    运维将 sms_type 从 0（官方云）切换为本地通道即可。
 *    此处对残留的 sms_type=0 调用抛出明确异常，避免短信静默丢失。
 * 2. 模板/安装包分发/IM 在线/短信记录/授权等官方云能力：空实现（决策点 #5）。
 *
 * @since 3.1.0-healthmall
 */
@Service
@Slf4j
public class CloundServiceImpl implements CloundService {

    @Override
    public boolean send(SmsDto smsDto) throws Exception {
        log.warn("官方云短信通道已随 core 自研替换停用，mobile={}，请将系统配置 sms_type 切换为本地通道(1-阿里/2-腾讯/3-华为)",
                smsDto == null ? null : smsDto.getMobile());
        throw new RuntimeException("短信云通道已停用：请将 sms_type 配置切换为本地通道(1-阿里/2-腾讯/3-华为)");
    }

    @Override
    public Map listSmsRecords(SmsDto smsDto, Integer page, Integer rows) throws Exception {
        return new HashMap();
    }

    @Override
    public Map getModuleTpl(Integer userId, String appKey) throws Exception {
        return new HashMap();
    }

    @Override
    public Map getDistrict(Integer userId, String appKey) throws Exception {
        return new HashMap();
    }

    @Override
    public String getMpApp(ReleaseDto releaseDto) throws Exception {
        return "";
    }

    @Override
    public String getLicence(Integer userId, String appKey) throws Exception {
        return "";
    }

    @Override
    public void initService(Map map) throws Exception {
        // 官方云授权初始化已随 core 自研替换移除，无操作
    }

    @Override
    public String getMpWeixin(ReleaseDto releaseDto) throws Exception {
        return "";
    }

    @Override
    public Map listImOnline(Integer userId, String appKey, String puids) throws Exception {
        return new HashMap();
    }

    @Override
    public String getPc(ReleaseDto releaseDto) throws Exception {
        return "";
    }

    @Override
    public String getWeb(ReleaseDto releaseDto) throws Exception {
        return "";
    }
}
