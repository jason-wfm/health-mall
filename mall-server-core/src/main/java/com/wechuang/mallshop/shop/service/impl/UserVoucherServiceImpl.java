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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.web.service.MessageService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.res.ActivityBaseRes;
import com.wechuang.mallshop.marketing.model.vo.*;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.model.entity.UserVoucherNum;
import com.wechuang.mallshop.shop.model.req.UserVoucherListReq;
import com.wechuang.mallshop.shop.model.res.UserVoucherRes;
import com.wechuang.mallshop.shop.model.res.VoucherCountRes;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.repository.UserVoucherNumRepository;
import com.wechuang.mallshop.shop.repository.UserVoucherRepository;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.CurrencyBaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户优惠券表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-06-29
 */
@Service
public class UserVoucherServiceImpl extends BaseServiceImpl<UserVoucherRepository, UserVoucher, UserVoucherListReq> implements UserVoucherService {

    @Autowired
    private ActivityBaseService activityBaseService;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private CurrencyBaseService currencyBaseService;

    @Autowired
    private UserVoucherNumRepository userVoucherNumRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private UserAdminService userAdminService;

    @Override
    public VoucherCountRes getEachVoucherNum(Integer voucherStateId, Integer userId) {
        VoucherCountRes voucherCountRes = new VoucherCountRes();
        QueryWrapper<UserVoucher> userVoucherQueryWrapper = new QueryWrapper<>();
        userVoucherQueryWrapper.eq("user_id", userId);

        if (CheckUtil.isNotEmpty(voucherStateId)) {
            userVoucherQueryWrapper.eq("voucher_state_id", voucherStateId);
        }
        // 全部优惠券数量
        voucherCountRes.setVoucherAllNum(count(userVoucherQueryWrapper));

        QueryWrapper<UserVoucher> offlineQuery = new QueryWrapper<>();
        offlineQuery.eq("user_id", userId);
        offlineQuery.gt("length(writeoff_code)", 0);

        if (CheckUtil.isNotEmpty(voucherStateId)) {
            offlineQuery.eq("voucher_state_id", voucherStateId);
        }
        // 线下优惠券数量
        voucherCountRes.setVoucherOfflinedNum(count(offlineQuery));

        QueryWrapper<UserVoucher> onlineQuery = new QueryWrapper<>();
        onlineQuery.eq("user_id", userId);
        onlineQuery.eq("writeoff_code", "");

        if (CheckUtil.isNotEmpty(voucherStateId)) {
            onlineQuery.eq("voucher_state_id", voucherStateId);
        }

        // 线上优惠券数量
        voucherCountRes.setVoucherOnlinedNum(count(onlineQuery));

        QueryWrapper<UserVoucher> unusedQuery = new QueryWrapper<>();
        unusedQuery.eq("user_id", userId)
                .eq("voucher_state_id", StateCode.VOUCHER_STATE_UNUSED);
        // 未使用优惠券
        voucherCountRes.setVoucherUnusedNum(count(unusedQuery));

        QueryWrapper<UserVoucher> userQuery = new QueryWrapper<>();
        userQuery.eq("user_id", userId)
                .eq("voucher_state_id", StateCode.VOUCHER_STATE_USED);
        // 已使用优惠券
        voucherCountRes.setVoucherUsedNum(count(userQuery));

        QueryWrapper<UserVoucher> timeoutQuery = new QueryWrapper<>();
        timeoutQuery.eq("user_id", userId)
                .eq("voucher_state_id", StateCode.VOUCHER_STATE_TIMEOUT);
        // 已过期优惠券
        voucherCountRes.setVoucherTimeoutNum(count(timeoutQuery));

        return voucherCountRes;
    }

