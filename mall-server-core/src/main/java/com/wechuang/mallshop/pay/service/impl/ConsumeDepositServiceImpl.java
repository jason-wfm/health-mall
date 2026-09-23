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
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pay.model.entity.ConsumeDeposit;
import com.wechuang.mallshop.pay.model.entity.ConsumeRecord;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import com.wechuang.mallshop.pay.model.output.ProcessPayOutput;
import com.wechuang.mallshop.pay.model.req.ConsumeDepositListReq;
import com.wechuang.mallshop.pay.model.req.PaymentReq;
import com.wechuang.mallshop.pay.model.vo.PayMetVo;
import com.wechuang.mallshop.pay.repository.*;
import com.wechuang.mallshop.pay.service.ConsumeDepositService;
import com.wechuang.mallshop.pay.service.ConsumeTradeService;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 支付表-支付回调callback使用-确认付款 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Service
public class ConsumeDepositServiceImpl extends BaseServiceImpl<ConsumeDepositRepository, ConsumeDeposit, ConsumeDepositListReq> implements ConsumeDepositService {
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
    private OrderInfoRepository orderInfoRepository;
    @Autowired
    private ConsumeTradeService consumeTradeService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public long processDeposit(ConsumeDeposit in) {
        List<ConsumeDeposit> deposits = find(new QueryWrapper<ConsumeDeposit>().eq("deposit_no", in.getDepositNo()).eq("deposit_trade_no", in.getDepositTradeNo()));
        ConsumeDeposit deposit;

        Date now = new Date();

        if (!deposits.isEmpty()) {
            deposit = deposits.get(0);
        } else {
            boolean flag = add(in);

            if (flag) {
                deposit = get(in.getDepositId());
            } else {
                throw new BusinessException(__("充值记录添加出错"));
            }
        }

        if (deposit.getDepositState() == 0) {
            List<ConsumeTrade> trades = consumeTradeRepository.find(new QueryWrapper<ConsumeTrade>().in("order_id", StrUtil.split(deposit.getOrderId(), ",")));

            BigDecimal depositTotalFee = deposit.getDepositTotalFee();

            ConsumeTrade tradeOne;

            if (CollUtil.isNotEmpty(trades)) {
                tradeOne = trades.get(0);
            } else {
                throw new BusinessException(__("交易订单不存在"));
            }

            // 用户账户增加充值额度
            UserResource userResource = userResourceRepository.get(tradeOne.getBuyerId());
            if (userResource == null) {
                throw new BusinessException(__("用户资源信息不存在"));
            }
            if (userResource.getUserMoney() == null) {
                userResource.setUserMoney(BigDecimal.ZERO);
            }
            userResource.setUserMoney(userResource.getUserMoney().add(deposit.getDepositTotalFee()));
            userResourceRepository.edit(userResource);
            //userResourceRepository.increment(tradeOne.getBuyerId(), userResourceRepository.Columns().getUserMoney(), deposit.getDepositTotalFee());

            // 写入充值流水
            ConsumeRecord record = new ConsumeRecord();
            record.setOrderId(tradeOne.getOrderId());
            record.setUserId(tradeOne.getBuyerId());
            UserInfo userInfo = userInfoRepository.get(tradeOne.getBuyerId());
            record.setUserNickname(userInfo != null ? userInfo.getUserNickname() : "");
            record.setStoreId(tradeOne.getBuyerStoreId());
            record.setChainId(0);
            record.setRecordTotal(deposit.getDepositTotalFee());
            record.setRecordMoney(deposit.getDepositTotalFee());
            record.setRecordDate(now);
            record.setRecordYear(DateUtil.year(now));
            record.setRecordMonth(DateUtil.month(now) + 1);
            record.setRecordDay(DateUtil.dayOfMonth(now));
            record.setRecordTitle(deposit.getDepositSubject());
            record.setRecordDesc(deposit.getDepositBody());
            record.setRecordTime(now.getTime());
            record.setTradeTypeId(StateCode.TRADE_TYPE_DEPOSIT);
            record.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);
            record.setPaymentTypeId(deposit.getDepositPaymentType());
            record.setPaymentChannelId(deposit.getPaymentChannelId());

            consumeRecordRepository.add(record);

            // 修改充值成功状态
            deposit.setDepositState(1);
            edit(deposit);


            //处理订单支付结果
            PayMetVo payInfo = new PayMetVo();
            payInfo.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);
            payInfo.setPaymentChannelId(deposit.getPaymentChannelId());
            payInfo.setPaymentTypeId(deposit.getDepositPaymentType());
            payInfo.setPmMoney(deposit.getDepositTotalFee());


