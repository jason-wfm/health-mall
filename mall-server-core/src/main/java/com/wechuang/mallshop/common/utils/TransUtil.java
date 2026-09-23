package com.wechuang.mallshop.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.wechuang.mallshop.common.utils.baidu.trans.TransApi;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TransUtil {

    @Resource
    private ConfigBaseService configBaseService;

    private static ConfigBaseService staticConfigBaseService;

    @PostConstruct
    public void init() {
        TransUtil.staticConfigBaseService = configBaseService;
    }

    // from = auto
    public static String translate(String query, String from, String to) {
        String baiduTranslateAppId = staticConfigBaseService.getConfig("baidu_translate_app_id", "");
        String baiduTranslateAppKey = staticConfigBaseService.getConfig("baidu_translate_app_key", "");

        TransApi api = new TransApi(baiduTranslateAppId, baiduTranslateAppKey);

        String transResult = api.getTransResult(query, "auto", to);
        if (CheckUtil.isNotEmpty(transResult)) {
            Map mapRes = JSONUtil.parseObject(transResult, Map.class);
            if (mapRes != null) {
                List<Map> transList = (List<Map>) mapRes.get("trans_result");
                if (CollUtil.isNotEmpty(transList)) {
                    Map map = transList.get(0);

                    return Convert.toStr(map.get("dst"));
                }
            }
        }

        return null;
    }
}
