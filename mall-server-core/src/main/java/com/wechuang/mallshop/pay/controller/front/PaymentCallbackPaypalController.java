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
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.pojo.dto.ErrorTypeEnum;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.IJPayUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.pay.model.entity.ConsumeCombine;
import com.wechuang.mallshop.pay.model.entity.ConsumeDeposit;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.pay.model.vo.PayPalVo;
import com.wechuang.mallshop.pay.repository.ConsumeCombineRepository;
import com.wechuang.mallshop.pay.repository.ConsumeTradeRepository;
import com.wechuang.mallshop.pay.service.TradeTypeService;
import com.wechuang.mallshop.pay.service.impl.ConsumeDepositServiceImpl;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.entity.CurrencyBase;
import com.wechuang.mallshop.sys.repository.CurrencyBaseRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

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
public class PaymentCallbackPaypalController extends BaseController {
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

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private CurrencyBaseRepository currencyBaseRepository;
    private static final Logger logger = LoggerFactory.getLogger(PaymentCallbackPaypalController.class);

    //https://github.com/Javen205/IJPay/blob/dev/IJPay-Demo-SpringBoot/src/main/java/com/ijpay/demo/controller/paypal/PayPalController.java
    @RequestMapping(value = "/paypalReturn")
    @ResponseBody
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) {
        PayPalVo payPalVo = configBaseService.getPayPalVo();

        PayPalApiConfig config = payPalVo.getConfig();

        try {
            String id = "";
            JSONObject captureObject;
            String token = request.getParameter("token");
            String payerId = request.getParameter("PayerID");
            logger.info("token:" + token);
            logger.info("payerId:" + payerId);

            // todo 校验及数据
            boolean verifyResult = false;

            //调用queryOrder确认订单信息
            config.setSandBox(true); //沙盒模式

            try {
                IJPayHttpResponse res = PayPalApi.queryOrder(config, token);
                logger.info(res.toString());
                if (res.getStatus() == 200) {
                    String querryStr = res.getBody();
                    JSONObject querryObject = JSONUtil.parseObj(querryStr);
                    id = querryObject.getStr("id");

                    if (!CheckUtil.isNotEmpty(id)) {
                        return "querry orderId failure";
                    }

                } else {
                    return "querry failure";
                }
            } catch (Exception e) {
                return "send querry failure";
            }

            //调用captureOrder
            String captureStr = "";
            String payStataus = "";
            try {
                IJPayHttpResponse captureResponse = PayPalApi.captureOrder(config, id, "");
                logger.info(captureResponse.toString());
                if (captureResponse.getStatus() == 200 || captureResponse.getStatus() == 201) {
                    captureStr = captureResponse.getBody();
                    captureObject = JSONUtil.parseObj(captureStr);
                    payStataus = captureObject.getStr("status");
                    if (!payStataus.equals("COMPLETED")) {
                        //支付未完成
                        return "payStataus failure";
                    } else {
                        verifyResult = true;
                    }

                } else {
                    return "captureOrder failure";
                }
            } catch (Exception e) {
                return "send captureOrder failure";
            }

            if (verifyResult) {
                // TODO 请在这里加上商户的业务逻辑程序代码 异步通知可能出现订单重复通知 需要做去重处理
                ConsumeDeposit consumeDeposit = new ConsumeDeposit();
                JSONArray purchaseUnits = captureObject.getJSONArray("purchase_units");

                JSONObject purchase = (JSONObject) purchaseUnits.get(0);
                JSONObject payments = (JSONObject) purchase.get("payments");
                JSONArray captures = payments.getJSONArray("captures");
                JSONObject capture = (JSONObject) captures.get(0);
                String outTradeNo = Convert.toStr(capture.get("invoice_id"));
                JSONObject amountObj = (JSONObject) capture.get("amount");
                JSONObject buyer = (JSONObject) captureObject.get("payer");

                ConsumeCombine consumeCombine = combineRepository.get(outTradeNo);

                //订单编号
                String orderId = consumeCombine != null ? consumeCombine.getOrderIds() : outTradeNo;

                BigDecimal amount = Convert.toBigDecimal(amountObj.get("value"));
                String buyer_id = Convert.toStr(buyer.get("payer_id"));

                consumeDeposit.setDepositNo(outTradeNo);
                consumeDeposit.setDepositTradeNo(Convert.toStr(capture.get("id")));
                consumeDeposit.setOrderId(orderId);

                consumeDeposit.setDepositSubject(orderId);
                consumeDeposit.setDepositQuantity(Convert.toInt(captureObject.getStr("quantity"), 1));
                Date notifyTime = Convert.toDate(capture.get("update_time"));
                consumeDeposit.setDepositNotifyTime(Convert.toStr(notifyTime.getTime()));

                //处理币种
                String currency_code = Convert.toStr(amountObj.get("currency_code"));
                QueryWrapper<CurrencyBase> currencyQueryWrapper = new QueryWrapper<>();
                currencyQueryWrapper.eq("currency_symbol_right", currency_code);
                CurrencyBase currencyBase = currencyBaseRepository.findOne(currencyQueryWrapper);

                if (CheckUtil.isNotEmpty(currencyBase.getCurrencyId())) {
                    consumeDeposit.setCurrencyId(currencyBase.getCurrencyId());
                    consumeDeposit.setCurrencySymbolLeft(currencyBase.getCurrencySymbolLeft());
                }

                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    consumeDeposit.setDepositTotalFee(amount);
                    consumeDeposit.setDepositPrice(amount);
                }

                consumeDeposit.setDepositBuyerId(buyer_id);
                consumeDeposit.setDepositTime(notifyTime.getTime());
                consumeDeposit.setDepositPaymentType(StateCode.PAYMENT_TYPE_ONLINE);
                consumeDeposit.setDepositExtraParam(captureStr);
                consumeDeposit.setPaymentChannelId(StateCode.PAYMENT_CHANNEL_PAYPAL);
                consumeDeposit.setDepositTradeStatus(payStataus);

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

                String redirectUrl;
                if (UserAgentUtil.parse(request.getHeader("user-agent")).isMobile()) {
                    redirectUrl = ConstantConfig.URL_H5 + "/member/order/detail?init_pay_flag=1&on=" + orderId;
                } else {
                    redirectUrl = ConstantConfig.URL_PC + "/user/order/detail?init_pay_flag=1&order_id=" + orderId;
                }

                try {
                    response.sendRedirect(redirectUrl);
                } catch (IOException e) {
                    LogUtil.error(ErrorTypeEnum.ERR_NOT_DEFINITION.getValue(), e);
                    throw new BusinessException(__("支付跳转失败"));
                }
            } else {
                return "verifyResult failure";
            }
        } catch (Exception e) {
            LogUtil.error(ConstantLog.PAY, e);
            return "paypal failure";
        }

        return "paypal failure";
    }

    @RequestMapping(value = "/paypalCancel")
    @ResponseBody
    public String cancelUrl(HttpServletRequest request, HttpServletResponse response) {
        String readData = IJPayUtil.readData(request);
        System.out.println(readData);
        return readData;
    }
}

