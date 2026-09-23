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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.model.vo.ExperienceVo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.common.api.LevelCode;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.*;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.common.web.service.MessageService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pay.excel.UserResourceTemp;
import com.wechuang.mallshop.pay.excel.UserResourceTempListener;
import com.wechuang.mallshop.pay.model.entity.*;
import com.wechuang.mallshop.pay.model.req.UserExpHistoryListReq;
import com.wechuang.mallshop.pay.model.req.UserResourceListReq;
import com.wechuang.mallshop.pay.model.res.DistributionCommissionRes;
import com.wechuang.mallshop.pay.model.res.SignInfoRes;
import com.wechuang.mallshop.pay.model.res.UserResourceRes;
import com.wechuang.mallshop.pay.model.vo.MoneyVo;
import com.wechuang.mallshop.pay.model.vo.PointStepVo;
import com.wechuang.mallshop.pay.repository.UserExpHistoryRepository;
import com.wechuang.mallshop.pay.repository.UserResourceRepository;
import com.wechuang.mallshop.pay.service.ConsumeRecordService;
import com.wechuang.mallshop.pay.service.UserPayService;
import com.wechuang.mallshop.pay.service.UserResourceService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户资源表-资金账户表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Service
public class UserResourceServiceImpl extends BaseServiceImpl<UserResourceRepository, UserResource, UserResourceListReq> implements UserResourceService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserExpHistoryRepository userExpHistoryRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Autowired
    private ConsumeRecordService consumeRecordService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserPayService userPayService;

    @Override
    public IPage<UserResourceRes> getList(UserResourceListReq userResourceListReq) {
        IPage<UserResourceRes> iPage = new Page<>();

        IPage<UserResource> page = lists(userResourceListReq);

        if (CollectionUtil.isNotEmpty(page.getRecords())) {
            BeanUtils.copyProperties(page, iPage);

            List<UserResourceRes> userResourceResList = new ArrayList<>();
            List<UserResource> resourceList = page.getRecords();
            List<Integer> userIds = CommonUtil.column(resourceList, UserResource::getUserId);
            Map<Integer, UserInfo> userInfoMap = userInfoRepository.getUserInfoMap(userIds);

            List<UserPay> userPayRows = userPayService.gets(userIds);
            Map<Integer, UserPay> userPayMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(userPayRows)) {
                userPayMap = userPayRows.stream().collect(Collectors.toMap(UserPay::getUserId, UserPay -> UserPay, (k1, k2) -> k1));
            }

            for (UserResource userResource : resourceList) {
                UserResourceRes userResourceRes = BeanUtil.copyProperties(userResource, UserResourceRes.class);

                if (CollUtil.isNotEmpty(userInfoMap)) {
                    UserInfo userInfo = userInfoMap.get(userResource.getUserId());

                    if (userInfo != null) {
                        userResourceRes.setUserNickname(userInfo.getUserNickname());
                    }
                }

                if(CollUtil.isNotEmpty(userPayMap) && userPayMap.containsKey(userResource.getUserId())){
                    userResourceRes.setSetPayPasswd(1);
                }
                else
                {
                    userResourceRes.setSetPayPasswd(0);
                }
                userResourceResList.add(userResourceRes);
            }
            iPage.setRecords(userResourceResList);
        }

        return iPage;
    }

    @Override
    public boolean initUserPoints(Integer userId) {
        UserResource resource = new UserResource();
        resource.setUserId(userId);

        userResourceRepository.save(resource);

        return true;
    }

    /**
     * 初始化用户经验等级
     *
     * @param userId
     * @return
     */
    @Override
    public boolean initUserExperience(Integer userId) {
        BigDecimal exp_reg = configBaseService.getConfig("exp_reg", BigDecimal.ZERO);
        ExperienceVo experienceVo = new ExperienceVo();
        experienceVo.setUserId(userId);
        experienceVo.setExp(exp_reg);
        experienceVo.setExpTypeId(LevelCode.EXP_TYPE_REG);
        experienceVo.setDesc(__("用户注册"));
        experience(experienceVo);

        return true;
    }

    /**
     * 操作用户经验
     *
     * @param experienceVo
     * @return
     */
    @Override
    public boolean experience(ExperienceVo experienceVo) {
        Date curTime = new Date();
        Integer userId = experienceVo.getUserId();
        Integer expTypeId = experienceVo.getExpTypeId();
        BigDecimal exp = experienceVo.getExp();
        DateTime curDate = DateUtil.parse(DateUtil.format(curTime, "yyyy-MM-dd"));
        int expKindId;

        if (exp.intValue() > 0) {
            expKindId = 1;
        } else {
            expKindId = 2;
        }

        handleLevelCode(expTypeId, userId, curDate);

        // 写入会员日志经验
        UserExpHistory expHistory = new UserExpHistory();
        expHistory.setExpKindId(expKindId); // 类型(ENUM):1-获取积分;2-消费积分;
        expHistory.setExpTypeId(expTypeId); // 积分类型(ENUM):1-会员注册;2-会员登录;3-商品评论;4-购买商品;5-管理员操作;7-积分换购商品;8-积分兑换优惠券
        expHistory.setUserId(userId); // 会员编号
        expHistory.setExpLogValue(NumberUtil.max(exp.longValue(), 1)); // 改变积分值
        expHistory.setExpLogDesc(experienceVo.getDesc()); // 描述
        expHistory.setExpLogTime(curTime); // 积分时间
        expHistory.setExpLogDate(curDate); // 积分日期
        expHistory.setExpLogDesc(experienceVo.getDesc());
        return addExp(expHistory);
    }

    @Transactional
    public boolean addExp(UserExpHistory expHistory) {
        Integer userId = expHistory.getUserId();

        //当前积分
        UserResource userResource = get(userId);
        Long userExp = Optional.ofNullable(userResource.getUserExp()).orElse(0L);

        expHistory.setUserExp(userExp);
        if (!userExpHistoryRepository.saveOrUpdate(expHistory)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        Long expLogVal = expHistory.getExpLogValue();
        Long userExpVal = userExp + expLogVal;

        UserResource resource = new UserResource();
        resource.setUserId(userId);
        resource.setUserExp(userExpVal);

        // 需要设置经验值，设置为0否则忽略不计
        QueryWrapper<UserLevel> levelQueryWrapper = new QueryWrapper<>();
        levelQueryWrapper.le("user_level_exp", userExpVal).gt("user_level_exp", 0).orderByDesc("user_level_exp");

        UserLevel userLevel = userLevelRepository.findOne(levelQueryWrapper);
        UserInfo userInfo = userInfoRepository.get(userId);

        // 判断是否需要更新用户等级
        Integer userLevelId = userInfo.getUserLevelId();
        if (userLevel != null && userLevelId < userLevel.getUserLevelId()) {
            userInfo.setUserLevelId(userLevel.getUserLevelId());
        }

        // 更新用户经验值
        if (!edit(resource)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        // 更新用户等级
        userInfo.setUserId(userId);
        if (!userInfoRepository.edit(userInfo)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        return true;

    }

    public void handleLevelCode(Integer expTypeId, Integer userId, DateTime curDate) {
        // 通用判断， 注册和
        switch (expTypeId) {
            case LevelCode.EXP_TYPE_REG:
                // 注册只可以触发一次
                QueryWrapper<UserExpHistory> regWrapper = new QueryWrapper<>();
                regWrapper.eq("user_id", userId).eq("exp_type_id", expTypeId);
                List<Serializable> reg_exp_log_id = userExpHistoryRepository.findKey(regWrapper);

                if (CollUtil.isNotEmpty(reg_exp_log_id)) {
                    throw new BusinessException(ResultCode.FAILED);
                }
                break;
            case LevelCode.EXP_TYPE_LOGIN:
                //登录，每天只可以触发一次
                QueryWrapper<UserExpHistory> loginWrapper = new QueryWrapper<>();
                loginWrapper.eq("user_id", userId).eq("exp_type_id", expTypeId).eq("exp_log_date", curDate);
                List<Serializable> login_exp_log_id = userExpHistoryRepository.findKey(loginWrapper);

                if (CollUtil.isNotEmpty(login_exp_log_id)) {
                    throw new BusinessException(ResultCode.FAILED);
                }
                break;

            case LevelCode.EXP_TYPE_EVALUATE_PRODUCT:
                break;
            case LevelCode.EXP_TYPE_EVALUATE_STORE:
                break;
            case LevelCode.EXP_TYPE_CONSUME:
                break;
            case LevelCode.EXP_TYPE_OTHER:
                break;
            case LevelCode.EXP_TYPE_EXCHANGE_PRODUCT:
                break;
            case LevelCode.EXP_TYPE_EXCHANGE_VOUCHER:
                break;
            default:
                break;
        }
    }

    /**
     * 获取签到基本信息
     *
     * @param userId
     * @return
     */
    @Override
    public SignInfoRes getSignInfo(Integer userId) {
        // 获取本月开始与结束时间
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        TimeRange timeRange = TimeUtil.lastNMonths(1);


        QueryWrapper<UserExpHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("exp_type_id", LevelCode.EXP_TYPE_LOGIN);
        queryWrapper.between("exp_log_time", timeRange.getStart(), timeRange.getEnd());
        queryWrapper.orderByDesc("exp_log_date");

        List<UserExpHistory> pointsHistoryList = userExpHistoryRepository.find(queryWrapper);

        //今日是否签到
        int todayIsSign = 0;
        //已签到日期集合
        List<String> signDayArr = new ArrayList<>();
        //连续签到标识
        int continueSignDays = 0;
        //判断连续签到情况
        boolean countDaysEnd = true;
        //一天的秒数
        int oneDaySeconds = 24 * 60 * 60 * 1000;
        //获取当天日期yyyy-mm-dd
        Date todayDate = null;
        try {
            todayDate = sdf.parse(DateUtil.today());
        } catch (ParseException e) {
            LogUtil.error(ConstantLog.DEFAULT, e);
        }

        for (UserExpHistory expHistory : pointsHistoryList) {
            Date pointsLogDate = expHistory.getExpLogDate();
            signDayArr.add(sdf.format(pointsLogDate));
            assert todayDate != null;

            // 判断今日是否已签到
            if (todayDate.equals(pointsLogDate)) {
                todayIsSign = 1;
            }

            // 判断连续签到情况
            if (countDaysEnd) {
                if (!pointsLogDate.equals(todayDate)) {
                    long dayDifference = (todayDate.getTime() - pointsLogDate.getTime()) / oneDaySeconds;
                    if (dayDifference == 1) {
                        continueSignDays++;
                    } else {
                        countDaysEnd = false;
                    }
                } else {
                    continueSignDays++;
                }
                // 更新今天的日期为当前记录的日期
                todayDate = pointsLogDate;
            }
        }

        SignInfoRes infoRes = new SignInfoRes();

        String signPointStep = configBaseService.getConfig("sign_point_step");
        List<PointStepVo> stepVos = null;
        if (StrUtil.isNotBlank(signPointStep)) {
            stepVos = Convert.toList(PointStepVo.class, JSONUtil.parseArray(signPointStep));
        }

        infoRes.setTodayIsSign(todayIsSign);
        infoRes.setContinueSignDays(continueSignDays);
        infoRes.setSignList(dealSignPointList(stepVos));
        infoRes.setSignDayArr(signDayArr);

        return infoRes;

    }

    @Override
    @Transactional
    public void sign(Integer userId) {
        Integer expLogin = configBaseService.getConfig("exp_login", 0);

        ExperienceVo experienceVo = new ExperienceVo(userId, BigDecimal.valueOf(expLogin), LevelCode.EXP_TYPE_LOGIN, "");
        experience(experienceVo);

        // todo 发送消息通知

    }

    @Override
    public Boolean getSignState(Integer userId) {
        TimeRange today = TimeUtil.today();
        long signed = userExpHistoryRepository.count(
                new QueryWrapper<UserExpHistory>()
                        .eq("user_id", userId)
                        .eq("exp_type_id", LevelCode.EXP_TYPE_LOGIN)
                        .between("exp_log_time", today.getStart(), today.getEnd()));

        return signed > 0;
    }

    /**
     * 处理前端映射数据
     *
     * @param stepVos
     * @return
     */
    public List<PointStepVo> dealSignPointList(List<PointStepVo> stepVos) {
        if (CollectionUtil.isNotEmpty(stepVos)) {
            List<PointStepVo> daysList = stepVos.stream().sorted(Comparator.comparing(PointStepVo::getDays)).collect(Collectors.toList());
            Integer pointsLogin = configBaseService.getConfig("points_login", 0);
            boolean hasOne = false;
            for (PointStepVo pointStepVo : daysList) {
                if (pointStepVo.getDays() == 1) {
                    hasOne = true;
                    pointStepVo.setValueStr(pointsLogin + " " + __("经验"));
                } else {
                    pointStepVo.setValueStr(pointStepVo.getMultiples() + " 倍");
                }
            }
            if (!hasOne) {
                PointStepVo stepVo = new PointStepVo();
                stepVo.setDays(1);
                stepVo.setMultiples("1");
                stepVo.setValueStr(pointsLogin + " " + __("经验"));
                daysList.add(0, stepVo);
            }
            return daysList;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserMoney(MoneyVo moneyVo) {
        BigDecimal recordTotal = moneyVo.getRecordTotal();

        if (recordTotal != null && ObjectUtil.compare(BigDecimal.ZERO, recordTotal) != 0) {
            moneyVo.setRecordDesc(__("管理员后台修改"));
            moneyVo.setTradeTypeDeposit(StateCode.TRADE_TYPE_DEPOSIT);
            userResourceRepository.money(moneyVo);
        } else {
            throw new BusinessException(ResultCode.VALIDATE_FAILED);
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean money(Integer userId, BigDecimal money, Integer tradeTypeDeposit, String desc, Integer paymentTypeId, BigDecimal withdrawFee, String orderId) {
        Date time = new Date();
        DateTime curTime = DateUtil.parse(DateUtil.format(time, "yyyy-MM-dd"));

        ConsumeRecord consumeRecord = new ConsumeRecord();

        consumeRecord.setOrderId(orderId);
        consumeRecord.setUserId(userId);
        UserInfo userInfo = userInfoRepository.get(userId);
        consumeRecord.setUserNickname(userInfo != null ? userInfo.getUserNickname() : "");
        consumeRecord.setRecordTime(time.getTime());
        consumeRecord.setRecordDate(curTime);
        consumeRecord.setRecordYear(DateUtil.year(time));
        consumeRecord.setRecordMonth(DateUtil.month(time));
        consumeRecord.setRecordDay(DateUtil.dayOfMonth(time));
        consumeRecord.setRecordDesc(desc);

        if (ObjectUtil.equal(StateCode.PAYMENT_TYPE_OFFLINE, paymentTypeId)) {
            consumeRecord.setRecordTitle(__("线下"));
        } else {
            consumeRecord.setRecordTitle(__("线上"));
        }

        UserResource userResource = get(userId);
        if (userResource == null) {
            userResource = new UserResource();
            userResource.setUserId(userId);
        }

        //通用判断
        String title = desc;
        switch (tradeTypeDeposit) {
            case StateCode.TRADE_TYPE_WITHDRAW:
                BigDecimal userMoneyFrozen = ObjectUtil.defaultIfNull(userResource.getUserMoneyFrozen(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) > 0 && ObjectUtil.compare(userMoneyFrozen, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));

                }
                userResource.setUserMoneyFrozen(NumberUtil.add(userMoneyFrozen, money));
                userResource.setUserSettledAmount(userResource.getUserSettledAmount().add(money.negate()));
                break;
            case StateCode.TRADE_TYPE_SHOPPING_CARD:
            case StateCode.TRADE_TYPE_DEPOSIT_CARD:
                BigDecimal userRechargeCard = ObjectUtil.defaultIfNull(userResource.getUserRechargeCard(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) < 0 && ObjectUtil.compare(userRechargeCard, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));

                }
                userResource.setUserRechargeCard(NumberUtil.add(userRechargeCard, money));
                break;
            case StateCode.TRADE_TYPE_DEPOSIT:
            case StateCode.TRADE_TYPE_TRANSFER:
            case StateCode.TRADE_TYPE_COMMISSION:
            case StateCode.TRADE_TYPE_RETURN_GROUPBOOKING:
            default:
                BigDecimal defaultUserMoney = ObjectUtil.defaultIfNull(userResource.getUserMoney(), BigDecimal.ZERO);
                if (ObjectUtil.compare(money, BigDecimal.ZERO) < 0 && ObjectUtil.compare(defaultUserMoney, money.negate()) < 0) {
                    throw new BusinessException(__("金额不足！"));

                }
                userResource.setUserMoney(NumberUtil.add(defaultUserMoney, money));
                break;
        }
        consumeRecord.setRecordTitle(title);

        if (ObjectUtil.equal(tradeTypeDeposit, StateCode.TRADE_TYPE_DEPOSIT_CARD) || ObjectUtil.equal(tradeTypeDeposit, StateCode.TRADE_TYPE_SHOPPING_CARD)) {
            consumeRecord.setPaymentMetId(StateCode.PAYMENT_MET_RECHARGE_CARD);
        } else {
            consumeRecord.setPaymentMetId(StateCode.PAYMENT_MET_MONEY);
        }
        consumeRecord.setRecordTotal(money);

        if (money.compareTo(BigDecimal.ZERO) > 0) {
            consumeRecord.setRecordMoney(NumberUtil.sub(money, withdrawFee));
        } else {
            consumeRecord.setRecordMoney(NumberUtil.add(money, withdrawFee));
        }

        consumeRecord.setRecordCommissionFee(withdrawFee); // 佣金
        consumeRecord.setTradeTypeId(tradeTypeDeposit);

        // 买家数据
        if (!consumeRecordService.add(consumeRecord)) {
            throw new BusinessException(__("添加交易明细表失败！"));
        }

        if (!edit(userResource)) {
            throw new BusinessException(ResultCode.FAILED);
        }
        //余额改变
        String messageId = "balance-change-reminder";
        HashMap<String, Object> args = new HashMap<>();
        args.put("user_money", userResource.getUserMoney());
        args.put("change_time", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        args.put("des", desc);
        args.put("user_money_freeze", 0);

        messageService.sendNoticeMsg(userId, messageId, args);

        return true;
    }

    @Override
    public DistributionCommissionRes getCommissionInfo(Integer userId) {
        return DistributionCommissionRes.empty(userId);
    }

    @Override
    public IPage<UserExpHistory> listsExp(UserExpHistoryListReq userExpHistoryListReq) {
        QueryWrapper<UserExpHistory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userExpHistoryListReq.getUserId());
        queryWrapper.orderByDesc("exp_log_id");

        return userExpHistoryRepository.lists(queryWrapper, userExpHistoryListReq.getPage(), userExpHistoryListReq.getSize());
    }

    @Override
    public UserResourceRes resource(Integer userId) {
        UserResourceRes userResourceRes = new UserResourceRes();
        UserResource userResource = get(userId);

        if (userResource == null) {
            throw new BusinessException(__("用户资源信息不存在！"));
        }
        BeanUtils.copyProperties(userResource, userResourceRes);
        userResourceRes.setUserCommission(BigDecimal.ZERO);

        return userResourceRes;
    }

    @Override
    public void importTemp(MultipartFile file) throws Exception {
        AnalysisEventListener userResourceTempListener = new UserResourceTempListener();
        Class<?> tempClass = UserResourceTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(userResourceTempListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }

    public boolean editResource(UserResource userResource) {
        Integer user_id = userResource.getUserId();
        BigDecimal user_money = userResource.getUserMoney();
        ContextUser user = ContextUtil.getLoginUser();
        if (user.isPlatform()) {
            //余额
            if (user_money != null && ObjectUtil.compare(BigDecimal.ZERO, user_money) != 0) {
                money(user_id, user_money, StateCode.TRADE_TYPE_DEPOSIT, __("管理员后台修改"), null, null, null);
            }
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
        return true;
    }

    @Override
    public void exportTemp(HttpServletResponse response) {
        //编码问题
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(__("" +
                    "" +
                    "") + "-" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(UserResourceTemp.class, 0, "导出用户资源模版");
            excelWriter.write(new ArrayList<UserResourceTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }

    }

    @Override
    public boolean checkUpdateUserLevel(Integer userId, Integer useExpTotal, BigDecimal userSpend) {


        if (useExpTotal > 0) {
            //经验值
            QueryWrapper<UserLevel> levelQueryWrapper = new QueryWrapper<>();
            levelQueryWrapper.le("user_level_exp", useExpTotal).gt("user_level_exp", 0).orderByDesc("user_level_exp");

            UserLevel userLevel = userLevelRepository.findOne(levelQueryWrapper);
            UserInfo userInfo = userInfoRepository.get(userId);

            // 判断是否需要更新用户等级
            Integer userLevelId = userInfo.getUserLevelId();
            if (userLevel != null && userLevelId < userLevel.getUserLevelId()) {
                userInfo.setUserLevelId(userLevel.getUserLevelId());
            }

            // 更新用户等级
            userInfo.setUserId(userId);
            if (!userInfoRepository.edit(userInfo)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }
        if (userSpend.compareTo(BigDecimal.ZERO) > 0) {
            Integer userSpendInt = userSpend.intValue();
            //消费
            QueryWrapper<UserLevel> levelQueryWrapper = new QueryWrapper<>();
            levelQueryWrapper.le("user_level_spend", userSpendInt).gt("user_level_spend", 0).orderByDesc("user_level_spend");

            UserLevel userLevel = userLevelRepository.findOne(levelQueryWrapper);


            UserInfo userInfo = userInfoRepository.get(userId);

            // 判断是否需要更新用户等级
            Integer userLevelId = userInfo.getUserLevelId();
            if (userLevel != null && userLevelId < userLevel.getUserLevelId()) {
                userInfo.setUserLevelId(userLevel.getUserLevelId());
            }

            // 更新用户等级
            userInfo.setUserId(userId);
            if (!userInfoRepository.edit(userInfo)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }
        return true;
    }
}
