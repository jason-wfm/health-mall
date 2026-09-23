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
package com.wechuang.mallshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.PayKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import com.ijpay.wxpay.model.v3.RefundAmount;
import com.ijpay.wxpay.model.v3.RefundModel;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.pay.model.entity.*;
import com.wechuang.mallshop.pay.model.vo.WxPayV3Vo;
import com.wechuang.mallshop.pay.repository.*;
import com.wechuang.mallshop.pay.service.ConsumeReturnService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.*;
import com.wechuang.mallshop.trade.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Service
@Slf4j
public class ConsumeReturnServiceImpl implements ConsumeReturnService {
    @Autowired
    private ConsumeCombineRepository consumeCombineRepository;

    @Autowired
    private ConsumeTradeRepository consumeTradeRepository;

    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private OrderDataRepository orderDataRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderReturnItemRepository orderReturnItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ConsumeDepositRepository consumeDepositRepository;

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private OrderReturnReasonRepository orderReturnReasonRepository;

    @Autowired
    private CreditOrderRepository creditOrderRepository;

    @Autowired
    private CreditInfoRepository creditInfoRepository;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    /**
     * 执行退款操作
     *
     * @param orderReturns
     * @return
     */
    @Override
    public boolean doRefund(List<OrderReturn> orderReturns) {

        List<String> paidReturnIds = new ArrayList<>();

        // 原路退回标记
        boolean orderRefundFlag = configBaseService.getConfig("order_refund_flag", false);
        List<String> orderIds = orderReturns.stream().map(OrderReturn::getOrderId).distinct().collect(Collectors.toList());
        List<Integer> userIds = orderReturns.stream().map(OrderReturn::getBuyerUserId).distinct().collect(Collectors.toList());
        List<String> returnIds = orderReturns.stream().map(OrderReturn::getReturnId).distinct().collect(Collectors.toList());

        List<OrderData> orderDataList = orderDataRepository.gets(orderIds);
        List<UserResource> userResourceList = userResourceRepository.gets(userIds);
        List<OrderInfo> orderInfoList = orderInfoRepository.gets(orderIds);
        List<OrderBase> orderBaseList = orderBaseRepository.gets(orderIds);
        if (orderDataList == null) {
            orderDataList = Collections.emptyList();
        }
        if (userResourceList == null) {
            userResourceList = Collections.emptyList();
        }
        if (orderInfoList == null) {
            orderInfoList = Collections.emptyList();
        }
        if (orderBaseList == null) {
            orderBaseList = Collections.emptyList();
        }
        Map<String, OrderBase> orderBaseMap = orderBaseList.stream().collect(Collectors.toMap(OrderBase::getOrderId, orderBase -> orderBase));

        QueryWrapper<OrderReturnItem> returnItemQueryWrapper = new QueryWrapper<>();
        returnItemQueryWrapper.in("return_id", returnIds);
        List<OrderReturnItem> orderReturnItems = orderReturnItemRepository.find(returnItemQueryWrapper);
        if (orderReturnItems == null) {
            orderReturnItems = Collections.emptyList();
        }
        List<UserInfo> userInfoList = userInfoRepository.gets(userIds);
        if (userInfoList == null) {
            userInfoList = Collections.emptyList();
        }

        List<Long> orderItemIds = orderReturnItems.stream().map(OrderReturnItem::getOrderItemId).distinct().collect(Collectors.toList());
        List<OrderItem> orderItemList = orderItemRepository.gets(orderItemIds);
        if (orderItemList == null) {
            orderItemList = Collections.emptyList();
        }

        Date curDate = new Date();
        DateTime ymdDate = DateUtil.parse(DateUtil.format(curDate, "yyyy-MM-dd"));

        // 积分抵扣，暂时忽略，不涉及此处支付。
        // 按照次序，依次支付。
        for (OrderReturn orderReturn : orderReturns) {
            Integer refundUserId = null;

            Integer userId = orderReturn.getBuyerUserId();
            if (CheckUtil.isEmpty(userId)) {
                throw new BusinessException(__("买家信息有误"));
            }

            Integer storeId = orderReturn.getStoreId();
            String orderId = orderReturn.getOrderId();
            OrderInfo orderInfo = orderInfoList.stream()
                    .filter(info -> info != null && Objects.equals(info.getOrderId(), orderId))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(__("订单主表信息不存在")));

            String userNickname = "";
            UserResource userResource = new UserResource();

            //公司付款用户
            if (CheckUtil.isNotEmpty(orderInfo.getPayUserId())) {
                refundUserId = orderInfo.getPayUserId();
                UserInfo userInfo = userInfoRepository.get(refundUserId);

                if (userInfo != null) {
                    userNickname = userInfo.getUserNickname();
                }

                userResource = userResourceRepository.get(refundUserId);
            } else {
                refundUserId = userId;
                userResource = userResourceList.stream()
                        .filter(resource -> resource != null && resource.getUserId() != null && resource.getUserId().equals(userId))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(__("用户资源信息不存在")));
                UserInfo userInfo = userInfoList.stream()
                        .filter(info -> info != null && info.getUserId() != null && info.getUserId().equals(userId))
                        .findFirst()
                        .orElse(null);
                userNickname = userInfo != null ? userInfo.getUserNickname() : "";
            }

