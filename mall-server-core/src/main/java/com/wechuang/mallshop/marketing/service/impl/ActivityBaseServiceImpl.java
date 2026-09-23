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
package com.wechuang.mallshop.marketing.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.entity.ActivityItem;
import com.wechuang.mallshop.marketing.model.req.*;
import com.wechuang.mallshop.marketing.model.res.ActivityBaseRes;
import com.wechuang.mallshop.marketing.model.res.ActivityItemRes;
import com.wechuang.mallshop.marketing.model.res.ActivityRuleItemRes;
import com.wechuang.mallshop.marketing.model.res.ActivityRuleRes;
import com.wechuang.mallshop.marketing.model.vo.*;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.marketing.repository.ActivityItemRepository;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.pt.model.entity.ProductBase;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.ProductBaseRepository;
import com.wechuang.mallshop.pt.repository.ProductImageRepository;
import com.wechuang.mallshop.pt.repository.ProductIndexRepository;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.model.entity.UserVoucherNum;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.repository.UserVoucherNumRepository;
import com.wechuang.mallshop.shop.repository.UserVoucherRepository;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import com.wechuang.mallshop.sys.service.CurrencyBaseService;
import com.wechuang.mallshop.trade.model.entity.OrderBase;
import com.wechuang.mallshop.trade.model.entity.OrderData;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import com.wechuang.mallshop.trade.repository.OrderBaseRepository;
import com.wechuang.mallshop.trade.repository.OrderDataRepository;
import com.wechuang.mallshop.trade.repository.OrderItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 活动表-通过插件实现	当为拼团是activity_rule中的group_remain_quantity用于标识拼团剩余需要人数，如果用户登录了，需要查询出activity_groupbooking中参与该团的剩余情况 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-06-29
 */
@Service
public class ActivityBaseServiceImpl extends BaseServiceImpl<ActivityBaseRepository, ActivityBase, ActivityBaseListReq> implements ActivityBaseService {
    @Autowired
    private ActivityItemRepository activityItemRepository;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Autowired
    private UserVoucherRepository voucherRepository;

    @Autowired
    private CurrencyBaseService currencyBaseService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private UserLevelRepository levelRepository;

    @Autowired
    private UserVoucherNumRepository userVoucherNumRepository;

    @Autowired
    private OrderDataRepository orderDataRepository;

    @Autowired
    private OrderBaseRepository orderBaseRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserVoucherService userVoucherService;

    /**
     * 读取店铺下面的活动信息
     *
     * @param storeIds 店铺编号
     * @return
     */
    @Override
    public Map getStoreActivityItmeData(List<Integer> storeIds, List<Integer> activityTypeIds) {
        if (CollUtil.isEmpty(activityTypeIds)) {
            activityTypeIds = ListUtil.toList(StateCode.ACTIVITY_TYPE_VOUCHER);
        }

        Map activity_data = new HashMap();
        Map activity_info = new HashMap();

        // 根据店铺读取活动
        QueryWrapper<ActivityBase> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("activity_type_id", activityTypeIds);
        queryWrapper.in("store_id", storeIds);
        queryWrapper.orderByAsc("activity_sort");
        queryWrapper.in("activity_state", StateCode.ACTIVITY_STATE_WAITING, StateCode.ACTIVITY_STATE_NORMAL);

        List<ActivityBase> activity_rows = fixActivityData(find(queryWrapper));

        if (CollUtil.isNotEmpty(activity_rows)) {

            Map<Integer, Map> activity_state_rows = activity_rows.stream().collect(Collectors.toMap(m -> Convert.toInt(m.getActivityState()), m -> {
                Map map = new HashMap();
                map.put(Convert.toInt(m.getActivityId()), m.getActivityName());
                return map;
            }, (old, now) -> now));

            Map activity_state_row = activity_state_rows.get(StateCode.ACTIVITY_STATE_NORMAL);
            if (ObjectUtil.isNotNull(activity_state_row)) {
                //限制会员等级的活动类型  等级筛选
                ArrayList<Integer> use_level_activity_type_id_row = ListUtil.toList(
                        StateCode.ACTIVITY_TYPE_VOUCHER
                );

                ContextUser loginUser = ContextUtil.getLoginUser();

                UserInfo userInfo = null;
                if (loginUser != null) {
                    userInfo = userInfoRepository.get(loginUser.getUserId());
                }

                for (ActivityBase activity_row : activity_rows) {
                    // 遍历过滤开启中状态
                    Integer activity_state = Convert.toInt(activity_row.getActivityState());
                    if (activity_state == StateCode.ACTIVITY_STATE_NORMAL) {
                        //是不是在限制会员等级的活动类型中，并且活动设置了限制会员等级
                        Integer activity_type_id = Convert.toInt(activity_row.getActivityTypeId());
                        List<Integer> activity_use_level = Convert.toList(Integer.class, activity_row.getActivityUseLevel());
                        if (userInfo != null && use_level_activity_type_id_row.contains(activity_type_id) && CollUtil.isNotEmpty(activity_use_level)) {
                            //if (activity_use_level.contains(userInfo.getUserLevelId())) { //包含模式
                            if (userInfo.getUserLevelId().intValue() > activity_use_level.get(0).intValue()) { //大于最小模式
                                Integer store_id = Convert.toInt(activity_row.getStoreId());
                                List<ActivityBase> activity_row_List = (List<ActivityBase>) ObjectUtil.defaultIfNull(activity_info.get(store_id), new ArrayList<>());
                                activity_row_List.add(activity_row);
                                activity_info.put(store_id, activity_row_List);
                            }
                        } else {
                            Integer store_id = Convert.toInt(activity_row.getStoreId());
                            List<ActivityBase> activity_row_List = (List<ActivityBase>) ObjectUtil.defaultIfNull(activity_info.get(store_id), new ArrayList<>());
                            activity_row_List.add(activity_row);
                            activity_info.put(store_id, activity_row_List);
                        }
                    }
                }
            }
        }

        activity_data.put("activity_info", activity_info);
        return activity_data;
    }


