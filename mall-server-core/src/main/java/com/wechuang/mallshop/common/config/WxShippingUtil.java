package com.wechuang.mallshop.common.config;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.getui.push.v2.sdk.common.ApiException;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.trade.service.impl.OrderBaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Configuration
public class WxShippingUtil implements WebMvcConfigurer {
    private final static String CHARSET_DEFAULT = "UTF-8";

    private static Logger logger = LoggerFactory.getLogger(OrderBaseServiceImpl.class);

    /**
     * 上传小程序发货
     *
     * @param mchId          商户号
     * @param outTradeNo     商户订单号
     * @param accessToken    accessToken
     * @param openid         买家openid
     * @param logistics_type 物流方式
     * @param delivery_mode  发货模式
     * @param isAllDelivered 分拆发货模式时必填，用于标识分拆发货模式下是否已全部发货完成，只有全部发货完成的情况下才会向用户推送发货完成通知。示例值: true/false
     * @param shippingList   物流信息列表
     * @return
     */
    public static Boolean uploadShippingInfo(String mchId, String outTradeNo, String accessToken, String openid, String transactionId, Integer logistics_type, Integer delivery_mode, Boolean isAllDelivered, List<Map<String, Object>> shippingList) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/wxa/sec/order/upload_shipping_info?access_token=" + accessToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject orderKey = new JSONObject();
        // 订单单号类型，用于确认需要上传详情的订单。枚举值1，使用下单商户号和商户侧单号；枚举值2，使用微信支付单号。
        orderKey.put("order_number_type", 2);
//        orderKey.put("mchid", mchId);
        orderKey.put("transaction_id", transactionId);
//        orderKey.put("out_trade_no", outTradeNo);

        JSONObject payer = new JSONObject();
        payer.put("openid", openid);

        JSONObject signObject = new JSONObject();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String uploadTime = simpleDateFormat.format(System.currentTimeMillis());

        signObject.put("order_key", orderKey);
        // 发货模式，发货模式枚举值：1、UNIFIED_DELIVERY（统一发货）2、SPLIT_DELIVERY（分拆发货） 示例值: 1
        signObject.put("delivery_mode", delivery_mode);
        // 物流模式，发货方式枚举值：1、实体物流配送采用快递公司进行实体物流配送形式 2、同城配送 3、虚拟商品，虚拟商品，例如话费充值，点卡等，无实体配送形式 4、用户自提
        signObject.put("logistics_type", logistics_type);
        signObject.put("shipping_list", shippingList);
        signObject.put("upload_time", uploadTime);
        signObject.put("is_all_delivered", isAllDelivered);
        signObject.put("payer", payer);
        logger.info("发货信息：{}", signObject);

        logger.info("开始远程调用 --- 微信发货接口: " + url);
        String questStr = JSONUtil.toJSONString(signObject);
        HttpResponse httpResponse = HttpRequest.post(url)
                .header("Content-Type", "application/json")
                .body(questStr)
                .timeout(2000)
                .execute();
        int status = httpResponse.getStatus();

        if (status == 200) {
            String resultStr = httpResponse.body();
            logger.info("请求响应结果Str=>>>>> " + resultStr);

            // 使用 Map 解析整个 JSON 字符串
            Map<String, Object> resultMap = JSON.parseObject(resultStr, Map.class);

            logger.info("请求响应结果Map=>>>>> " + resultMap);

            Object codeObj = resultMap.get("errcode");
            if (codeObj instanceof Number && ((Number) codeObj).intValue() == 0) {
                return true;
            } else {
                String desc = resultMap.get("desc") != null ? resultMap.get("desc").toString() : "未知错误";
                throw new ApiException(desc + " - " + url);
            }
        } else {
            throw new ApiException(__("接口请求失败！"));
        }
    }

}
