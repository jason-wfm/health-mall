package com.wechuang.mallshop.analytics.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.analytics.dao.AnalytiscTradeDao;
import com.wechuang.mallshop.analytics.model.res.DashboardTopRes;
import com.wechuang.mallshop.analytics.model.vo.TradeAmountVo;
import com.wechuang.mallshop.analytics.service.AnalytiscTradeService;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.TimeRange;
import com.wechuang.mallshop.common.utils.TimeUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

@Service
public class AnalytiscTradeServiceImpl implements AnalytiscTradeService {

    @Autowired
    private AnalytiscTradeDao analytiscTradeDao;

    /**
     * 计算本月销售额，今日销售额，昨日销售额，以及日环比
     *
     * @return DashboardTopRes
     */
    @Override
    public DashboardTopRes getSalesAmount() {
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

        // 获取当日交易额
        TimeRange range = TimeUtil.today();
        TradeAmountVo todayTradeAmount = analytiscTradeDao.getTradeAmount(range.getStart(), range.getEnd(), null, chainId, storeId);

        if (todayTradeAmount == null) {
            todayTradeAmount = new TradeAmountVo();
        } else {
            topRes.setToday(todayTradeAmount.getAmount());
        }

        //昨日
        range = TimeUtil.yestoday();
        TradeAmountVo yestodayTradeAmount = analytiscTradeDao.getTradeAmount(range.getStart(), range.getEnd(), null, chainId, storeId);

        if (ObjectUtil.isNotEmpty(yestodayTradeAmount)) {
            topRes.setYestoday(yestodayTradeAmount.getAmount());

            // 计算日环比 日环比 = (当日数据 - 前一日数据) / 前一日数据 * 100%
            BigDecimal daym2m = BigDecimal.ZERO;
            if (yestodayTradeAmount.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                daym2m = (todayTradeAmount.getAmount().subtract(yestodayTradeAmount.getAmount())).divide(yestodayTradeAmount.getAmount(), 2, BigDecimal.ROUND_HALF_UP);
                //.multiply(new BigDecimal("100"));
            } else {

            }

            topRes.setDaym2m(daym2m);
        }

        //本月
        range = TimeUtil.month();
        TradeAmountVo monthTradeAmount = analytiscTradeDao.getTradeAmount(range.getStart(), range.getEnd(), null, chainId, storeId);
        if (ObjectUtil.isNotEmpty(monthTradeAmount)) {
            topRes.setMonth(monthTradeAmount.getAmount());
        }

        return topRes;
    }

}
