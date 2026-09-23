package com.wechuang.mallshop.common.utils.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.SignUtil;
import com.wechuang.mallshop.common.utils.api.dto.UserCouponRollbackDto;
import com.wechuang.mallshop.common.utils.api.dto.UserCouponValidationDto;
import com.wechuang.mallshop.common.utils.api.dto.UserCouponVerifyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

public class CouponUtil {

    private static Logger logger = LoggerFactory.getLogger(CouponUtil.class);

    /**
     * 卡券验证
     */
    public static String verifyUserCoupon(UserCouponVerifyDto userCouponVerifyDto) {
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);

        String url = "BsscConstant.URL_COUPON" + "/api/coupon/v1/userCoupon/verifyUserCoupon";
        logger.info("开始远程调用 --- 卡券验证接口: " + url);
        String questStr = JSON.toJSONString(userCouponVerifyDto);
        logger.info("请求参数" + questStr);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("appid", "SignConstant.APPID")
                .header("timestamp", String.valueOf(timestamp))
                .header("sign", sign)
                .body(questStr)
                .execute();
        int status = httpResponse.getStatus();

        if (status == 200) {
            String resultStr = httpResponse.body();
            JSONObject jsonObject = JSONUtil.parseObj(resultStr);
            boolean success = Convert.toBool(jsonObject.get("success"));
            Integer errCode = Convert.toInt(jsonObject.get("errCode"));
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));

                if (jsonArray.isEmpty()) {
                    throw new BusinessException(__("卡券返回结果为空！"));
                }
                Object object = jsonArray.get(0);
                JSONObject couponData = JSONUtil.parseObj(object);

                String expireDateStr = Convert.toStr(couponData.get("expireDate"));
                Date expireDate = DateUtil.parse(expireDateStr);

                if (DateUtil.compare(expireDate, new Date()) < 0) {
                    throw new BusinessException(__("卡券已过期！"));
                }

                Integer couponStatus = Convert.toInt(couponData.get("status"));

                if (couponStatus != 0) {
                    throw new BusinessException(__("卡券不是未使用状态！"));
                }

                return Convert.toStr(couponData.get("sn"));
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("可以使用的卡券校验请求失败！"));
        }
    }

    /**
     * 卡券核销
     */
    public static boolean validateUserCoupon(UserCouponValidationDto userCouponValidationDto) {
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);

        String url = "BsscConstant.URL_COUPON" + "/api/coupon/v1/userCoupon/validateUserCoupon";
        logger.info("开始远程调用 --- 卡券核销接口: " + url);
        String questStr = JSON.toJSONString(userCouponValidationDto);
        logger.info("请求参数" + questStr);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("appid", "SignConstant.APPID")
                .header("timestamp", String.valueOf(timestamp))
                .header("sign", sign)
                .body(questStr)
                .execute();
        int status = httpResponse.getStatus();

        if (status == 200) {
            String resultStr = httpResponse.body();
            JSONObject jsonObject = JSONUtil.parseObj(resultStr);
            boolean success = Convert.toBool(jsonObject.get("success"));
            Integer errCode = Convert.toInt(jsonObject.get("errCode"));
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));

                if (jsonArray.isEmpty()) {
                    throw new BusinessException(__("卡券返回结果为空！"));
                }

                return true;
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("卡券核销请求失败！"));
        }
    }


    /**
     * 卡券返还
     */
    public static boolean rollbackUserCoupon(UserCouponRollbackDto userCouponRollbackDto) {
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);

        String url = "BsscConstant.URL_COUPON" + "/api/coupon/v1/userCoupon/rollbackUserCoupon";
        logger.info("开始远程调用 --- 卡券返还接口: " + url);
        String questStr = JSON.toJSONString(userCouponRollbackDto);
        logger.info("请求参数" + questStr);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .header("appid", "SignConstant.APPID")
                .header("timestamp", String.valueOf(timestamp))
                .header("sign", sign)
                .body(questStr)
                .execute();
        int status = httpResponse.getStatus();

        if (status == 200) {
            String resultStr = httpResponse.body();
            JSONObject jsonObject = JSONUtil.parseObj(resultStr);
            boolean success = Convert.toBool(jsonObject.get("success"));
            Integer errCode = Convert.toInt(jsonObject.get("errCode"));
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONArray jsonArray = JSONUtil.parseArray(jsonObject.get("data"));

                if (jsonArray.isEmpty()) {
                    throw new BusinessException(__("卡券返回结果为空！"));
                }

                return true;
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("卡券返还请求失败！"));
        }
    }


}
