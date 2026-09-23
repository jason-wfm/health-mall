package com.wechuang.mallshop.common.utils.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.SignUtil;
import com.wechuang.mallshop.common.utils.api.dto.MessageRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

public class MessageUtil {

    private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    /**
     * 消息中心数据推送
     */
    public static boolean send(MessageRequestDto messageRequest) {
        String url = "BsscConstant.URL_MESSAGE" + "/mall/message/send";
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);
        logger.info("开始远程调用 --- 消息中心数据推送接口: " + url);
        String questStr = JSON.toJSONString(messageRequest);
        logger.info("请求参数" + questStr);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("appid", "SignConstant.APPID")
                .header("timestamp", String.valueOf(timestamp))
                .header("sign", sign)
                .body(questStr)
                .execute();
        int status = httpResponse.getStatus();
        String resultStr = httpResponse.body();

        if (status == 200) {
            JSONObject jsonObject = JSONUtil.parseObj(resultStr);
            boolean success = Convert.toBool(jsonObject.get("success"));
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {

                return true;
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__(url + "消息中心数据推送失败！"));
        }
    }

}