    @Override
    public IPage<UserVoucherRes> getList(UserVoucherListReq voucherListReq) {
        IPage<UserVoucherRes> voucherResPage = new Page<>();
        QueryWrapper<UserVoucher> voucherQueryWrapper = new QueryWrapper<>();

        if (CheckUtil.isNotEmpty(voucherListReq.getUserId())) {
            voucherQueryWrapper.eq("user_id", voucherListReq.getUserId());
        }

        if (CheckUtil.isNotEmpty(voucherListReq.getActivityId())) {
            voucherQueryWrapper.eq("activity_id", voucherListReq.getActivityId());
        }

        if (CheckUtil.isNotEmpty(voucherListReq.getStoreId())) {
            voucherQueryWrapper.eq("store_id", voucherListReq.getStoreId());
        }
        //优惠券是否生效
        long time = new Date().getTime();

        if (voucherListReq.getVoucherEffect()) {
            voucherQueryWrapper.le("voucher_start_date", time);
            voucherQueryWrapper.ge("voucher_end_date", time);
        }
        // 处理全部券1、线下券2、线上券3
        if (CheckUtil.isNotEmpty(voucherListReq.getVoucherUserWay())) {
            switch (voucherListReq.getVoucherUserWay()) {
                case 2:
                    voucherQueryWrapper.ne("writeoff_code", "");
                    break;
                case 3:
                    voucherQueryWrapper.eq("writeoff_code", "");
                    break;
            }
        }

        if (CheckUtil.isNotEmpty(voucherListReq.getVoucherStateId())) {
            voucherQueryWrapper.eq("voucher_state_id", voucherListReq.getVoucherStateId());
        }

        voucherQueryWrapper.orderByDesc("user_voucher_time");
        voucherQueryWrapper.orderByAsc("voucher_state_id");
        IPage<UserVoucher> voucherPage = lists(voucherQueryWrapper, voucherListReq.getPage(), voucherListReq.getSize());

        if (voucherPage != null && CollectionUtil.isNotEmpty(voucherPage.getRecords())) {
            BeanUtils.copyProperties(voucherPage, voucherResPage);
            voucherResPage.setRecords(BeanUtil.copyToList(voucherPage.getRecords(), UserVoucherRes.class));

            List<UserVoucher> userVoucherList = voucherPage.getRecords();
            List<UserVoucherRes> userVoucherReList = new ArrayList<>();
            Long currentTime = new Date().getTime();

            Map<Integer, UserInfo> userInfoMap = userInfoRepository.getUserInfoMap(CommonUtil.column(userVoucherList, UserVoucher::getUserId));

            List<StoreBase> storeBases = storeBaseRepository.gets(CommonUtil.column(userVoucherList, UserVoucher::getStoreId));
            Map<Integer, String> storeNameMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(storeBases)) {
                storeNameMap = storeBases.stream().collect(Collectors.toMap(StoreBase::getStoreId, StoreBase::getStoreName, (k1, k2) -> k1));
            }

            for (UserVoucher userVoucher : userVoucherList) {
                UserVoucherRes userVoucherRes = new UserVoucherRes();
                BeanUtils.copyProperties(userVoucher, userVoucherRes);
                userVoucherRes.setId(userVoucher.getUserVoucherId());

                if (CollUtil.isNotEmpty(userInfoMap)) {
                    UserInfo userInfo = userInfoMap.get(userVoucher.getUserId());

                    if (userInfo != null) {
                        userVoucherRes.setUserNickname(userInfo.getUserNickname());
                    }
                }
                Integer storeId = userVoucher.getStoreId();

                if (CheckUtil.isNotEmpty(storeId) && storeNameMap.containsKey(storeId)) {
                    userVoucherRes.setStoreName(storeNameMap.get(storeId));
                }

                long voucherEndDate = userVoucher.getVoucherEndDate();

                if (Objects.equals(userVoucher.getVoucherStateId(), StateCode.VOUCHER_STATE_UNUSED)) {
                    if (voucherEndDate < currentTime) {
                        userVoucherRes.setVoucherStateId(StateCode.VOUCHER_STATE_TIMEOUT);
                        // 更新数据
                        UserVoucher voucher = new UserVoucher();
                        voucher.setUserVoucherId(userVoucher.getUserVoucherId());
                        voucher.setVoucherStateId(StateCode.VOUCHER_STATE_TIMEOUT);
                        edit(voucher);

                        // 优惠券过期提醒
                        String messageId = "imminent-expiration-reminder";
                        Map<String, Object> args = new HashMap<>();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        args.put("endtime", simpleDateFormat.format(voucherEndDate));
                        messageService.sendNoticeMsg(userVoucher.getUserId(), messageId, args);
                    }
                    //未生效标记
                    Long voucherStartDate = userVoucher.getVoucherStartDate();

                    if (voucherStartDate > currentTime) {
                        userVoucherRes.setVoucherEffect(false);
                    }
                }

                userVoucherReList.add(userVoucherRes);
            }

            voucherResPage.setRecords(userVoucherReList);
        }

