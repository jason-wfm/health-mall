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
package com.wechuang.mallshop.shop.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.service.ProductBaseService;
import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import com.wechuang.mallshop.shop.model.req.StoreAnalyticsListReq;
import com.wechuang.mallshop.shop.repository.StoreAnalyticsRepository;
import com.wechuang.mallshop.shop.service.StoreAnalyticsService;
import com.wechuang.mallshop.trade.model.vo.StoreAnalyticsVo;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 店铺统计表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-08-01
 */
@Service
public class StoreAnalyticsServiceImpl extends BaseServiceImpl<StoreAnalyticsRepository, StoreAnalytics, StoreAnalyticsListReq> implements StoreAnalyticsService {

    @Autowired
    private ProductBaseService productBaseService;

    @Override
    public List<StoreInfoVo> getAnalytics(List<Integer> storeIds, List<StoreInfoVo> infoVoList) {
        List<StoreAnalytics> analyticsList = gets(storeIds);

        // 构造 Map，加快查找效率
        Map<Integer, StoreAnalyticsVo> analyticsMap = new HashMap<>();
        for (StoreAnalytics analytics : analyticsList) {
            recalculateCredits(analytics);

            StoreAnalyticsVo analyticsVo = new StoreAnalyticsVo();
            BeanUtils.copyProperties(analytics, analyticsVo);
            analyticsMap.put(analyticsVo.getStoreId(), analyticsVo);
        }

        for (StoreInfoVo infoVo : infoVoList) {
            StoreAnalyticsVo analyticsVo = analyticsMap.get(infoVo.getStoreId());
            if (analyticsVo != null) {
                BeanUtils.copyProperties(analyticsVo, infoVo);
            }
        }

        return infoVoList;
    }

    @Override
    public StoreAnalytics getStoreAnalytics(Integer storeId) {
        StoreAnalytics storeAnalytics = get(storeId);
        if (storeAnalytics != null) {
            recalculateCredits(storeAnalytics);
        }

        return storeAnalytics;
    }


    // 评分计算逻辑
    private void recalculateCredits(StoreAnalytics analytics) {
        Integer evaluationNum = analytics.getStoreEvaluationNum();
        if (CheckUtil.isNotEmpty(evaluationNum) && evaluationNum != 0) {
            BigDecimal storeDescCredit = NumberUtil.div(analytics.getStoreDesccredit(), evaluationNum, 1);
            BigDecimal storeServiceCredit = NumberUtil.div(analytics.getStoreServicecredit(), evaluationNum, 1);
            BigDecimal StoreDeliveryCredit = NumberUtil.div(analytics.getStoreDeliverycredit(), evaluationNum, 1);

            analytics.setStoreDesccredit(storeDescCredit);
            analytics.setStoreServicecredit(storeServiceCredit);
            analytics.setStoreDeliverycredit(StoreDeliveryCredit);

            // 综合评分 = 描述相符评分 + 服务态度评分 + 物流评分 / 3
            analytics.setStoreEvaluationRate(NumberUtil.div(NumberUtil.add(storeDescCredit, storeServiceCredit, StoreDeliveryCredit), 3, 1));
        }
    }

    @Override
    public boolean saveProductAnalyticsNum(Integer storeId) {
        StoreAnalytics storeAnalytics = get(storeId);

        if (storeAnalytics == null) {
            throw new BusinessException(__("该店铺统计信息不存在！"));
        }

        StoreAnalytics analytics = new StoreAnalytics();
        long storeProductNewNum = productBaseService.getProductNum(StateCode.PRODUCT_STATE_NORMAL, null, storeId, -30, null);
        long storeProductNormalNum = productBaseService.getProductNum(StateCode.PRODUCT_STATE_NORMAL, null, storeId, null, null);

        analytics.setStoreId(storeId);
        analytics.setStoreProductNewNum(Convert.toInt(storeProductNewNum));
        analytics.setStoreProductNormalNum(Convert.toInt(storeProductNormalNum));

        if (!edit(analytics)) {
            throw new BusinessException(__("更新商品数量统计失败！"));
        }

        return true;
    }

}