            // 判断是否需要退佣金
            BigDecimal returnCommisionFee = BigDecimal.ZERO;

            // 不是退运费
            Integer returnIsShippingFee = orderReturn.getReturnIsShippingFee();
            if (CheckUtil.isEmpty(returnIsShippingFee)) {
                Float withdrawReceivedDay = Convert.toFloat(configBaseService.getConfig("withdraw_received_day"));
                if (withdrawReceivedDay == 0) withdrawReceivedDay = 7f;

                if (withdrawReceivedDay >= 0) {
                    Integer orderStateId = orderInfo.getOrderStateId();
                    Integer orderIsPaid = orderInfo.getOrderIsPaid();
                    Long updateTime = orderInfo.getUpdateTime();

                    int second = NumberUtil.mul(withdrawReceivedDay, 60, 60, 24).intValue();
                    long withdrawTime = DateUtil.offsetSecond(new Date(), -second).getTime();

                    // 未到可结算时间可退佣金（避免 Integer/Long 为 null 时 equals/unbox NPE）
                    if (Objects.equals(orderStateId, StateCode.ORDER_STATE_FINISH)
                        && Objects.equals(orderIsPaid, StateCode.ORDER_PAID_STATE_YES)
                        && updateTime != null && updateTime < withdrawTime) {
                    } else {
                        returnCommisionFee = orderReturn.getReturnCommisionFee();
                    }
                }
            }

            BigDecimal waitingRefundAmount = orderReturn.getReturnRefundAmount(); // 需要退款金额
            if (CheckUtil.isNotEmpty(waitingRefundAmount)) {
                OrderData orderData = orderDataList.stream()
                        .filter(data -> data != null && Objects.equals(data.getOrderId(), orderId))
                        .findFirst()
                        .orElseThrow(() -> new BusinessException(__("订单扩展数据不存在")));

                BigDecimal orderCredit = orderData.getTradePaymentCredit();
                BigDecimal orderRefundAgreeCredit = BigDecimal.ZERO;

                BigDecimal buyerUserMoney = waitingRefundAmount;
                BigDecimal buyerUserPoints = BigDecimal.ZERO;
                BigDecimal buyerUserCredit = BigDecimal.ZERO;

                BigDecimal sellerUserMoney = waitingRefundAmount.negate();
                String returnId = orderReturn.getReturnId();

                //如果此订单是信用支付，并且已还款，则退款到余额
                CreditOrder creditOrder = creditOrderRepository.get(orderId);
                if (creditOrder != null) {
                    CreditInfo creditInfo = creditInfoRepository.get(creditOrder.getCreditId());
                    if (creditInfo != null && creditInfo.getCreditState() != null && creditInfo.getCreditState().intValue() == 2) {
                        orderCredit = BigDecimal.ZERO;
                    }
                }

                // 写入流水
                ConsumeRecord buyerConsumeRecord = new ConsumeRecord();
                buyerConsumeRecord.setOrderId(returnId);
                buyerConsumeRecord.setUserId(refundUserId);
                buyerConsumeRecord.setStoreId(orderReturn.getBuyerStoreId());
                buyerConsumeRecord.setUserNickname(userNickname);
                buyerConsumeRecord.setRecordDate(ymdDate);
                buyerConsumeRecord.setRecordYear(DateUtil.year(ymdDate));
                buyerConsumeRecord.setRecordMonth(DateUtil.month(ymdDate) + 1);
                buyerConsumeRecord.setRecordDay(DateUtil.dayOfMonth(ymdDate));
                buyerConsumeRecord.setRecordTitle(__("退款单:") + returnId);
                buyerConsumeRecord.setRecordTime(curDate.getTime());
                buyerConsumeRecord.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);