            ProcessPayOutput processPayOutput = consumeTradeService.processPay(deposit.getOrderId(), payInfo);
            /*
            for (ConsumeTrade trade : trades) {
                if (depositTotalFee.compareTo(BigDecimal.ZERO) > 0 && trade.getTradeIsPaid().intValue() != StateCode.ORDER_PAID_STATE_YES) {

                    BigDecimal tradePaymentAmount = trade.getTradePaymentAmount();

                    if (depositTotalFee.compareTo(tradePaymentAmount) >= 0) {
                        // 订单处理
                        trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_YES);
                        trade.setPaymentChannelId(deposit.getPaymentChannelId());
                        trade.setTradePaymentAmount(BigDecimal.ZERO);
                        trade.setTradePaymentMoney(trade.getTradePaymentMoney().add(tradePaymentAmount));

                        consumeTradeRepository.edit(trade);

                        depositTotalFee = depositTotalFee.subtract(tradePaymentAmount);
                    } else {
                        // 订单处理
                        trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_PART);
                        trade.setTradePaymentAmount(trade.getTradePaymentAmount().subtract(depositTotalFee));
                        trade.setTradePaymentMoney(trade.getTradePaymentMoney().add(depositTotalFee));

                        consumeTradeRepository.edit(trade);

                        depositTotalFee = BigDecimal.ZERO;
                    }

                    //订单扣除流水
                    //订单消费流水
                    //涉及佣金结算问题
                    if (StateCode.TRADE_TYPE_SHOPPING == trade.getTradeTypeId().intValue()) {
                        // 1. 买家流水及订单扣除
                        //ConsumeRecord record = new ConsumeRecord();
                        record.setConsumeRecordId(null);
                        record.setOrderId(trade.getOrderId());
                        record.setUserId(trade.getBuyerId());
                        record.setUserNickname("");
                        record.setStoreId(trade.getBuyerStoreId());
                        record.setChainId(0);
                        record.setTradeTypeId(trade.getTradeTypeId());
                        record.setRecordTotal(BigDecimal.ZERO.subtract(tradePaymentAmount));
                        record.setRecordMoney(record.getRecordTotal());

                        consumeRecordRepository.add(record);

                        UserResource userResourceBuy = userResourceRepository.get(trade.getBuyerId());
                        userResourceBuy.setUserMoney(userResourceBuy.getUserMoney().subtract(tradePaymentAmount));
                        userResourceRepository.edit(userResourceBuy);
                        //userResourceRepository.decrement(trade.getBuyerId(), userResourceRepository.Columns().getUserMoney(), tradePaymentAmount.floatValue());

                        // 2. 卖家订单流水增加
                        record.setConsumeRecordId(null);
                        record.setUserId(trade.getSellerId());
                        record.setUserNickname("");
                        record.setStoreId(trade.getStoreId());
                        record.setChainId(trade.getChainId());
                        record.setTradeTypeId(StateCode.TRADE_TYPE_SALES);
                        record.setPaymentTypeId(deposit.getDepositPaymentType());
                        record.setPaymentChannelId(deposit.getPaymentChannelId());
                        record.setRecordTotal(tradePaymentAmount);

                        //卖家收益涉及佣金问题， 可以分多次付款，支付完成才扣佣金
                        if (trade.getTradeIsPaid().intValue() == StateCode.ORDER_PAID_STATE_YES) {
                            //卖家收益，进入冻结中?
                            if (StateCode.PAYMENT_TYPE_OFFLINE == deposit.getDepositPaymentType().intValue()) {
                                record.setRecordMoney(tradePaymentAmount);
                                record.setRecordCommissionFee(BigDecimal.ZERO);
                            } else {
                                record.setRecordMoney(tradePaymentAmount.subtract(trade.getOrderCommissionFee()));//佣金平台获取。 是否需要加入一个统计字段中？
                                record.setRecordCommissionFee(trade.getOrderCommissionFee());//佣金平台获取

                                //平台佣金总额
                            }
                        } else {
                            record.setRecordMoney(tradePaymentAmount);
                        }

                        consumeRecordRepository.add(record);

                        //卖家收益，进入冻结中?
                        if (StateCode.PAYMENT_TYPE_OFFLINE == deposit.getDepositPaymentType().intValue()) {
                            //线下支付，需要扣除商家交易佣金？
                        } else {
                            UserResource userResourceSeller = userResourceRepository.get(trade.getSellerId());
                            userResourceSeller.setUserMoney(userResourceSeller.getUserMoney().add(record.getRecordMoney()));
                            userResourceRepository.edit(userResourceSeller);
                            //userResourceRepository.increment(trade.getSellerId(), userResourceRepository.Columns().getUserMoney(), record.getRecordMoney());
                        }
                    }
                } else {
                    trade.setTradeIsPaid(StateCode.ORDER_PAID_STATE_YES);
                }

                //单纯充值
                if (StateCode.TRADE_TYPE_DEPOSIT == trade.getTradeTypeId().intValue()) {
                    //$flag_row[] = $this->notifyDeposit($order_id, $trade_row);
                }

                if (StateCode.ORDER_PAID_STATE_YES == trade.getTradeIsPaid().intValue()) {
                    orderService.setPaidYes(trade.getOrderId());
                }
            }
             */
        }

        return deposit.getDepositId();
    }

    @Transactional
    @Override
    public Long offlinePay(ConsumeDeposit in) {
        if (CheckUtil.isEmpty(in.getDepositSubject())) {
            OrderInfo orderInfo = orderInfoRepository.get(in.getOrderId());
            //in.setDepositSubject(in.getDepositNo());
            if (orderInfo != null) {
                in.setDepositSubject(orderInfo.getOrderTitle());
            }
        }

        long l = processDeposit(in);
        return l;
    }

    @Transactional
    @Override
    public boolean bankPay(PaymentReq req) {
        String orderId = req.getOrderId();
        QueryWrapper<ConsumeDeposit> consumeDepositQueryWrapper = new QueryWrapper<>();
        consumeDepositQueryWrapper.eq("deposit_no", req.getDepositTradeNo());
        consumeDepositQueryWrapper.eq("deposit_trade_no", req.getDepositTradeNo());
        long count = count(consumeDepositQueryWrapper);

        if (count > 0) {
            throw new BusinessException(__("支付流水号已经存在！"));
        }
        consumeDepositQueryWrapper.clear();
        consumeDepositQueryWrapper.eq("order_id", orderId);
        long countOrder = count(consumeDepositQueryWrapper);

        if (countOrder > 0) {
            throw new BusinessException(__("该订单已有支付记录！"));
        }
        OrderInfo orderInfo = orderInfoRepository.get(orderId);

        if (orderInfo == null) {
            throw new BusinessException(__("该订单不存在！"));
        }
        QueryWrapper<ConsumeTrade> consumeTradeQueryWrapper = new QueryWrapper<>();
        consumeTradeQueryWrapper.eq("order_id", orderId);
        ConsumeTrade consumeTrade = consumeTradeRepository.findOne(consumeTradeQueryWrapper);

        if (consumeTrade == null) {
            throw new BusinessException(__("交易订单信息不存在！"));
        }

        ConsumeDeposit consumeDeposit = BeanUtil.copyProperties(req, ConsumeDeposit.class);
        consumeDeposit.setDepositNo(req.getDepositTradeNo());
        consumeDeposit.setPaymentChannelId(StateCode.PAYMENT_CHANNEL_BANK);
        consumeDeposit.setDepositSubject(orderInfo.getOrderTitle());
        consumeDeposit.setDepositPaymentType(StateCode.PAYMENT_TYPE_BANK);
        consumeDeposit.setDepositTotalFee(consumeTrade.getTradePaymentAmount());
        consumeDeposit.setStoreId(orderInfo.getStoreId());
        consumeDeposit.setUserId(orderInfo.getUserId());
        consumeDeposit.setDepositTime(new Date().getTime());

        if (!add(consumeDeposit)) {
            throw new BusinessException(__("保存支付表信息失败！"));
        }
        orderInfo.setOrderIsPaid(StateCode.ORDER_PAID_STATE_FINANCE_REVIEW);
        orderInfo.setPaymentTypeId(StateCode.PAYMENT_TYPE_BANK);

        if (!orderInfoRepository.edit(orderInfo)) {
            throw new BusinessException(__("修改订单信息失败！"));
        }

        return true;
    }

    @Transactional
    @Override
    public boolean orderPayReview(ConsumeDeposit consumeDeposit) {
        consumeDeposit.setDepositExamineTime(new Date().getTime());

        if (!edit(consumeDeposit)) {
            throw new BusinessException(__("修改支付表信息失败！"));
        }

        if (Objects.equals(consumeDeposit.getDepositReview(), 1)) {
            long result = processDeposit(get(consumeDeposit.getDepositId()));

            if (CheckUtil.isEmpty(result)) {
                throw new BusinessException(__("银行打款审核失败！"));
            }
        }

        return true;
    }
}
