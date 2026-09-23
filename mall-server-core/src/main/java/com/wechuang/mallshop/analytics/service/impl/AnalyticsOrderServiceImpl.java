package com.wechuang.mallshop.analytics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.model.entity.UserLogin;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLoginRepository;
import com.wechuang.mallshop.analytics.dao.AnalyticsOrderDao;
import com.wechuang.mallshop.analytics.dao.AnalyticsProductDao;
import com.wechuang.mallshop.analytics.dao.AnalyticsUserDao;
import com.wechuang.mallshop.analytics.model.input.AnalyticsOrderInput;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.AdminDashBoardRes;
import com.wechuang.mallshop.analytics.model.res.AmountRes;
import com.wechuang.mallshop.analytics.model.res.DashBoardTimelineRes;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.model.vo.OrderNumVo;
import com.wechuang.mallshop.analytics.service.AnalyticsOrderService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.TimeRange;
import com.wechuang.mallshop.common.utils.TimeUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.pay.repository.ConsumeTradeRepository;
import com.wechuang.mallshop.sys.dao.AccessHistoryDao;
import com.wechuang.mallshop.sys.model.output.AnalyticsAccessHistoryOutput;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.model.entity.UserCart;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.repository.UserCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Service
public class AnalyticsOrderServiceImpl implements AnalyticsOrderService {

    @Autowired
    private ConsumeTradeRepository consumeTradeRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private AnalyticsOrderDao analyticsOrderDao;

    @Autowired
    private AnalyticsProductDao analyticsProductDao;

    @Autowired
    private AnalyticsUserDao analyticsUserDao;

    @Autowired
    private UserCartRepository userCartRepository;

    @Autowired
    private AccessHistoryDao accessHistoryDao;

    /**
     * 获取后台仪表盘中部柱形数据，包含[订单数据，用户数据，商品数据，销售额数据]
     *
     */
    @Override
    public DashBoardTimelineRes getDashboardTimeLine(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        DashBoardTimelineRes dashBoardTimeLineRes = new DashBoardTimelineRes();

        List<TimelineOutput> orderTimelineOutput = analyticsOrderDao.getOrderTimeLine(input.getStime(), input.getEtime(), chainId, storeId);
        List<TimelineOutput> userTimelineOutput = analyticsUserDao.getUserTimeLine(input.getStime(), input.getEtime());
        List<TimelineOutput> ptTimelineOutput = analyticsProductDao.getProductTimeLine(input.getStime(), input.getEtime(), storeId);
        List<TimelineOutput> payTimelineOutput = analyticsOrderDao.getPayTimeLine(input.getStime(), input.getEtime(), chainId, storeId);

        dashBoardTimeLineRes.setOrderTimeLine(orderTimelineOutput);
        dashBoardTimeLineRes.setUserTimeLine(userTimelineOutput);
        dashBoardTimeLineRes.setPtTimeLine(ptTimelineOutput);
        dashBoardTimeLineRes.setPayTimeLine(payTimelineOutput);
        return dashBoardTimeLineRes;
    }

    @Override
    public List<AmountRes> getSaleOrderAmount(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        return analyticsOrderDao.getSaleOrderAmount(input.getStime(), input.getEtime(), chainId, storeId);
    }

    /**
     * 获取总交易额和近一周交易额增长率
     */
    public void getTradeData(AdminDashBoardRes dashBoardRes, DateTime recentOneWeek, DateTime recentTwoWeek) {
        // 总交易额
        LambdaQueryWrapper<ConsumeTrade> tradeQueryWrapper = new LambdaQueryWrapper<>();
        tradeQueryWrapper.eq(ConsumeTrade::getTradeIsPaid, StateCode.ORDER_PAID_STATE_YES);
        tradeQueryWrapper.in(ConsumeTrade::getTradeTypeId, Arrays.asList(StateCode.TRADE_TYPE_SHOPPING, StateCode.TRADE_TYPE_FAVORABLE));
        Long tradeAmount = consumeTradeRepository.getBaseMapper().selectCount(tradeQueryWrapper);
        dashBoardRes.setTradeAmount(tradeAmount);

        // 总交易额近一周增长率
        tradeQueryWrapper.gt(ConsumeTrade::getTradePaidTime, recentOneWeek);
        Long recentOneWeekTradeAmount = consumeTradeRepository.getBaseMapper().selectCount(tradeQueryWrapper);
        tradeQueryWrapper.gt(ConsumeTrade::getTradePaidTime, recentTwoWeek);
        Long recentTwoWeekTradeAmount = consumeTradeRepository.getBaseMapper().selectCount(tradeQueryWrapper);

        Double tradeAmountRate = 0.0;
        if (tradeAmount != 0 && recentOneWeekTradeAmount != 0) {
            tradeAmountRate = Convert.toDouble(((recentOneWeekTradeAmount - recentTwoWeekTradeAmount) / recentOneWeekTradeAmount) * 100);
        }
        dashBoardRes.setTradeAmountIncreaseRate(tradeAmountRate);
    }