    /**
     * 取得活动相关产品信息
     *
     * @param activityBaseList
     * @return
     */
    @Override
    public List<ActivityBase> fixActivityData(List<ActivityBase> activityBaseList) {

        if (CollUtil.isEmpty(activityBaseList)) return new ArrayList<>();

        /*
        List<Integer> storeIds = activityBaseList.stream().map(base -> base.getStoreId()).distinct().collect(Collectors.toList());
        QueryWrapper<StoreBase> baseQueryWrapper = new QueryWrapper<>();
        baseQueryWrapper.in("store_id", storeIds);
        List<StoreBase> storeBaseList = storeBaseService.find(baseQueryWrapper);
         */

        //活动分类信息
        /*
        List<Integer> activityTypeIds = activityBaseList.stream().map(base -> base.getActivityTypeId()).distinct().collect(Collectors.toList());
        QueryWrapper<ActivityType> typeQueryWrapper = new QueryWrapper<>();
        typeQueryWrapper.in("activity_type_id", activityTypeIds);
        List<ActivityType> activityTypeList = activityTypeRepository.getActivityTypeList(typeQueryWrapper);
        */

        Date now = new Date();
        long time = now.getTime();

        for (ActivityBase baseMap : activityBaseList) {

            Integer activity_id = baseMap.getActivityId();
            //判断活动店铺有效
            Integer activity_state = baseMap.getActivityState();
            // 活动开始时间
            Long activity_starttime = baseMap.getActivityStarttime();
            // 活动结束时间
            Long activity_endtime = baseMap.getActivityEndtime();
            // 活动类型
            Integer activity_type = baseMap.getActivityType();

            if (CheckUtil.isNotEmpty(activity_type) && Objects.equals(activity_type, StateCode.GET_VOUCHER_BY_PURCHASE)) {
                //根据售卖优惠券id获取对应商品信息
            }

            switch (activity_state) {
                case StateCode.ACTIVITY_STATE_WAITING:
                    if (activity_starttime.compareTo(time) <= 0) {
                        if (activity_endtime.compareTo(time) <= 0) {
                            baseMap.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
                        } else {
                            baseMap.setActivityState(StateCode.ACTIVITY_STATE_NORMAL);
                        }

                        //更新数据
                        editActivityBase(activity_id, baseMap);
                    }
                    break;
                case StateCode.ACTIVITY_STATE_NORMAL:
                    if (activity_endtime.compareTo(time) < 0) {
                        baseMap.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
                        //更新数据
                        editActivityBase(activity_id, baseMap);
                    }

                    if (activity_starttime.compareTo(time) > 0) {
                        baseMap.setActivityState(StateCode.ACTIVITY_STATE_WAITING);

                        //更新数据
                        editActivityBase(activity_id, baseMap);
                    }
                    break;
                case StateCode.ACTIVITY_STATE_FINISHED:
                    if (activity_starttime.compareTo(time) <= 0) {
                        if (activity_endtime.compareTo(time) <= 0) {
                            baseMap.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
                        } else {
                            baseMap.setActivityState(StateCode.ACTIVITY_STATE_NORMAL);
                            //更新数据
                            editActivityBase(activity_id, baseMap);
                        }
                    }

                    if (activity_starttime.compareTo(time) > 0) {
                        baseMap.setActivityState(StateCode.ACTIVITY_STATE_WAITING);

                        //更新数据
                        editActivityBase(activity_id, baseMap);
                    }

                    break;
                case StateCode.ACTIVITY_STATE_CLOSED:
                    if (activity_starttime.compareTo(time) <= 0 && activity_endtime.compareTo(time) <= 0) {
                        baseMap.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
                        //更新数据
                        editActivityBase(activity_id, baseMap);
                    }

                    break;
            }

            /*
            Integer storeId = baseMap.getStoreId();
            // 获取store_name
            Optional<String> nameOpl = storeBaseList.stream().filter(store -> Objects.equals(store.getStore_id(), storeId)).map(StoreBase::getStore_name).findFirst();
            nameOpl.ifPresent(n -> baseMap.put("store_name", n));
             */
        }

        return activityBaseList;
    }


