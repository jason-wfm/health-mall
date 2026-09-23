package com.wechuang.mallshop.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.dao.UserInfoDao;
import com.wechuang.mallshop.account.excel.UserInfoTemp;
import com.wechuang.mallshop.account.excel.UserInfoTempListener;
import com.wechuang.mallshop.account.model.entity.*;
import com.wechuang.mallshop.account.model.output.UserInfoOutput;
import com.wechuang.mallshop.account.model.req.UserInfoListReq;
import com.wechuang.mallshop.account.model.res.UserInfoRes;
import com.wechuang.mallshop.account.model.res.UserSaleRes;
import com.wechuang.mallshop.account.model.vo.UserInfoVo;
import com.wechuang.mallshop.account.repository.*;
import com.wechuang.mallshop.account.service.*;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
import com.wechuang.mallshop.analytics.dao.AnalyticsOrderDao;
import com.wechuang.mallshop.analytics.dao.AnalytiscTradeDao;
import com.wechuang.mallshop.analytics.model.vo.OrderNumVo;
import com.wechuang.mallshop.analytics.model.vo.TradeAmountVo;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.excel.ExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.*;
import com.wechuang.mallshop.common.utils.phone.PhoneModel;
import com.wechuang.mallshop.common.utils.phone.PhoneNumberUtils;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.marketing.model.vo.VoucherVo;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.pay.model.entity.UserResource;
import com.wechuang.mallshop.pay.repository.UserResourceRepository;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.dao.OrderBaseDao;
import com.wechuang.mallshop.trade.dao.OrderInfoDao;
import com.wechuang.mallshop.trade.model.vo.OrderBaseVo;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户详细信息表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoRepository, UserInfo, UserInfoListReq> implements UserInfoService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private UserDeliveryAddressRepository addressRepository;

    @Autowired
    private UserLevelRepository levelRepository;

    @Autowired
    private UserResourceRepository userResourceRepository;

    @Autowired
    private UserLoginRepository loginRepository;

    @Autowired
    private UserTagBaseRepository tagBaseRepository;

    @Autowired
    private UserTagGroupRepository tagGroupRepository;

    @Autowired
    private UserLoginHistoryRepository loginHistoryRepository;

    @Autowired
    private AnalyticsOrderDao analyticsOrderDao;

    @Autowired
    private AnalytiscTradeDao analytiscTradeDao;

    @Autowired
    private UserAnalyticsRepository userAnalyticsRepository;

    @Autowired
    private OrderBaseDao orderBaseDao;

    @Autowired
    private UserBindConnectRepository userBindConnectRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Autowired
    private UserVoucherService userVoucherService;

    @Autowired
    private ActivityBaseService activityBaseService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ConfigBaseService configBaseService;
    @Autowired
    private UserBaseService userBaseService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Override
    public Page<UserInfo> getList(QueryWrapper<UserInfo> queryWrapper, Integer page, Integer size) {
        return lists(queryWrapper, page, size);
    }

    @Override
    public UserInfoOutput getUserData(Integer userId) {
        UserInfoOutput userInfoOutput = new UserInfoOutput();
        //用户基本信息
        UserBase userBase = userBaseRepository.get(userId);

        if (userBase != null) {
            BeanUtils.copyProperties(userBase, userInfoOutput);
        }
        //用户详情信息
        UserInfo userInfo = get(userId);

        if (userInfo != null) {
            BeanUtils.copyProperties(userInfo, userInfoOutput);
            //身份证图片
            if (StrUtil.isNotEmpty(userInfo.getUserIdcardImages())) {
                userInfoOutput.setUserIdcardImageList(Convert.toList(String.class, userInfo.getUserIdcardImages()));
            }

            //用户等级
            UserLevel userLevel = levelRepository.get(userInfo.getUserLevelId());

            if (userLevel != null) {
                userInfoOutput.setUserLevelName(userLevel.getUserLevelName());
            }
            //用户标签、分组
            if (StrUtil.isNotEmpty(userInfo.getTagIds())) {
                List<Integer> tagIds = Convert.toList(Integer.class, userInfo.getTagIds());
                List<UserTagBase> userTagBases = tagBaseRepository.gets(tagIds);

                if (CollectionUtil.isNotEmpty(userTagBases)) {
                    List<String> tagNames = userTagBases.stream().map(UserTagBase::getTagTitle).collect(Collectors.toList());
                    //用户标签
                    userInfoOutput.setTagTitleList(tagNames);
                    userInfoOutput.setTagTitles(String.join("、", tagNames));
                    //用户分组
                    List<Integer> tagGroupIds = userTagBases.stream().map(UserTagBase::getTagGroupId).distinct().collect(Collectors.toList());
                    List<UserTagGroup> tagGroupGroups = tagGroupRepository.gets(tagGroupIds);

                    if (CollectionUtil.isNotEmpty(tagGroupGroups)) {
                        List<String> groupNames = tagGroupGroups.stream().map(UserTagGroup::getTagGroupName).collect(Collectors.toList());
                        userInfoOutput.setTagGroupNames(String.join("、", groupNames));
                    }
                }
            }
        }
        //统计没有取消的订单
        List<Integer> orderState = Arrays.asList(StateCode.ORDER_STATE_WAIT_PAY, StateCode.ORDER_STATE_WAIT_PAID, StateCode.ORDER_STATE_WAIT_REVIEW, StateCode.ORDER_STATE_WAIT_FINANCE_REVIEW, StateCode.ORDER_STATE_PICKING, StateCode.ORDER_STATE_WAIT_SHIPPING, StateCode.ORDER_STATE_SHIPPED, StateCode.ORDER_STATE_RECEIVED, StateCode.ORDER_STATE_FINISH, StateCode.ORDER_STATE_SELF_PICKUP);
        TimeRange month = TimeUtil.month();
        //本月订单
        OrderNumVo orderNum = analyticsOrderDao.getOrderNum(month.getStart(), month.getEnd(), orderState, null, userId, null, null, null);

        if (orderNum != null) {
            userInfoOutput.setMonthOrder(orderNum.getOrderNum());
        }

        //总计订单
        OrderNumVo totalNum = analyticsOrderDao.getOrderNum(null, null, orderState, null, userId, null, null, null);

        if (totalNum != null) {
            userInfoOutput.setTotalOrder(totalNum.getOrderNum());
        }

        //本月消费金额
        TradeAmountVo tradeAmount = analytiscTradeDao.getTradeAmount(month.getStart(), month.getEnd(), userId, null, null);

        if (tradeAmount != null) {
            userInfoOutput.setMonthTrade(tradeAmount.getAmount());
        }
        //总消费金额
        TradeAmountVo totalAmount = analytiscTradeDao.getTradeAmount(null, null, userId, null, null);

        if (totalAmount != null) {
            userInfoOutput.setTotalTrade(totalAmount.getAmount());
        }

        //用户地址
        QueryWrapper<UserDeliveryAddress> addressQueryWrapper = new QueryWrapper<>();
        addressQueryWrapper.eq("user_id", userId);
        UserDeliveryAddress address = addressRepository.findOne(addressQueryWrapper);

        if (address != null) {
            userInfoOutput.setUdAddress(address.getUdProvince() + address.getUdCity() + address.getUdCounty() + address.getUdAddress());
        }

        //用户资源
        UserResource userResource = userResourceRepository.get(userId);

        if (userResource != null) {
            BeanUtils.copyProperties(userResource, userInfoOutput);
        }
        //用户登录信息
        UserLogin userLogin = loginRepository.get(userId);

        if (userLogin != null) {
            userInfoOutput.setUserRegTime(userLogin.getUserRegTime());
        }

        //登录时间
        QueryWrapper<UserLoginHistory> loginHistoryQueryWrapper = new QueryWrapper<>();
        loginHistoryQueryWrapper.eq("user_id", userId);
        loginHistoryQueryWrapper.orderByDesc("user_login_time");
        IPage<UserLoginHistory> loginHistoryPage = loginHistoryRepository.lists(loginHistoryQueryWrapper, 1, 1);
        if (loginHistoryPage != null && CollectionUtil.isNotEmpty(loginHistoryPage.getRecords())) {
            List<UserLoginHistory> loginHistories = loginHistoryPage.getRecords();
            userInfoOutput.setUserLoginTime(loginHistories.get(0).getUserLoginTime());
        }

        return userInfoOutput;
    }

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editUser(UserInfo userInfo) {
        UserInfo info = get(userInfo.getUserId());

        if (info == null) {
            throw new BusinessException(__("用户信息不存在！"));
        }

        boolean success = edit(userInfo);

        //手机号修改
        if (StrUtil.isNotEmpty(userInfo.getUserMobile())) {
            UserBindConnect userBindConnect = new UserBindConnect();
            //判断手机号
            PhoneModel phoneModelWithCountry = PhoneNumberUtils.getPhoneModelWithCountry(userInfo.getUserIntl() + userInfo.getUserMobile());

            if (phoneModelWithCountry == null) {
                throw new BusinessException(__("手机号码不正确！"));
            }

            String bindId = String.format("%s%d", phoneModelWithCountry.getCountryCodeStr(), phoneModelWithCountry.getNationalNumber());

            if (StrUtil.isNotEmpty(info.getUserMobile()) && !info.getUserMobile().equals(userInfo.getUserMobile())) {
                userBindConnect.setBindId(bindId);
            } else if (StrUtil.isEmpty(info.getUserMobile())) {
                userBindConnect.setBindId(bindId);
            }

            if (StrUtil.isNotEmpty(userBindConnect.getBindId())) {
                QueryWrapper<UserBindConnect> userBindConnectQueryWrapper = new QueryWrapper<>();
                userBindConnectQueryWrapper.eq("bind_id", userBindConnect.getBindId());
                long count = userBindConnectRepository.count(userBindConnectQueryWrapper);

                if (count > 0) {
                    throw new BusinessException(__("手机号已绑定账户！"));
                }

                QueryWrapper<UserBindConnect> userBindConnectQuery = new QueryWrapper<>();
                userBindConnectQuery.eq("user_id", info.getUserId());
                userBindConnectQuery.eq("bind_type", 1);
                List<Serializable> bindKey = userBindConnectRepository.findKey(userBindConnectQuery);

                if (CollectionUtil.isNotEmpty(bindKey)) {

                    if (!userBindConnectRepository.remove(bindKey)) {
                        throw new BusinessException(__("删除旧绑定信息失败！"));
                    }
                }

                userBindConnect.setBindType(1);
                userBindConnect.setUserId(userInfo.getUserId());
                userBindConnect.setBindNickname(userInfo.getUserNickname());
                userBindConnect.setBindOpenid(bindId);
                userBindConnect.setBindActive(true);

                if (!userBindConnectRepository.save(userBindConnect)) {
                    throw new BusinessException(__("保存用户绑定信息失败！"));
                }
            }
        }

        return success;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean passWordEdit(Integer userId, String userPassword) {
        if (CheckUtil.isEmpty(userId)) {
            throw new BusinessException(__("用户Id为空"));
        }

        if (CheckUtil.isEmpty(userPassword)) {
            throw new BusinessException(__("密码为空"));
        }

        //修改密码
        String userSalt = IdUtil.simpleUUID();
        String resetPassWord = SecureUtil.md5(userPassword + userSalt);
        UserBase userBase = new UserBase();
        userBase.setUserId(userId);
        userBase.setUserSalt(userSalt);
        userBase.setUserPassword(resetPassWord);

        return userBaseRepository.edit(userBase);
    }

    @Override
    public boolean addTags(String userIds, String tagIds) {
        if (CheckUtil.isEmpty(userIds)) {
            throw new BusinessException(__("用户编号为空"));
        }
        List<Integer> userIdList = Convert.toList(Integer.class, userIds);
        List<UserInfo> userInfos = new ArrayList<>();
        for (Integer userId : userIdList) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setTagIds(tagIds);
            userInfos.add(userInfo);
        }

        return edit(userInfos);
    }

    /**
     * 删除用户账号
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public boolean removeUser(Integer userId) {
        UserAdmin userAdmin = userAdminRepository.get(userId);

        if (ObjectUtil.isNotEmpty(userAdmin) && userAdmin.getUserIsSuperadmin()) {
            throw new BusinessException(__("该账号为系统管理员，不可删除！"));
        }


        remove(userId);
        userBaseRepository.remove(userId);
        userLoginRepository.remove(userId);
        userResourceRepository.remove(userId);

        QueryWrapper<UserBindConnect> connectQueryWrapper = new QueryWrapper<>();
        connectQueryWrapper.eq("user_id", userId);
        List<Serializable> bindKey = userBindConnectRepository.findKey(connectQueryWrapper);
        userBindConnectRepository.remove(bindKey);


//                'User_FriendModel',
//                'User_GroupModel',

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
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(UserInfoTemp.class, 0, "导出用户信息模版");
            excelWriter.write(new ArrayList<UserInfoTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }

    }

    @Override
    public void exportFile(HttpServletResponse response, List<Integer> userIds) {
        List<UserInfo> userInfoList = gets(userIds);

        if (CollectionUtil.isEmpty(userInfoList)) {
            throw new BusinessException(__("选中数据为空"));
        }
        List<UserInfoVo> userInfoVos = new ArrayList<>();
        for (UserInfo userInfo : userInfoList) {
            Integer userId = userInfo.getUserId();
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(userInfo, userInfoVo);

            //状态
            if (userInfo.getUserState() != null) {
                Integer userState = userInfo.getUserState();
                if (Objects.equals(userState, StateCode.USER_STATE_LOCKING)) {
                    userInfoVo.setUserState(__("锁定"));
                } else if (Objects.equals(userState, StateCode.USER_STATE_ACTIVATION)) {
                    userInfoVo.setUserState(__("已激活"));
                } else if (Objects.equals(userState, StateCode.USER_STATE_NOTACTIVE)) {
                    userInfoVo.setUserState(__("未激活"));
                }
            }

            //性别
            if (userInfo.getUserGender() != null) {
                Integer userGender = userInfo.getUserGender();
                if (userGender.equals(0)) {
                    userInfoVo.setUserGender(__("保密"));
                } else if (userGender.equals(1)) {
                    userInfoVo.setUserGender(__("男"));
                } else if (userGender.equals(2)) {
                    userInfoVo.setUserGender(__("女"));
                }
            }

            //生日
            if (userInfo.getUserBirthday() != null) {
                String userBirthday = DateUtil.format(userInfo.getUserBirthday(), "yyyy-MM-dd");
                userInfoVo.setUserBirthday(userBirthday);
            }

            //认证状态
            if (userInfo.getUserIsAuthentication() != null) {
                Integer userIsAuthentication = userInfo.getUserIsAuthentication();
                if (userIsAuthentication.equals(0)) {
                    userInfoVo.setUserIsAuthentication(__("未认证"));
                } else if (userIsAuthentication.equals(1)) {
                    userInfoVo.setUserIsAuthentication(__("待审核"));
                } else if (userIsAuthentication.equals(2)) {
                    userInfoVo.setUserIsAuthentication(__("认证通过"));
                } else if (userIsAuthentication.equals(3)) {
                    userInfoVo.setUserIsAuthentication(__("认证失败"));
                }
            }

            //新人标识
            if (userInfo.getUserNew() != null) {
                userInfoVo.setUserNew(userInfo.getUserNew() ? __("是") : __("不是"));
            }

            if (userInfo.getUserParentId() != null) {
                userInfoVo.setUserParentId(userInfo.getUserParentId());
            }

            userInfoVos.add(userInfoVo);
        }
        ExcelUtil.exportReport(response, 0, "用户信息列表", userInfoVos);
    }

    @Override
    public void importTemp(MultipartFile file) throws Exception {
        AnalysisEventListener userInfoTempListener = new UserInfoTempListener();
        Class<?> tempClass = UserInfoTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(userInfoTempListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addVouchers(List<Integer> userIds, Integer activityId) {
        if (CollectionUtil.isEmpty(userIds)) {
            throw new BusinessException(__("用户编号为空"));
        }
        ActivityBase activityBase = activityBaseService.get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动不存在！"));
        }
        // 判断代金券数量是否足够
        String activityRule = activityBase.getActivityRule();
        ActivityRuleVo activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);

        if (activityRuleVo == null) {
            throw new BusinessException(__("活动规则为空！"));
        }
        VoucherVo voucherVo = activityRuleVo.getVoucher();

        if (voucherVo == null) {
            throw new BusinessException(__("活动优惠券信息为空！"));
        }
        Integer voucherQuantity = voucherVo.getVoucherQuantity();
        Integer voucher_quantity_use = voucherVo.getVoucherQuantityUse();

        if (NumberUtil.sub(voucherQuantity, voucher_quantity_use).intValue() - userIds.size() < 0) {
            throw new BusinessException(__("用户数大于优惠券剩余数量，添加失败！"));
        }

        for (Integer userId : userIds) {
            userVoucherService.addVoucher(activityId, userId, null, StateCode.VOUCHER_SOURCE_ADMIN);
        }
    }

    @Override
    public Map fixUserAvatar(Map row, Boolean fix_user_account) {
        List<Map> rows = ListUtil.toList(row);
        return fixUserAvatar(rows, fix_user_account).get(0);
    }

    /**
     * @param rows
     * @param fix_user_account 是否将user_account一起读取
     * @return
     */
    @Override
    public List<Map> fixUserAvatar(List<Map> rows, Boolean fix_user_account) {
        if (fix_user_account == null) fix_user_account = false;
        Set<Integer> user_ids = getDifferentUserIds(rows);

        if (CollUtil.isNotEmpty(user_ids)) {
            List<UserInfo> user_info_rows = gets(user_ids);
            List<UserLevel> user_level_rows = levelRepository.find(new QueryWrapper<>());

            String user_no_avatar = configBaseService.getConfig("user_no_avatar");
            for (Map row : rows) {
                Integer user_other_id = Convert.toInt(row.get("userOtherId"));

                if (user_other_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(user_other_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    Integer userId = userInfo.getUserId();
                    if (CheckUtil.isNotEmpty(userId)) {

                        row.put("user_other_nickname", userInfo.getUserNickname());
                        row.put("user_other_avatar", user_avatar);
                        //row.put("user_other_sign", userInfo.getUserSign());
                        row.put("user_other_level_name", userLevel.getUserLevelName());

                        if (fix_user_account) {
                            row.put("user_account", userInfo.getUserAccount());
                        }
                    } else {
                        row.put("user_other_nickname", __("临时:") + user_other_id);
                        row.put("user_other_avatar", user_no_avatar);
                        row.put("user_other_sign", "");
                        row.put("user_other_level_name", "");

                        if (fix_user_account) {
                            row.put("user_account", "");
                        }
                    }
                }

                Integer buyer_user_id = Convert.toInt(row.get("buyerUserId"));
                if (buyer_user_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(buyer_user_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("buyer_user_nickname", userInfo.getUserNickname());
                    row.put("buyer_user_avatar", user_avatar);
                    //row.put("buyer_user_sign", userInfo.getUser_sign());
                    row.put("buyer_user_level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("buyer_user_account", userInfo.getUserAccount());
                    }
                }

                Integer friend_id = Convert.toInt(row.get("friendId"));

                if (friend_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(friend_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_nickname = userInfo.getUserNickname();
                    if (StrUtil.isBlank(user_nickname)) user_nickname = __("佚名");

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("username", user_nickname);
                    row.put("avatar", user_avatar);
                    //row.put("sign", userInfo.getUser_sign());
                    row.put("level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("account", userInfo.getUserAccount());
                    }
                }

                Integer user_id = Convert.toInt(row.get("userId"));
                if (user_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(user_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("user_nickname", userInfo.getUserNickname());
                    row.put("user_avatar", user_avatar);
                    //row.put("user_sign", userInfo.getUser_sign());
                    row.put("user_level_name", userLevel.getUserLevelName());
                    //row.put("rid", userBase.getRid());

                    if (fix_user_account) {
                        row.put("user_account", userInfo.getUserAccount());
                    }
                }

                Integer buyer_id = Convert.toInt(row.get("buyerId"));
                if (buyer_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(buyer_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("buyer_nickname", userInfo.getUserNickname());
                    row.put("buyer_avatar", user_avatar);
                    //row.put("buyer_sign", userInfo.getUser_sign());
                    row.put("buyer_level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("buyer_account", userInfo.getUserAccount());
                    }
                }

                Integer user_id_to = Convert.toInt(row.get("userIdTo"));
                if (user_id_to != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(user_id_to, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("user_nickname_to", userInfo.getUserNickname());
                    row.put("user_avatar_to", user_avatar);
                    //row.put("user_sign_to", userInfo.getUser_sign());
                    row.put("user_level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("user_account_to", userInfo.getUserAccount());
                    }
                }

                Integer to_user_id = Convert.toInt(row.get("toUserId"));
                if (to_user_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(to_user_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("to_user_nickname", userInfo.getUserNickname());
                    row.put("to_user_avatar", user_avatar);
                    //row.put("to_user_sign", userInfo.getUser_sign());
                    row.put("user_level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("to_user_account", userInfo.getUserAccount());
                    }
                }

                Integer user_parent_id = Convert.toInt(row.get("userParentId"));
                if (user_parent_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(user_parent_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    Integer user_level_id = userInfo.getUserLevelId();
                    Optional<UserLevel> userLevelOpl = user_level_rows.stream().filter(s -> ObjectUtil.equal(user_level_id, s.getUserLevelId())).findFirst();
                    UserLevel userLevel = userLevelOpl.orElseGet(UserLevel::new);

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    row.put("user_parent_nickname", userInfo.getUserNickname());
                    row.put("user_parent_avatar", user_avatar);
                    //row.put("user_parent_sign", userInfo.getUser_sign());
                    row.put("user_parent_level_name", userLevel.getUserLevelName());

                    if (fix_user_account) {
                        row.put("user_parent_account", userInfo.getUserAccount());
                    }
                }

                Integer withdraw_user_id = Convert.toInt(row.get("withdraw_user_id"));
                if (withdraw_user_id != null) {
                    Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(user_parent_id, s.getUserId())).findFirst();
                    UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                    String withdraw_user_nickname = userInfo.getUserNickname();
                    row.put("withdraw_user_nickname", withdraw_user_nickname);
                }
            }
        }

        return rows;
    }

    /**
     * 获取不重复的userId
     *
     * @param rows
     * @return
     */
    private Set<Integer> getDifferentUserIds(List<Map> rows) {
        HashSet<Integer> ids = new HashSet<>();
        for (Map row : rows) {
            Integer to_user_id = Convert.toInt(row.get("toUserId"));
            if (ObjectUtil.isNotNull(to_user_id)) ids.add(to_user_id);

            Integer user_id_to = Convert.toInt(row.get("userIdTo"));
            if (ObjectUtil.isNotNull(user_id_to)) ids.add(user_id_to);

            Integer user_other_id = Convert.toInt(row.get("userOtherId"));
            if (ObjectUtil.isNotNull(user_other_id)) ids.add(user_other_id);

            Integer user_id = Convert.toInt(row.get("userId"));
            if (ObjectUtil.isNotNull(user_id)) ids.add(user_id);

            Integer friend_id = Convert.toInt(row.get("friendId"));
            if (ObjectUtil.isNotNull(friend_id)) ids.add(friend_id);

            Integer buyer_user_id = Convert.toInt(row.get("buyerUserId"));
            if (ObjectUtil.isNotNull(buyer_user_id)) ids.add(buyer_user_id);

            Integer buyer_id = Convert.toInt(row.get("buyerId"));
            if (ObjectUtil.isNotNull(buyer_id)) ids.add(buyer_id);

            Integer user_parent_id = Convert.toInt(row.get("userParentId"));
            if (ObjectUtil.isNotNull(user_parent_id)) ids.add(user_parent_id);
        }
        return ids;
    }

    @Override
    public boolean addCustomer(Integer saleId, String userAccount) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_account", userAccount);
        UserInfo userInfo = findOne(userInfoQueryWrapper);

        if (userInfo == null) {
            throw new BusinessException(__("客户不存在！"));
        }
        userInfo.setUserSaleId(saleId);
        userInfo.setUserCustomerTime(new Date().getTime());

        if (!edit(userInfo)) {
            throw new BusinessException(__("添加客户失败！"));
        }

        return true;
    }

    @Override
    public IPage<UserInfoRes> getList(UserInfoListReq userInfoListReq) {
        IPage<UserInfoRes> userInfoResPage = new Page<>();
        userInfoListReq.setSidx("user_customer_time");

        IPage<UserInfo> userInfoPage = lists(userInfoListReq);

        if (CollectionUtil.isNotEmpty(userInfoPage.getRecords())) {
            BeanUtils.copyProperties(userInfoPage, userInfoResPage);
            List<UserInfoRes> userInfoResList = new ArrayList<>();

            List<UserInfo> userInfos = userInfoPage.getRecords();
            List<Integer> userIds = CommonUtil.column(userInfos, UserInfo::getUserId);
            List<UserAnalytics> userAnalytics = userAnalyticsRepository.gets(userIds);
            Map<Integer, UserAnalytics> userAnalyticsMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(userAnalytics)) {
                userAnalyticsMap = userAnalytics.stream().collect(Collectors.toMap(UserAnalytics::getUserId, UserAnalytics -> UserAnalytics, (k1, k2) -> k1));
            }
            List<OrderBaseVo> newOrderTime = orderBaseDao.getNewOrderTime(userIds);
            Map<Integer, Date> timeMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(newOrderTime)) {
                timeMap = newOrderTime.stream().collect(Collectors.toMap(OrderBaseVo::getUserId, OrderBaseVo::getOrderTime, (k1, k2) -> k1));
            }

            for (UserInfo userInfo : userInfos) {
                UserInfoRes userInfoRes = BeanUtil.copyProperties(userInfo, UserInfoRes.class);
                Integer userId = userInfoRes.getUserId();

                if (userAnalyticsMap.containsKey(userId)) {
                    UserAnalytics analytics = userAnalyticsMap.get(userId);
                    userInfoRes.setOrderTotal(analytics.getUserSpend().subtract(analytics.getUserRefund()));
                }

                if (timeMap.containsKey(userId)) {
                    userInfoRes.setOrderTime(timeMap.get(userId));
                }
                userInfoResList.add(userInfoRes);
            }
            userInfoResPage.setRecords(userInfoResList);
        }

        return userInfoResPage;
    }

    @Override
    public UserInfo getCustomer(Integer saleId, String userId) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("user_sale_id", saleId);
        userInfoQueryWrapper.eq("user_id", userId);
        UserInfo userInfo = findOne(userInfoQueryWrapper);

        if (userInfo == null) {
            throw new BusinessException(__("销售员与用户不匹配！"));
        }

        return userInfo;
    }

    @Override
    public UserSaleRes saleDataStatistics(Integer userId) {
        UserInfo userInfo = get(userId);

        if (userInfo == null) {
            throw new BusinessException(__("用户信息不存在！"));
        }

        if (!userInfo.getUserIsSale()) {
            throw new BusinessException(__("该用户不是销售员！"));
        }

        UserSaleRes userSaleRes = new UserSaleRes();
        TimeRange range = TimeUtil.today();

        // 今日新增客户
        userSaleRes.setTodayCustomerNum(userInfoDao.getUserNum(range.getStart(), range.getEnd(), userId));
        // 总客户数量
        userSaleRes.setTotalCustomerNum(userInfoDao.getUserNum(null, null, userId));

        // 今日订单数量
        userSaleRes.setTodayOrderNum(orderInfoDao.getOrderNumBySale(range.getStart(), range.getEnd(), userId));

        // 总订单数量
        userSaleRes.setTotalOrderNum(orderInfoDao.getOrderNumBySale(null, null, userId));

        // 今日订单额
        userSaleRes.setTodayOrderAmount(orderInfoDao.getOrderAmountBySale(range.getStart(), range.getEnd(), userId));

        // 总订单额
        userSaleRes.setTotalOrderAmount(orderInfoDao.getOrderAmountBySale(null, null, userId));

        return userSaleRes;
    }

}