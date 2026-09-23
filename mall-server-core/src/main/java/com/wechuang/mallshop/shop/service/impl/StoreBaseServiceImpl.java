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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserBase;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.input.RegInput;
import com.wechuang.mallshop.account.repository.UserBaseRepository;
import com.wechuang.mallshop.account.service.LoginService;
import com.wechuang.mallshop.account.service.UserInfoService;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.config.Point;
import com.wechuang.mallshop.common.consts.BindConnectCode;
import com.wechuang.mallshop.common.consts.ConstantRole;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.*;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.vo.ActivityRuleVo;
import com.wechuang.mallshop.marketing.model.vo.PopupVo;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.pt.dao.ProductIndexDao;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.input.ProductIndexInput;
import com.wechuang.mallshop.pt.model.output.ProductOutput;
import com.wechuang.mallshop.pt.model.res.ProductListRes;
import com.wechuang.mallshop.pt.repository.ProductIndexRepository;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.wechuang.mallshop.shop.dao.StoreBaseDao;
import com.wechuang.mallshop.shop.model.entity.*;
import com.wechuang.mallshop.shop.model.input.StoreStreetInput;
import com.wechuang.mallshop.shop.model.output.StoreStreetOutput;
import com.wechuang.mallshop.shop.model.req.StoreBaseAddReq;
import com.wechuang.mallshop.shop.model.req.StoreBaseEditReq;
import com.wechuang.mallshop.shop.model.req.StoreBaseListReq;
import com.wechuang.mallshop.shop.model.req.StoreCategoryListReq;
import com.wechuang.mallshop.shop.model.res.StoreBaseRes;
import com.wechuang.mallshop.shop.model.res.StoreCategoryRes;
import com.wechuang.mallshop.shop.model.vo.StoreDetailVo;
import com.wechuang.mallshop.shop.model.vo.StoreNearVo;
import com.wechuang.mallshop.shop.model.vo.StoreSortVo;
import com.wechuang.mallshop.shop.repository.*;
import com.wechuang.mallshop.shop.service.StoreAnalyticsService;
import com.wechuang.mallshop.shop.service.StoreBaseService;
import com.wechuang.mallshop.shop.service.StoreCategoryService;
import com.wechuang.mallshop.shop.service.StoreEmployeeService;
import com.wechuang.mallshop.sys.model.vo.PagePopUpVo;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MessageTemplateService;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 店铺基础信息表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Service
public class StoreBaseServiceImpl extends BaseServiceImpl<StoreBaseRepository, StoreBase, StoreBaseListReq> implements StoreBaseService {

    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @Autowired
    private StoreCompanyRepository storeCompanyRepository;

    @Autowired
    private StoreAnalyticsService storeAnalyticsService;

    @Autowired
    private StoreEmployeeService employeeService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private UserFavoritesStoreRepository userFavoritesStoreRepository;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private ProductIndexDao productIndexDao;

    @Autowired
    private ProductIndexService productIndexService;

    @Autowired
    private StoreAnalyticsRepository storeAnalyticsRepository;

    @Autowired
    private StoreTransportTypeRepository storeTransportTypeRepository;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @Autowired
    private StoreCategoryService storeCategoryService;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private StoreBaseDao storeBaseDao;



    private static List<Map> StoreFilterOptData;


    /**
     * 获取店铺信息
     *
     * @param storeIds
     * @return
     */
    @Override
    public List<StoreInfoVo> getStore(List<Integer> storeIds) {
        List<StoreBase> baseList = gets(storeIds);
        if (CollUtil.isEmpty(baseList)) {
            throw new BusinessException(__("店铺不存在"));
        }

        List<StoreInfoVo> infoVoList = new ArrayList<>();

        List<StoreInfo> infoList = storeInfoRepository.gets(storeIds);
        for (StoreInfo storeInfo : infoList) {
            for (StoreBase storeBase : baseList) {
                if (storeInfo.getStoreId().equals(storeBase.getStoreId())) {
                    StoreInfoVo storeInfoVo = new StoreInfoVo();
                    BeanUtils.copyProperties(storeInfo, storeInfoVo);
                    BeanUtils.copyProperties(storeBase, storeInfoVo);
                    infoVoList.add(storeInfoVo);
                }
            }
        }

        return storeAnalyticsService.getAnalytics(storeIds, infoVoList);
    }

    @Override
    public Integer getStoreId(Integer userId) {
        QueryWrapper<StoreEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("user_id", userId);
        employeeQueryWrapper.eq("employee_is_admin", 1);
        StoreEmployee storeEmployee = employeeService.findOne(employeeQueryWrapper);

        if (storeEmployee != null) {
            return storeEmployee.getStoreId();
        }

        return 0;
    }

