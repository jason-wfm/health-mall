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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pay.model.entity.*;
import com.wechuang.mallshop.pay.model.output.ProcessPayOutput;
import com.wechuang.mallshop.pay.model.req.ConsumeTradeListReq;
import com.wechuang.mallshop.pay.model.vo.PayMetVo;
import com.wechuang.mallshop.pay.repository.*;
import com.wechuang.mallshop.pay.service.ConsumeTradeService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.OrderData;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.repository.*;
import com.wechuang.mallshop.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 交易订单表-强调唯一订单-充值则先创建充值订单 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Service
public class ConsumeTradeServiceImpl extends BaseServiceImpl<ConsumeTradeRepository, ConsumeTrade, ConsumeTradeListReq> implements ConsumeTradeService {
    @Autowired
    private ConsumeCombineRepository consumeCombineRepository;

    @Autowired
    private ConsumeTradeRepository consumeTradeRepository;

    @Autowired
    private ConsumeRecordRepository consumeRecordRepository;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Autowired
    private OrderService orderService;

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

    /**
     * 订单付款
     *
     * @param ids
     * @param deposit
     * @return
     */
    @Override
    public ProcessPayOutput processPay(String ids, PayMetVo deposit) {
        ProcessPayOutput out = new ProcessPayOutput();
        out.setOrderId(ids);

        if (deposit != null && Objects.equals(deposit.getPaymentMetId(), StateCode.PAYMENT_MET_CREDIT)) {
            throw new BusinessException(__("开源版不支持信用支付"));
        }
        if (deposit != null && Objects.equals(deposit.getPaymentMetId(), StateCode.PAYMENT_MET_POINTS)) {
            throw new BusinessException(__("开源版不支持积分支付"));
        }

        List<ConsumeTrade> trades = consumeTradeRepository.find(new QueryWrapper<ConsumeTrade>().in("order_id", StrUtil.split(ids, ",")));

        Date now = new Date();
        BigDecimal depositTotalFee = BigDecimal.ZERO;

        switch (deposit.getPaymentMetId().intValue()) {
            case StateCode.PAYMENT_MET_MONEY:
                depositTotalFee = deposit.getPmMoney();
                break;
            case StateCode.PAYMENT_MET_RECHARGE_CARD:
                depositTotalFee = deposit.getPmRechargeCard();
                break;
            default:
                throw new BusinessException(__("支付渠道不合法"));
        }

        //处理订单支付结果
        //设置支付完成 封装为独立方法
        for (ConsumeTrade trade : trades) {
            trade.setTradePaidTime(now.getTime());
            Integer userId = trade.getBuyerId();

            if (Objects.equals(trade.getTradeTypeId(), StateCode.TRADE_TYPE_BUY_PLUS)) {
                throw new BusinessException(__("开源版不支持PLUS会员购买"));
            }
            if (Objects.equals(trade.getTradeTypeId(), StateCode.PAYMENT_TYPE_CREDIT)) {
                throw new BusinessException(__("开源版不支持账期/信用还款"));
            }

            UserInfo userInfo = userInfoRepository.get(userId);
            //积分支付
            UserResource resource = userResourceRepository.get(userId);

            if (resource == null) {
                throw new BusinessException(__("用户资源不存在！"));
            }

            BigDecimal tradePaymentAmount = trade.getTradePaymentAmount();

            // 写入充值流水
            List<ConsumeRecord> consumeRecords = new ArrayList<>();

            ConsumeRecord record = new ConsumeRecord();
            record.setOrderId(trade.getOrderId());

            if (CheckUtil.isNotEmpty(deposit.getPayUserId())) {
                userId = deposit.getPayUserId();
                record.setUserId(userId);
                record.setUserNickname(deposit.getPayUserNickname());
                record.setStoreId(deposit.getPayStoreId());
            } else {
                record.setUserId(userId);
                record.setUserNickname(userInfo != null ? userInfo.getUserNickname() : "");
                record.setStoreId(trade.getBuyerStoreId());
            }
            record.setChainId(0);
            record.setRecordTotal(depositTotalFee);
            record.setRecordMoney(depositTotalFee);
            record.setRecordDate(now);
            record.setRecordYear(DateUtil.year(now));
            record.setRecordMonth(DateUtil.month(now) + 1);
            record.setRecordDay(DateUtil.dayOfMonth(now));
            record.setRecordTitle(trade.getTradeTitle());
            record.setRecordDesc(trade.getTradeDesc());
            record.setRecordTime(now.getTime());
            record.setTradeTypeId(StateCode.TRADE_TYPE_DEPOSIT);
            record.setPaymentTypeId(deposit.getPaymentTypeId());
            record.setPaymentChannelId(deposit.getPaymentChannelId());

            if (depositTotalFee.compareTo(BigDecimal.ZERO) > 0 && trade.getTradeIsPaid().intValue() != StateCode.ORDER_PAID_STATE_YES) {

                //余额支付
                if (Objects.equals(deposit.getPaymentMetId(), StateCode.PAYMENT_MET_MONEY)) {
                    record.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);

                    if (depositTotalFee.compareTo(tradePaymentAmount) >= 0) {
                        // 订单处理
                        trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_YES);
                        trade.setPaymentChannelId(deposit.getPaymentChannelId());
                        trade.setTradePaymentAmount(BigDecimal.ZERO);
                        trade.setTradePaymentMoney(trade.getTradePaymentMoney().add(tradePaymentAmount));

                        depositTotalFee = depositTotalFee.subtract(tradePaymentAmount);
                    } else {
                        // 订单处理
                        trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_PART);
                        trade.setTradePaymentAmount(trade.getTradePaymentAmount().subtract(depositTotalFee));
                        trade.setTradePaymentMoney(trade.getTradePaymentMoney().add(depositTotalFee));

                        depositTotalFee = BigDecimal.ZERO;
                    }

                    //订单扣除流水
                    //订单消费流水
                    //涉及佣金结算问题
                    if (StateCode.TRADE_TYPE_SHOPPING == trade.getTradeTypeId().intValue()) {
                        // 1. 买家流水及订单扣除
                        //ConsumeRecord record = new ConsumeRecord();
                        record.setConsumeRecordId(null);

                        if (CheckUtil.isNotEmpty(deposit.getPayUserId())) {
                            record.setUserId(deposit.getPayUserId());
                            record.setUserNickname(deposit.getPayUserNickname());
                            record.setStoreId(deposit.getPayStoreId());
                        } else {
                            record.setUserId(userId);
                            record.setUserNickname(userInfo != null ? userInfo.getUserNickname() : "");
                            record.setStoreId(trade.getBuyerStoreId());
                        }
                        record.setChainId(0);
                        record.setTradeTypeId(trade.getTradeTypeId());
                        record.setRecordTotal(BigDecimal.ZERO.subtract(tradePaymentAmount));
                        record.setRecordMoney(record.getRecordTotal());

                        consumeRecords.add(record);

                        resource.setUserMoney(resource.getUserMoney().subtract(tradePaymentAmount));
                        userResourceRepository.edit(resource);
                        //userResourceRepository.decrement(trade.getBuyerId(), userResourceRepository.Columns().getUserMoney(), tradePaymentAmount.floatValue());

                        if(trade.getTradeTypeId() == StateCode.TRADE_TYPE_SHOPPING){
                            // 2. 卖家订单流水增加
                            ConsumeRecord sellerRecord = BeanUtil.copyProperties(record, ConsumeRecord.class);
                            sellerRecord.setConsumeRecordId(null);
                            sellerRecord.setUserId(trade.getSellerId());
                            UserInfo user = userInfoRepository.get(trade.getSellerId());
                            sellerRecord.setUserNickname(user != null ? user.getUserNickname() : "");
                            sellerRecord.setStoreId(trade.getStoreId());
                            sellerRecord.setChainId(trade.getChainId());
                            sellerRecord.setTradeTypeId(StateCode.TRADE_TYPE_SALES);
                            sellerRecord.setRecordDistributionCommissionFee(BigDecimal.ZERO);

                            sellerRecord.setPaymentTypeId(deposit.getPaymentTypeId());
                            sellerRecord.setRecordTotal(tradePaymentAmount);

                            //卖家收益涉及佣金问题， 可以分多次付款，支付完成才扣佣金
                            if (trade.getTradeIsPaid().intValue() == StateCode.ORDER_PAID_STATE_YES) {
                                //卖家收益，进入冻结中?
                                if (StateCode.PAYMENT_TYPE_OFFLINE == deposit.getPaymentTypeId().intValue()) {
                                    sellerRecord.setRecordMoney(tradePaymentAmount);
                                    sellerRecord.setRecordCommissionFee(BigDecimal.ZERO);
                                } else {
                                    sellerRecord.setRecordMoney(tradePaymentAmount.subtract(trade.getOrderCommissionFee()));//佣金平台获取。 是否需要加入一个统计字段中？
                                    sellerRecord.setRecordCommissionFee(trade.getOrderCommissionFee());//佣金平台获取

                                    //平台佣金总额
                                }
                            } else {
                                sellerRecord.setRecordMoney(tradePaymentAmount);
                            }

                            consumeRecords.add(sellerRecord);

                            //卖家收益，进入冻结中?
                            if (StateCode.PAYMENT_TYPE_OFFLINE == deposit.getPaymentTypeId().intValue()) {
                                //线下支付，需要扣除商家交易佣金？
                            } else {
                                UserResource userResourceSeller = userResourceRepository.get(trade.getSellerId());

                                if (userResourceSeller == null) {
                                    throw new BusinessException(__("商家资源信息不存在！"));
                                }
                                userResourceSeller.setUserMoney(userResourceSeller.getUserMoney().add(sellerRecord.getRecordMoney()));

                                if (!userResourceRepository.edit(userResourceSeller)) {
                                    throw new BusinessException(__("修改商家资源信息失败！"));
                                }
                                //userResourceRepository.increment(trade.getSellerId(), userResourceRepository.Columns().getUserMoney(), record.getRecordMoney());
                            }
                        }

                    }
                }

