package com.wechuang.mallshop.sys.service;

import com.wechuang.mallshop.common.pojo.dto.SmsDto;
import com.wechuang.mallshop.common.pojo.dto.UploadDto;

/**
 * 第三方服务
 */
public interface ThirdService {

    void send(SmsDto smsDto);

    String upload(UploadDto uploadDto);

}
