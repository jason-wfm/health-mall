package com.wechuang.mallshop.common.utils.api;

import cn.hutool.core.convert.Convert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.SignUtil;
import com.wechuang.mallshop.common.utils.api.dto.PointsUseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

public class PointUtil {

    private static Logger logger = LoggerFactory.getLogger(PointUtil.class);

    /**
     * 积分使用
     */
    public static boolean usePoints(PointsUseDto pointsUseCmd) {
        String url = "BsscConstant.URL_POINTS" + "/api/points/v1/usePoints";
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);
        logger.info("开始远程调用 --- 积分使用接口: " + url);
        String questStr = JSON.toJSONString(pointsUseCmd);
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
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONObject pointData = (JSONObject) jsonObject.get("data");

                if (pointData == null) {
                    throw new BusinessException(__("积分使用返回结果为空！"));
                }

                return Convert.toBool(pointData.get("isOk"), false);
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("积分使用请求失败！"));
        }
    }

    /**
     * 积分返还
     */
    public static boolean rollbackPoints(PointsUseDto pointsUseCmd) {
        String url = "BsscConstant.URL_POINTS" + "/api/points/v1/rollbackPoints";
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);
        logger.info("开始远程调用 --- 积分返还接口: " + url);
        String questStr = JSON.toJSONString(pointsUseCmd);
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
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONObject pointData = (JSONObject) jsonObject.get("data");

                if (pointData == null) {
                    throw new BusinessException(__("积分返还结果为空！"));
                }

                return Convert.toBool(pointData.get("isOk"), false);
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("积分返还请求失败！"));
        }
    }

    /**
     * 积分查询
     */
    public static BigDecimal pointsSummary(String oneId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("oneId", oneId);
        String url = "BsscConstant.URL_POINTS" + "/api/points/v1/pointsSummary";
        long timestamp = new Date().getTime() / 1000;
        String sign = SignUtil.generateSign("SignConstant.APPID", "BsscConstant.BASE_SECRET", timestamp);
        logger.info("开始远程调用 --- 积分查询接口: " + url);
        logger.info("请求参数" + paramMap);
        HttpResponse httpResponse = HttpRequest.get(url)
                .header("appid", "SignConstant.APPID")
                .header("timestamp", String.valueOf(timestamp))
                .header("sign", sign)
                .form(paramMap)
                .execute();
        int status = httpResponse.getStatus();

        if (status == 200) {
            String resultStr = httpResponse.body();
            JSONObject jsonObject = JSONUtil.parseObj(resultStr);
            boolean success = Convert.toBool(jsonObject.get("success"));
            String errMessage = Convert.toStr(jsonObject.get("errMessage"));

            if (success) {
                JSONObject pointData = (JSONObject) jsonObject.get("data");

                if (pointData == null) {
                    throw new BusinessException(__("查询积分汇总信息为空！"));
                }

                return Convert.toBigDecimal(pointData.get("totalPoints"));
            } else {
                throw new BusinessException(errMessage + " - " + url);
            }
        } else {
            throw new BusinessException(__("查询积分汇总信息请求失败！"));
        }
    }
}