    /**
     * 取得活动相关产品信息
     *
     * @param activity_id
     * @param data
     */
    @Transactional
    public boolean editActivityBase(Integer activity_id, ActivityBase data) {
        Integer activityTypeId = data.getActivityTypeId();

        /*
        if (ObjectUtil.equal(activity_type_id, StateCode.ACTIVITY_TYPE_GROUPBOOKING) || ObjectUtil.equal(activity_type_id, StateCode.ACTIVITY_TYPE_CUTPRICE)) {

            // 判断商品是否更改
            QueryWrapper<ActivityItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.eq("activity_id", activity_id);
            List<ActivityItem> activity_item_rows = activityItemRepository.find(itemQueryWrapper);

            List<Long> item_id_row = activity_item_rows.stream().map(s -> s.getItemId()).distinct().collect(Collectors.toList());

            Object obj_activity_rule = data.get("activity_rule");
            JSONObject activity_rule = new JSONObject();
            if (obj_activity_rule instanceof String) {
                activity_rule = JSONObject.parseObject((String) obj_activity_rule);
            } else if (obj_activity_rule instanceof JSONObject) {
                activity_rule = (JSONObject) obj_activity_rule;
            }

            Long itemId = Convert.toLong(activity_rule.get("item_id"));

            for (Long item_id : item_id_row) {
                if (ObjectUtil.notEqual(itemId, item_id)) {
                    if (!removeItem(activity_id, item_id, data)) {
                        throw new BusinessException(__("修改活动商品信息失败！"));
                    }
                }
            }

            if (!item_id_row.contains(itemId)) {
                if (!addItem(activity_id, Arrays.asList(itemId), data)) {
                    throw new BusinessException(__("修改活动商品信息失败！"));
                }
            }
        }
         */

        if (!repository.save(data)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        Integer activity_state = data.getActivityState();

        if (activity_state != null) {
            // 取得对应的product_id
            QueryWrapper<ActivityItem> _itemQueryWrapper = new QueryWrapper<>();
            _itemQueryWrapper.eq("activity_id", activity_id);
            List<ActivityItem> activity_item_rows = activityItemRepository.find(_itemQueryWrapper);

            // 1. 修改store_activity_item
            if (CollUtil.isNotEmpty(activity_item_rows)) {

                List<Long> activity_item_ids = activity_item_rows.stream().map(s -> s.getActivityItemId()).distinct().collect(Collectors.toList());
                QueryWrapper<ActivityItem> actItemQueryWrapper = new QueryWrapper<>();
                actItemQueryWrapper.in("activity_item_id", activity_item_ids);

                ActivityItem activityItem = new ActivityItem();
                activityItem.setActivityItemState(activity_state);

                if (!activityItemRepository.edit(activityItem, actItemQueryWrapper)) {
                    throw new BusinessException(ResultCode.FAILED);
                }
            }

            List<ProductItem> productItems = new ArrayList<>();

            if (activityTypeId == StateCode.ACTIVITY_TYPE_MULTIPLEDISCOUNT
                    || activityTypeId == StateCode.ACTIVITY_TYPE_BATDISCOUNT
                    || activityTypeId == StateCode.ACTIVITY_TYPE_GIFTBAG) {
                String activityItemIds = data.getActivityItemIds();

                if (StrUtil.isNotEmpty(activityItemIds)) {
                    productItems = productItemRepository.gets(Convert.toList(Long.class, activityItemIds));
                }
            }

            // 2. 判断是否有状态的变化，如果有有，则修改product_index
            if (CollUtil.isNotEmpty(activity_item_rows) || CollectionUtil.isNotEmpty(productItems)) {
                ActivityBase activity_row = get(activity_id);
                Integer _activity_type_id = activity_row.getActivityTypeId();
                List<Long> product_id_row = new ArrayList<>();

                if (CollectionUtil.isNotEmpty(activity_item_rows)) {
                    product_id_row = activity_item_rows.stream().map(s -> s.getProductId()).distinct().collect(Collectors.toList());
                } else {
                    product_id_row = productItems.stream().map(s -> s.getProductId()).distinct().collect(Collectors.toList());
                }

                // 修改product_index中数据
                List<ProductIndex> product_index_rows = productIndexRepository.gets(product_id_row);
                for (ProductIndex product_index_row : product_index_rows) {
                    String str_activity_type_ids = product_index_row.getActivityTypeIds();
                    List<Integer> activity_type_ids = Convert.toList(Integer.class, str_activity_type_ids);

                    if (ObjectUtil.equal(activity_state, StateCode.ACTIVITY_STATE_NORMAL)) {
                        // 不存在则添加
                        if (!activity_type_ids.contains(_activity_type_id)) {
                            activity_type_ids.add(_activity_type_id);
                            // 修改
                            product_index_row.setActivityTypeIds(CollUtil.join(activity_type_ids, ","));
                        }
                    } else {
                        // 存在则删除
                        if (activity_type_ids.contains(_activity_type_id)) {
                            activity_type_ids.remove(_activity_type_id);
                            // 修改
                            product_index_row.setActivityTypeIds(CollUtil.join(activity_type_ids, ","));
                        }
                    }

                    if (!productIndexRepository.save(product_index_row)) {
                        throw new BusinessException(ResultCode.FAILED);
                    }
                }
            }
        }

        return true;
    }


    @Transactional
    public boolean addItem(Integer activity_id, List<Long> item_ids, ActivityBase activity_row) {

        if (ObjectUtil.isEmpty(activity_row)) {
            activity_row = get(activity_id);
        }

        List<ActivityItem> activity_item_rows = new ArrayList<>(item_ids.size());
        Integer activity_state = Convert.toInt(activity_row.getActivityState());

        List<ProductItem> product_item_rows = productItemRepository.gets(item_ids);
        List<Long> product_ids = product_item_rows.stream().map(s -> Convert.toLong(s.getProductId())).distinct().collect(Collectors.toList());
        List<ProductIndex> product_index_rows = productIndexRepository.gets(product_ids);

        for (Long item_id : item_ids) {
            Optional<ProductItem> itemOpl = product_item_rows.stream().filter(s -> ObjectUtil.equal(Convert.toLong(s.getItemId()), item_id)).findFirst();
            if (itemOpl.isPresent()) {
                ProductItem product_index_row = itemOpl.get();
                Long product_id = Convert.toLong(product_index_row.getProductId());
                BigDecimal itemUnitPrice = product_index_row.getItemUnitPrice();

                Optional<ProductIndex> indexOpl = product_index_rows.stream().filter(s -> ObjectUtil.equal(s.getProductId(), product_id)).findFirst();
                ProductIndex index = indexOpl.orElseGet(ProductIndex::new);

                ActivityItem item = new ActivityItem();
                activity_item_rows.add(item);

                item.setProductId(product_id);
                item.setCategoryId(index.getCategoryId());
                item.setItemId(item_id);
                item.setActivityItemStarttime(activity_row.getActivityStarttime());
                item.setActivityItemEndtime(activity_row.getActivityEndtime());
                item.setActivityTypeId(activity_row.getActivityTypeId());
                item.setActivityId(activity_id);
                item.setStoreId(activity_row.getStoreId());
                item.setActivityItemState(activity_state);
                item.setActivityItemPrice(itemUnitPrice);
            }
        }

        if (!activityItemRepository.saves(activity_item_rows)) {
            throw new BusinessException(ResultCode.FAILED);
        } else {
            Integer activity_type_id = activity_row.getActivityTypeId();

            // 需要判断活动状态， 如果活动装填当前是正常的，则添加
            if (ObjectUtil.equal(activity_state, StateCode.ACTIVITY_STATE_NORMAL)) {
                // 更新响应活动数据
                List<Long> product_id_row = activity_item_rows.stream().map(s -> s.getProductId()).distinct().collect(Collectors.toList());

                // 添加活动
                // 修改product_index中数据,需要重新查询添加成功的activity_item_rows的product，进行添加其索引
                List<ProductIndex> _product_index_rows = productIndexRepository.gets(product_id_row);

                List<ProductIndex> indexList = new ArrayList<>(_product_index_rows.size());
                for (ProductIndex product_index_row : _product_index_rows) {
                    // 不存在，则添加
                    String str_activity_type_ids = product_index_row.getActivityTypeIds();
                    List<Integer> activity_type_ids = Convert.toList(Integer.class, str_activity_type_ids);
                    if (!activity_type_ids.contains(activity_type_id)) {
                        ProductIndex index = new ProductIndex();

                        indexList.add(index);
                        activity_type_ids.add(activity_type_id);

                        index.setProductId(product_index_row.getProductId());
                        index.setActivityTypeIds(CollUtil.join(activity_type_ids, ","));
                    }
                }

                if (CollUtil.isNotEmpty(indexList)) {
                    if (!productIndexRepository.saveOrUpdate(indexList)) {
                        throw new BusinessException(ResultCode.FAILED);
                    }
                }
            }
        }

        return true;
    }


    @Transactional
    public boolean removeItems(Integer activity_id, List<Long> itemIds, ActivityBase activity_row) {

        if (ObjectUtil.isEmpty(activity_row)) {
            activity_row = get(activity_id);
        }

        QueryWrapper<ActivityItem> itemQueryWrapper = new QueryWrapper<>();
        itemQueryWrapper.eq("activity_id", activity_id).in("item_id", itemIds);
        List<ActivityItem> activity_item_rows = activityItemRepository.find(itemQueryWrapper);

        List<Long> activity_item_ids = activity_item_rows.stream().map(s -> s.getActivityItemId()).collect(Collectors.toList());
        activityItemRepository.remove(activity_item_ids);

        List<Long> product_id_row = activity_item_rows.stream().map(s -> s.getProductId()).distinct().collect(Collectors.toList());

        // 判断SKU中其它同SPU下商品是否仍然存在活动中
        QueryWrapper<ActivityItem> productQueryWrapper = new QueryWrapper<>();
        productQueryWrapper.eq("activity_id", activity_id).in("product_id", product_id_row);
        long count = activityItemRepository.count(productQueryWrapper);

        if (count == 0) {
            Integer activity_type_id = activity_row.getActivityTypeId();
            // 修改product_index中数据
            List<ProductIndex> product_index_rows = productIndexRepository.gets(product_id_row);
            for (ProductIndex product_index_row : product_index_rows) {
                // 存在，则删除
                String str_activity_type_ids = product_index_row.getActivityTypeIds();
                List<Integer> activity_type_ids = Convert.toList(Integer.class, str_activity_type_ids);
                if (activity_type_ids.contains(activity_type_id)) {
                    activity_type_ids.remove(activity_type_id);

                    product_index_row.setActivityTypeIds(CollUtil.join(activity_type_ids, ","));
                    if (!productIndexRepository.saveOrUpdate(product_index_row)) {
                        throw new BusinessException(__("删除失败！"));
                    }
                }
            }
        }

        return true;
    }


    @Override
    public IPage<ActivityBaseRes> listVoucher(ActivityBaseListReq activityBaseListReq) {
        IPage<ActivityBaseRes> activityBaseResPage = new Page<>();
        Integer userId = activityBaseListReq.getUserId();

        if ("index".equals(activityBaseListReq.getMet())) {
            activityBaseListReq.setSize(3);
        }
        // 根据店铺读取活动
        QueryWrapper<ActivityBase> activityQuery = new QueryWrapper<>();

        if (CheckUtil.isNotEmpty(activityBaseListReq.getStoreId()) || (activityBaseListReq.getStoreId() != null && activityBaseListReq.getStoreId().equals(0))) {
            activityQuery.eq("store_id", activityBaseListReq.getStoreId());
        }

        if (CheckUtil.isNotEmpty(activityBaseListReq.getActivityType())) {
            activityQuery.eq("activity_type", activityBaseListReq.getActivityType());
        } else {
            activityQuery.in("activity_type", Collections.singletonList(StateCode.GET_VOUCHER_FREE));
        }

        activityQuery.eq("activity_state", StateCode.ACTIVITY_STATE_NORMAL);
        activityQuery.eq("activity_type_id", StateCode.ACTIVITY_TYPE_VOUCHER);
        activityQuery.orderByDesc("activity_addtime");

        activityQuery.eq("activity_is_plus", false); // 过滤掉 PLUS专享

        IPage<ActivityBase> activityPage = lists(activityQuery, activityBaseListReq.getPage(), activityBaseListReq.getSize());

        if (activityPage != null && CollectionUtil.isNotEmpty(activityPage.getRecords())) {
            BeanUtils.copyProperties(activityPage, activityBaseResPage);
            List<ActivityBase> activityList = activityPage.getRecords();
            List<ActivityBaseRes> activityBaseResList = new ArrayList<>();
            //store_name
            List<Integer> storeIds = activityList.stream().map(ActivityBase::getStoreId).collect(Collectors.toList());
            List<StoreBase> storeBaseList = storeBaseRepository.gets(storeIds);
            Map<Integer, String> storeNameMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(storeBaseList)) {
                storeNameMap = storeBaseList.stream().collect(Collectors.toMap(StoreBase::getStoreId, StoreBase::getStoreName, (k1, k2) -> k1));
            }

            List<Integer> activityIds = CommonUtil.column(activityList, ActivityBase::getActivityId);
            List<UserVoucherNum> userVoucherNumList = userVoucherNumRepository.find(new QueryWrapper<UserVoucherNum>().in("activity_id", activityIds).eq("user_id", userId));

            List<UserLevel> userLevels = levelRepository.find(new QueryWrapper<>());
            Map<Integer, String> userLevelMap = userLevels.stream()
                    .collect(Collectors.toMap(UserLevel::getUserLevelId, UserLevel::getUserLevelName));

            for (ActivityBase activityBase : activityList) {
                ActivityBaseRes activityBaseRes = new ActivityBaseRes();
                BeanUtils.copyProperties(activityBase, activityBaseRes);
                String activityRule = activityBase.getActivityRule();

                // 会员等级名称
                List<String> activityUseLevelName = new ArrayList<>();
                List<Integer> activityUserLevels = Convert.toList(Integer.class, activityBase.getActivityUseLevel());
                Collections.sort(activityUserLevels);
                for (Integer userLevel : activityUserLevels) {
                    String val = userLevelMap.get(userLevel);
                    activityUseLevelName.add(val);
                }
                if (CollUtil.isNotEmpty(activityUseLevelName)) {
                    activityBaseRes.setActivityUseLevelName(CollUtil.join(activityUseLevelName, "、") + __("专属"));
                }

                //store_name
                if (!storeNameMap.isEmpty()) {
                    activityBaseRes.setStoreName(storeNameMap.get(activityBase.getStoreId()));
                }

                //处理ruleJson 为jsonObject
                ActivityRuleVo activityRuleVo = new ActivityRuleVo();
                if (StrUtil.isNotEmpty(activityRule)) {
                    activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
                    activityBaseRes.setActivityRuleJson(activityRuleVo);
                }

                //优惠券达到领取上限ifGain
                Integer voucherSize = 0;
                UserVoucherNum userVoucherNum = userVoucherNumList.stream().filter(s -> ObjectUtil.equal(s.getActivityId(), activityBase.getActivityId())).findFirst().orElse(null);

                if (ObjectUtil.isNotEmpty(userVoucherNum)) {
                    voucherSize = userVoucherNum.getUvnNum();
                }

                boolean isRemain = false;

                if (activityRuleVo != null) {
                    VoucherVo voucher = activityRuleVo.getVoucher();

                    if (voucher != null) {
                        Integer voucherPreQuantity = voucher.getVoucherPreQuantity();
                        activityBaseRes.setIfGain(CheckUtil.isNotEmpty(userId) && voucherPreQuantity > voucherSize);

                        Integer voucherQuantity = voucher.getVoucherQuantity();
                        Integer voucherQuantityFree = voucher.getVoucherQuantityFree();

                        if (NumberUtil.add(voucherQuantity, voucherQuantityFree).intValue() > 0) {
                            isRemain = true;
                        }
                    }
                } else {
                    activityBaseRes.setIfGain(false);
                }

                if (activityBaseRes.getIfGain() && isRemain) {
                    activityBaseResList.add(activityBaseRes);
                }
            }
            activityBaseResPage.setRecords(activityBaseResList);
        }

        return activityBaseResPage;
    }

