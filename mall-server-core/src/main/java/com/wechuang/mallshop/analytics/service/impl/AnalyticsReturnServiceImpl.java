package com.wechuang.mallshop.analytics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.analytics.dao.AnalyticsReturnDao;
import com.wechuang.mallshop.analytics.model.input.AnalyticsReturnInput;
import com.wechuang.mallshop.analytics.model.input.OrderItemNumTimelineInput;
import com.wechuang.mallshop.analytics.model.input.TimelineInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsOrderItemNumOutput;
import com.wechuang.mallshop.analytics.model.output.TimelineOutput;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.service.AnalyticsReturnService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.TimeRange;
import com.wechuang.mallshop.common.utils.TimeUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

@Service
public class AnalyticsReturnServiceImpl implements AnalyticsReturnService {
    @Autowired
    private AnalyticsReturnDao analyticsReturnDao;

    @Override
    public List<TimelineOutput> getReturnAmountTimeline(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }
        //统计没有取消的退单
        List<Integer> returnStateIds = Arrays.asList(StateCode.RETURN_PROCESS_FINISH, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND, StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION, StateCode.RETURN_PROCESS_REFUSED, StateCode.RETURN_PROCESS_SUBMIT);

        return analyticsReturnDao.getReturnAmountTimeline(input.getStime(), input.getEtime(), returnStateIds, storeId);
    }

    @Override
    public AnalyticsNumOutput getReturnNum(AnalyticsReturnInput input) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }
        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        //统计没有取消的退单
        List<Integer> returnStateIds = Arrays.asList(StateCode.RETURN_PROCESS_FINISH, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND, StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION, StateCode.RETURN_PROCESS_REFUSED, StateCode.RETURN_PROCESS_SUBMIT);
        input.setReturnStateId(returnStateIds);

        // 获取当前周期内数据
        CommonNumVo currentRegNum = analyticsReturnDao.getReturnNum(input.getStime(), input.getEtime(), input.getReturnStateId(), storeId);

        if (currentRegNum != null) {
            topRes.setCurrent(currentRegNum.getNum());
        }

        //上个周期
        AnalyticsReturnInput preInput = BeanUtil.copyProperties(input, AnalyticsReturnInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());

            CommonNumVo preRegNum = analyticsReturnDao.getReturnNum(preInput.getStime(), preInput.getEtime(), preInput.getReturnStateId(), storeId);

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
    public AnalyticsNumOutput getReturnAmount(AnalyticsReturnInput input) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        //统计没有取消的退单
        List<Integer> returnStateIds = Arrays.asList(StateCode.RETURN_PROCESS_FINISH, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND, StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION, StateCode.RETURN_PROCESS_REFUSED, StateCode.RETURN_PROCESS_SUBMIT);
        input.setReturnStateId(returnStateIds);

        // 获取当前周期内数据
        CommonNumVo currentRegNum = analyticsReturnDao.getReturnAmount(input.getStime(), input.getEtime(), input.getReturnStateId(), storeId);

        if (currentRegNum == null) {
            currentRegNum = new CommonNumVo();
        }

        topRes.setCurrent(currentRegNum.getNum());

        //上个周期
        AnalyticsReturnInput preInput = BeanUtil.copyProperties(input, AnalyticsReturnInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());

            CommonNumVo preRegNum = analyticsReturnDao.getReturnAmount(preInput.getStime(), preInput.getEtime(), input.getReturnStateId(), storeId);

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
    public DashboardTopRes getReturnNumToday() {
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

        //统计没有取消的退单
        List<Integer> returnStateIds = Arrays.asList(StateCode.RETURN_PROCESS_FINISH, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND, StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION, StateCode.RETURN_PROCESS_REFUSED, StateCode.RETURN_PROCESS_SUBMIT);

        // 获取当前周期内数据
        CommonNumVo todayNum = analyticsReturnDao.getReturnNum(todayRange.getStart(), todayRange.getEnd(), returnStateIds, storeId);

        if (todayNum != null) {
            topRes.setToday(todayNum.getNum());
        }

        TimeRange yesRange = TimeUtil.yestoday();

        CommonNumVo yestodayNum = analyticsReturnDao.getReturnNum(yesRange.getStart(), yesRange.getEnd(), returnStateIds, storeId);

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
        CommonNumVo monthOrderNum = analyticsReturnDao.getReturnNum(monthRange.getStart(), monthRange.getEnd(), returnStateIds, storeId);

        if (ObjectUtil.isNotEmpty(monthOrderNum)) {
            topRes.setMonth(monthOrderNum.getNum());
        }

        return topRes;
    }

    @Override
    public List<TimelineOutput> getReturnNumTimeline(TimelineInput input) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }
        //统计没有取消的退单
        List<Integer> returnStateIds = Arrays.asList(StateCode.RETURN_PROCESS_FINISH, StateCode.RETURN_PROCESS_CHECK, StateCode.RETURN_PROCESS_RECEIVED, StateCode.RETURN_PROCESS_REFUND, StateCode.RETURN_PROCESS_RECEIPT_CONFIRMATION, StateCode.RETURN_PROCESS_REFUSED, StateCode.RETURN_PROCESS_SUBMIT);

        return analyticsReturnDao.getReturnTimeLine(input.getStime(), input.getEtime(), returnStateIds, storeId);
    }

    @Override
    public List<TimelineOutput> getReturnItemNumTimeLine(OrderItemNumTimelineInput input) {
        return analyticsReturnDao.getReturnItemNumTimeLine(input);
    }

    @Override
    public AnalyticsNumOutput getReturnItemNum(OrderItemNumTimelineInput input) {
        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        // 获取当前周期内数据
        CommonNumVo currentProductNum = analyticsReturnDao.getReturnItemNum(input);
        if (currentProductNum != null) {
            topRes.setCurrent(currentProductNum.getNum());
        }

        //上个周期
        OrderItemNumTimelineInput preInput = BeanUtil.copyProperties(input, OrderItemNumTimelineInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());


            CommonNumVo preProductNum = analyticsReturnDao.getReturnItemNum(preInput);

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
    public List<AnalyticsOrderItemNumOutput> listReturnItemNum(OrderItemNumTimelineInput input) {
        return analyticsReturnDao.listReturnItemNum(input);
    }

    /**
     * 退单客户统计
     *
     * @param input
     * @return
     */
    @Override
    public List<TimelineOutput> getReturnCustomerNumTimeline(TimelineInput input) {
        return null;
    }
}