    /**
     * 获取总订单成交和订单成交近一周增长率
     */
    public void getOrderFinishData(AdminDashBoardRes dashBoardRes, DateTime recentOneWeek, DateTime recentTwoWeek) {
        // 订单完成总成交
        LambdaQueryWrapper<OrderInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
        infoQueryWrapper.in(OrderInfo::getOrderStateId, StateCode.ORDER_STATE_FINISH);
        Long orderFinishNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);
        dashBoardRes.setOrderFinishNum(orderFinishNum);

        // 订单完成成交增长率
        infoQueryWrapper.gt(OrderInfo::getOrderReceivedTime, recentOneWeek);
        Long recentOneWeekOrderFinishNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);
        infoQueryWrapper.gt(OrderInfo::getOrderReceivedTime, recentTwoWeek);
        Long recentTwoWeekOrderFinishNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);

        Double orderFinishNumIncreaseRate = 0.0;
        if (orderFinishNum != 0 && recentOneWeekOrderFinishNum != 0) {
            orderFinishNumIncreaseRate = Convert.toDouble(((recentOneWeekOrderFinishNum - recentTwoWeekOrderFinishNum) / recentOneWeekOrderFinishNum) * 100);
        }
        dashBoardRes.setOrderFinishNumIncreaseRate(orderFinishNumIncreaseRate);
    }

    /**
     * 获取订单总量和订单近一周增长率
     */
    public void getOrderData(AdminDashBoardRes dashBoardRes, DateTime recentOneWeek, DateTime recentTwoWeek) {
        // 订单总成交
        LambdaQueryWrapper<OrderInfo> infoQueryWrapper = new LambdaQueryWrapper<>();
        infoQueryWrapper.in(OrderInfo::getOrderStateId, getOrderStates());
        Long orderNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);
        dashBoardRes.setOrderNum(orderNum);

        // 订单总成交近一周增长率
        infoQueryWrapper.gt(OrderInfo::getOrderReceivedTime, recentOneWeek);
        Long recentOneWeekOrderNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);
        infoQueryWrapper.gt(OrderInfo::getOrderReceivedTime, recentTwoWeek);
        Long recentTwoWeekOrderNum = orderInfoRepository.getBaseMapper().selectCount(infoQueryWrapper);

        Double orderNumIncreaseRate = 0.0;
        if (orderNum != 0 && recentOneWeekOrderNum != 0) {
            orderNumIncreaseRate = Convert.toDouble(((recentOneWeekOrderNum - recentTwoWeekOrderNum) / recentOneWeekOrderNum) * 100);
        }
        dashBoardRes.setOrderNumIncreaseRate(orderNumIncreaseRate);
    }

    /**
     * 获取用户总数和用户近一周增长率
     */
    public void getUserCertificationData(AdminDashBoardRes dashBoardRes, DateTime recentOneWeek, DateTime recentTwoWeek) {
        // 会员总数
        long userCertificationNum = userInfoRepository.count(new QueryWrapper<>());
        dashBoardRes.setUserCertificationNum(userCertificationNum);

        // 会员近一周增长率
        LambdaQueryWrapper<UserLogin> loginQueryWrapper = new LambdaQueryWrapper<>();
        loginQueryWrapper.gt(UserLogin::getUserRegTime, recentOneWeek);
        Long recentOneWeekCertificationNum = userLoginRepository.getBaseMapper().selectCount(loginQueryWrapper);
        loginQueryWrapper.gt(UserLogin::getUserRegTime, recentTwoWeek);
        Long recentTwoWeekCertificationNum = userLoginRepository.getBaseMapper().selectCount(loginQueryWrapper);

        Double userCertificationNumIncreaseRate = 0.0;
        if (userCertificationNum != 0 && recentOneWeekCertificationNum != 0) {
            userCertificationNumIncreaseRate = Convert.toDouble(((recentOneWeekCertificationNum - recentTwoWeekCertificationNum) / recentOneWeekCertificationNum) * 100);
        }
        dashBoardRes.setUserCertificationNumIncreaseRate(userCertificationNumIncreaseRate);
    }

    /**
     * 获取所有订单状态
     *
     */
    List<Integer> getOrderStates() {
        return Arrays.asList(
                StateCode.ORDER_STATE_WAIT_PAY,
                StateCode.ORDER_STATE_WAIT_PAID,
                StateCode.ORDER_STATE_WAIT_REVIEW,
                StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW,
                StateCode.ORDER_STATE_PICKING,
                StateCode.ORDER_STATE_WAIT_SHIPPING,
                StateCode.ORDER_STATE_SHIPPED,
                StateCode.ORDER_STATE_RECEIVED,
                StateCode.ORDER_STATE_FINISH,
                StateCode.ORDER_STATE_CANCEL,
                StateCode.ORDER_STATE_SELF_PICKUP);
    }

    @Override
    public List<TimelineOutput> getOrderCustomerNumTimeline(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }
        return analyticsOrderDao.getOrderCustomerNumTimeline(input.getStime(), input.getEtime(), chainId, storeId);
    }

    /**
     * 计算本月订单量，今日订单量，昨日订单量，以及日环比
     *
     * @return DashboardTopRes
     */
    @Override
    public DashboardTopRes getOrderDashboardNum() {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        DashboardTopRes topRes = new DashboardTopRes();

        // 获取当日订单量
        TimeRange range = TimeUtil.today();

        //统计没有取消的订单
        List<Integer> orderState = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_PAID, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING, StateCode.ORDER_STATE_SHIPPED, StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH, StateCode.ORDER_STATE_SELF_PICKUP);
        List<Integer> paidState = Arrays.asList(StateCode.ORDER_PAID_STATE_PART, StateCode.ORDER_PAID_STATE_YES);

        OrderNumVo todayOrderNum = analyticsOrderDao.getOrderNum(range.getStart(), range.getEnd(), orderState, null, null, null, chainId, storeId);
        if (todayOrderNum != null) {
            topRes.setToday(todayOrderNum.getOrderNum());
        }

        //昨日
        range = TimeUtil.yestoday();
        OrderNumVo yestodayOrderNum = analyticsOrderDao.getOrderNum(range.getStart(), range.getEnd(), orderState, null, null, null, chainId, storeId);

        if (ObjectUtil.isNotEmpty(yestodayOrderNum)) {
            topRes.setYestoday(yestodayOrderNum.getOrderNum());

            // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
            BigDecimal daym2m = BigDecimal.ZERO;
            if (yestodayOrderNum.getOrderNum().compareTo(BigDecimal.ZERO) != 0) {
                daym2m = (todayOrderNum.getOrderNum().subtract(yestodayOrderNum.getOrderNum())).divide(yestodayOrderNum.getOrderNum(), 2, BigDecimal.ROUND_HALF_UP);
                //.multiply(new BigDecimal("100"));
            } else {

            }

            topRes.setDaym2m(daym2m);
        }

        //本月
        range = TimeUtil.month();
        OrderNumVo monthOrderNum = analyticsOrderDao.getOrderNum(range.getStart(), range.getEnd(), orderState, null, null, null, chainId, storeId);
        if (ObjectUtil.isNotEmpty(monthOrderNum)) {
            topRes.setMonth(monthOrderNum.getOrderNum());
        }

        return topRes;
    }

    @Override
    public AnalyticsNumOutput getOrderDashboardNum(AnalyticsOrderInput input) {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        //统计没有取消的订单
        List<Integer> orderState = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_PAID, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING, StateCode.ORDER_STATE_SHIPPED, StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH, StateCode.ORDER_STATE_SELF_PICKUP);
        List<Integer> paidState = Arrays.asList(StateCode.ORDER_PAID_STATE_PART, StateCode.ORDER_PAID_STATE_YES);
        paidState = null;

        input.setOrderStateId(orderState);
        input.setOrderIsPaid(paidState);

        // 获取当前周期内数据
        OrderNumVo regUser = analyticsOrderDao.getOrderNum(input.getStime(), input.getEtime(), input.getOrderStateId(), input.getOrderIsPaid(), input.getUserId(), input.getKindId(), chainId, storeId);
        CommonNumVo currentRegNum = new CommonNumVo();
        currentRegNum.setNum(regUser.getOrderNum());

        if (currentRegNum != null) {
            topRes.setCurrent(currentRegNum.getNum());
        }

        //上个周期
        AnalyticsOrderInput preInput = BeanUtil.copyProperties(input, AnalyticsOrderInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());


            OrderNumVo preRegUser = analyticsOrderDao.getOrderNum(preInput.getStime(), preInput.getEtime(), preInput.getOrderStateId(), preInput.getOrderIsPaid(), preInput.getUserId(), preInput.getKindId(), chainId, storeId);
            CommonNumVo preRegNum = new CommonNumVo();
            preRegNum.setNum(preRegUser.getOrderNum());


            if (ObjectUtil.isNotEmpty(preRegNum)) {
                topRes.setPre(preRegNum.getNum());

                // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
                BigDecimal daym2m = BigDecimal.ZERO;
                if (preRegNum.getNum().compareTo(BigDecimal.ZERO) != 0) {
                    daym2m = (currentRegNum.getNum().subtract(preRegNum.getNum())).divide(preRegNum.getNum(), 2, BigDecimal.ROUND_HALF_UP);
                    //.multiply(new BigDecimal("100"));
                } else {

                }

                topRes.setDaym2m(daym2m);
            }
        }

        return topRes;
    }

    @Override
    public AnalyticsNumOutput getOrderDashboardAmount(AnalyticsOrderInput input) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        //统计没有取消的订单
        List<Integer> orderState = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_PAID, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING, StateCode.ORDER_STATE_SHIPPED, StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH, StateCode.ORDER_STATE_SELF_PICKUP);
        List<Integer> paidState = Arrays.asList(StateCode.ORDER_PAID_STATE_PART, StateCode.ORDER_PAID_STATE_YES);

        orderState = null;
        input.setOrderStateId(orderState);
        input.setOrderIsPaid(paidState);

        // 获取当前周期内数据
        CommonNumVo currentRegNum = analyticsOrderDao.getOrderAmount(input.getStime(), input.getEtime(), storeId);

        if (currentRegNum == null) {
            currentRegNum = new CommonNumVo();
        }

        topRes.setCurrent(currentRegNum.getNum());

        //上个周期
        AnalyticsOrderInput preInput = BeanUtil.copyProperties(input, AnalyticsOrderInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());

            CommonNumVo preRegNum = analyticsOrderDao.getOrderAmount(preInput.getStime(), preInput.getEtime(), storeId);

            if (ObjectUtil.isNotEmpty(preRegNum)) {
                topRes.setPre(preRegNum.getNum());

                // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
                BigDecimal daym2m = BigDecimal.ZERO;
                if (preRegNum.getNum().compareTo(BigDecimal.ZERO) != 0) {
                    daym2m = (currentRegNum.getNum().subtract(preRegNum.getNum())).divide(preRegNum.getNum(), 2, BigDecimal.ROUND_HALF_UP);
                    //.multiply(new BigDecimal("100"));
                } else {

                }

                topRes.setDaym2m(daym2m);
            }
        }

        return topRes;
    }

    @Override
    public DashboardTopRes getVoucherActiveDashboardNum() {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isEmpty(loginUser.getStoreId())) {
            throw new BusinessException(__("店铺编号为空！"));
        }
        Integer storeId = loginUser.getStoreId();

        DashboardTopRes topRes = new DashboardTopRes();

        // 获取当日订单量
        TimeRange todayRange = TimeUtil.today();

        // 获取当前周期内数据
        CommonNumVo todayNum = analyticsOrderDao.getVoucherActiveNum(todayRange.getStart() == null ? null : new Date(todayRange.getStart()), todayRange.getEnd() == null ? null : new Date(todayRange.getEnd()), storeId);

        if (todayNum != null) {
            topRes.setToday(todayNum.getNum());
        }

        TimeRange yesRange = TimeUtil.yestoday();

        CommonNumVo yestodayNum = analyticsOrderDao.getVoucherActiveNum(yesRange.getStart() == null ? null : new Date(yesRange.getStart()), yesRange.getEnd() == null ? null : new Date(yesRange.getEnd()), storeId);

        if (ObjectUtil.isNotEmpty(yestodayNum)) {
            topRes.setYestoday(yestodayNum.getNum());

            // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
            BigDecimal daym2m = BigDecimal.ZERO;
            if (yestodayNum.getNum().compareTo(BigDecimal.ZERO) != 0) {
                daym2m = (todayNum.getNum().subtract(yestodayNum.getNum())).divide(yestodayNum.getNum(), 2, BigDecimal.ROUND_HALF_UP);
                //.multiply(new BigDecimal("100"));
            } else {

            }

            topRes.setDaym2m(daym2m);
        }

        //本月
        TimeRange monthRange = TimeUtil.month();
        CommonNumVo monthOrderNum = analyticsOrderDao.getVoucherActiveNum(monthRange.getStart() == null ? null : new Date(monthRange.getStart()), monthRange.getEnd() == null ? null : new Date(monthRange.getEnd()), storeId);

        if (ObjectUtil.isNotEmpty(monthOrderNum)) {
            topRes.setMonth(monthOrderNum.getNum());
        }

        return topRes;
    }

    @Override
    public List<TimelineOutput> getOrderNumTimeline(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer chainId = null;
        Integer storeId = null;

        if (loginUser != null) {
            chainId = loginUser.getChainId();

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        return analyticsOrderDao.getOrderTimeLine(input.getStime(), input.getEtime(), chainId, storeId);
    }


    @Override
    public List<TimelineOutput> getOrderItemNumTimeLine(OrderItemNumTimelineInput input) {
        return analyticsOrderDao.getOrderItemNumTimeLine(input);
    }

    @Override
    public AnalyticsNumOutput getOrderItemNum(OrderItemNumTimelineInput input) {
        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        // 获取当前周期内数据
        CommonNumVo currentProductNum = analyticsOrderDao.getOrderItemNum(input);
        if (currentProductNum != null) {
            topRes.setCurrent(currentProductNum.getNum());
        }

        //上个周期
        OrderItemNumTimelineInput preInput = BeanUtil.copyProperties(input, OrderItemNumTimelineInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());


            CommonNumVo preProductNum = analyticsOrderDao.getOrderItemNum(preInput);

            if (ObjectUtil.isNotEmpty(preProductNum)) {
                topRes.setPre(preProductNum.getNum());

                // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
                BigDecimal daym2m = BigDecimal.ZERO;
                if (preProductNum.getNum().compareTo(BigDecimal.ZERO) != 0) {
                    daym2m = (currentProductNum.getNum().subtract(preProductNum.getNum())).divide(preProductNum.getNum(), 2, BigDecimal.ROUND_HALF_UP);
                    //.multiply(new BigDecimal("100"));
                } else {

                }

                topRes.setDaym2m(daym2m);
            }

        }

        return topRes;
    }

    @Override
    public List<AnalyticsOrderItemNumOutput> listOrderItemNum(OrderItemNumTimelineInput input) {
        List<AnalyticsOrderItemNumOutput> outputs = analyticsOrderDao.listOrderItemNum(input);

        if (CollectionUtil.isNotEmpty(outputs)) {
            List<Long> itemIds = CommonUtil.column(outputs, AnalyticsOrderItemNumOutput::getItemId);

            Map<Long, Integer> itemCartQuantityMap = new HashMap<>();
            Map<Long, Integer> accessMap = new HashMap<>();

            QueryWrapper<UserCart> userCartQueryWrapper = new QueryWrapper<>();
            userCartQueryWrapper.in("item_id", itemIds);
            List<UserCart> userCarts = userCartRepository.find(userCartQueryWrapper);

            if (CollectionUtil.isNotEmpty(userCarts)) {
                itemCartQuantityMap = userCarts.stream().collect(Collectors.groupingBy(UserCart::getItemId, Collectors.summingInt(UserCart::getCartQuantity)));
            }

            List<AnalyticsAccessHistoryOutput> userByItemIds = accessHistoryDao.getUserByItemIds(itemIds);

            if (CollectionUtil.isNotEmpty(userByItemIds)) {
                accessMap = userByItemIds.stream().collect(Collectors.toMap(AnalyticsAccessHistoryOutput::getItemId, AnalyticsAccessHistoryOutput::getUserNum, (k1, k2) -> k1));
            }

            for (AnalyticsOrderItemNumOutput output : outputs) {
                Long itemId = output.getItemId();
                Integer userNum = output.getUserNum();

                int sum = itemCartQuantityMap.getOrDefault(itemId, 0);
                output.setCartNum(sum);

                if (accessMap.containsKey(itemId)) {
                    Integer allNum = accessMap.get(itemId);
                    BigDecimal rate = BigDecimal.valueOf(userNum)
                            .divide(BigDecimal.valueOf(allNum), 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

                    output.setConversionRate(rate.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(100) : rate);
                }
            }
        }

        return outputs;
    }
}
