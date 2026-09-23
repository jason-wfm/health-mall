package com.wechuang.mallshop.pay.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pay.model.entity.ConsumeWithdraw;
import com.wechuang.mallshop.pay.model.entity.StoreSettlement;
import com.wechuang.mallshop.pay.model.entity.StoreSettlementOrder;
import com.wechuang.mallshop.pay.model.entity.UserBankCard;
import com.wechuang.mallshop.pay.model.req.StoreSettlementListReq;
import com.wechuang.mallshop.pay.repository.ConsumeWithdrawRepository;
import com.wechuang.mallshop.pay.repository.StoreSettlementOrderRepository;
import com.wechuang.mallshop.pay.repository.StoreSettlementRepository;
import com.wechuang.mallshop.pay.repository.UserBankCardRepository;
import com.wechuang.mallshop.pay.service.StoreSettlementService;
import com.wechuang.mallshop.sys.service.NumberSeqService;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderData;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.repository.OrderDataRepository;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.repository.OrderReturnRepository;
import com.wechuang.mallshop.trade.service.OrderBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商户结算单 服务实现类
 * </p>
 * 金额口径与既有商家实时结算 OrderServiceImpl.getSettlementList 保持一致：
 * 应结 = 订单实付 - 平台佣金(order_commission_fee) - 售后退款(退款额-退款返还佣金)
 *
 * @author jason
 * @since 2026-09-18
 */
@Service
public class StoreSettlementServiceImpl extends BaseServiceImpl<StoreSettlementRepository, StoreSettlement, StoreSettlementListReq> implements StoreSettlementService {

    @Autowired
    private StoreSettlementOrderRepository storeSettlementOrderRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderDataRepository orderDataRepository;

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private NumberSeqService numberSeqService;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private ConsumeWithdrawRepository consumeWithdrawRepository;

    @Autowired
    private UserBankCardRepository userBankCardRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateSettlements() {
        Long withdrawTime = orderBaseService.getWithdrawTime();

        QueryWrapper<OrderInfo> infoWrapper = new QueryWrapper<>();
        infoWrapper.eq("order_state_id", StateCode.ORDER_STATE_FINISH);
        infoWrapper.le("update_time", withdrawTime);
        infoWrapper.eq("order_is_paid", StateCode.ORDER_PAID_STATE_YES);
        infoWrapper.eq("order_is_settlemented", 0);
        infoWrapper.gt("store_id", 0);
        List<OrderInfo> orderInfos = orderInfoRepository.find(infoWrapper);

        if (CollectionUtil.isEmpty(orderInfos)) {
            return true;
        }

        Map<Integer, List<OrderInfo>> orderByStore = orderInfos.stream()
                .filter(info -> info.getStoreId() != null)
                .collect(Collectors.groupingBy(OrderInfo::getStoreId));

        for (Map.Entry<Integer, List<OrderInfo>> entry : orderByStore.entrySet()) {
            generateForStore(entry.getKey(), entry.getValue(), withdrawTime);
        }

        return true;
    }

