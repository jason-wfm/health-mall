package com.wechuang.mallshop.analytics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.analytics.dao.AnalyticsProductDao;
import com.wechuang.mallshop.analytics.model.input.AnalyticsProductInput;
import com.wechuang.mallshop.analytics.model.output.AnalyticsNumOutput;
import com.wechuang.mallshop.analytics.model.vo.CommonNumVo;
import com.wechuang.mallshop.analytics.service.AnalyticsProductService;
import com.wechuang.mallshop.common.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AnalyticsProductServiceImpl implements AnalyticsProductService {
    @Autowired
    private AnalyticsProductDao analyticsProductDao;


    /**
     * 商品数量
     *
     * @return
     */
    @Override
    public AnalyticsNumOutput getProductDashboardNum(AnalyticsProductInput input) {

        AnalyticsNumOutput topRes = new AnalyticsNumOutput();

        // 获取当前周期内数据
        CommonNumVo currentProductNum = analyticsProductDao.getProductNum(input);
        if (currentProductNum != null) {
            topRes.setCurrent(currentProductNum.getNum());
        }

        //上个周期
        AnalyticsProductInput preInput = BeanUtil.copyProperties(input, AnalyticsProductInput.class);

        if (CheckUtil.isNotEmpty(input.getStime()) && CheckUtil.isNotEmpty(input.getEtime())) {
            preInput.setStime(input.getStime() - (input.getEtime() - input.getStime()));
            preInput.setEtime(input.getStime());


            CommonNumVo preProductNum = analyticsProductDao.getProductNum(preInput);

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
}
