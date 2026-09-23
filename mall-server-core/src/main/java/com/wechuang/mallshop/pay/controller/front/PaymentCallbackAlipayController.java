// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.pay.controller.front;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.utils.IJPayUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.pay.model.entity.ConsumeCombine;
import com.wechuang.mallshop.pay.model.entity.ConsumeDeposit;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.pay.model.vo.AliPayVo;
import com.wechuang.mallshop.pay.repository.ConsumeCombineRepository;
import com.wechuang.mallshop.pay.repository.ConsumeTradeRepository;
import com.wechuang.mallshop.pay.service.TradeTypeService;
import com.wechuang.mallshop.pay.service.impl.ConsumeDepositServiceImpl;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 交易类型表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Tag(name = "交易类型表")
@RestController
@RequestMapping("/front/pay/callback")
public class PaymentCallbackAlipayController extends BaseController {
    @Autowired
    private TradeTypeService tradeTypeService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private ConsumeCombineRepository combineRepository;

    @Autowired
    private ConsumeDepositServiceImpl depositServiceImpl;

    @Autowired
    private ConsumeTradeRepository tradeRepository;

    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackAlipayController.class);

    @RequestMapping(value = "/returnUrl")
    public void returnUrl(HttpServletRequest request, HttpServletResponse response) {
        // 获取支付宝GET过来反馈信息
        Map<String, String> map = IJPayUtil.toMap(request);
        String outTradeNo = map.get("out_trade_no");

        ConsumeCombine consumeCombine = combineRepository.get(outTradeNo);
        //订单编号
        String orderId = consumeCombine != null ? consumeCombine.getOrderIds() : outTradeNo;

        String redirectUrl;
        if (UserAgentUtil.parse(request.getHeader("user-agent")).isMobile()) {
            redirectUrl = ConstantConfig.URL_H5 + "/member/order/detail?init_pay_flag=1&on=" + orderId;
        } else {
            redirectUrl = ConstantConfig.URL_PC + "/user/order/detail?init_pay_flag=1&order_id=" + orderId;
        }
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            LogUtil.error(ErrorTypeEnum.ERR_ALI_PAY.getValue(), e);
            throw new BusinessException(__("支付跳转失败"));
        }
    }

    @Operation(summary = "证书支付返回", description = "证书支付返回")
    @RequestMapping(value = "/returnCertUrl")
    public String certReturnUrl(HttpServletRequest request) {
        AliPayVo aliPayVo = configBaseService.getAliPayVo();
        try {
            // 获取支付宝GET过来反馈信息
            Map<String, String> map = IJPayUtil.toMap(request);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, aliPayVo.getAliPayCertPath(), "UTF-8",
                    "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码
                System.out.println("certReturnUrl 验证成功");

                return "success";
            } else {
                System.out.println("certReturnUrl 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            LogUtil.error(ConstantLog.PAY, e);
            return "failure";
        }
    }


    @RequestMapping(value = "/alipayNotify")
    public String notifyUrl(HttpServletRequest request) {
        AliPayVo aliPayVo = configBaseService.getAliPayVo();
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = IJPayUtil.toMap(request);
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliPayVo.getAliPayCertPath(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理


                ConsumeDeposit consumeDeposit = new ConsumeDeposit();
                JSONObject jsonObject = JSONUtil.parseObj(params);

                //判断是否支付完成回调
                String depositTradeStatus = jsonObject.get("trade_status", String.class);
                if (depositTradeStatus.equals("TRADE_SUCCESS") || depositTradeStatus.equals("TRADE_FINISHED")) {
                    consumeDeposit.setDepositTradeStatus(depositTradeStatus);

                    //订单号
                    String outTradeNo = jsonObject.getStr("out_trade_no");

                    ConsumeCombine consumeCombine = combineRepository.get(outTradeNo);

                    //订单编号
                    String orderId = consumeCombine != null ? consumeCombine.getOrderIds() : outTradeNo;

                    BigDecimal amount = jsonObject.get("total_amount", BigDecimal.class);
                    String buyer_id = jsonObject.get("buyer_id", String.class);

                    consumeDeposit.setDepositNo(outTradeNo);
                    consumeDeposit.setDepositTradeNo(jsonObject.getStr("trade_no"));
                    consumeDeposit.setOrderId(orderId);
                    consumeDeposit.setDepositSubject(jsonObject.getStr("subject"));
                    consumeDeposit.setDepositQuantity(Convert.toInt(jsonObject.getStr("quantity"), 0));
                    Date notifyTime = jsonObject.getDate("notify_time");
                    consumeDeposit.setDepositNotifyTime(Convert.toStr(notifyTime.getTime()));
                    consumeDeposit.setDepositSellerId(jsonObject.getStr("seller_id"));
                    consumeDeposit.setDepositIsTotalFeeAdjust(StrUtil.isNotEmpty(jsonObject.getStr("is_total_fee_adjust")));

                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        consumeDeposit.setDepositTotalFee(amount);
                        consumeDeposit.setDepositPrice(amount);
                    }

                    consumeDeposit.setDepositBuyerId(buyer_id);
                    consumeDeposit.setDepositTime(jsonObject.getDate("gmt_payment").getTime());
                    consumeDeposit.setDepositPaymentType(StateCode.PAYMENT_TYPE_ONLINE);
                    consumeDeposit.setDepositService(jsonObject.getStr("exterface"));
                    consumeDeposit.setDepositSign(jsonObject.getStr("sign"));
                    consumeDeposit.setDepositExtraParam(JSONUtil.toJsonStr(consumeDeposit));
                    consumeDeposit.setPaymentChannelId(StateCode.PAYMENT_CHANNEL_ALIPAY);
                    consumeDeposit.setDepositTradeStatus(jsonObject.getStr("trade_status"));

                    //判断是否可以联合支付
                    Integer storeId = 0;
                    Integer chainId = 0;
                    ConfigBase tradeMode = configBaseService.get("trade_mode");

                    if (tradeMode == null || !"1".equals(tradeMode.getConfigValue())) {
                        QueryWrapper<ConsumeTrade> consumeTradeQueryWrapper = new QueryWrapper<>();
                        consumeTradeQueryWrapper.eq("order_id", outTradeNo);
                        ConsumeTrade consumeTrade = tradeRepository.findOne(consumeTradeQueryWrapper);

                        if (consumeTrade != null) {
                            storeId = consumeTrade.getStoreId();
                            chainId = consumeTrade.getChainId();
                        }
                    }
                    consumeDeposit.setStoreId(storeId);
                    consumeDeposit.setChainId(chainId);

                    depositServiceImpl.processDeposit(consumeDeposit);
                }

                return "success";
            }
            return "failure";
        } catch (AlipayApiException e) {
            LogUtil.error(ConstantLog.PAY, e);
            return "failure";
        }
    }

    @Operation(summary = "证书支付回调", description = "证书支付回调")
    @RequestMapping(value = "/alipayCertNotify")
    public String certNotifyUrl(HttpServletRequest request) {
        AliPayVo aliPayVo = configBaseService.getAliPayVo();
        try {
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = IJPayUtil.toMap(request);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }

            boolean verifyResult = AlipaySignature.rsaCertCheckV1(params, aliPayVo.getAliPayCertPath(), "UTF-8", "RSA2");

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                System.out.println("certNotifyUrl 验证成功succcess");
                return "success";
            } else {
                System.out.println("certNotifyUrl 验证失败");
                // TODO
                return "failure";
            }
        } catch (AlipayApiException e) {
            LogUtil.error(ConstantLog.PAY, e);
            return "failure";
        }
    }
}