    /**
     * 单店铺结算单生成（口径对齐 OrderServiceImpl.getSettlementList）
     */
    private void generateForStore(Integer storeId, List<OrderInfo> orderInfos, Long withdrawTime) {
        List<String> orderIds = CommonUtil.column(orderInfos, OrderInfo::getOrderId);

        //在途售后中的订单剔除,待售后终结后进入下一期
        QueryWrapper<OrderReturn> checkingWrapper = new QueryWrapper<>();
        checkingWrapper.eq("return_state_id", StateCode.RETURN_PROCESS_CHECK);
        checkingWrapper.in("order_id", orderIds);
        List<OrderReturn> checkingReturns = orderReturnRepository.find(checkingWrapper);

        if (CollectionUtil.isNotEmpty(checkingReturns)) {
            Set<String> checkingOrderIds = checkingReturns.stream()
                    .map(OrderReturn::getOrderId).collect(Collectors.toSet());
            orderInfos = orderInfos.stream()
                    .filter(info -> !checkingOrderIds.contains(info.getOrderId()))
                    .collect(Collectors.toList());

            if (CollectionUtil.isEmpty(orderInfos)) {
                return;
            }

            orderIds = CommonUtil.column(orderInfos, OrderInfo::getOrderId);
        }

        //订单实付金额
        Map<String, OrderBase> orderBaseMap = orderBaseRepository.gets(orderIds).stream()
                .collect(Collectors.toMap(OrderBase::getOrderId, base -> base, (k1, k2) -> k1));

        //平台佣金
        Map<String, OrderData> orderDataMap = orderDataRepository.gets(orderIds).stream()
                .collect(Collectors.toMap(OrderData::getOrderId, data -> data, (k1, k2) -> k1));

        //已终结待结算的售后退款(退款额-返还佣金),按订单聚合
        QueryWrapper<OrderReturn> refundWrapper = new QueryWrapper<>();
        refundWrapper.eq("store_id", storeId);
        refundWrapper.eq("return_is_settlemented", 0);
        refundWrapper.in("return_state_id", Arrays.asList(
                StateCode.RETURN_PROCESS_RECEIVED,
                StateCode.RETURN_PROCESS_REFUND,
                StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION,
                StateCode.RETURN_PROCESS_FINISH));
        refundWrapper.in("order_id", orderIds);
        List<OrderReturn> refundReturns = orderReturnRepository.find(refundWrapper);

        Map<String, BigDecimal> refundByOrder = new HashMap<>();
        for (OrderReturn returnItem : refundReturns) {
            BigDecimal refund = NumberUtil.sub(
                    returnItem.getReturnRefundAmount() == null ? BigDecimal.ZERO : returnItem.getReturnRefundAmount(),
                    returnItem.getReturnCommisionFee() == null ? BigDecimal.ZERO : returnItem.getReturnCommisionFee());
            refundByOrder.merge(returnItem.getOrderId(), refund, BigDecimal::add);
        }

        //账期:上一张有效结算单的period_end至当前
        QueryWrapper<StoreSettlement> lastWrapper = new QueryWrapper<>();
        lastWrapper.eq("store_id", storeId);
        lastWrapper.ne("settlement_state", StateCode.SETTLEMENT_STATE_REJECTED);
        lastWrapper.orderByDesc("period_end");
        List<StoreSettlement> lastSettlements = lists(lastWrapper, 1, 1).getRecords();
        Date periodStart = CollectionUtil.isNotEmpty(lastSettlements)
                ? lastSettlements.get(0).getPeriodEnd() : new Date(0L);
        Date periodEnd = new Date();

        //汇总与明细快照
        BigDecimal orderAmount = BigDecimal.ZERO;
        BigDecimal commissionAmount = BigDecimal.ZERO;
        BigDecimal refundAmount = BigDecimal.ZERO;
        Date now = new Date();

        StoreSettlement settlement = new StoreSettlement();
        settlement.setSettlementNumber(numberSeqService.getNextSeqString("JS"));
        settlement.setStoreId(storeId);
        settlement.setPeriodStart(periodStart);
        settlement.setPeriodEnd(periodEnd);
        settlement.setAdjustAmount(BigDecimal.ZERO);
        settlement.setSettlementState(StateCode.SETTLEMENT_STATE_WAIT_CONFIRM);
        settlement.setSellerUserId(userAdminService.getUserIdByStoreId(storeId));

        if (!save(settlement)) {
            throw new BusinessException(__("生成结算单失败！"));
        }

        for (OrderInfo orderInfo : orderInfos) {
            String orderId = orderInfo.getOrderId();
            OrderBase orderBase = orderBaseMap.get(orderId);
            OrderData orderData = orderDataMap.get(orderId);

            BigDecimal payment = orderBase != null && orderBase.getOrderPaymentAmount() != null
                    ? orderBase.getOrderPaymentAmount() : BigDecimal.ZERO;
            BigDecimal commission = orderData != null && orderData.getOrderCommissionFee() != null
                    ? orderData.getOrderCommissionFee() : BigDecimal.ZERO;
            BigDecimal refund = refundByOrder.getOrDefault(orderId, BigDecimal.ZERO);

            StoreSettlementOrder detail = new StoreSettlementOrder();
            detail.setSettlementId(settlement.getSettlementId());
            detail.setOrderId(orderId);
            detail.setOrderPaymentAmount(payment);
            detail.setOrderCommissionFee(commission);
            detail.setRefundAmount(refund);
            detail.setOrderSettleAmount(NumberUtil.sub(NumberUtil.sub(payment, commission), refund));

            if (!storeSettlementOrderRepository.save(detail)) {
                throw new BusinessException(__("生成结算单明细失败！"));
            }

            orderAmount = orderAmount.add(payment);
            commissionAmount = commissionAmount.add(commission);
            refundAmount = refundAmount.add(refund);
        }

        settlement.setOrderCount(orderInfos.size());
        settlement.setOrderAmount(orderAmount);
        settlement.setCommissionAmount(commissionAmount);
        settlement.setRefundAmount(refundAmount);
        settlement.setSettleAmount(NumberUtil.sub(NumberUtil.sub(orderAmount, commissionAmount), refundAmount));

        if (!edit(settlement)) {
            throw new BusinessException(__("更新结算单汇总失败！"));
        }

        //订单置结算中(条件更新保证幂等:并发的实时结算已占用则本次失败回滚)
        OrderInfo infoUpdate = new OrderInfo();
        infoUpdate.setOrderIsSettlemented(2);
        infoUpdate.setOrderSettlementTime(now);
        QueryWrapper<OrderInfo> infoUpdateWrapper = new QueryWrapper<>();
        infoUpdateWrapper.in("order_id", orderIds);
        infoUpdateWrapper.eq("order_is_settlemented", 0);
        if (!orderInfoRepository.edit(infoUpdate, infoUpdateWrapper)) {
            throw new BusinessException(__("更改订单结算状态失败！"));
        }

        //退款单置结算中
        if (CollectionUtil.isNotEmpty(refundReturns)) {
            List<String> returnIds = CommonUtil.column(refundReturns, OrderReturn::getReturnId);
            OrderReturn returnUpdate = new OrderReturn();
            returnUpdate.setReturnIsSettlemented(2);
            returnUpdate.setReturnSettlementTime(now);
            QueryWrapper<OrderReturn> returnUpdateWrapper = new QueryWrapper<>();
            returnUpdateWrapper.in("return_id", returnIds);
            returnUpdateWrapper.eq("return_is_settlemented", 0);
            if (!orderReturnRepository.edit(returnUpdate, returnUpdateWrapper)) {
                throw new BusinessException(__("更改退款结算状态失败！"));
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmSettlement(Long settlementId) {
        StoreSettlement settlement = checkStoreOwner(settlementId, StateCode.SETTLEMENT_STATE_WAIT_CONFIRM);

        settlement.setSettlementState(StateCode.SETTLEMENT_STATE_CONFIRMED);
        settlement.setConfirmTime(new Date().getTime());

        if (!edit(settlement)) {
            throw new BusinessException(__("确认结算单失败！"));
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectSettlement(Long settlementId) {
        StoreSettlement settlement = checkStoreOwner(settlementId, StateCode.SETTLEMENT_STATE_WAIT_CONFIRM);

        settlement.setSettlementState(StateCode.SETTLEMENT_STATE_REJECTED);

        if (!edit(settlement)) {
            throw new BusinessException(__("驳回结算单失败！"));
        }

        //明细订单回滚为未结算,进入下一期
        QueryWrapper<StoreSettlementOrder> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("settlement_id", settlementId);
        List<StoreSettlementOrder> details = storeSettlementOrderRepository.find(detailWrapper);

        if (CollectionUtil.isNotEmpty(details)) {
            List<String> orderIds = CommonUtil.column(details, StoreSettlementOrder::getOrderId);

            OrderInfo infoUpdate = new OrderInfo();
            infoUpdate.setOrderIsSettlemented(0);
            QueryWrapper<OrderInfo> infoUpdateWrapper = new QueryWrapper<>();
            infoUpdateWrapper.in("order_id", orderIds);
            infoUpdateWrapper.eq("order_is_settlemented", 2);
            orderInfoRepository.edit(infoUpdate, infoUpdateWrapper);

            //随单退款回滚(按订单归属匹配,排除被实时结算占用的记录由条件eq(2)保证)
            OrderReturn returnUpdate = new OrderReturn();
            returnUpdate.setReturnIsSettlemented(0);
            QueryWrapper<OrderReturn> returnUpdateWrapper = new QueryWrapper<>();
            returnUpdateWrapper.in("order_id", orderIds);
            returnUpdateWrapper.eq("return_is_settlemented", 2);
            orderReturnRepository.edit(returnUpdate, returnUpdateWrapper);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean paySettlement(Long settlementId, Integer userBankId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isPlatform()) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        StoreSettlement settlement = get(settlementId);

        if (settlement == null) {
            throw new BusinessException(__("结算单不存在！"));
        }

        if (!Objects.equals(settlement.getSettlementState(), StateCode.SETTLEMENT_STATE_CONFIRMED)) {
            throw new BusinessException(__("结算单状态不允许出金(需商家已确认)！"));
        }

        if (settlement.getSettleAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(__("结算单应结金额非正数,无需出金！"));
        }

        //提现收款账户(商家管理员本人的银行卡)
        UserBankCard userBankCard = userBankCardRepository.get(userBankId);

        if (userBankCard == null) {
            throw new BusinessException(__("提现银行卡不存在！"));
        }

        if (!Objects.equals(userBankCard.getUserId(), settlement.getSellerUserId())) {
            throw new BusinessException(__("提现银行卡用户与结算单收款人不匹配！"));
        }

        QueryWrapper<StoreSettlementOrder> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("settlement_id", settlementId);
        List<StoreSettlementOrder> details = storeSettlementOrderRepository.find(detailWrapper);
        List<String> orderIds = CommonUtil.column(details, StoreSettlementOrder::getOrderId);

        Date now = new Date();
        ConsumeWithdraw withdraw = new ConsumeWithdraw();
        withdraw.setUserId(settlement.getSellerUserId());
        withdraw.setStoreId(settlement.getStoreId());
        withdraw.setOrderId(CollUtil.join(orderIds, ","));
        withdraw.setReturnId("");
        withdraw.setWithdrawAmount(settlement.getSettleAmount());
        withdraw.setWithdrawState(0); //申请中,实际打款走既有提现审核/微信商家转账链路
        withdraw.setWithdrawBank(userBankCard.getUserBankCardAddress());
        withdraw.setWithdrawMobile(userBankCard.getUserBankCardMobile());
        withdraw.setWithdrawAccountNo(userBankCard.getUserBankCardCode());
        withdraw.setWithdrawAccountName(userBankCard.getUserBankCardName());
        withdraw.setWithdrawTime(now.getTime());

        if (!consumeWithdrawRepository.save(withdraw)) {
            throw new BusinessException(__("创建提现申请失败！"));
        }

        settlement.setSettlementState(StateCode.SETTLEMENT_STATE_PAYING);
        settlement.setWithdrawId(Long.valueOf(withdraw.getWithdrawId()));

        if (!edit(settlement)) {
            throw new BusinessException(__("更新结算单出金状态失败！"));
        }

        return true;
    }

    @Override
    public Map<String, Object> getSettlementDetail(Long settlementId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        StoreSettlement settlement = get(settlementId);

        if (settlement == null) {
            throw new BusinessException(__("结算单不存在！"));
        }

        if (loginUser.isStore() && !Objects.equals(settlement.getStoreId(), loginUser.getStoreId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        QueryWrapper<StoreSettlementOrder> detailWrapper = new QueryWrapper<>();
        detailWrapper.eq("settlement_id", settlementId);
        detailWrapper.orderByAsc("id");
        List<StoreSettlementOrder> details = storeSettlementOrderRepository.find(detailWrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("settlement", settlement);
        result.put("orders", details);

        return result;
    }

    @Override
    public IPage<StoreSettlement> getList(StoreSettlementListReq req) {
        ContextUser loginUser = getLoginUser();

        QueryWrapper<StoreSettlement> queryWrapper = new QueryWrapper<>();

        //商家身份只看本店(入参storeId忽略);平台可按店铺筛选
        if (loginUser != null && loginUser.isStore()) {
            queryWrapper.eq("store_id", loginUser.getStoreId());
        } else if (req.getStoreId() != null) {
            queryWrapper.eq("store_id", req.getStoreId());
        }

        if (req.getSettlementState() != null) {
            queryWrapper.eq("settlement_state", req.getSettlementState());
        }

        return lists(queryWrapper, req.getPage(), req.getSize());
    }

    /**
     * 商家确认/驳回前的归属与状态校验
     */
    private StoreSettlement checkStoreOwner(Long settlementId, Integer expectState) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (!loginUser.isStore()) {
            throw new BusinessException(__("非商家用户！"));
        }

        StoreSettlement settlement = get(settlementId);

        if (settlement == null) {
            throw new BusinessException(__("结算单不存在！"));
        }

        if (!Objects.equals(settlement.getStoreId(), loginUser.getStoreId())) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        if (!Objects.equals(settlement.getSettlementState(), expectState)) {
            throw new BusinessException(__("结算单状态不允许该操作！"));
        }

        return settlement;
    }
}
