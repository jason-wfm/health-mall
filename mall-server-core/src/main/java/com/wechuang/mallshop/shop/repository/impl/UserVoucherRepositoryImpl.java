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
package com.wechuang.mallshop.shop.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.marketing.model.vo.RequirementVo;
import com.wechuang.mallshop.marketing.model.vo.VoucherVo;
import com.wechuang.mallshop.shop.dao.UserVoucherDao;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.model.res.UserVoucherRes;
import com.wechuang.mallshop.shop.repository.UserVoucherRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 用户优惠券表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-06-29
 */
@Repository
public class UserVoucherRepositoryImpl extends BaseRepositoryImpl<UserVoucherDao, UserVoucher> implements UserVoucherRepository {

    @Override
    public Integer countByActivityIdAndUserIdAndSource(Integer activityId, Integer userId, String source, String orderSn) {
        LambdaQueryWrapper<UserVoucher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserVoucher::getActivityId, activityId)
                .eq(UserVoucher::getUserId, userId)
                .eq(StrUtil.isNotEmpty(source), UserVoucher::getVoucherSource, source);

        if (StrUtil.isNotEmpty(orderSn)) {
            wrapper.eq(UserVoucher::getSourceOrderSn, orderSn);
        }

        return Math.toIntExact(count(wrapper));
    }

    @Override
    public Map<Integer, Integer> getUserReceivedCount(List<Integer> activityIds, Integer userId, String grantType) {
        if (CollectionUtil.isEmpty(activityIds) || userId == null) {
            return new HashMap<>();
        }

        QueryWrapper<UserVoucher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("activity_id", "COUNT(*) as cnt");
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("activity_id", activityIds);

        if (StrUtil.isNotBlank(grantType)) {
            queryWrapper.eq("voucher_source", grantType);
        }

        // 月度券限制周期
        if (StateCode.VOUCHER_SOURCE_PLUS_MONTHLY.equals(grantType)) {
            String period = DateUtil.format(new Date(), "yyyy-MM"); // 2026-04
            queryWrapper.eq("voucher_period", period);
        }

        queryWrapper.groupBy("activity_id");

        List<Map<String, Object>> list = baseMapper.selectMaps(queryWrapper);

        Map<Integer, Integer> result = new HashMap<>();

        for (Map<String, Object> row : list) {
            result.put(
                    Convert.toInt(row.get("activity_id")),
                    Convert.toInt(row.get("cnt"))
            );
        }

        return result;
    }

    @Override
    public boolean hasReceivedMonthly(Integer userId, String grantType) {

        String period = DateUtil.format(new Date(), "yyyy-MM"); // 2026-04

        QueryWrapper<UserVoucher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("voucher_source", grantType);
        queryWrapper.eq("voucher_period", period);
        queryWrapper.last("LIMIT 1");

        return count(queryWrapper) > 0;
    }

    @Override
    public void fillVoucherInfo(UserVoucherRes vo, ActivityRuleVo rule) {

        if (vo.getActivityName() != null) {
            vo.setActivityName(vo.getActivityName());
        }

        if (vo.getItemId() != null) {
            vo.setItemId(vo.getItemId());
        }

        if (rule == null) return;

        VoucherVo voucherVo = rule.getVoucher();

        if (voucherVo != null) {
            vo.setVoucherPrice(voucherVo.getVoucherPrice());
            vo.setVoucherStartDate(voucherVo.getVoucherStartDate());
            vo.setVoucherEndDate(voucherVo.getVoucherEndDate());
            vo.setVoucherProductLimit(voucherVo.getVoucherProductLimit());

            if (StrUtil.isNotBlank(voucherVo.getVoucherImage())) {
                vo.setVoucherImage(voucherVo.getVoucherImage());
            }

            vo.setVoucherPreQuantity(voucherVo.getVoucherPreQuantity());
        }

        RequirementVo requirement = rule.getRequirement();
        if (requirement != null && requirement.getBuy() != null) {
            vo.setVoucherSubtotal(requirement.getBuy().getSubtotal());

            if (StrUtil.isBlank(vo.getItemId())) {
                List<Long> item = requirement.getBuy().getItem();
                if (CollUtil.isNotEmpty(item)) {
                    vo.setItemId(CollUtil.join(item, ","));
                }
            }
        }
    }

}