    @Override
    public List<ActivityBaseRes> getActivityBases(List<Integer> activityIds, Integer currencyId) {
        List<ActivityBase> activityBases = gets(activityIds);
        List<ActivityBaseRes> activityBaseResList = new ArrayList<>();
        // 修改汇率
        BigDecimal currencyRate = currencyBaseService.getCurrencyRate(currencyId);

        if (CollectionUtil.isNotEmpty(activityBases)) {
            for (ActivityBase activityBase : activityBases) {
                ActivityBaseRes activityBaseRes = new ActivityBaseRes();
                BeanUtils.copyProperties(activityBase, activityBaseRes);
                Integer activityTypeId = activityBase.getActivityTypeId();
                String activityRule = activityBase.getActivityRule();
                ActivityRuleVo activityRuleVo = new ActivityRuleVo();

                switch (activityTypeId) {
                    case StateCode.ACTIVITY_TYPE_VOUCHER:
                        activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);

                        if (activityRuleVo != null) {
                            VoucherVo voucher = activityRuleVo.getVoucher();

                            if (voucher != null) {
                                BigDecimal voucherPrice = voucher.getVoucherPrice();
                                voucher.setVoucherPrice(NumberUtil.mul(voucherPrice, currencyRate));
                                RequirementVo requirement = activityRuleVo.getRequirement();

                                if (requirement != null) {
                                    BuyVo buy = requirement.getBuy();

                                    if (buy != null) {
                                        BigDecimal subtotal = buy.getSubtotal();
                                        buy.setSubtotal(NumberUtil.mul(subtotal, currencyRate));
                                    }
                                }
                            }
                        }

                        break;
                    case StateCode.ACTIVITY_TYPE_POP:
                        if (StrUtil.isNotEmpty(activityRule)) {
                            activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
                        }
                        break;
                    default:
                        if (StrUtil.isNotEmpty(activityRule)) {
                            activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
                        }
                        break;
                }
                activityBaseRes.setActivityRuleJson(activityRuleVo);
                activityBaseResList.add(activityBaseRes);
            }
        }