        return voucherResPage;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserVoucher addVoucher(Integer activityId, Integer userId, Long orderItemId, String grantType) {

        ActivityBase activityBase = activityBaseRepository.get(activityId);
        if (activityBase == null) {
            throw new BusinessException(__("活动不存在！"));
        }

        if (!Objects.equals(activityBase.getActivityState(), StateCode.ACTIVITY_STATE_NORMAL)) {
            throw new BusinessException(__("活动未开启,领取失败！"));
        }

        // 解析活动规则
        ActivityRuleVo activityRuleVo = parseActivityRule(activityBase);
        VoucherVo voucherVo = activityRuleVo.getVoucher();
        if (voucherVo == null) {
            throw new BusinessException(__("活动优惠券信息为空！"));
        }

        // 等级限制校验
        validateUserLevel(activityBase, userId);

        // 新增 PLUS 专享券校验
        validatePlusPrivilege(activityBase, userId, grantType);

        // 库存校验
        validateVoucherStock(voucherVo);

        if (!activityBase.getActivityIsPlus()) {
            // 全局限领校验
            validateGlobalLimit(activityId, userId, voucherVo);
        }

        // 积分扣减（如需）
        handlePointsDeduction(activityBase, activityRuleVo, userId);

        UserVoucher voucher = new UserVoucher();
        voucher.setUserVoucherTime(new Date());
        voucher.setActivityId(activityId);
        voucher.setVoucherStateId(StateCode.VOUCHER_STATE_UNUSED);
        voucher.setUserId(userId);
        voucher.setVoucherSource(grantType); // 设置来源
        String period = DateUtil.format(new Date(), "yyyy-MM"); // 2026-04
        voucher.setVoucherPeriod(period);  // 设置周期
        voucher.setActivityIsPlus(activityBase.getActivityIsPlus()); // 是否PLUS专享

        if (CheckUtil.isNotEmpty(orderItemId)) {
            voucher.setSrcOrderItemId(orderItemId);
        }

        // 保存用户优惠券
        saveUserVoucher(voucher, activityBase, voucherVo, activityRuleVo);

        // 更新活动库存
        updateActivityStock(activityBase, activityId, voucherVo, activityRuleVo);

        // 发送到账通知
        sendVoucherNotice(userId, activityBase);

        return voucher;
    }

    /**
     * 校验用户等级是否符合优惠券领取条件
     */
    private void validateUserLevel(ActivityBase activityBase, Integer userId) {
        String activityUseLevel = activityBase.getActivityUseLevel();
        if (StrUtil.isEmpty(activityUseLevel)) {
            return;
        }

        List<Integer> userLevels = Convert.toList(Integer.class, activityUseLevel);
        if (CollectionUtil.isEmpty(userLevels)) {
            return;
        }

        UserInfo userInfo = userInfoRepository.get(userId);
        if (userInfo == null) {
            throw new BusinessException(__("用户信息不存在！"));
        }

        if (!userLevels.contains(userInfo.getUserLevelId())) {
            throw new BusinessException(__("不属于该优惠券指定的会员等级，领取失败！"));
        }
    }

