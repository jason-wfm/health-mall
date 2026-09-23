package com.wechuang.mallshop.sys.service.impl;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.pojo.dto.SmsDto;
import com.wechuang.mallshop.common.pojo.dto.UploadDto;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.service.CloundService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.ThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
public class ShopsuiteServiceImpl implements ThirdService {

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private CloundService cloundService;

    @Override
    public void send(SmsDto smsDto) {
        boolean flag = true;
        String configBaseUserId = configBaseService.getConfig("service_user_id", "");
        String configBaseAppKey = configBaseService.getConfig("service_app_key", "");

        com.wechuang.mallshop.core.web.model.SmsDto smsData = new com.wechuang.mallshop.core.web.model.SmsDto();
        smsData.setMobile(smsDto.getMobile());
        smsData.setServiceUserId(configBaseUserId);
        smsData.setServiceAppKey(configBaseAppKey);

        String msgContent = smsDto.getContent();

        for (Map.Entry<String, Object> entry : smsDto.getParamMap().entrySet()) {
            String variable = "[" + entry.getKey() + "]";
            Object value = entry.getValue();
            msgContent = msgContent.replace(variable, value.toString());
        }

        smsData.setContent(msgContent);


        String smsSign = configBaseService.getConfig("sms_sign");
        smsData.setMessageTplSender(smsSign);
        smsData.setTplId(smsDto.getTemplateCode());
        smsData.setTplParas(smsDto.getParamMap());

        try {
            cloundService.send(smsData);
        } catch (Exception e) {
            LogUtil.error(ErrorTypeEnum.ERR_NOT_DEFINITION.getValue(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public String upload(UploadDto uploadDto) {
        return "";
    }
}