    @Override
    @Transactional
    public boolean addStore(StoreBaseAddReq storeBaseAddReq) {
        StoreBase storeBase = BeanUtil.copyProperties(storeBaseAddReq, StoreBase.class);
        //平台代入驻即运营:显式置已开通(绕过审核流);若运营要求走审核,可改为 STORE_STATE_WAIT_VERIFY
        if (CheckUtil.isEmpty(storeBase.getStoreStateId())) {
            storeBase.setStoreStateId(StateCode.STORE_STATE_OPENED);
        }
        storeBase.setStoreIsOpen(true);
        //未传经纬度时 Convert.toDouble(null) 返回null,Point拆箱NPE,故带默认值0
        storeBase.setStoreLocation(new Point(Convert.toDouble(storeBase.getStoreLongitude(), 0D), Convert.toDouble(storeBase.getStoreLatitude(), 0D)));

        if (!save(storeBase)) {
            throw new BusinessException(__("保存店铺基础信息失败！"));
        }

        StoreInfo storeInfo = BeanUtil.copyProperties(storeBaseAddReq, StoreInfo.class);
        storeInfo.setStoreId(storeBase.getStoreId());

        //store_end_time 非空无默认,缺省给10年营业期
        if (storeInfo.getStoreEndTime() == null) {
            storeInfo.setStoreEndTime(new Date(System.currentTimeMillis() + 10L * 365 * 24 * 3600 * 1000));
        }

        if (!storeInfoRepository.save(storeInfo)) {
            throw new BusinessException(__("保存店铺信息失败！"));
        }

        RegInput in = BeanUtil.copyProperties(storeBaseAddReq, RegInput.class);
        in.setUserAccount(storeBaseAddReq.getUserAccount());
        in.setPassword(storeBaseAddReq.getUserPassword());
        in.setUserIntl(storeBaseAddReq.getStoreIntl());
        in.setUserMobile(Convert.toLong(storeBaseAddReq.getStoreTel()));
        in.setBindType(BindConnectCode.ACCOUNT);
        in.setRoleId(2);
        in.setStoreId(storeBase.getStoreId());

        if (CheckUtil.isEmpty(storeBaseAddReq.getUserPassword())) {
            in.setPassword("Shopsuite@2018" + UUID.randomUUID());
        }
        Integer userId = loginService.register(in);
        //手机绑定
        ContextUser user = new ContextUser();
        user.setUserId(userId);
        loginService.doBindMobile(user, in.getUserIntl(), in.getUserMobile());

        //补建店铺员工记录(管理员),与入驻审核 editStateId 口径一致
        QueryWrapper<StoreEmployee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("store_id", storeBase.getStoreId());
        employeeQueryWrapper.eq("user_id", userId);
        if (employeeService.findOne(employeeQueryWrapper) == null) {
            StoreEmployee employee = new StoreEmployee();
            employee.setStoreId(storeBase.getStoreId());
            employee.setUserId(userId);
            employee.setEmployeeIsAdmin(1);
            employee.setEmployeeIsKefu(0);
            employee.setRightsGroupId("0");

            if (!employeeService.save(employee)) {
                throw new BusinessException(__("保存店铺员工信息失败！"));
            }
        }

        StoreCompany storeCompany = BeanUtil.copyProperties(storeBaseAddReq, StoreCompany.class);
        storeCompany.setStoreId(storeBase.getStoreId());
        storeCompany.setUserId(userId);

        if (!storeCompanyRepository.save(storeCompany)) {
            throw new BusinessException(__("保存店铺公司信息失败！"));
        }

        // 初始化统计数据
        StoreAnalytics storeAnalytics = new StoreAnalytics();
        storeAnalytics.setStoreId(storeBase.getStoreId());
        storeAnalyticsService.add(storeAnalytics);

        StoreTransportType storeTransportType = new StoreTransportType();
        storeTransportType.setTransportTypeName("全免");
        storeTransportType.setStoreId(storeBase.getStoreId());
        storeTransportType.setTransportTypeFree(true);

        if (!storeTransportTypeRepository.add(storeTransportType)) {
            throw new BusinessException(__("全免默认模板添加失败！"));
        }

//        UserInfo userInfo = BeanUtil.copyProperties(req, UserInfo.class);
//        userInfo.setUserIntl(req.getChainIntl());
//        userInfo.setUserMobile(req.getUserMobile());
//        userInfo.setUserId(userId);
//
//        if (!userInfoService.save(userInfo)) {
//            throw new BusinessException(__("添加门店错误"));
//        }

        return true;
    }