    /**
     * 解析活动规则 JSON
     */
    private ActivityRuleVo parseActivityRule(ActivityBase activityBase) {
        String activityRule = activityBase.getActivityRule();
        ActivityRuleVo activityRuleVo = com.wechuang.mallshop.common.utils.JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
        if (activityRuleVo == null) {
            throw new BusinessException(__("活动规则为空！"));
        }

        return activityRuleVo;
    }

    /**
     * 校验 PLUS 专享券领取权限（开源版已移除 PLUS）
     */
    private void validatePlusPrivilege(ActivityBase activityBase, Integer userId, String grantType) {
        Boolean isPlusActivity = activityBase.getActivityIsPlus();
        if (Boolean.TRUE.equals(isPlusActivity)) {
            throw new BusinessException(__("开源版不支持PLUS专享优惠券"));
        }
    }

    /**
     * 校验优惠券库存
     */
    private void validateVoucherStock(VoucherVo voucherVo) {
        Integer voucherQuantity = voucherVo.getVoucherQuantity();
        Integer voucherQuantityUse = ObjectUtil.defaultIfNull(voucherVo.getVoucherQuantityUse(), 0);
        Integer remainingQuantity = NumberUtil.sub(voucherQuantity, voucherQuantityUse).intValue();

        if (remainingQuantity <= 0) {
            throw new BusinessException(__("代金券已经被抢完,领取失败！"));
        }
    }

    /**
     * 校验全局限领次数
     */
    private void validateGlobalLimit(Integer activityId, Integer userId, VoucherVo voucherVo) {
        Integer voucherPreQuantity = voucherVo.getVoucherPreQuantity(); // 每人限领数量
        if (voucherPreQuantity == null || voucherPreQuantity <= 0) {
            return;
        }

        Integer voucherSize = 0; // 用户已领取数量
        UserVoucherNum userVoucherNum = userVoucherNumRepository.findOne(
                new QueryWrapper<UserVoucherNum>()
                        .eq("activity_id", activityId)
                        .eq("user_id", userId)
        );

        if (ObjectUtil.isNotEmpty(userVoucherNum)) {
            voucherSize = userVoucherNum.getUvnNum();
        }

        if (voucherSize >= voucherPreQuantity) {
            throw new BusinessException(__("领取数量超限！"));
        }
    }

    /**
     * 处理积分兑换扣减
     */
    private void handlePointsDeduction(ActivityBase activityBase, ActivityRuleVo activityRuleVo, Integer userId) {
        Integer activityType = activityBase.getActivityType();
        if (Objects.equals(activityType, StateCode.GET_VOUCHER_BY_POINT)) {
            throw new BusinessException(__("开源版不支持积分兑换优惠券活动"));
        }
    }

    /**
     * 保存用户优惠券并更新领取计数
     */
    private void saveUserVoucher(UserVoucher voucher, ActivityBase activityBase,
                                 VoucherVo voucherVo, ActivityRuleVo activityRuleVo) {
        RequirementVo requirement = activityRuleVo.getRequirement();
        BuyVo buy = requirement.getBuy();

        voucher.setVoucherSubtotal(buy.getSubtotal());
        voucher.setVoucherPrice(voucherVo.getVoucherPrice());
        voucher.setVoucherStartDate(voucherVo.getVoucherStartDate());
        voucher.setVoucherEndDate(voucherVo.getVoucherEndDate());
        voucher.setStoreId(activityBase.getStoreId());
        voucher.setActivityName(activityBase.getActivityName());
        voucher.setActivityRule(activityBase.getActivityRule());

        List<Long> item = buy.getItem();
        if (CollUtil.isNotEmpty(item)) {
            voucher.setItemId(CollUtil.join(item, ","));
        }

        if (!add(voucher)) {
            throw new BusinessException(__("用户优惠券领取失败！"));
        }

        // 更新用户领取计数
        updateUserVoucherNum(activityBase.getActivityId(), voucher.getUserId());
    }