        return activityBaseResList;
    }

    @Override
    public IPage<ActivityBaseRes> getList(ActivityBaseListReq activityBaseListReq) {
        ContextUser loginUser = ContextUtil.getLoginUser();

        QueryWrapper<ActivityBase> wrapper = new BaseQueryWrapper<ActivityBase, ActivityBaseListReq>(activityBaseListReq).getWrapper();

        if (ObjectUtil.isNotEmpty(loginUser) && ContextUtil.isManage()) {
            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                wrapper.eq("store_id", loginUser.getStoreId());
            }

            if (loginUser.isPlatform() && CheckUtil.isNotEmpty(activityBaseListReq.getRoleId())) {
                if (activityBaseListReq.getRoleId().equals(2)) {
                    wrapper.ne("store_id", 0);
                } else if (activityBaseListReq.getRoleId().equals(9)) {
                    wrapper.eq("store_id", 0);
                }
            }
        }

        // 开源版营销活动仅展示优惠券与弹窗
        Integer reqActivityTypeId = activityBaseListReq.getActivityTypeId();
        if (reqActivityTypeId == null) {
            wrapper.in("activity_type_id", StateCode.ACTIVITY_TYPE_VOUCHER, StateCode.ACTIVITY_TYPE_POP);
        } else if (!reqActivityTypeId.equals(StateCode.ACTIVITY_TYPE_VOUCHER) && !reqActivityTypeId.equals(StateCode.ACTIVITY_TYPE_POP)) {
            wrapper.eq("activity_type_id", -1);
        }

        wrapper.orderByDesc("activity_id");

        IPage<ActivityBaseRes> activityBaseResPage = new Page<>();
        IPage<ActivityBase> activityBaseIPage = lists(wrapper, activityBaseListReq.getPage(), activityBaseListReq.getSize());

        if (activityBaseIPage == null || CollectionUtil.isEmpty(activityBaseIPage.getRecords())) {
            return activityBaseResPage;
        }
        BeanUtils.copyProperties(activityBaseIPage, activityBaseResPage);
        List<ActivityBase> activityBaseList = activityBaseIPage.getRecords();
        //修正活动最新状态
        List<ActivityBase> activityBases = fixActivityData(activityBaseList);
        List<ActivityBaseRes> activityBaseRes = new ArrayList<>();
        for (ActivityBase activityBase : activityBases) {
            ActivityBaseRes activityRes = new ActivityBaseRes();
            BeanUtils.copyProperties(activityBase, activityRes);
            //会员等级处理
            String activityUseLevel = activityBase.getActivityUseLevel();

            if (StrUtil.isNotEmpty(activityUseLevel)) {
                List<Integer> levelIds = Convert.toList(Integer.class, activityUseLevel);
                List<UserLevel> userLevelList = levelRepository.gets(levelIds);

                if (CollectionUtil.isNotEmpty(userLevelList)) {
                    List<String> useLevelList = userLevelList.stream().map(UserLevel::getUserLevelName).collect(Collectors.toList());
                    String useLevelStr = String.join(",", useLevelList);
                    activityRes.setUseLevel(useLevelStr);
                }
            }

            Integer activityTypeId = activityBase.getActivityTypeId();
            String activityRule = activityBase.getActivityRule();
            ActivityRuleVo activityRuleVo = new ActivityRuleVo();

            if (StrUtil.isNotEmpty(activityRule)) {
                activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
            }
            if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_VOUCHER) && activityRuleVo != null) {
                RequirementVo requirement = activityRuleVo.getRequirement();
                if (requirement != null) {
                    BuyVo buy = requirement.getBuy();
                    if (buy != null && CollUtil.isNotEmpty(buy.getItem())) {
                        activityRes.setItem(productBaseRepository.getItems(buy.getItem(), null));
                    }
                }
            }
            List<Long> itemIds = getActivityAllItemIds(activityBase);
            if (CollectionUtil.isNotEmpty(itemIds) && CollectionUtil.isEmpty(activityRes.getItem())) {
                activityRes.setItem(productBaseRepository.getItems(itemIds, null));
            }
            activityRes.setActivityRuleJson(activityRuleVo);


            activityBaseRes.add(activityRes);
        }


        activityBaseResPage.setRecords(activityBaseRes);

        return activityBaseResPage;
    }

    private List<Long> getActivityAllItemIds(ActivityBase activityBase) {
        if (activityBase == null) {
            return null;
        }
        if (!Objects.equals(activityBase.getActivityTypeId(), StateCode.ACTIVITY_TYPE_VOUCHER)) {
            return new ArrayList<>();
        }
        ActivityRuleVo activityRuleVo = new ActivityRuleVo();
        if (StrUtil.isNotEmpty(activityBase.getActivityRule())) {
            activityRuleVo = JSONUtil.parseObject(activityBase.getActivityRule(), ActivityRuleVo.class);
        }
        if (activityRuleVo == null) {
            return new ArrayList<>();
        }
        RequirementVo requirement = activityRuleVo.getRequirement();
        if (requirement != null && requirement.getBuy() != null && CollUtil.isNotEmpty(requirement.getBuy().getItem())) {
            return new ArrayList<>(requirement.getBuy().getItem());
        }
        return new ArrayList<>();
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addActivityBase(ActivityBase activityBase) {

        if (activityBase == null) {
            return false;
        }
        //商家身份强制归属本店;平台活动(storeId=0)仅平台可建
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (loginUser.isStore()) {
            activityBase.setStoreId(loginUser.getStoreId());
        } else if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            activityBase.setStoreId(0);
        }
        Integer activityTypeId = activityBase.getActivityTypeId();
        if (activityTypeId == null || (!activityTypeId.equals(StateCode.ACTIVITY_TYPE_VOUCHER) && !activityTypeId.equals(StateCode.ACTIVITY_TYPE_POP))) {
            throw new BusinessException(__("开源版仅支持创建优惠券活动与弹窗活动"));
        }
        activityBase.setActivityName(activityBase.getActivityTitle());
        ActivityRuleVo activityRuleVo = new ActivityRuleVo();

        if (StrUtil.isNotEmpty(activityBase.getActivityRule())) {
            activityRuleVo = JSONUtil.parseObject(activityBase.getActivityRule(), ActivityRuleVo.class);
        }

        long currentTime = new Date().getTime();

        if (currentTime < activityBase.getActivityStarttime()) {
            activityBase.setActivityState(StateCode.ACTIVITY_STATE_WAITING);
        } else if (currentTime > activityBase.getActivityEndtime()) {
            activityBase.setActivityState(StateCode.ACTIVITY_STATE_FINISHED);
        } else {
            activityBase.setActivityState(StateCode.ACTIVITY_STATE_NORMAL);
        }

        if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_VOUCHER)) {
            activityBase.setActivitySort(50);
        } else {
            activityBase.setActivitySort(80);
        }

        if (!add(activityBase)) {
            return false;
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateActivityBase(ActivityBase activityBase) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityBase.getActivityId();
        ActivityBase activity = get(activityId);

        if (activity == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }
        Integer existingActivityTypeId = activity.getActivityTypeId();
        if (existingActivityTypeId == null
                || (!existingActivityTypeId.equals(StateCode.ACTIVITY_TYPE_VOUCHER) && !existingActivityTypeId.equals(StateCode.ACTIVITY_TYPE_POP))) {
            throw new BusinessException(__("开源版仅支持编辑优惠券活动与弹窗活动"));
        }

        activityBase.setActivityIsPlus(activity.getActivityIsPlus()); // 发布后禁止修改，使用原来数据

        if (CheckUtil.isEmpty(activity.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activity, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        activityBase.setActivityName(activityBase.getActivityTitle());

        if (activityBase.getActivityStarttime() < new Date().getTime() && Objects.equals(activityBase.getActivityState(), StateCode.ACTIVITY_STATE_NORMAL)) {
            //throw new BusinessException(__("活动已开始不可进行修改！"));
        }

        return editActivityBase(activityId, activityBase);
    }

    private void dealVoucher(ActivityBase activityBase) {
        ActivityRuleVo activityRuleVo = JSONUtil.parseObject(activityBase.getActivityRule(), ActivityRuleVo.class);

        if (activityRuleVo != null) {
            VoucherVo voucherVo = activityRuleVo.getVoucher();

            if (voucherVo != null) {
                QueryWrapper<UserVoucher> userVoucherQueryWrapper = new QueryWrapper<>();
                userVoucherQueryWrapper.eq("activity_id", activityBase.getActivityId());
                List<UserVoucher> userVouchers = voucherRepository.find(userVoucherQueryWrapper);

                if (CollectionUtil.isNotEmpty(userVouchers)) {
                    for (UserVoucher userVoucher : userVouchers) {
                        userVoucher.setActivityName(activityBase.getActivityName());
                        userVoucher.setActivityRule(activityBase.getActivityRule());
                        userVoucher.setStoreId(activityBase.getStoreId());
                        userVoucher.setVoucherStartDate(voucherVo.getVoucherStartDate());
                        userVoucher.setVoucherEndDate(voucherVo.getVoucherEndDate());
                        userVoucher.setVoucherPrice(voucherVo.getVoucherPrice());

                        RequirementVo requirement = activityRuleVo.getRequirement();

                        if (requirement != null) {
                            BuyVo buy = requirement.getBuy();

                            if (buy != null) {
                                userVoucher.setVoucherSubtotal(buy.getSubtotal());

                                List<Long> item = buy.getItem();

                                if (CollUtil.isNotEmpty(item)) {
                                    userVoucher.setItemId(CollUtil.join(item, ","));
                                }
                            }
                        }
                    }

                    if (!voucherRepository.edit(userVouchers)) {
                        throw new BusinessException(__("更新用户领取优惠券失败！"));
                    }
                }
            }
        }
    }


    private void compareItemIds(List<Long> itemIdList, List<Long> oldItemIds) {
        if (CollectionUtil.isEmpty(itemIdList)) {
            throw new BusinessException(__("商品集合为空！"));
        }
        if (CollectionUtil.isEmpty(oldItemIds)) {
            throw new BusinessException(__("已选商品集合为空！"));
        }

        List<Long> itemIdRow = new ArrayList<>();
        for (Long itemId : itemIdList) {
            if (oldItemIds.contains(itemId)) {
                itemIdRow.add(itemId);
            }
        }

        if (CollectionUtil.isNotEmpty(itemIdRow)) {
            for (Long itemId : itemIdRow) {
                itemIdList.remove(itemId);
            }

            List<ProductItemVo> productItems = productBaseRepository.getItems(itemIdRow, null);
            throw new BusinessException(String.format(__("您所选中的商品  %s 正在参与其它活动"), CollUtil.join(CommonUtil.column(productItems, ProductItemVo::getProductItemName), ",")));
        }

        if (CollUtil.isEmpty(itemIdList)) {
            throw new BusinessException(__("您所选中的商品均在参与活动！"));
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeActivity(Integer activityId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        ActivityBase activity = get(activityId);

        if (activity == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activity.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activity, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        List<Integer> activityState = Arrays.asList(StateCode.ACTIVITY_STATE_NORMAL);

        if (activityState.contains(activity.getActivityState())) {
            throw new BusinessException(__("该状态无法删除！"));
        }

        /*
        if (activity.getActivityStarttime() < new Date().getTime()) {
            throw new BusinessException(__("活动已开始不可进行删除！"));
        }
         */

        if (!remove(activityId)) {
            throw new BusinessException(__("删除失败！"));
        }

        // 取得对应的product_id
        List<ActivityItem> activityItemRows = activityItemRepository.find(new QueryWrapper<ActivityItem>().eq("activity_id", activityId));

        if (CollUtil.isNotEmpty(activityItemRows)) {
            List<Long> activityItemIdRow = activityItemRows.stream().map(s -> s.getActivityItemId()).distinct().collect(Collectors.toList());

            if (CollUtil.isNotEmpty(activityItemIdRow)) {
                if (!activityItemRepository.remove(activityItemIdRow)) {
                    throw new BusinessException(__("删除失败"));
                }

                List<Long> productIdRow = activityItemRows.stream().map(s -> s.getProductId()).distinct().collect(Collectors.toList());

                if (CollUtil.isNotEmpty(productIdRow)) {
                    List<ProductIndex> productIndexRows = productIndexRepository.gets(productIdRow);

                    for (ProductIndex productIndex : productIndexRows) {
                        String activityTypeIds = productIndex.getActivityTypeIds();
                        List<Integer> activityTypeIdRows = Convert.toList(Integer.class, activityTypeIds);
                        Iterator<Integer> activityTypeIdRowsIt = activityTypeIdRows.iterator();

                        while (activityTypeIdRowsIt.hasNext()) {
                            Integer i = activityTypeIdRowsIt.next();
                            if (i.equals(activity.getActivityTypeId())) {
                                activityTypeIdRowsIt.remove();
                            }
                        }

                        QueryWrapper<ProductIndex> wrapper = new QueryWrapper<>();
                        wrapper.in("product_id", productIdRow);
                        ProductIndex shopProductIndex = new ProductIndex();
                        shopProductIndex.setActivityTypeIds(CollUtil.join(activityTypeIdRows, ","));

                        if (!productIndexRepository.edit(shopProductIndex, wrapper)) {
                            throw new BusinessException(__("删除商品索引失败"));
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<ActivityItemRes> getActivityBuyItems(Integer activityId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        List<ActivityItemRes> activityItemResList = new ArrayList<>();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            return activityItemResList;
        }

        if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        QueryWrapper<ActivityItem> activityItemQueryWrapper = new QueryWrapper<>();
        activityItemQueryWrapper.eq("activity_id", activityId);
        List<ActivityItem> activityItemList = activityItemRepository.find(activityItemQueryWrapper);

        if (CollectionUtil.isEmpty(activityItemList)) {
            return activityItemResList;
        }

        List<Long> itemIds = activityItemList.stream().map(ActivityItem::getItemId).collect(Collectors.toList());
        List<ProductItemVo> items = productBaseRepository.getItems(itemIds, null);

        for (ActivityItem activityItem : activityItemList) {
            ActivityItemRes activityItemRes = new ActivityItemRes();

            ProductItemVo itemVo = items.stream().filter(s -> s.getItemId().equals(activityItem.getItemId())).findFirst().orElse(null);
            if (itemVo != null) {
                BeanUtils.copyProperties(itemVo, activityItemRes);
            }

            BeanUtils.copyProperties(activityItem, activityItemRes);
            activityItemResList.add(activityItemRes);
        }

        return activityItemResList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addActivityBuyItems(ActivityBaseEditReq activityBaseEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityBaseEditReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        List<Long> itemIdList = Convert.toList(Long.class, activityBaseEditReq.getItemIds());
        if (CollUtil.isEmpty(itemIdList)) {
            throw new BusinessException(__("商品不存在！"));
        }

        // 审核中的商品或者审核不通过的商品不允许参与活动
        QueryWrapper<ProductItem> productItemQueryWrapper = new QueryWrapper<>();
        productItemQueryWrapper.in("item_id", itemIdList);
        List<ProductItem> productItems = productItemRepository.find(productItemQueryWrapper);

        if (CollectionUtil.isEmpty(productItems)) {
            throw new BusinessException(__("商品不存在！"));
        }
        List<Long> productIds = productItems.stream().map(ProductItem::getProductId).collect(Collectors.toList());
        List<ProductIndex> productIndices = productIndexRepository.gets(productIds);

        if (CollectionUtil.isNotEmpty(productIndices)) {
            for (ProductIndex productIndex : productIndices) {

                if (Objects.equals(productIndex.getProductVerifyId(), StateCode.PRODUCT_VERIFY_WAITING)
                        || Objects.equals(productIndex.getProductVerifyId(), StateCode.PRODUCT_VERIFY_REFUSED)) {
                    throw new BusinessException(__("商品在审核中或审核未通过,不允许参与活动！"));
                }
            }
        }

        if (!Objects.equals(activityBase.getActivityTypeId(), StateCode.ACTIVITY_TYPE_VOUCHER)) {
            throw new BusinessException(__("开源版仅支持为优惠券活动配置适用商品"));
        }
        return addBuyItem(activityId, itemIdList, activityBase);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addBuyItem(Integer activityId, List<Long> itemIdList, ActivityBase activityBase) {
        if (activityBase == null) {
            activityBase = get(activityId);
        }

        // 折上折活动不参与活动判断
        if (!Objects.equals(activityBase.getActivityTypeId(), StateCode.ACTIVITY_TYPE_REDUCTION_AGAIN)) {
            checkItem(itemIdList, activityBase);
        }

        if (CollectionUtil.isNotEmpty(itemIdList)) {

            if (!addItem(activityId, itemIdList, activityBase)) {
                throw new BusinessException(__("新增活动商品基础数据失败！"));
            }
        }
        String activityRule = activityBase.getActivityRule();
        ActivityRuleVo activityRuleVo = new ActivityRuleVo();

        if (StrUtil.isNotEmpty(activityRule)) {
            activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);
        }

        if (activityRuleVo == null) {
            throw new BusinessException(__("活动规则为空！"));
        }

        RequirementVo requirement = ObjectUtil.defaultIfNull(activityRuleVo.getRequirement(), new RequirementVo());
        BuyVo buy = ObjectUtil.defaultIfNull(requirement.getBuy(), new BuyVo());
        List<Long> item = Optional.ofNullable(buy.getItem()).orElseGet(ArrayList::new);
        item.addAll(itemIdList);
        buy.setItem(item);
        requirement.setBuy(buy);
        activityRuleVo.setRequirement(requirement);
        activityBase.setActivityRule(JSONUtil.toJSONString(activityRuleVo));

        //修改活动信息
        if (!editActivityBase(activityId, activityBase)) {
            throw new BusinessException(__("修改活动基础数据失败！"));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addDiscountItem(Integer activityId, List<Long> itemIdList, ActivityBase activityBase) {
        if (activityBase == null) {
            activityBase = get(activityId);
        }

        //检查活动商品是否已经存在于现存的活动中
        checkItem(itemIdList, activityBase);

        //修改活动信息
        if (!editActivityBase(activityId, activityBase)) {
            throw new BusinessException(__("修改活动基础数据失败！"));
        }

        if (CollectionUtil.isNotEmpty(itemIdList)) {

            if (!addItem(activityId, itemIdList, activityBase)) {
                throw new BusinessException(__("新增活动商品基础数据失败！"));
            }
        }

        return true;
    }

    /**
     * 检查活动商品是否已经存在于现存的活动中
     * 返回值：
     * 1.全部item_id均在现存的活动中，直接抛错
     * 2.部分item_id处于现存的活动中，返回未参加过活动的item_id，并修改msg
     * 3.全部item_id未在现存的活动中，返回全部item_id
     *
     * @param itemIdList
     * @return
     */
    private void checkItem(List<Long> itemIdList, ActivityBase activityBase) {
        QueryWrapper<ActivityItem> activityItemQueryWrapper = new QueryWrapper<>();
        activityItemQueryWrapper.in("item_id", itemIdList)
                .le("activity_item_starttime", activityBase.getActivityEndtime())
                .ge("activity_item_endtime", activityBase.getActivityStarttime())
				.ne("activity_item_state", 3); // 排除已关闭的
        List<ActivityItem> activityItemList = activityItemRepository.find(activityItemQueryWrapper);

        if (CollectionUtil.isNotEmpty(activityItemList)) {
            List<Long> itemIds = activityItemList.stream().map(ActivityItem::getItemId).distinct().collect(Collectors.toList());
            compareItemIds(itemIdList, itemIds);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeActivityBuyItems(ActivityBaseEditReq activityBaseEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityBaseEditReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        String itemIds = activityBaseEditReq.getItemIds();
        List<Long> itemIdList = Convert.toList(Long.class, itemIds);

        if (CollectionUtil.isEmpty(itemIdList)) {
            throw new BusinessException(__("商品编号集合为空！"));
        }

        if (!Objects.equals(activityBase.getActivityTypeId(), StateCode.ACTIVITY_TYPE_VOUCHER)) {
            throw new BusinessException(__("开源版仅支持从优惠券活动中移除适用商品"));
        }
        return removeBuyItems(activityId, itemIdList, activityBase);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeBuyItems(Integer activityId, List<Long> itemIds, ActivityBase activityBase) {
        if (activityBase == null) {
            activityBase = get(activityId);
        }
        String activityRule = activityBase.getActivityRule();

        if (StrUtil.isNotEmpty(activityRule)) {
            ActivityRuleVo activityRuleVo = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);

            if (activityRuleVo != null) {
                RequirementVo requirement = activityRuleVo.getRequirement();

                if (requirement != null) {
                    BuyVo buy = requirement.getBuy();

                    if (buy != null) {
                        List<Long> item = buy.getItem();

                        if (CollectionUtil.isNotEmpty(item)) {
                            for (Long itemId : itemIds) {
                                item.remove(itemId);
                            }
                            buy.setItem(item);
                            requirement.setBuy(buy);
                            activityRuleVo.setRequirement(requirement);
                            activityBase.setActivityRule(JSONUtil.toJSONString(activityRuleVo));

                            if (!edit(activityBase)) {
                                throw new BusinessException(__("修改活动信息失败！"));
                            }
                        }
                    }
                }
            }
        }

        if (!removeItems(activityId, itemIds, activityBase)) {
            throw new BusinessException(__("删除活动商品基础数据失败！"));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeDiscountItems(Integer activityId, List<Long> itemIds, ActivityBase activityBase) {
        if (activityBase == null) {
            activityBase = get(activityId);
        }

        if (!removeItems(activityId, itemIds, activityBase)) {
            throw new BusinessException(__("删除活动商品基础数据失败！"));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editActivityItem(ActivityItem activityItem) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityItem.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息为空！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持限时折扣类活动商品价格编辑"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editBatchPrice(ActivityItemBatchPriceEditReq activityItemBatchPriceEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ActivityBase activityBase = get(activityItemBatchPriceEditReq.getActivityId());

        if (activityBase == null) {
            throw new BusinessException(__("该活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持统一折扣批量改价"));
    }

    @Override
    public List<ActivityRuleRes> getActivityRuleList(Integer activityId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        return new ArrayList<>();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addActivityRule(Integer activityId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持通过该接口维护活动规则行"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editActivityRule(ActivityRuleReq activityRuleReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityRuleReq.getActivityId();

        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持通过该接口维护活动规则行"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeActivityRule(ActivityRuleReq activityRuleReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityRuleReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持通过该接口维护活动规则行"));
    }

    @Override
    public List<ActivityRuleItemRes> getActivityRuleItemList(ActivityRuleItemReq activityRuleItemReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityRuleItemReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        return new ArrayList<>();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addActivityRuleItem(ActivityRuleItemReq activityRuleItemReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityRuleItemReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持通过该接口维护活动规则行商品"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeActivityRuleItem(ActivityRuleItemReq activityRuleItemReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer activityId = activityRuleItemReq.getActivityId();
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持通过该接口维护活动规则行商品"));
    }

    @Override
    public boolean cellEditReplace(Integer activityId, int totalId, Long itemId, BigDecimal itemReplaceRrice) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ActivityBase activityBase = get(activityId);

        if (activityBase == null) {
            throw new BusinessException(__("活动信息不存在！"));
        }

        if (CheckUtil.isEmpty(activityBase.getStoreId())) {
            if (!loginUser.isPlatform()) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        } else {
            if (!CheckUtil.checkDataRights(loginUser.getStoreId(), activityBase, ActivityBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }
        }

        throw new BusinessException(__("开源版不支持加价购换购价编辑"));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void doActivityVoucherPrize(OrderData orderData) {

        String activityManhuiId = orderData.getActivityManhuiId();

        if (StrUtil.isEmpty(activityManhuiId)) {
            //异常数据，更新状态
            orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_ERR);

        } else {
            OrderBase orderBase = orderBaseRepository.get(orderData.getOrderId());

            if (orderBase == null) {
                throw new BusinessException(__("该订单信息不存在！"));
            }

            if (Objects.equals(orderBase.getOrderStateId(), StateCode.ORDER_STATE_CANCEL)) {
                orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_NO);
            } else {
                List<Integer> userVoucherIds = new ArrayList<>();
                List<Integer> manhuiIds = Convert.toList(Integer.class, activityManhuiId);
                QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
                orderItemQueryWrapper.eq("order_id", orderData.getOrderId());
                orderItemQueryWrapper.eq("activity_type_id", StateCode.ACTIVITY_TYPE_MANHUI);
                List<OrderItem> orderItemList = orderItemRepository.find(orderItemQueryWrapper);

                if (CollectionUtil.isEmpty(orderItemList)) {
                    throw new BusinessException(__("订单商品信息不存在！"));
                }

                for (Integer activityId : manhuiIds) {
                    Optional<OrderItem> optional = orderItemList.stream().filter(s -> ObjectUtil.equals(s.getOrderGiveId(), activityId)).findFirst();

                    if (!optional.isPresent()) {
                        throw new BusinessException(__("订单商品信息有误！"));
                    }
                    OrderItem orderItem = optional.get();

                    UserVoucher userVoucher = userVoucherService.addVoucher(activityId, orderBase.getUserId(), orderItem.getOrderItemId(), StateCode.VOUCHER_SOURCE_FULL_RETURN);
                    userVoucherIds.add(userVoucher.getUserVoucherId());

                    //两个活动赠送的优惠券活动id一样
                    Integer srcActivityId = orderItem.getActivityId();
                    orderItemList = orderItemList.stream().filter(s -> !s.getActivityId().equals(srcActivityId)).collect(Collectors.toList());
                }
                orderData.setOrderActivityManhuiState(StateCode.CHECK_STATE_OK);
                orderData.setActivityVoucherId(CollUtil.join(userVoucherIds, ","));
            }
        }

        if (!orderDataRepository.edit(orderData)) {
            throw new BusinessException(String.format(__("订单order_id : %s 满返状态修改失败"), orderData.getOrderId()));
        }
    }

}