    @Override
    @Transactional
    public boolean editStore(StoreBaseEditReq storeBaseEditReq) {
        Integer storeId = storeBaseEditReq.getStoreId();
        StoreBase store = get(storeId);

        if (store == null) {
            throw new BusinessException(__("店铺基础信息！"));
        }

        StoreBase storeBase = BeanUtil.copyProperties(storeBaseEditReq, StoreBase.class);
        storeBase.setStoreLocation(new Point(Convert.toDouble(storeBase.getStoreLongitude()), Convert.toDouble(storeBase.getStoreLatitude())));

        if (!edit(storeBase)) {
            throw new BusinessException(__("修改店铺基础信息失败！"));
        }

        StoreInfo storeInfo = BeanUtil.copyProperties(storeBaseEditReq, StoreInfo.class);

        if (!storeInfoRepository.edit(storeInfo)) {
            throw new BusinessException(__("修改店铺信息失败！"));
        }

        if (storeBaseEditReq.getStoreIsSelfsupport() != null && !storeBaseEditReq.getStoreIsSelfsupport().equals(store.getStoreIsSelfsupport())) {
            QueryWrapper<ProductIndex> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("store_id", storeId);
            long count = productIndexRepository.count(queryWrapper);

            if (count > 0) {
                ProductIndex productIndex = new ProductIndex();
                productIndex.setStoreIsSelfsupport(storeBaseEditReq.getStoreIsSelfsupport());

                if (!productIndexRepository.edit(productIndex, queryWrapper)) {
                    throw new BusinessException(__("修改商品店铺自营状态失败！"));
                }
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean removeStore(Integer storeId) {

        if (!remove(storeId)) {
            throw new BusinessException(__("删除店铺基础信息失败！"));
        }

        if (!storeInfoRepository.remove(storeId)) {
            throw new BusinessException(__("删除店铺信息失败！"));
        }
        QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
        userAdminQueryWrapper.eq("store_id", storeId);
        UserAdmin userAdmin = userAdminRepository.findOne(userAdminQueryWrapper);

        if (userAdmin == null) {
            throw new BusinessException(__("用户管理员不存在！"));
        }

        if (!userInfoService.removeUser(userAdmin.getUserId())) {
            throw new BusinessException(__("删除用户失败！"));
        }

        if (!userAdminRepository.remove(userAdmin.getUserId())) {
            throw new BusinessException(__("删除用户管理员失败！"));
        }

        return true;
    }

    @Override
    public IPage<StoreBaseRes> getList(StoreBaseListReq storeBaseListReq) {
        IPage<StoreBaseRes> storeBaseResPage = new Page<>();
        IPage<StoreBase> basePage = lists(storeBaseListReq);

        if (basePage != null && CollectionUtil.isNotEmpty(basePage.getRecords())) {
            BeanUtil.copyProperties(basePage, storeBaseResPage);
            List<StoreBase> storeBases = basePage.getRecords();
            List<StoreBaseRes> baseResList = new ArrayList<>();

            List<Integer> storeIds = CommonUtil.column(storeBases, StoreBase::getStoreId);
            List<Integer> categoryIds = CommonUtil.column(storeBases, StoreBase::getStoreCategoryId);

            List<StoreInfo> storeInfoList = storeInfoRepository.gets(storeIds);

            if (CollUtil.isEmpty(storeInfoList)) {
                throw new BusinessException(__("店铺信息不存在！"));
            }
            Map<Integer, StoreInfo> infoMap = storeInfoList.stream().collect(Collectors.toMap(StoreInfo::getStoreId, Function.identity()));
            Map<Integer, String> storeCategoryNameMap = new HashMap<>();
            Map<Integer, Integer> storeIdMap = new HashMap<>();
            Map<Integer, String> accountMap = new HashMap<>();

            List<StoreCategory> storeCategoryList = storeCategoryRepository.gets(categoryIds);

            if (CollectionUtil.isNotEmpty(storeCategoryList)) {
                storeCategoryNameMap = storeCategoryList.stream().collect(Collectors.toMap(StoreCategory::getStoreCategoryId, StoreCategory::getStoreCategoryName));
            }

            QueryWrapper<StoreCompany> storeCompanyQueryWrapper = new QueryWrapper<>();
            storeCompanyQueryWrapper.in("store_id", storeIds);
            List<StoreCompany> storeCompanies = storeCompanyRepository.find(storeCompanyQueryWrapper);


            if (CollectionUtil.isNotEmpty(storeCompanies)) {
                List<Integer> userIds = CommonUtil.column(storeCompanies, StoreCompany::getUserId);
                List<UserBase> userBaseList = userBaseRepository.gets(userIds);

                if (CollectionUtil.isNotEmpty(userBaseList)) {
                    storeIdMap = storeCompanies.stream().collect(Collectors.toMap(StoreCompany::getStoreId, StoreCompany::getUserId, (k1, k2) -> k1));
                    accountMap = userBaseList.stream().collect(Collectors.toMap(UserBase::getUserId, UserBase::getUserAccount, (k1, k2) -> k1));
                }
            }

            for (StoreBase storeBase : storeBases) {
                StoreBaseRes storeBaseRes = BeanUtil.copyProperties(storeBase, StoreBaseRes.class);
                Integer storeId = storeBase.getStoreId();
                StoreInfo storeInfo = infoMap.get(storeId);

                if (storeInfo != null) {
                    BeanUtil.copyProperties(storeInfo, storeBaseRes);
                }
                Integer userId = storeIdMap.get(storeId);

                if (CheckUtil.isNotEmpty(userId)) {
                    storeBaseRes.setUserAccount(accountMap.get(userId));
                }
                storeBaseRes.setStoreCategoryName(storeCategoryNameMap.get(storeBase.getStoreCategoryId()));

                baseResList.add(storeBaseRes);
            }
            storeBaseResPage.setRecords(baseResList);
        }

        return storeBaseResPage;
    }

    // 获取店铺详情
    @Override
    public StoreDetailVo getStoreDetail(Integer storeId) {
        StoreDetailVo storeDetailVo = new StoreDetailVo();
        StoreBase storeBase = get(storeId);
        StoreInfo storeInfo = storeInfoRepository.get(storeId);
        StoreAnalytics storeAnalytics = storeAnalyticsService.getStoreAnalytics(storeId);
        storeDetailVo.setStoreInfo(storeInfo);
        storeDetailVo.setStoreBase(storeBase);
        storeDetailVo.setStoreAnalytics(storeAnalytics);

        ContextUser user = ContextUtil.getLoginUser();
        if (ObjectUtil.isNotEmpty(user)) {
            Integer userId = user.getUserId();
            if (userId != null) {
                QueryWrapper<UserFavoritesStore> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", userId);
                queryWrapper.eq("store_id", storeId);
                UserFavoritesStore userFavoritesStore = userFavoritesStoreRepository.findOne(queryWrapper);
                if (ObjectUtil.isNotEmpty(userFavoritesStore)) {
                    storeDetailVo.setIsFavorite(true);
                }
            }
        }

        QueryWrapper<ActivityBase> activityBaseQueryWrapper = new QueryWrapper<>();
        activityBaseQueryWrapper.eq("activity_state", StateCode.ACTIVITY_STATE_NORMAL);
        activityBaseQueryWrapper.eq("activity_type_id", StateCode.ACTIVITY_TYPE_POP);
        activityBaseQueryWrapper.eq("store_id", storeId);
        List<ActivityBase> activityBases = activityBaseRepository.find(activityBaseQueryWrapper);

        if (CollectionUtil.isNotEmpty(activityBases)) {
            if (user == null) {
                storeDetailVo.setPopUps(dealWithPopUp(activityBases, null));
            } else {
                UserInfo userInfo = userInfoService.get(user.getUserId());
                storeDetailVo.setPopUps(dealWithPopUp(activityBases, userInfo));
            }
        }

        return storeDetailVo;
    }

    private List<PagePopUpVo> dealWithPopUp(List<ActivityBase> activityList, UserInfo userInfo) {
        List<PagePopUpVo> pagePopUpVos = new ArrayList<>();

        if (CollectionUtil.isEmpty(activityList)) {
            throw new BusinessException(__("活动列表信息为空！"));
        }
        for (ActivityBase activityBase : activityList) {

            if (StrUtil.isNotEmpty(activityBase.getActivityRule())) {
                ActivityRuleVo activityRuleJson = JSONUtil.parseObject(activityBase.getActivityRule(), ActivityRuleVo.class);

                if (activityRuleJson != null) {
                    PopupVo popUp = activityRuleJson.getPopup();

                    if (popUp != null) {
                        Integer popUpType = popUp.getPopUpType();

                        if (userInfo != null) {
                            //如果用户不符合弹窗等级，过滤此弹窗
                            String activityUseLevel = activityBase.getActivityUseLevel();
                            List<Integer> userLevelList = Convert.toList(Integer.class, activityUseLevel);

                            if (CollectionUtil.isNotEmpty(userLevelList)) {

                                if (!userLevelList.contains(userInfo.getUserLevelId())) {

                                    continue;
                                }
                            }

                            //如果不是新人，则不展示新人礼包弹窗
                            if (popUpType == 0 && !userInfo.getUserNew()) {

                                continue;
                            }
                        }

                        PagePopUpVo pagePopUpVo = new PagePopUpVo();
                        pagePopUpVo.setPopUpEnable(true);
                        pagePopUpVo.setPopUpImage(popUp.getPopUpImage());
                        pagePopUpVo.setPopUpUrl(popUp.getPopUpUrl());
                        pagePopUpVos.add(pagePopUpVo);
                    }
                }
            }
        }

        return pagePopUpVos;
    }

    @Override
    public IPage<StoreBaseRes> getStoreList(StoreBaseListReq storeBaseListReq) {
        IPage<StoreBaseRes> storeBaseResPage = new Page<>();

        ContextUser user = ContextUtil.getLoginUser();

        if (storeBaseListReq.getStoreType() == null) {
            storeBaseListReq.setStoreType(user != null && user.isStore() && configBaseService.ifSupplierMarket() ? 2 : 1);
        }
        storeBaseListReq.setStoreIsOpen(true);
        StoreStreetInput storeStreetInput = BeanUtil.copyProperties(storeBaseListReq, StoreStreetInput.class);

        // 处理附近的店铺
        Map<String, String> coordinate = ObjectUtil.defaultIfNull(Convert.toMap(String.class, String.class, getParameter("coordinate")), new HashMap<>());

        if (CollUtil.isEmpty(coordinate)) {
            String lng = Convert.toStr(getParameter("lng"), "");
            String lat = Convert.toStr(getParameter("lat"), "");

            if (StrUtil.isNotBlank(lng) && StrUtil.isNotBlank(lat)) {
                coordinate.put("lng", lng);
                coordinate.put("lat", lat);
            }
        }

        if (CollUtil.isNotEmpty(coordinate)) {

            if (StrUtil.isNotBlank(storeBaseListReq.getKeywords())) {
                storeStreetInput.setStoreName(storeBaseListReq.getKeywords());
            }
            String lng = Convert.toStr(coordinate.get("lng"));
            String lat = Convert.toStr(coordinate.get("lat"));
            storeStreetInput.setLat(lat);
            storeStreetInput.setLng(lng);
        } else {
            storeStreetInput.setLat("0");
            storeStreetInput.setLng("0");
        }
        IPage<StoreStreetOutput> outputIPage = storeBaseDao.getNearShop(new Page<>(storeBaseListReq.getPage(), storeBaseListReq.getSize()), storeStreetInput);

        if (outputIPage != null && CollectionUtil.isNotEmpty(outputIPage.getRecords())) {
            BeanUtil.copyProperties(outputIPage, storeBaseResPage);
            List<StoreStreetOutput> outputs = outputIPage.getRecords();
            List<StoreBaseRes> baseResList = new ArrayList<>();

            List<Integer> storeIds = CommonUtil.column(outputs, StoreStreetOutput::getStoreId);

            List<StoreInfo> storeInfoList = storeInfoRepository.gets(storeIds);

            if (CollUtil.isEmpty(storeInfoList)) {
                throw new BusinessException(__("店铺信息不存在！"));
            }
            Map<Integer, StoreInfo> infoMap = storeInfoList.stream().collect(Collectors.toMap(StoreInfo::getStoreId, Function.identity()));

            List<Integer> productIds = new ArrayList<>();

            String version = productIndexDao.getVersion();

            if (StrUtil.isEmpty(version)) {
                throw new BusinessException(I18nUtil.__("获取数据库版本失败！"));
            }
            int dotIndex = version.indexOf(".");
            Integer versionNum = Convert.toInt(version.substring(0, dotIndex));

            if (versionNum < 8) {
                productIds = productIndexDao.getProductIdByStoreQuery(StateCode.PRODUCT_STATE_NORMAL, storeIds, storeBaseListReq.getKeyType());
            } else {
                productIds = productIndexDao.getProductIdByStoreHigh(StateCode.PRODUCT_STATE_NORMAL, storeIds, storeBaseListReq.getKeyType());
            }
            Map<Integer, List<ProductOutput>> listMap = new HashMap<>();
            Map<Integer, UserFavoritesStore> favoritesStoreMap = new HashMap<>();
            /*Map<Integer, StoreAnalytics> storeAnalyticsMap = new HashMap<>();

            List<StoreAnalytics> analyticsList = storeAnalyticsRepository.gets(storeIds);

            if (CollectionUtil.isNotEmpty(analyticsList)) {
                storeAnalyticsMap = analyticsList.stream().collect(Collectors.toMap(StoreAnalytics::getStoreId, StoreAnalytics -> StoreAnalytics, (k1, k2) -> k1));
            }*/

            if (CollectionUtil.isNotEmpty(productIds)) {
                ProductIndexInput input = new ProductIndexInput();
                input.setProductIds(productIds);
                ProductListRes pageList = productIndexService.listItem(input);

                if (pageList != null && CollectionUtil.isNotEmpty(pageList.getItems())) {
                    List<ProductOutput> productOutputs = pageList.getItems();
                    listMap = productOutputs.stream().collect(Collectors.groupingBy(ProductOutput::getStoreId));
                }
            }

            ContextUser loginUser = ContextUtil.getLoginUser();

            if (loginUser != null) {
                QueryWrapper<UserFavoritesStore> favoritesStoreQueryWrapper = new QueryWrapper<>();
                favoritesStoreQueryWrapper.eq("user_id", loginUser.getUserId());
                favoritesStoreQueryWrapper.in("store_id", storeIds);
                List<UserFavoritesStore> favoritesStores = userFavoritesStoreRepository.find(favoritesStoreQueryWrapper);

                if (CollectionUtil.isNotEmpty(favoritesStores)) {
                    favoritesStoreMap = favoritesStores.stream().collect(Collectors.toMap(UserFavoritesStore::getStoreId, UserFavoritesStore -> UserFavoritesStore, (k1, k2) -> k1));
                }
            }

            for (StoreStreetOutput streetOutput : outputs) {
                StoreBaseRes storeBaseRes = BeanUtil.copyProperties(streetOutput, StoreBaseRes.class);
                Integer storeId = streetOutput.getStoreId();
                StoreInfo storeInfo = infoMap.get(storeId);

                if (storeInfo != null) {
                    BeanUtil.copyProperties(storeInfo, storeBaseRes);
                }

                if (listMap.containsKey(storeId)) {
                    storeBaseRes.setProducts(listMap.get(storeId));
                }
                storeBaseRes.setStoreIsFavorites(favoritesStoreMap.containsKey(storeId) ? 1 : 0);

                /*if (storeAnalyticsMap.containsKey(storeId)) {
                    StoreAnalytics storeAnalytics = storeAnalyticsMap.get(storeId);
                    storeBaseRes.setStoreFavoriteNum(storeAnalytics.getStoreFavoriteNum());
                }*/

                baseResList.add(storeBaseRes);
            }
            storeBaseResPage.setRecords(baseResList);
        }

        return storeBaseResPage;
    }

    @Override
    @Transactional
    public boolean editState(StoreBase storeBase) {

        if (!edit(storeBase)) {
            throw new BusinessException(__("修改店铺状态失败！"));
        }

        QueryWrapper<ProductIndex> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeBase.getStoreId());
        long count = productIndexRepository.count(queryWrapper);

        if (count > 0) {
            ProductIndex productIndex = new ProductIndex();
            productIndex.setStoreIsOpen(storeBase.getStoreIsOpen());

            if (!productIndexRepository.edit(productIndex, queryWrapper)) {
                throw new BusinessException(__("修改商品店铺状态失败！"));
            }
        }

        return true;
    }

    @Override
    @Transactional
    public boolean editStateId(StoreBase storeBase) {

        if (!edit(storeBase)) {
            throw new BusinessException(__("修改店铺审核状态失败！"));
        }
        Integer storeId = storeBase.getStoreId();
        QueryWrapper<StoreCompany> storeCompanyQueryWrapper = new QueryWrapper<>();
        storeCompanyQueryWrapper.eq("store_id", storeId);
        StoreCompany storeCompany = storeCompanyRepository.findOne(storeCompanyQueryWrapper);

        if (storeCompany == null) {
            throw new BusinessException(__("店铺公司信息不存在！"));
        }
        Integer userId = storeCompany.getUserId();

        if (Objects.equals(storeBase.getStoreStateId(), StateCode.STORE_STATE_YES)) {
            QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
            userAdminQueryWrapper.eq("store_id", storeId);
            UserAdmin userAdmin = userAdminRepository.findOne(userAdminQueryWrapper);

            if (userAdmin == null) {
                //商家入驻：入驻角色可配置(seller_role_id，缺省沿用原硬编码1002)
                userAdmin = new UserAdmin();
                userAdmin.setUserId(userId);
                userAdmin.setRoleId(ConstantRole.ROLE_SELLER);
                userAdmin.setUserRoleId(configBaseService.getConfig("seller_role_id", 1002));
                userAdmin.setStoreId(storeId);
                userAdmin.setUserEnable(true);
                userAdmin.setUserIsSuperadmin(false);

                if (!userAdminRepository.save(userAdmin)) {
                    throw new BusinessException(__("保存管理员信息失败！"));
                }
            }

            //补建店铺员工记录(管理员)，商家子账号体系要求存在
            QueryWrapper<StoreEmployee> employeeQueryWrapper = new QueryWrapper<>();
            employeeQueryWrapper.eq("store_id", storeId);
            employeeQueryWrapper.eq("user_id", userId);
            StoreEmployee employee = employeeService.findOne(employeeQueryWrapper);

            if (employee == null) {
                employee = new StoreEmployee();
                employee.setStoreId(storeId);
                employee.setUserId(userId);
                employee.setEmployeeIsAdmin(1);
                employee.setEmployeeIsKefu(0);
                employee.setRightsGroupId("0");

                if (!employeeService.save(employee)) {
                    throw new BusinessException(__("保存店铺员工信息失败！"));
                }
            }
        }

        //消息通知
        String messageId = "store-review-remind";
        Map<String, Object> args = new HashMap<>();
        args.put("result", storeBase.getStoreStateId() == StateCode.STORE_STATE_WAIT_PROFILE ? "待完善资料"
                : storeBase.getStoreStateId() == StateCode.STORE_STATE_NO ? "审核失败" : "审核通过");
        messageTemplateService.send(userId, messageId, args);

        return true;
    }

    @Override
    @Transactional
    public boolean openStore(Integer storeId) {
        StoreBase storeBase = get(storeId);

        if (storeBase == null) {
            throw new BusinessException(__("店铺不存在！"));
        }

        if (!Objects.equals(storeBase.getStoreStateId(), StateCode.STORE_STATE_YES)) {
            throw new BusinessException(__("店铺状态不允许开通(需资料审核通过)！"));
        }

        StoreBase update = new StoreBase();
        update.setStoreId(storeId);
        update.setStoreStateId(StateCode.STORE_STATE_OPENED);
        update.setStoreIsOpen(true);

        //复用editState：更新店铺状态并同步商品storeIsOpen
        return editState(update);
    }

    @Override
    @Transactional
    public boolean storeEnter(StoreBaseEditReq storeBaseEditReq) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (user.isPlatform()) {
            throw new BusinessException(__("管理员不可申请店铺入驻！"));
        }
        Integer userId = user.getUserId();
        StoreBase storeBase = BeanUtil.copyProperties(storeBaseEditReq, StoreBase.class);
        StoreCompany company = BeanUtil.copyProperties(storeBaseEditReq, StoreCompany.class);

        StoreCompany storeCompany = storeCompanyRepository.findOne(new QueryWrapper<StoreCompany>().eq("user_id", userId));

        if (storeCompany != null) {
            Integer storeId = storeCompany.getStoreId();

            StoreBase base = get(storeId);

            if (base == null) {
                throw new BusinessException(__("店铺基础信息异常！"));
            }

            if (!Objects.equals(base.getStoreStateId(), StateCode.STORE_STATE_NO)) {
                throw new BusinessException(__("请勿重复申请！"));
            }
            storeBase.setStoreId(storeId);
            company.setCompanyId(storeCompany.getCompanyId());
        }

        storeBase.setStoreStateId(StateCode.STORE_STATE_WAIT_VERIFY);
        storeBase.setStoreLocation(new Point(Convert.toDouble(storeBase.getStoreLongitude()), Convert.toDouble(storeBase.getStoreLatitude())));

        if (!save(storeBase)) {
            throw new BusinessException(__("保存店铺基础信息失败！"));
        }
        Integer storeId = storeBase.getStoreId();

        StoreInfo storeInfo = BeanUtil.copyProperties(storeBaseEditReq, StoreInfo.class);
        storeInfo.setStoreId(storeId);

        if (!storeInfoRepository.saveOrUpdate(storeInfo)) {
            throw new BusinessException(__("保存店铺详细信息失败！"));
        }

        company.setStoreId(storeId);
        company.setUserId(userId);

        if (!storeCompanyRepository.saveOrUpdate(company)) {
            throw new BusinessException(__("保存店铺公司信息失败！"));
        }

       if (storeCompany == null) {
           // 初始化统计数据
           StoreAnalytics storeAnalytics = new StoreAnalytics();
           storeAnalytics.setStoreId(storeId);

           if (!storeAnalyticsRepository.add(storeAnalytics)) {
               throw new BusinessException(__("店铺统计添加失败！"));
           }

           StoreTransportType storeTransportType = new StoreTransportType();
           storeTransportType.setTransportTypeName("全免");
           storeTransportType.setStoreId(storeId);
           storeTransportType.setTransportTypeFree(true);

           if (!storeTransportTypeRepository.add(storeTransportType)) {
               throw new BusinessException(__("全免默认模板添加失败！"));
           }
       }

        return true;
    }

    @Override
    public StoreDetailVo getStoreInfo() {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer userId = user.getUserId();

        StoreCompany storeCompany = storeCompanyRepository.findOne(new QueryWrapper<StoreCompany>().eq("user_id", userId));

        if (storeCompany == null) {
            return null;
        }
        Integer storeId = storeCompany.getStoreId();
        StoreDetailVo storeDetailVo = new StoreDetailVo();
        StoreBase storeBase = get(storeId);

        if (storeBase == null) {
            throw new BusinessException(__("店铺基础信息不存在！"));
        }
        storeDetailVo.setStoreBase(storeBase);

        StoreInfo storeInfo = storeInfoRepository.get(storeId);

        if (storeInfo == null) {
            throw new BusinessException(__("店铺详细信息不存在！"));
        }
        storeDetailVo.setStoreInfo(storeInfo);
        storeDetailVo.setStoreCompany(storeCompany);

        return storeDetailVo;
    }

    @Override
    public List<Map> getFilterOpt() {
        if (CollUtil.isNotEmpty(StoreFilterOptData)) return StoreFilterOptData;

        List<Map> data = new ArrayList<>();

        Map<String, Object> categoryRows = new HashMap<>();
        categoryRows.put("name", I18nUtil.__("全部分类"));
        categoryRows.put("type", "hierarchy");
        StoreCategoryListReq storeCategoryListReq = new StoreCategoryListReq();
        storeCategoryListReq.setStoreCategoryIsEnable(true);
        List<StoreCategoryRes> categoryResList = storeCategoryService.getTree(storeCategoryListReq);
        StoreCategoryRes storeCategoryRes = new StoreCategoryRes();
        storeCategoryRes.setName(I18nUtil.__("全部"));
        storeCategoryRes.setValue(0);
        categoryResList.add(0, storeCategoryRes);

        categoryRows.put("children", categoryResList);
        data.add(categoryRows);

        Map<String, Object> comprehensiveRanking = new HashMap<>();
        comprehensiveRanking.put("name", I18nUtil.__("综合排序"));
        comprehensiveRanking.put("type", "hierarchy");
        List<StoreSortVo> storeSortVos = new ArrayList<>();
        storeSortVos.add(new StoreSortVo(I18nUtil.__("综合排序"), "DESC", "store_sales_num"));
        storeSortVos.add(new StoreSortVo(I18nUtil.__("销量优先"), "DESC", "store_sales_num"));
        storeSortVos.add(new StoreSortVo(I18nUtil.__("距离优先"), "ASC", "store_distance"));
        storeSortVos.add(new StoreSortVo(I18nUtil.__("评分优先"), "DESC", "store_evaluation_rate"));
        storeSortVos.add(new StoreSortVo(I18nUtil.__("速度优先"), "DESC", "store_deliverycredit"));

        comprehensiveRanking.put("children", storeSortVos);
        data.add(comprehensiveRanking);

        Map<String, Object> nearOptRows = new HashMap<>();
        nearOptRows.put("name", I18nUtil.__("附近"));
        nearOptRows.put("type", "hierarchy");
        List<StoreNearVo> storeNearVos = new ArrayList<>();
        storeNearVos.add(new StoreNearVo(I18nUtil.__("附近"), 100000));
        storeNearVos.add(new StoreNearVo(I18nUtil.__("1km"), 1000));
        storeNearVos.add(new StoreNearVo(I18nUtil.__("3km"), 3000));
        storeNearVos.add(new StoreNearVo(I18nUtil.__("5km"), 5000));
        storeNearVos.add(new StoreNearVo(I18nUtil.__("10km"), 10000));
        storeNearVos.add(new StoreNearVo(I18nUtil.__("全部"), 999999999));
        List<Map<String, Object>> childNear = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("index", "near");
        map.put("name", I18nUtil.__("附近"));
        map.put("value", 100000);
        map.put("children", storeNearVos);
        childNear.add(map);
        nearOptRows.put("children", childNear);
        data.add(nearOptRows);

        StoreFilterOptData = data;
        return data;
    }

}