    /**
     * 更新用户在该活动的领取次数
     */
    private void updateUserVoucherNum(Integer activityId, Integer userId) {
        UserVoucherNum userVoucherNum = userVoucherNumRepository.findOne(
                new QueryWrapper<UserVoucherNum>()
                        .eq("activity_id", activityId)
                        .eq("user_id", userId)
        );

        if (ObjectUtil.isNotEmpty(userVoucherNum)) {
            userVoucherNum.setUvnNum(userVoucherNum.getUvnNum() + 1);
            if (!userVoucherNumRepository.edit(userVoucherNum)) {
                throw new BusinessException(__("优惠券领取失败！"));
            }
        } else {
            userVoucherNum = new UserVoucherNum();
            userVoucherNum.setActivityId(activityId);
            userVoucherNum.setUserId(userId);
            userVoucherNum.setUvnNum(1);
            if (!userVoucherNumRepository.add(userVoucherNum)) {
                throw new BusinessException(__("优惠券领取失败！"));
            }
        }
    }

    /**
     * 更新活动库存状态
     */
    private void updateActivityStock(ActivityBase activityBase, Integer activityId, VoucherVo voucherVo, ActivityRuleVo activityRuleVo) {
        Integer voucherQuantity = voucherVo.getVoucherQuantity();
        Integer voucherQuantityUse = voucherVo.getVoucherQuantityUse() == null ? 1 : voucherVo.getVoucherQuantityUse() + 1;
        Integer remainingQuantity = NumberUtil.sub(voucherQuantity, voucherQuantityUse).intValue();
        if (remainingQuantity < 0) {
            throw new BusinessException(__("代金券已经被抢完,领取失败！"));
        }

        voucherVo.setVoucherQuantityUse(voucherQuantityUse);
        voucherVo.setVoucherQuantityFree(remainingQuantity);
        activityRuleVo.setVoucher(voucherVo);

        ActivityBase activity = new ActivityBase();
        activity.setActivityId(activityId);
        activity.setActivityRule(com.wechuang.mallshop.common.utils.JSONUtil.toJSONString(activityRuleVo));

        // 是否领完判断
        if (remainingQuantity <= 0) {
            activity.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
        }

        if (!activityBaseRepository.edit(activity)) {
            throw new BusinessException(__("更新优惠券信息失败！"));
        }

        // 库存预警通知 优惠券低于库存设定额提醒
        if (remainingQuantity <= 5) {
            Integer sellerId = userAdminService.getNoticeUserId(activityBase.getStoreId());

            String messageId = "coupon-is-below-stock-alert";
            Map<String, Object> args = new HashMap<>();
            args.put("activity_id", activityId);

            messageService.sendNoticeMsg(sellerId, messageId, args);
        }
    }

    /**
     * 发送优惠券到账通知
     */
    private void sendVoucherNotice(Integer userId, ActivityBase activityBase) {
        String messageId = "coupons-to-the-accounts";
        Map<String, Object> args = new HashMap<>();
        args.put("name", activityBase.getActivityTitle());
        String endTime = DateUtil.format(DateUtil.date(activityBase.getActivityEndtime()), "yyyy-MM-dd HH:mm:ss");
        args.put("endtime", endTime);
        messageService.sendNoticeMsg(userId, messageId, args);
    }