                // 增加流水
                buyerConsumeRecord.setRecordMoney(waitingRefundAmount);
                buyerConsumeRecord.setTradeTypeId(StateCode.TRADE_TYPE_REFUND_GATHERING);

                // 卖家流水记录
                ConsumeRecord sellerConsumeRecord = new ConsumeRecord();
                BeanUtils.copyProperties(buyerConsumeRecord, sellerConsumeRecord);

                Integer sellerId = userAdminService.getSellerUserId(storeId);

                UserInfo userInfo = userInfoRepository.get(sellerId);

                if (userInfo == null) {
                    throw new BusinessException(__("卖家详细信息不存在!"));
                }

                sellerConsumeRecord.setUserId(sellerId);
                sellerConsumeRecord.setUserNickname(userInfo.getUserNickname());
                sellerConsumeRecord.setStoreId(storeId);
                sellerConsumeRecord.setRecordMoney(NumberUtil.add(waitingRefundAmount.negate(), returnCommisionFee));
                sellerConsumeRecord.setRecordCommissionFee(returnCommisionFee.negate());
                sellerConsumeRecord.setTradeTypeId(StateCode.TRADE_TYPE_REFUND_PAY);

                orderData.setOrderRefundAgreeAmount(NumberUtil.add(waitingRefundAmount, orderData.getOrderRefundAgreeAmount()));