                if (!consumeTradeRepository.edit(trade)) {
                    throw new BusinessException(__("修改交易订单信息失败！"));
                }

                if (CollectionUtil.isNotEmpty(consumeRecords)) {
                    if (!consumeRecordRepository.saveOrUpdate(consumeRecords)) {
                        throw new BusinessException(__("保存交易明细信息失败！"));
                    }
                }



            } else {
                trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_YES);
            }
            ConsumeTrade consumeTradeRow = findOne(new QueryWrapper<ConsumeTrade>().eq("order_id", trade.getOrderId()));
            //单纯充值
            if (StateCode.TRADE_TYPE_DEPOSIT == trade.getTradeTypeId().intValue()) {
                //$flag_row[] = $this->notifyDeposit($order_id, $trade_row);
            }

            if (StateCode.ORDER_PAID_STATE_YES == trade.getTradeIsPaid().intValue()) {
                OrderInfo orderInfoOld = orderInfoRepository.get(trade.getOrderId());

                    if (orderInfoOld.getOrderStateId().intValue() == StateCode.ORDER_STATE_WAIT_PAY) {
                        if (orderService.setPaidYes(trade.getOrderId())) {
                            out.setPaid(true);

                            //判断支付用户
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.setOrderId(trade.getOrderId());
                            if (CheckUtil.isNotEmpty(deposit.getPayUserId())) {
                                orderInfo.setPayUserId(deposit.getPayUserId());
                            }

                            if (CheckUtil.isNotEmpty(orderInfo.getPayUserId()) || CheckUtil.isNotEmpty(orderInfo.getPaymentTypeId())) {
                                if (!orderInfoRepository.edit(orderInfo)) {
                                    throw new BusinessException("修改订单支付用户信息失败！");
                                }
                            }


                            OrderData orderData = new OrderData();
                            orderData.setOrderId(trade.getOrderId());
                            orderData.setTradePaymentCredit(consumeTradeRow.getTradePaymentCredit());
                            orderData.setTradePaymentMoney(consumeTradeRow.getTradePaymentMoney());
                            if (!orderDataRepository.edit(orderData)) {
                                throw new BusinessException("修改订单详细信息失败！");
                            }
                        }
                    } else {
                        if (orderInfoOld.getPaymentTypeId().intValue() == StateCode.PAYMENT_TYPE_OFFLINE) {

                        }

                        //判断是否线下支付
                        if (StateCode.PAYMENT_TYPE_OFFLINE == deposit.getPaymentTypeId().intValue()) {
                            //直接处理订单支付状态， 不处理订单状态
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.setOrderId(trade.getOrderId());
                            orderInfo.setOrderIsPaid(StateCode.ORDER_PAID_STATE_YES);
                            if (orderInfoRepository.edit(orderInfo)) {
                                out.setPaid(true);
                            }
                        } else {
                            if (orderService.setPaidYes(trade.getOrderId())) {
                                out.setPaid(true);
                                OrderData orderData = new OrderData();
                                orderData.setOrderId(trade.getOrderId());
                                orderData.setTradePaymentCredit(consumeTradeRow.getTradePaymentCredit());
                                orderData.setTradePaymentMoney(consumeTradeRow.getTradePaymentMoney());
                                if (!orderDataRepository.edit(orderData)) {
                                    throw new BusinessException("修改订单详细信息失败！");
                                }
                            }
                        }
                    }
            } else {
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setOrderId(trade.getOrderId());
                orderInfo.setOrderIsPaid(StateCode.ORDER_PAID_STATE_PART);

                out.setPaid(false);
            }
        }

        return out;
    }
}