    @Override
    public UserVoucherRes getVoucher(Integer activityId, Integer userVoucherId, Integer currencyId) {
//        //是否为商家后台访问
//        if (user == null || !(user.isAdmin() && user.isStore())) {
//            // 修改汇率
//            BigDecimal currencyRate = currencyBaseService.getCurrencyRate(currencyId);
//            voucher.setVoucherPrice(NumberUtil.mul(voucher.getVoucherPrice(), currencyRate));
//            voucher.setVoucherSubtotal(NumberUtil.mul(voucher.getVoucherSubtotal(), currencyRate));
//        }

        UserVoucher voucher = null;

        if (CheckUtil.isNotEmpty(userVoucherId)) {
            voucher = get(userVoucherId);
        }

        UserVoucherRes userVoucherRes = new UserVoucherRes();
        List<ActivityBaseRes> activityBases = activityBaseService.getActivityBases(Collections.singletonList(activityId), currencyId);

        if (ObjectUtil.isEmpty(voucher)) {
            if (CollectionUtil.isNotEmpty(activityBases)) {
                ActivityBaseRes activityBaseRes = activityBases.get(0);
                BeanUtils.copyProperties(activityBaseRes, userVoucherRes);

                BuyVo buy = activityBaseRes.getActivityRuleJson().getRequirement().getBuy();
                VoucherVo voucherVo = activityBaseRes.getActivityRuleJson().getVoucher();
                userVoucherRes.setVoucherSubtotal(buy.getSubtotal());
                userVoucherRes.setVoucherPrice(voucherVo.getVoucherPrice());
                userVoucherRes.setVoucherStartDate(voucherVo.getVoucherStartDate());
                userVoucherRes.setVoucherEndDate(voucherVo.getVoucherEndDate());
                userVoucherRes.setStoreId(activityBaseRes.getStoreId());
                userVoucherRes.setActivityName(activityBaseRes.getActivityName());
                userVoucherRes.setActivityRule(activityBaseRes.getActivityRule());
                
                /*
                //storeName
                StoreBase storeBase = storeBaseService.get(userVoucherRes.getStoreId());

                if (storeBase != null) {
                    userVoucherRes.setStoreName(storeBase.getStoreName());
                }
                */
                //userVoucherRes.setActivityName(activityBaseRes.getActivityName());
                //userVoucherRes.setActivityRuleJson(activityBaseRes.getActivityRuleJson());
                userVoucherRes.setActivityState(activityBaseRes.getActivityState());
                List<Long> item = activityBaseRes.getActivityRuleJson().getRequirement().getBuy().getItem();
                if (CollUtil.isNotEmpty(item)) {
                    voucher.setItemId(CollUtil.join(item, ","));
                } else {
                }
            } else {
                userVoucherRes.setActivityState(StateCode.ACTIVITY_STATE_CLOSED);
            }
        } else {
            BeanUtils.copyProperties(voucher, userVoucherRes);

            if (CollectionUtil.isNotEmpty(activityBases)) {
                ActivityBaseRes activityBaseRes = activityBases.get(0);
                userVoucherRes.setActivityState(activityBaseRes.getActivityState());
            }
        }

        userVoucherRes.setItemIds(Convert.toList(String.class, userVoucherRes.getItemId()));

        ActivityRuleVo activityRuleVo = com.wechuang.mallshop.common.utils.JSONUtil.parseObject(userVoucherRes.getActivityRule(), ActivityRuleVo.class);
        userVoucherRes.setActivityRuleJson(activityRuleVo);

        Integer voucherStateId = userVoucherRes.getVoucherStateId();

        if (CheckUtil.isNotEmpty(voucherStateId) && Objects.equals(voucherStateId, StateCode.VOUCHER_STATE_UNUSED)) {
            if (userVoucherRes.getVoucherEndDate() < userVoucherRes.getVoucherStartDate()) {
                //更新数据
                UserVoucher userVoucher = new UserVoucher();
                userVoucher.setUserVoucherId(userVoucherRes.getUserVoucherId());
                userVoucher.setVoucherStateId(StateCode.VOUCHER_STATE_TIMEOUT);
                edit(userVoucher);

                userVoucherRes.setVoucherStateId(StateCode.VOUCHER_STATE_TIMEOUT);
            }
        }

        return userVoucherRes;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean withdrawVoucher(List<Long> srcOrderItemIds) {

        if (CollectionUtil.isEmpty(srcOrderItemIds)) {
            throw new BusinessException(__("来源订单商品编号集合为空！"));
        }

        QueryWrapper<UserVoucher> userVoucherQueryWrapper = new QueryWrapper<>();
        userVoucherQueryWrapper.in("src_order_item_id", srcOrderItemIds);
        List<UserVoucher> userVoucherList = find(userVoucherQueryWrapper);

        if (CollectionUtil.isEmpty(userVoucherList)) {
            throw new BusinessException(__("用户优惠券为空！"));
        }

        List<UserVoucher> usedVouchers = userVoucherList.stream().filter(item -> Objects.equals(item.getVoucherStateId(), StateCode.VOUCHER_STATE_USED)).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(usedVouchers)) {
            throw new BusinessException(__("赠送优惠券已使用，无法操作！"));
        }

        for (UserVoucher userVoucher : userVoucherList) {
            //多次退货退款
            if (Objects.equals(userVoucher.getVoucherStateId(), StateCode.VOUCHER_STATE_DEL)) {

                continue;
            }

            userVoucher.setVoucherStateId(StateCode.VOUCHER_STATE_DEL);

            if (!edit(userVoucher)) {
                throw new BusinessException(__("优惠券收回失败！"));
            }

            Integer activityId = userVoucher.getActivityId();
            Integer userId = userVoucher.getUserId();

            UserVoucherNum userVoucherNum = userVoucherNumRepository.findOne(new QueryWrapper<UserVoucherNum>().eq("activity_id", activityId).eq("user_id", userId));

            if (userVoucherNum == null) {
                throw new BusinessException(__("用户代金券领取数量信息为空！"));
            }
            userVoucherNum.setUvnNum(userVoucherNum.getUvnNum() > 0 ? userVoucherNum.getUvnNum() - 1 : 0);

            if (!userVoucherNumRepository.edit(userVoucherNum)) {
                throw new BusinessException(__("用户代金券领取数量信息修改失败！"));
            }

            ActivityBase activityBase = activityBaseRepository.get(activityId);

            if (activityBase == null) {
                throw new BusinessException(__("活动不存在！"));
            }

            // 返回代金券数量
            String activityRule = activityBase.getActivityRule();
            ActivityRuleVo activityRuleVo = com.wechuang.mallshop.common.utils.JSONUtil.parseObject(activityRule, ActivityRuleVo.class);

            if (activityRuleVo == null) {
                throw new BusinessException(__("活动规则为空！"));
            }
            VoucherVo voucherVo = activityRuleVo.getVoucher();

            if (voucherVo == null) {
                throw new BusinessException(__("活动优惠券信息为空！"));
            }

            Integer voucherQuantity = voucherVo.getVoucherQuantity();
            //活动数据
            Integer voucherQuantityUse = voucherVo.getVoucherQuantityUse() > 0 ? voucherVo.getVoucherQuantityUse() - 1 : 0;
            Integer remainingQuantity = NumberUtil.sub(voucherQuantity, voucherQuantityUse).intValue();
            if (remainingQuantity < 0) {
                remainingQuantity = 0;
            }

            voucherVo.setVoucherQuantityUse(voucherQuantityUse);
            voucherVo.setVoucherQuantityFree(remainingQuantity);
            activityRuleVo.setVoucher(voucherVo);

            ActivityBase activity = new ActivityBase();
            activity.setActivityId(activityId);
            activity.setActivityRule(com.wechuang.mallshop.common.utils.JSONUtil.toJSONString(activityRuleVo));

            if (!activityBaseRepository.edit(activity)) {
                throw new BusinessException(__("更新优惠券信息失败！"));
            }
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean takePlusVouchers(Integer userId, String grantType, String orderSn) {
        throw new BusinessException(__("开源版不支持PLUS会员领券"));
    }

}