                // 读取退款单项目
                List<OrderReturnItem> orderReturnItemList = orderReturnItems.stream().filter(orderReturnItem -> orderReturnItem.getReturnId().equals(returnId)).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(orderItemList)) {

                    List<OrderItem> orderItems = new ArrayList<>();

                    for (OrderReturnItem orderReturnItem : orderReturnItemList) {
                        Long orderItemId = orderReturnItem.getOrderItemId();
                        Optional<OrderItem> orderItemOpl = orderItemList.stream().filter(orderItem -> orderItem.getOrderItemId().equals(orderItemId)).findFirst();
                        if (orderItemOpl.isPresent()) {
                            OrderItem orderItem = orderItemOpl.get();
                            BigDecimal returnItemSubtotal = orderReturnItem.getReturnItemSubtotal();
                            Integer returnItemNum = orderReturnItem.getReturnItemNum();
                            BigDecimal orderItemReturnAgreeAmount = orderItem.getOrderItemReturnAgreeAmount();
                            Integer orderItemReturnAgreeNum = Optional.ofNullable(orderItem.getOrderItemReturnAgreeNum()).orElse(0);

                            orderItem.setOrderItemReturnAgreeAmount(NumberUtil.add(orderItemReturnAgreeAmount, returnItemSubtotal));
                            orderItem.setOrderItemReturnAgreeNum(orderItemReturnAgreeNum + returnItemNum);

                            // 未结算才发放用金
                            if (CheckUtil.isNotEmpty(returnCommisionFee)) {
                                BigDecimal returnItemCommisionFee = orderReturnItem.getReturnItemCommisionFee();
                                BigDecimal returnItemCommisionFeeRefund = Optional.ofNullable(orderItem.getOrderItemCommissionFeeRefund()).orElse(BigDecimal.ZERO);
                                orderItem.setOrderItemCommissionFeeRefund(NumberUtil.add(returnItemCommisionFee, returnItemCommisionFeeRefund));
                            }
                            orderItems.add(orderItem);
                        }
                    }

                    if (CollUtil.isNotEmpty(orderItems)) {
                        if (!orderItemRepository.edit(orderItems)) {
                            throw new BusinessException(__("修改订单商品数据失败"));
                        }
                    }
                }

                // 买家数据
                if (!consumeRecordRepository.saveOrUpdate(buyerConsumeRecord)) {
                    throw new BusinessException(__("增加买家流水数据失败"));
                }

                //信用支付
                if (orderCredit.compareTo(BigDecimal.ZERO) > 0 && orderCredit.compareTo(orderRefundAgreeCredit) > 0) {
                    BigDecimal refundCredit = NumberUtil.round(NumberUtil.min(NumberUtil.sub(orderCredit, orderRefundAgreeCredit)), 2);
                    buyerUserMoney = NumberUtil.round(NumberUtil.sub(buyerUserMoney, refundCredit), 2);

                    buyerUserCredit = refundCredit;
                    orderData.setOrderRefundAgreeCredit(NumberUtil.add(orderRefundAgreeCredit, refundCredit));
                }

                // 操作退款数据
                boolean flag = doRefundOrder(orderRefundFlag, refundUserId, storeId, userResource, orderId, buyerUserMoney, buyerUserPoints, buyerUserCredit, returnId, returnCommisionFee);

                if (!flag) return true;

                if (!orderDataRepository.edit(orderData)) {
                    throw new BusinessException(__("修改详细信息失败"));
                }

                // 修改订单状态
                if (buyerUserMoney != null) {
                    // 流水记录
                    if (!consumeRecordRepository.saveOrUpdate(sellerConsumeRecord)) {
                        throw new BusinessException(__("写入卖家信息失败"));
                    }

                    /*sellerUserMoney = NumberUtil.add(buyerUserMoney.negate(), returnCommisionFee);
                    // todo ? get(商家用户编号)
                    UserResource resource = userResourceRepository.get(0);
                    resource.setUserMoney(NumberUtil.add(userResource.getUserMoney(), sellerUserMoney));
                    if (!userResourceRepository.edit(userResource)) {
                        throw new BusinessException(__("卖家用户退款失败"));
                    }*/
                }
                paidReturnIds.add(returnId);
            }
        }

        if (CollUtil.isNotEmpty(paidReturnIds)) {
            // 远程服务器订单更改放入
            // 本地服务器订单更改
            if (!setReturnPaidYes(paidReturnIds)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }

        return true;
    }


    /**
     * 修改为退款已支付状态
     *
     * @param returnIds
     * @return
     */
    @Override
    public boolean setReturnPaidYes(List<String> returnIds) {
        if (CollUtil.isEmpty(returnIds)) return false;

        OrderReturn orderReturn = new OrderReturn();
        orderReturn.setReturnIsPaid(true);

        QueryWrapper<OrderReturn> returnQueryWrapper = new QueryWrapper<>();
        returnQueryWrapper.in("return_id", returnIds);
        returnQueryWrapper.in("return_is_paid", false);
        if (!orderReturnRepository.edit(orderReturn, returnQueryWrapper)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        return true;
    }

    /**
     * 操作退款数据
     *
     * @param orderRefundFlag 退款标志
     * @param userId          用户编号
     * @param storeId         店铺编号
     * @param userResource    用户资源数据
     * @param orderId         订单编号
     * @param buyerUserMoney  买家用户余额
     * @param buyerUserPoints 买家用户积分
     * @param returnId        退款编号
     */
    private boolean doRefundOrder(boolean orderRefundFlag, Integer userId, Integer storeId, UserResource userResource, String orderId, BigDecimal buyerUserMoney, BigDecimal buyerUserPoints, BigDecimal buyerUserCredit, String returnId, BigDecimal returnCommisionFee) {
        if (orderRefundFlag) {
            // 读取在线支付信息，如果无在线支付信息，则余额支付，否则在线支付【联合支付】判断
            QueryWrapper<ConsumeDeposit> depositQueryWrapper = new QueryWrapper<>();
            CheckUtil.handleFindInSet(Arrays.asList(orderId), "order_id", depositQueryWrapper);

            ConsumeDeposit consumeDeposit = consumeDepositRepository.findOne(depositQueryWrapper);
            if (consumeDeposit != null) {
                Integer paymentChannelId = consumeDeposit.getPaymentChannelId();
                BigDecimal depositTotalFee = consumeDeposit.getDepositTotalFee();
                String channelCode = configBaseService.getPaymentChannelCode(paymentChannelId);

                // 微信，支付宝支付
                if (Arrays.asList("alipay", "wxpay").contains(channelCode)) {
                    BigDecimal dMoney = NumberUtil.round(NumberUtil.sub(buyerUserMoney, depositTotalFee), 2);
                    if (dMoney.compareTo(BigDecimal.ZERO) > 0) {
                        UserResource updateUserResource = new UserResource();
                        updateUserResource.setUserId(userId);
                        updateUserResource.setUserMoney(NumberUtil.add(userResource.getUserMoney(), dMoney));
                        if (!userResourceRepository.edit(updateUserResource)) {
                            throw new BusinessException(__("用户退款失败"));
                        }
                    }
                    String depositTradeNo = consumeDeposit.getDepositTradeNo();
                    OrderReturn aReturn = new OrderReturn();
                    aReturn.setReturnId(returnId);
                    aReturn.setReturnChannelCode(channelCode);
                    aReturn.setDepositTradeNo(depositTradeNo);
                    aReturn.setPaymentChannelId(paymentChannelId);
                    aReturn.setTradePaymentAmount(depositTotalFee);
                    if (!orderReturnRepository.edit(aReturn)) {
                        throw new BusinessException(__("修改退单信息失败"));
                    }
                    // 执行第三方接口退款流程
                    doOnLineRefund(returnId);
                    return true;
                }
            }

            if (buyerUserMoney.compareTo(BigDecimal.ZERO) > 0) {
                //处理买家余额
                UserResource updateBuyerMoney = new UserResource();
                updateBuyerMoney.setUserId(userId);
                updateBuyerMoney.setUserMoney(NumberUtil.add(userResource.getUserMoney(), buyerUserMoney));
                if (!userResourceRepository.edit(updateBuyerMoney)) {
                    throw new BusinessException(__("用户退款失败！"));
                }

                //卖家余额 -- 单商户及多商户判断
                UserResource sellerResource = new UserResource();
                if (storeId.equals(0)) {
                    sellerResource = userResourceRepository.get(StateCode.ADMIN_PLANTFORM_USERID);
                } else {
                    // 根据店铺ID查询 管理员用户ID
                    if (storeId != null && storeId > 0) {
                        Integer sellerId = userAdminService.getUserIdByStoreId(storeId);
                        sellerResource = userResourceRepository.get(sellerId);
                    }
                }

                UserResource updateSellerMoney = new UserResource();
                updateSellerMoney.setUserId(sellerResource.getUserId());
                updateSellerMoney.setUserMoney(NumberUtil.sub(sellerResource.getUserMoney(), NumberUtil.sub(buyerUserMoney, returnCommisionFee)));
                if (!userResourceRepository.edit(updateSellerMoney)) {
                    throw new BusinessException(__("商家退款失败！"));
                }
            }


            if (buyerUserCredit.compareTo(BigDecimal.ZERO) > 0) {
                UserResource updateCredit = new UserResource();
                updateCredit.setUserId(userId);
                updateCredit.setUserCredit(NumberUtil.add(userResource.getUserCredit(), buyerUserCredit));
                updateCredit.setUserCreditUsed(NumberUtil.sub(userResource.getUserCreditUsed(), buyerUserCredit));
                if (!userResourceRepository.edit(updateCredit)) {
                    throw new BusinessException(__("用户退信用金额失败！"));
                }

                CreditOrder creditOrder = creditOrderRepository.get(orderId);
                if (creditOrder != null) {
                    CreditInfo creditInfo = creditInfoRepository.get(creditOrder.getCreditId());
                    creditInfo.setCreditReturnAmount(NumberUtil.add(creditInfo.getCreditReturnAmount(), buyerUserCredit));
                    creditInfo.setCreditSurplusAmount(NumberUtil.max(NumberUtil.sub(creditInfo.getCreditSurplusAmount(), buyerUserCredit), BigDecimal.ZERO));

                    if (!creditInfoRepository.save(creditInfo)) {
                        throw new BusinessException(__("更新账单数据失败！"));
                    }

                    CreditOrder returnCreditOrder = new CreditOrder();
                    returnCreditOrder.setOrderId(returnId);
                    returnCreditOrder.setCreditId(creditInfo.getCreditId());
                    returnCreditOrder.setCreditOrderUsed(buyerUserCredit);
                    returnCreditOrder.setOrderTitle(creditOrder.getOrderTitle());
                    returnCreditOrder.setBuyerUserId(userId);
                    returnCreditOrder.setSubsiteId(creditOrder.getSubsiteId());
                    returnCreditOrder.setCreditEndDate(creditOrder.getCreditEndDate());
                    returnCreditOrder.setCreditOrderTime(new Date());
                    returnCreditOrder.setCreditType(2);
                    returnCreditOrder.setCreditState(creditOrder.getCreditState());
                    if (!creditOrderRepository.add(returnCreditOrder)) {
                        throw new BusinessException(__("添加信用退单信息失败"));
                    }
                }
            }

            OrderReturn orderReturn = new OrderReturn();
            orderReturn.setReturnId(returnId);
            orderReturn.setReturnChannelFlag(1);
            if (!orderReturnRepository.edit(orderReturn)) {
                throw new BusinessException(__("修改退单信息失败"));
            }
        }
        return true;
    }

    /**
     * 执行线上支付退款
     *
     * @param returnId
     */
    public void doOnLineRefund(String returnId) {
        OrderReturn orderReturn = orderReturnRepository.get(returnId);

        if (orderReturn == null) {
            throw new BusinessException(String.format(__("退单号: %s 退款退货信息不存在！"), returnId));
        }
        String returnChannelCode = orderReturn.getReturnChannelCode();
        String depositTradeNo = orderReturn.getDepositTradeNo();
        BigDecimal tradePaymentAmount = orderReturn.getTradePaymentAmount();

        OrderReturn shopOrderReturn = new OrderReturn();
        shopOrderReturn.setReturnChannelFlag(1);
        shopOrderReturn.setOrderId(orderReturn.getOrderId());

        try {
            if ("alipay".equals(returnChannelCode)) {
                doAliPayRefund(returnId, depositTradeNo, tradePaymentAmount, shopOrderReturn);
            } else if ("wxpay".equals(returnChannelCode)) {
                doWxPayRefund(depositTradeNo, tradePaymentAmount, shopOrderReturn);
            } else if ("paypal".equals(returnChannelCode)) {
                doPayPalRefund(returnId, depositTradeNo, tradePaymentAmount, shopOrderReturn);
            }

            // 更新退款状态
            updateRefundOrderReturn(shopOrderReturn);
        } catch (Exception e) {
            LogUtil.error(ConstantLog.NETWORK, e);
        }
    }

    /**
     * 支付宝退款
     *
     * @param returnId
     * @param depositTradeNo
     * @param tradePaymentAmount
     * @param shopOrderReturn
     * @throws AlipayApiException
     */
    private void doAliPayRefund(String returnId, String depositTradeNo, BigDecimal tradePaymentAmount, OrderReturn shopOrderReturn) throws AlipayApiException {
        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("");
        if (StrUtil.isNotEmpty(depositTradeNo)) {
            refundModel.setTradeNo(depositTradeNo);
        }
        refundModel.setRefundAmount(NumberUtil.toStr(tradePaymentAmount));
        try {
            String body = AliPayApi.tradeRefundToResponse(refundModel).getBody();
            JSONObject jsonObject = JSONObject.parseObject(body);
            JSONObject alipay_trade_refund_response = (JSONObject) jsonObject.get("alipay_trade_refund_response");
            String msg = Convert.toStr(alipay_trade_refund_response.get("msg"));
            String code = Convert.toStr(alipay_trade_refund_response.get("code"));
            if (!"Success".equals(msg) || !"10000".equals(code)) {
                String errMsg = String.format("return_id : %s 原路退回出错", returnId);
                LogUtil.error(ConstantLog.NETWORK, errMsg);

                throw new BusinessException(errMsg);
            }
            DateTime successTime = DateUtil.parse(Convert.toStr(alipay_trade_refund_response.get("gmt_refund_pay")), "yyyy-MM-dd HH:mm:ss");
            String outTradeNo = Convert.toStr(alipay_trade_refund_response.get("out_trade_no"));
            String tradeNo = Convert.toStr(alipay_trade_refund_response.get("trade_no"));

            shopOrderReturn.setDepositTradeNo(tradeNo);
            shopOrderReturn.setReturnChannelTransId(outTradeNo);
            shopOrderReturn.setReturnChannelTime(successTime);
        } catch (AlipayApiException e) {
            LogUtil.error(ConstantLog.NETWORK, e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 微信退款
     *
     * @param depositTradeNo
     * @param tradePaymentAmount
     * @param shopOrderReturn
     */
    private void doWxPayRefund(String depositTradeNo, BigDecimal tradePaymentAmount, OrderReturn shopOrderReturn) {
        if (StrUtil.isEmpty(depositTradeNo)) {
            depositTradeNo = "";
        }
        // 设置订单金额 单位为分且最小为1
        int amount = NumberUtil.mul(NumberUtil.round(tradePaymentAmount, 2), 100).intValue();

        WxPayV3Vo wxPayV3Vo = configBaseService.getWxPayV3Vo();
        String outRefundNo = PayKit.generateStr();
        String returnBuyerMessage = shopOrderReturn.getReturnBuyerMessage();

        RefundModel refundModel = new RefundModel()
                .setOut_refund_no(outRefundNo)
                .setTransaction_id(depositTradeNo)
                .setReason(StrUtil.isNotBlank(returnBuyerMessage) ? returnBuyerMessage : __("系统退款"))
                .setNotify_url(wxPayV3Vo.getNotifyUrl())
                .setAmount(new RefundAmount().setRefund(amount).setTotal(amount).setCurrency("CNY"))
                .setGoods_detail(null);

        try {
            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.POST,
                    WxDomainEnum.CHINA.toString(),
                    BasePayApiEnum.REFUND.toString(),
                    wxPayV3Vo.getMchId(),
                    getSerialNumber(),
                    wxPayV3Vo.getPublicKeyId(),
                    wxPayV3Vo.getKeyPath(),
                    JSON.toJSONString(refundModel)
            );

            /*boolean verifySignature = WxPayKit.verifySignature(response, wxPayV3Vo.getPlatformCertPath());*/

            // 根据微信支付公钥验证签名结果
            boolean verifySignature = WxPayKit.verifyPublicKeySignature(response, wxPayV3Vo.getPublicKeyPath());

            log.info("verifySignature: {}", verifySignature);
            log.info(__("退款响应 {}"), response.getBody());
            if (!verifySignature) {
                LogUtil.error(ConstantLog.NETWORK, __("退款验签失败！"));
                throw new BusinessException(__("退款验签失败！"));
            }

            String responseBody = response.getBody();
            JSONObject refundObj = JSON.parseObject(responseBody);
            String status = refundObj.getString("status");
            if (StrUtil.isNotEmpty(status) && "PROCESSING".equals(status)) {
                shopOrderReturn.setDepositTradeNo(depositTradeNo);
                String refundId = refundObj.getString("refund_id");
                shopOrderReturn.setReturnChannelTransId(refundId);
                shopOrderReturn.setReturnChannelTime(new Date());
            } else {
                String errMsg = refundObj.getString("message");
                throw new BusinessException(errMsg);
            }

        } catch (Exception e) {
            LogUtil.error(ConstantLog.NETWORK, e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * Paypal退款
     *
     * @param returnId
     * @param depositTradeNo
     * @param tradePaymentAmount
     * @param shopOrderReturn
     * @throws AlipayApiException
     */
    private void doPayPalRefund(String returnId, String depositTradeNo, BigDecimal tradePaymentAmount, OrderReturn shopOrderReturn) throws AlipayApiException {

        AlipayTradeRefundModel refundModel = new AlipayTradeRefundModel();
        refundModel.setOutTradeNo("");
        if (StrUtil.isNotEmpty(depositTradeNo)) {
            refundModel.setTradeNo(depositTradeNo);
        }
        refundModel.setRefundAmount(NumberUtil.toStr(tradePaymentAmount));
        try {
            PayPalApiConfig config = configBaseService.getPayPalVo().getConfig();
            System.out.println("id>" + shopOrderReturn.getOrderId());

            Map<String, Object> map = new HashMap<>();
            map.put("invoice_id", returnId);
            map.put("note_to_payer", "test product");

            Map<String, String> amount = new HashMap<>();
            amount.put("value", "1.00");
            amount.put("currency_code", "USD");

            map.put("amount", amount);

            String data = JSONUtil.toJsonStr(map);
            log.info("refund data：" + data);
            IJPayHttpResponse response = PayPalApi.refund(config, shopOrderReturn.getOrderId(), data);
            log.info(response.toString());
            if (response.getStatus() == 201) {
                String responseBody = response.getBody();

                //todo 确认返回数据
                JSONObject refundObj = JSON.parseObject(responseBody);

                shopOrderReturn.setDepositTradeNo(depositTradeNo);
                String refundId = refundObj.getString("refund_id");
                shopOrderReturn.setReturnChannelTransId(refundId);
                shopOrderReturn.setReturnChannelTime(new Date());

            } else {
                String errMsg = String.format("return_id : %s 原路退回出错, 接口请求错误码为 %d", returnId, response.getStatus());
                LogUtil.error(ConstantLog.NETWORK, errMsg);

                throw new BusinessException(errMsg);
            }
        } catch (Exception e) {
            LogUtil.error(ConstantLog.NETWORK, e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 更新退款订单状态
     *
     * @param orderReturn
     */
    public void updateRefundOrderReturn(OrderReturn orderReturn) {
        QueryWrapper<OrderReturn> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderReturn.getOrderId())
                .eq("deposit_trade_no", orderReturn.getDepositTradeNo());
        OrderReturn dbOrderReturn = orderReturnRepository.findOne(queryWrapper);
        if (dbOrderReturn != null && CheckUtil.isEmpty(dbOrderReturn.getReturnChannelFlag())) {
            log.info(String.format("更新退款订单状态！returnId : %s", dbOrderReturn.getReturnId()));
            dbOrderReturn.setReturnChannelTransId(orderReturn.getReturnChannelTransId());
            dbOrderReturn.setReturnChannelTime(orderReturn.getReturnChannelTime());
            dbOrderReturn.setReturnChannelFlag(orderReturn.getReturnChannelFlag());
            if (!orderReturnRepository.edit(dbOrderReturn)) {
                LogUtil.error(ConstantLog.NETWORK, String.format("returnId : %s 退款失败！", orderReturn.getReturnId()));
            }
        }
    }

    /**
     * 获取证书序列号
     */
    private String getSerialNumber() {
        //证书序列号
        String serialNo = configBaseService.getConfig("wechat_pay_serial_no");

        if (StrUtil.isEmpty(serialNo)) {
            //这个是证书文件，后续要调整成读取证书文件的服务器存放地址  微信支付证书
            String cert = configBaseService.getConfig("wechat_pay_apiclient_cert");

            if (cert != null) {
                // 获取证书序列号
                X509Certificate certificate = PayKit.getCertificate(cert);
                if (certificate != null) {
                    serialNo = certificate.getSerialNumber().toString(16).toUpperCase();
                }
            }
        }

        return serialNo;
    }

}
