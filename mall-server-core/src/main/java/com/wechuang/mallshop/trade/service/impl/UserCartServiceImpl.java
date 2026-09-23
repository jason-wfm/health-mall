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
package com.wechuang.mallshop.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserDeliveryAddress;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.entity.UserLevel;
import com.wechuang.mallshop.account.repository.UserDeliveryAddressRepository;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.marketing.model.entity.ActivityBase;
import com.wechuang.mallshop.marketing.model.vo.*;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.pay.model.entity.ConsumeTrade;
import com.wechuang.mallshop.pay.repository.ConsumeTradeRepository;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.entity.ProductValidPeriod;
import com.wechuang.mallshop.pt.model.vo.ProductItemInfoVo;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.ProductBaseRepository;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.pt.repository.ProductValidPeriodRepository;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.StoreTransportType;
import com.wechuang.mallshop.shop.model.req.UserVoucherListReq;
import com.wechuang.mallshop.shop.model.res.UserVoucherRes;
import com.wechuang.mallshop.shop.model.vo.OrderFreightVo;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.repository.StoreTransportTypeRepository;
import com.wechuang.mallshop.shop.service.UserVoucherService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.OrderInfo;
import com.wechuang.mallshop.trade.model.entity.OrderItem;
import com.wechuang.mallshop.trade.model.entity.OrderReturn;
import com.wechuang.mallshop.trade.model.entity.UserCart;
import com.wechuang.mallshop.trade.model.input.CartAddInput;
import com.wechuang.mallshop.trade.model.input.CheckoutInput;
import com.wechuang.mallshop.trade.model.input.UserCartSelectInput;
import com.wechuang.mallshop.trade.model.output.CheckoutOutput;
import com.wechuang.mallshop.trade.model.req.UserCartListReq;
import com.wechuang.mallshop.trade.model.vo.CheckoutItemVo;
import com.wechuang.mallshop.trade.model.vo.FixOrderVo;
import com.wechuang.mallshop.trade.model.vo.StoreItemVo;
import com.wechuang.mallshop.trade.repository.OrderInfoRepository;
import com.wechuang.mallshop.trade.repository.OrderItemRepository;
import com.wechuang.mallshop.trade.repository.OrderReturnRepository;
import com.wechuang.mallshop.trade.repository.UserCartRepository;
import com.wechuang.mallshop.trade.service.UserCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 购物车表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-07-26
 */
@Service
public class UserCartServiceImpl extends BaseServiceImpl<UserCartRepository, UserCart, UserCartListReq> implements UserCartService {
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private UserDeliveryAddressRepository userDeliveryAddressRepository;

    @Autowired
    private StoreTransportTypeRepository storeTransportTypeRepository;

    @Autowired
    private UserVoucherService voucherService;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private ProductValidPeriodRepository productValidPeriodRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private ConsumeTradeRepository consumeTradeRepository;

    @Autowired
    private OrderReturnRepository orderReturnRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Override
    public CheckoutOutput getList(UserCartListReq req) {
        QueryWrapper<UserCart> wrapper = new BaseQueryWrapper<UserCart, UserCartListReq>(req).getWrapper();
        List<UserCart> userCarts = find(wrapper);

        CheckoutInput in = new CheckoutInput();
        in.setUserId(req.getUserId());
        in.setItems(BeanUtil.copyToList(userCarts, CheckoutItemVo.class));

        CheckoutOutput out = checkout(in);

        return out;
    }

    /**
     * 生成订单数据，结算checkout预览及生成订单, 理论上属于订单模块
     * <p>
     * 1、购物车中商品基本信息读取
     * 2、格式化数据（阶梯价计算等等），店铺数据分组，商品仓库分组
     * 3、活动数据、优惠折扣团购等等
     * 4、活动商品数据满赠满减、加价购、换购等等
     * 5、根据选择，计算订单信息，将上一步权限验证并将结果计入数据中, 店铺商品总价 = 加价购商品总价 + 购物车非活动商品总价（按照限时折扣和团购价计算）
     * 6、
     * 7、结算使用部分
     * 7.1、可用店铺优惠券、店铺礼品卡、店铺红包
     * 7.2、可用平台红包、平台优惠券、礼品卡
     * 7.3、最终折扣计算(最终支付价格打折)
     * 7.4、计算每样商品的佣金
     * 7.5、计算最终运费，运费根据重量计费，同一店铺一次发货，商品独立计算快递费用不合理。
     * 9、计算总价
     *
     * @param in
     * @return
     */
    @Override
    public CheckoutOutput checkout(CheckoutInput in) {

        CheckoutOutput out = formatCartRows(in);

        return out;
    }

    // 是否拥有PLUS免运费权益（开源版已移除 PLUS）
    private boolean freeShippingPrivilege(Integer userId) {
        return false;
    }

    /**
     * 生成订单数据，结算checkout预览及生成订单
     * <p>
     * 1、购物车中商品基本信息读取
     * 2、店铺数据分组，商品仓库分组
     * 3、活动数据、优惠折扣、团购、（阶梯价计算）等等
     * 4、活动商品数据满赠、兑换等等
     * 5、根据选择，计算订单信息，将上一步权限验证并将结果计入数据中, 店铺商品总价 = 加价购商品总价 + 购物车非活动商品总价（按照限时折扣和团购价计算）
     * 6、
     *
     * @param in 购物车数据
     */
    @Override
    public CheckoutOutput formatCartRows(CheckoutInput in) {
        CheckoutOutput out = new CheckoutOutput();
        out.setUserId(in.getUserId());
        UserInfo userInfo = userInfoRepository.get(in.getUserId());

        if (userInfo == null) {
            throw new BusinessException(__("用户信息不存在！"));
        }
        out.setUserLevelId(userInfo.getUserLevelId());

        // 处理店铺活动
        ActivityBase activityBase = null;

        if (CheckUtil.isNotEmpty(in.getActivityId())) {
            //参加活动 产品及数量 - 活动信息使用
            Map<Long, ItemNumVo> itemNumVoMap = new HashMap<>();

            activityBase = activityBaseRepository.get(in.getActivityId());

            if (activityBase != null) {
                if (!Objects.equals(activityBase.getActivityState(), StateCode.ACTIVITY_STATE_NORMAL)) {
                    throw new BusinessException(__("活动尚未开启！"));
                }
                Integer storeActTypeId = activityBase.getActivityTypeId();
                if (storeActTypeId != null
                        && !Objects.equals(storeActTypeId, StateCode.ACTIVITY_TYPE_VOUCHER)
                        && !Objects.equals(storeActTypeId, StateCode.ACTIVITY_TYPE_POP)) {
                    throw new BusinessException(__("该店铺营销活动类型已停用"));
                }
                //会员等级判断
                checkoutLevel(activityBase.getActivityUseLevel(), userInfo.getUserLevelId());
                String activityRule = activityBase.getActivityRule();

                if (StrUtil.isNotEmpty(activityRule)) {
                    itemNumVoMap = activityBaseRepository.getActivityItemNum(activityBase);
                }
            } else {
                throw new BusinessException(__("非法活动参数！"));
            }
        }

        BigDecimal orderProductAmount = BigDecimal.ZERO; //商品订单原价
        BigDecimal orderItemAmount = BigDecimal.ZERO; //单品优惠后价格累加
        BigDecimal orderFreightAmount = BigDecimal.ZERO;
        BigDecimal orderMoneyAmount = BigDecimal.ZERO;
        BigDecimal orderDiscountAmount = BigDecimal.ZERO;
        BigDecimal orderSalePersonDiscount = BigDecimal.ZERO;
        BigDecimal orderPointsAmount = BigDecimal.ZERO;
        BigDecimal orderSpAmount = BigDecimal.ZERO;
        BigDecimal orderPlusAmount = BigDecimal.ZERO;

        List<Long> itemIds = CommonUtil.column(in.getItems(), CheckoutItemVo::getItemId);
        List<Integer> storeIds = CommonUtil.column(in.getItems(), CheckoutItemVo::getStoreId);

        //购物车选中赠品
        List<Long> cartIds = CommonUtil.column(in.getItems(), CheckoutItemVo::getCartId);
        Map<Long, UserCart> cartMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(cartIds)) {
            QueryWrapper<UserCart> userCartQueryWrapper = new QueryWrapper<>();
            userCartQueryWrapper.in("cart_id", cartIds);
            userCartQueryWrapper.ne("activity_id", 0);
            List<UserCart> userCarts = find(userCartQueryWrapper);

            if (CollectionUtil.isNotEmpty(userCarts)) {
                cartMap = userCarts.stream().collect(Collectors.toMap(UserCart::getCartId, UserCart -> UserCart, (k1, k2) -> k1));
            }
        }

        if (CheckUtil.isNotEmpty(in.getChainId()) && in.getChainId() > 0) {
            throw new BusinessException(__("开源版不支持连锁门店（O2O）结算"));
        }

        List<ProductItemVo> productItemList = productBaseRepository.getItems(itemIds, in.getUserId());
        // 销售员折扣
        Integer orderSaleId = in.getOrderSaleId();
        Boolean userIsSale = false;

        if (CheckUtil.isNotEmpty(orderSaleId)) {
            UserInfo saleUserInfo = userInfoRepository.get(orderSaleId);

            if (saleUserInfo == null) {
                throw new BusinessException(__("用户不存在！"));
            }

            userIsSale = saleUserInfo.getUserIsSale();
        }

        if (CollectionUtil.isNotEmpty(productItemList)) {
            storeIds = CommonUtil.column(productItemList, ProductItemVo::getStoreId);
        }

        // 获取店铺数据：自营/营业状态与店铺表同步
        Map<Integer, StoreBase> storeBaseMap = new HashMap<>();
        if (CollUtil.isNotEmpty(storeIds)) {
            List<StoreBase> storeBases = storeBaseRepository.gets(storeIds);
            storeBaseMap = storeBases.stream()
                    .collect(Collectors.toMap(StoreBase::getStoreId, Function.identity()));
        }

        //活动商品数量 多件折 等使用
        Map<Integer, Integer> activityItemQuantityTotalMap = new HashMap<>();

        //店铺分组
        Map<Integer, List<ProductItemVo>> storeItemsMap = new HashMap<>();

        for (ProductItemVo it : productItemList) {
            // 根据店铺分组数据
            List<ProductItemVo> storeItemsList = ObjectUtil.defaultIfNull(storeItemsMap.get(it.getStoreId()), new ArrayList<>());
            if (CollUtil.isEmpty(storeItemsList)) storeItemsMap.put(it.getStoreId(), storeItemsList);

            //设置购物车商品数量
            CheckoutItemVo checkoutItemVo = in.getItems().stream().filter(s -> s.getItemId().equals(it.getItemId())).findFirst().orElse(new CheckoutItemVo());
            ProductItemVo productItemVo = BeanUtil.copyProperties(it, ProductItemVo.class);

            //处理商品购买信息
            productItemVo.setCartId(checkoutItemVo.getCartId());
            productItemVo.setCartQuantity(checkoutItemVo.getCartQuantity());
            productItemVo.setCartSelect(checkoutItemVo.getCartSelect());

            //判断可用库存
            productItemVo.setAvailableQuantity(it.getItemQuantity() - it.getItemQuantityFrozen());
            Integer kindId = it.getKindId();
            Boolean isOnSale = ObjectUtil.equal(kindId, StateCode.PRODUCT_KIND_EDU) || ProductItemRepository.ifOnSale(it);
            productItemVo.setIsOnSale(isOnSale);

            //商品是否可销售
            if (checkoutItemVo.getCartSelect()) {
                productItemVo.setCartSelect(productItemVo.getIsOnSale());
            }

            //判断是否在配送区域
            if (checkoutItemVo.getCartSelect()) {
                productItemVo.setCartSelect(true);
            }

            //购物车活动id
            List<CheckoutItemVo> items = in.getItems();

            if (CollectionUtil.isNotEmpty(items)) {
                for (CheckoutItemVo item : items) {
                    Long itemId = item.getItemId();

                    if (CheckUtil.isNotEmpty(item.getActivityId()) && itemId.equals(it.getItemId())) {
                        productItemVo.setActivityId(item.getActivityId());
                    }
                }
            }

            if (CheckUtil.isNotEmpty(productItemVo.getCartId())) {

                if (cartMap.containsKey(productItemVo.getCartId())) {
                    UserCart userCart = cartMap.get(productItemVo.getCartId());
                    productItemVo.setActivityId(userCart.getActivityId());
                }
            }

            storeItemsList.add(productItemVo);
        }

        UserDeliveryAddress deliveryAddress = new UserDeliveryAddress();

        //配送地址 || 联系方式
        if (CheckUtil.isEmpty(in.getUdId())) {
            // 默认配送地址
            UserDeliveryAddress defaultAddress = userDeliveryAddressRepository.findOne(new QueryWrapper<UserDeliveryAddress>().eq("ud_is_default", 1).eq("user_id", in.getUserId()));

            //配送地址为空，自动将第一个地址填入订单中
            if (defaultAddress == null) {
                deliveryAddress = userDeliveryAddressRepository.findOne(new QueryWrapper<UserDeliveryAddress>().eq("user_id", in.getUserId()));
            } else {
                deliveryAddress = defaultAddress;
            }
        } else {
            UserDeliveryAddress address = userDeliveryAddressRepository.get(in.getUdId());

            if (address != null) {

                if (!address.getUserId().equals(in.getUserId())) {
                    throw new BusinessException(__("地址编号不属于当前用户！"));
                }

                deliveryAddress = address;
            }
        }

        out.setUserDeliveryAddress(deliveryAddress);

        //可用代金券列表
        UserVoucherListReq userVoucherListReq = new UserVoucherListReq();
        userVoucherListReq.setUserId(in.getUserId());
        userVoucherListReq.setVoucherStateId(StateCode.VOUCHER_STATE_UNUSED);
        userVoucherListReq.setPage(1);
        userVoucherListReq.setSize(ConstantConfig.MAX_LIST_NUM);
        userVoucherListReq.setVoucherEffect(true);
        IPage<UserVoucherRes> voucherResIPage = voucherService.getList(userVoucherListReq);
        List<UserVoucherRes> voucherItems = voucherResIPage.getRecords();

        for (Integer storeId : storeIds) {
            //是否虚拟商品
            Boolean isVirtual = false;
            Integer kindId = 0;

            //是否需要配送
            Boolean isDelivery = false;

            if (in.getDeliveryTypeId() != null) {
                if (Objects.equals(in.getDeliveryTypeId(), StateCode.DELIVERY_TYPE_EXP)) {
                    isDelivery = true;
                }
            }

            StoreItemVo storeItemVo = new StoreItemVo();
            storeItemVo.setStoreId(storeId);
            storeItemVo.setActivityBase(activityBase);
            List<ProductItemVo> items = storeItemsMap.getOrDefault(storeId, new ArrayList<>());
            if (CheckUtil.isNotEmpty(storeId)) {
                StoreBase storeBase = storeBaseMap.get(storeId);
                if (storeBase != null) {
                    storeItemVo.setStoreIsOpen(storeBase.getStoreIsOpen());
                    storeItemVo.setStoreIsSelfsupport(Boolean.TRUE.equals(storeBase.getStoreIsSelfsupport()));
                }
            } else {
                storeItemVo.setStoreIsSelfsupport(true);
            }

            if (CollUtil.isEmpty(items)) {
                continue;
            }

            BigDecimal productAmount = BigDecimal.ZERO;
            BigDecimal freightAmount = BigDecimal.ZERO;
            BigDecimal discountAmount = BigDecimal.ZERO;
            BigDecimal moneyAmount = BigDecimal.ZERO;
            BigDecimal itemAmount = BigDecimal.ZERO; //商品累加销售总额
            BigDecimal pointsAmount = BigDecimal.ZERO;
            BigDecimal spAmount = BigDecimal.ZERO;
            BigDecimal itemSubtotal = BigDecimal.ZERO;
            BigDecimal plusDiscountAmount = BigDecimal.ZERO;
            BigDecimal levelDiscountAmount = BigDecimal.ZERO;

            //涉及商品个数
            int itemSelectedSize = 0;

            List<ProductItemVo> activityReductionProductItems = new ArrayList<>(); //满减活动商品
            List<ProductItemVo> activityManhuiProductItems = new ArrayList<>(); //满返活动商品

            for (ProductItemVo item : items) {
                if (!item.getCartSelect()) {
                    continue;
                }

                itemSelectedSize++;

                BigDecimal _unitPts = ObjectUtil.defaultIfNull(item.getItemUnitPoints(), BigDecimal.ZERO);
                BigDecimal _salePx = ObjectUtil.defaultIfNull(item.getItemSalePrice(), BigDecimal.ZERO);
                if (_unitPts.compareTo(BigDecimal.ZERO) > 0 && _salePx.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessException(String.format(__("开源版不支持积分换购商品：%s"), item.getProductName()));
                }
                item.setItemUnitPoints(BigDecimal.ZERO);

                //todo 处理单品活动价格（开源版仅保留优惠券、弹窗，其它活动标记清理）
                if (ObjectUtil.isNotEmpty(item.getActivityInfo())) {
                    Integer atid = item.getActivityInfo().getActivityTypeId();
                    if (atid != null
                            && !Objects.equals(atid, StateCode.ACTIVITY_TYPE_VOUCHER)
                            && !Objects.equals(atid, StateCode.ACTIVITY_TYPE_POP)) {
                        item.setActivityInfo(null);
                        item.setActivityId(null);
                    }
                }

                //修正订单商品价格
                if (in.getFixOrder() != null && in.getFixOrder().isFixPrice()) {
                    FixOrderVo fixOrder = in.getFixOrder();

                    item.setItemSalePrice(fixOrder.getItemUnitPrice());
                }

                BigDecimal itemOriSubtotal = item.getItemUnitPrice().multiply(BigDecimal.valueOf(item.getCartQuantity()));
                itemSubtotal = item.getItemSalePrice().multiply(BigDecimal.valueOf(item.getCartQuantity()));
                BigDecimal itemPointsSubtotal = item.getItemUnitPoints().multiply(BigDecimal.valueOf(item.getCartQuantity()));

                //汇总
                productAmount = productAmount.add(itemOriSubtotal);
                itemAmount = itemAmount.add(itemSubtotal);
                moneyAmount = moneyAmount.add(itemSubtotal);
                discountAmount = productAmount.subtract(moneyAmount);
                pointsAmount = pointsAmount.add(itemPointsSubtotal);
                //spAmount = spAmount.add(item.getItemUnitSp());

                item.setItemPointsSubtotal(itemPointsSubtotal);
                item.setItemSubtotal(itemSubtotal);
                item.setItemDiscountAmount(itemOriSubtotal.subtract(itemSubtotal));

                // PLUS单品优惠总金额
                BigDecimal itemPlusDiscountPrice = item.getItemPlusDiscountPrice() == null ? BigDecimal.ZERO : item.getItemPlusDiscountPrice();
                BigDecimal itemPlusDiscountAmount = itemPlusDiscountPrice.multiply(BigDecimal.valueOf(item.getCartQuantity())).setScale(2, RoundingMode.HALF_UP);
                item.setItemPlusDiscountAmount(itemPlusDiscountAmount);
                plusDiscountAmount = plusDiscountAmount.add(itemPlusDiscountAmount);

                // 会员折扣优惠总金额
                BigDecimal itemLevelDiscountPrice = item.getItemLevelDiscountPrice() == null ? BigDecimal.ZERO : item.getItemLevelDiscountPrice();
                BigDecimal itemLevelDiscountAmount = itemLevelDiscountPrice.multiply(BigDecimal.valueOf(item.getCartQuantity())).setScale(2, RoundingMode.HALF_UP);
                item.setItemLevelDiscountAmount(itemLevelDiscountAmount);
                levelDiscountAmount = levelDiscountAmount.add(itemLevelDiscountAmount);

                //为虚拟商品，有一个就是虚拟的
                List<Integer> kinds = Arrays.asList(StateCode.PRODUCT_KIND_FUWU, StateCode.PRODUCT_KIND_CARD);
                if (kinds.contains(item.getKindId())) {
                    isVirtual = true;
                    // 修正虚拟数据
                    ProductValidPeriod productValidPeriod = productValidPeriodRepository.get(item.getProductId());

                    if (productValidPeriod != null) {
                        item.setProductValidType(productValidPeriod.getProductValidType());
                        item.setProductServiceDateFlag(productValidPeriod.getProductServiceDateFlag());
                        item.setProductServiceContactorFlag(productValidPeriod.getProductServiceContactorFlag());
                    }
                }
                kindId = item.getKindId();
            }

            if (CollectionUtil.isNotEmpty(activityReductionProductItems)) {
                List<Integer> activityIds = CommonUtil.column(activityReductionProductItems, ProductItemVo::getActivityId);

                if (CollectionUtil.isNotEmpty(activityIds)) {
                    for (Integer activityId : activityIds) {
                        List<ProductItemVo> productItemVoList = items.stream().filter(item -> item.getActivityInfo() != null && CheckUtil.isNotEmpty(item.getActivityId()) && item.getActivityId().equals(activityId)).collect(Collectors.toList());
                        BigDecimal totalPrice = productItemVoList.stream().map(ProductItemVo::getItemSubtotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                        ActivityBase activityInfo = activityBaseRepository.get(activityId);

                        if (activityInfo == null) {
                            throw new BusinessException(__("活动信息不存在！"));
                        }
                        Integer activityTypeId = activityInfo.getActivityTypeId();

                        if (StrUtil.isNotEmpty(activityInfo.getActivityRule())) {
                            ActivityRuleVo activityRuleVo = JSONUtil.parseObject(activityInfo.getActivityRule(), ActivityRuleVo.class);

                            if (activityRuleVo == null) {
                                throw new BusinessException(__("活动规则不存在！"));
                            }

                            List<RuleVo> rules = activityRuleVo.getRule();

                            if (CollectionUtil.isEmpty(rules)) {
                                throw new BusinessException(__("活动详细规则不存在！"));
                            }

                            // 使用 Comparator 进行排序
                            rules.sort(Comparator.comparing(RuleVo::getAmount));

                            //满减
                            if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_REDUCTION)) {
                                int percent = 0;

                                for (RuleVo rule : rules) {
                                    if (totalPrice.compareTo(rule.getAmount()) >= 0) {
                                        percent = rule.getMaxNum();
                                    } else {
                                        break;
                                    }
                                }
                                Map<Long, BigDecimal> itemSavePriceMap = new HashMap<>();

                                //符合满减金额
                                if (percent != 0) {
                                    //均分满减金额
                                    BigDecimal sharePrice = BigDecimal.valueOf(0);
                                    BigDecimal nowPriceTotal = BigDecimal.valueOf(0); //已分配额度
                                    int i = 0;

                                    for (int idx = 0; idx < productItemVoList.size(); idx++) {
                                        ProductItemVo productItemVo = productItemVoList.get(idx);
                                        i++;

                                        if (i == productItemVoList.size()) {
                                            sharePrice = NumberUtil.sub(percent, nowPriceTotal);
                                        } else {
                                            sharePrice = NumberUtil.round(NumberUtil.mul(NumberUtil.div(productItemVo.getItemSubtotal(), totalPrice), percent), 2);
                                            nowPriceTotal = NumberUtil.add(nowPriceTotal, sharePrice);
                                        }
                                        itemSavePriceMap.put(productItemVo.getItemId(), sharePrice);
                                    }
                                }

                                for (ProductItemVo item : items) {
                                    if (CheckUtil.isNotEmpty(item.getActivityId()) && item.getActivityId().equals(activityId) && item.getCartSelect()) {
                                        //是否触发满减
                                        if (percent == 0) {
                                            //未触发，清除活动标记
                                            item.setActivityInfo(null);
                                            item.setActivityId(null);
                                        } else {
                                            BigDecimal newSavePrice = itemSavePriceMap.get(item.getItemId());
                                            item.setItemSubtotal(item.getItemSubtotal().subtract(newSavePrice));
                                            item.setItemSalePrice(item.getItemSubtotal().divide(BigDecimal.valueOf(item.getCartQuantity()), 6, BigDecimal.ROUND_HALF_UP));
                                            item.setItemSavePrice(item.getItemUnitPrice().subtract(item.getItemSalePrice()));
                                            item.setItemDiscountAmount(newSavePrice);

                                            moneyAmount = moneyAmount.subtract(newSavePrice);
                                            discountAmount = discountAmount.add(newSavePrice);
                                        }
                                    }
                                }
                            } else if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_GIFT)) {
                                Map<Long, BigDecimal> activity_rule_item_id_row = new HashMap();
                                Map<Long, Integer> activity_rule_item_num_row = new HashMap();
                                int max_num = 0;

                                for (RuleVo rule : rules) {
                                    if (totalPrice.compareTo(rule.getAmount()) >= 0) {
                                        //限购
                                        max_num = rule.getMaxNum();
                                        List<ItemNumVo> item = rule.getItem();

                                        if (CollectionUtil.isNotEmpty(item)) {
                                            List<Long> ids = CommonUtil.column(item, ItemNumVo::getItemId);
                                            for (Long id : ids) {
                                                activity_rule_item_id_row.put(id, BigDecimal.ZERO);
                                                activity_rule_item_num_row.put(id, max_num);
                                            }
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                // 选择商品
                                List<ProductItemInfoVo> select_info = new ArrayList<>();
                                ProductItemVo bargain_activity_row = new ProductItemVo();

                                if (CollUtil.isNotEmpty(activity_rule_item_id_row)) {
                                    // 获取所有商品信息, 需要过滤库存，如果库存不足，则剔除。
                                    List<Long> activity_rule_item_ids = new ArrayList<>();
                                    List<BigDecimal> activity_rule_item_id_values = new ArrayList<>();
                                    activity_rule_item_id_row.forEach((k, v) -> {
                                        activity_rule_item_ids.add(Convert.toLong(k));
                                        activity_rule_item_id_values.add(Convert.toBigDecimal(v));
                                    });

                                    List<ProductItemVo> activity_item_temp_rows = productBaseRepository.getItems(activity_rule_item_ids, null);

                                    if (CollectionUtil.isEmpty(activity_item_temp_rows)) {
                                        throw new BusinessException(__("活动赠送商品不存在！"));
                                    }


                                    ProductItemVo select_row = activity_item_temp_rows.get(activity_item_temp_rows.size() - 1);

                                    for (ProductItemVo item_row : activity_item_temp_rows) {
                                        ProductItemInfoVo item_select_row = new ProductItemInfoVo();
                                        BeanUtils.copyProperties(item_row, item_select_row);
                                        item_select_row.setItemSalePrice(activity_rule_item_id_row.get(item_row.getItemId()));
                                        item_select_row.setCartQuantity(activity_rule_item_num_row.get(item_row.getItemId()));
                                        select_info.add(item_select_row);
                                    }

                                    // 选择商品
                                    BigDecimal min_sale_price = Collections.min(activity_rule_item_id_values);
                                    bargain_activity_row.setActivityId(activityId);
                                    // 总有一个商品为默认选中的。
                                    bargain_activity_row.setMaxnum(NumberUtil.max(1, max_num));
                                    bargain_activity_row.setNum(0);
                                    bargain_activity_row.setItemSalePrice(min_sale_price);
                                    bargain_activity_row.setProductId(select_row.getProductId());
                                    bargain_activity_row.setItemId(select_row.getItemId());
                                    bargain_activity_row.setProductName(select_row.getProductName());
                                    bargain_activity_row.setItemName(select_row.getItemName());
                                    bargain_activity_row.setItemUnitPrice(select_row.getItemUnitPrice());
                                    bargain_activity_row.setProductItemName(select_row.getProductItemName());
                                    bargain_activity_row.setProductImage(select_row.getProductImage());
                                    bargain_activity_row.setSubtotal(NumberUtil.mul(min_sale_price, 0));
                                    bargain_activity_row.setCartSelect(true);
                                    bargain_activity_row.setCartQuantity(NumberUtil.max(1, max_num));
                                    bargain_activity_row.setTransportTypeId(select_row.getTransportTypeId());
                                    // 库存锁定策略（1001 下单锁 / 1002 支付锁）与类目等写入赠品行，供下单与 setPaidYes 使用
                                    bargain_activity_row.setProductInventoryLock(select_row.getProductInventoryLock());
                                    bargain_activity_row.setKindId(select_row.getKindId());
                                    bargain_activity_row.setCategoryId(select_row.getCategoryId());

                                    // 另外一种，直接选择商品， 是否显示为活动名称
                                    if (select_info.size() > 1) {
                                        bargain_activity_row.setSelectable(true);
                                        bargain_activity_row.setSelectInfo(select_info);
                                    } else {
                                        bargain_activity_row.setSelectable(false);
                                    }
                                }

                                //赠品只赠送一个SKU
                                // 需要判断是否在购物车中， 如果存在，将购物车数据移到此处显示
                                Integer bargins_current_num = 0;
                                Integer bargins_current_maxnum = 0;  // 当前活动已经加入购物车的数量

                                // 循环，处理加在购物车表里的赠品
                                Iterator<ProductItemVo> iterator = items.iterator();
                                while (iterator.hasNext()) {
                                    ProductItemVo _item = iterator.next();
                                    Long item_id = _item.getItemId();
                                    Integer cart_quantity = _item.getCartQuantity();
                                    Integer activity_id = _item.getActivityId();

                                    if (activity_rule_item_id_row.get(item_id) != null && ObjectUtil.equal(activity_id, activityId)) {
                                        bargins_current_maxnum = NumberUtil.add(Convert.toBigDecimal(bargins_current_maxnum), Convert.toBigDecimal(cart_quantity)).intValue();
                                        bargins_current_num = NumberUtil.add(bargins_current_num, cart_quantity).intValue();
                                        // 如果可购买已经达到上限, 再出现的不设置为赠品商品
                                        Integer maxnum = bargain_activity_row.getMaxnum();
                                        if (ObjectUtil.isNotNull(maxnum) && maxnum >= bargins_current_num) {
                                            //同一商品一种类型的活动只有一次
                                            //修正价格为赠品价格
                                            BigDecimal item_sale_price = BigDecimal.ZERO;

                                            // 修正显示为店铺加价购
                                            _item.setItemSalePrice(item_sale_price);
                                            _item.setBuyLimit(NumberUtil.sub(maxnum, bargins_current_maxnum).intValue());
                                            // 不再判断是否属于单一商品
                                            // 修正全店数据，购物车放在此处显示。
                                            //目前不从原数组删除，bargins_cart里面只存cart_id, js中通过cart_id获取数据。
                                            iterator.remove();

                                            // 赠品加入购物车,但是不以购物车数据形式展现，提交订单判断。
                                            Integer num = bargain_activity_row.getNum();
                                            bargain_activity_row.setNum(NumberUtil.add(num, cart_quantity).intValue());
                                            bargain_activity_row.setCartQuantity(bargain_activity_row.getNum());
                                            bargain_activity_row.setCartId(_item.getCartId());
                                            bargain_activity_row.setItemId(_item.getItemId());

                                            // 全店购物车中中数据处理
                                            bargain_activity_row.setProductId(_item.getProductId());
                                            bargain_activity_row.setProductName(_item.getProductName());
                                            bargain_activity_row.setItemName(_item.getItemName());
                                            bargain_activity_row.setProductItemName(_item.getProductItemName());
                                            bargain_activity_row.setItemUnitPrice(_item.getItemUnitPrice());
                                            bargain_activity_row.setProductImage(_item.getProductImage());
                                            bargain_activity_row.setSubtotal(BigDecimal.ZERO);
                                            bargain_activity_row.setCartSelect(true);
                                            bargain_activity_row.setProductInventoryLock(_item.getProductInventoryLock());
                                            bargain_activity_row.setKindId(_item.getKindId());
                                            bargain_activity_row.setCategoryId(_item.getCategoryId());

                                            // 减去赠品金额
                                            BigDecimal giftPrice = NumberUtil.mul(bargain_activity_row.getCartQuantity(), bargain_activity_row.getItemUnitPrice());
                                            moneyAmount = moneyAmount.subtract(giftPrice);
                                        }
                                    }
                                }
                                // 判断活动是否生效
                                if (CheckUtil.isNotEmpty(bargain_activity_row.getActivityId())) {
                                    Integer num = bargain_activity_row.getNum();
                                    bargain_activity_row.setNum(NumberUtil.max(1, num));

                                    for (ProductItemVo item : items) {

                                        if (CheckUtil.isNotEmpty(item.getActivityId()) && item.getActivityId().equals(activityId)) {
                                            List<ProductItemVo> gift = new ArrayList<>();
                                            gift.add(bargain_activity_row);

                                            item.setPulseGiftCart(gift);

                                            // 两个商品参加一个满送活动 只有一个商品有满送商品
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //满返
            if (CollectionUtil.isNotEmpty(activityManhuiProductItems)) {
                List<Integer> activityIds = CommonUtil.column(activityManhuiProductItems, ProductItemVo::getActivityId);

                if (CollectionUtil.isNotEmpty(activityIds)) {
                    for (Integer activityId : activityIds) {
                        List<ProductItemVo> productItemVoList = items.stream().filter(item -> CheckUtil.isNotEmpty(item.getActivityId()) && item.getActivityId().equals(activityId)).collect(Collectors.toList());
                        BigDecimal totalPrice = productItemVoList.stream().map(ProductItemVo::getItemSubtotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        if (CollUtil.isEmpty(productItemVoList) || productItemVoList.get(0) == null) {
                            continue;
                        }
                        ActivityInfoVo activityInfo = productItemVoList.get(0).getActivityInfo();

                        if (activityInfo == null) {
                            throw new BusinessException(__("活动信息不存在！"));
                        }

                        int activityVoucherId = 0;

                        if (StrUtil.isNotEmpty(activityInfo.getActivityBase().getActivityRule())) {
                            ActivityRuleVo activityRuleVo = JSONUtil.parseObject(activityInfo.getActivityBase().getActivityRule(), ActivityRuleVo.class);

                            if (activityRuleVo == null) {
                                throw new BusinessException(__("活动规则不存在！"));
                            }

                            List<RuleVo> rules = activityRuleVo.getRule();

                            if (CollectionUtil.isEmpty(rules)) {
                                throw new BusinessException(__("活动详细规则不存在！"));
                            }

                            for (RuleVo rule : rules) {
                                if (totalPrice.compareTo(rule.getAmount()) >= 0) {
                                    activityVoucherId = rule.getMaxNum();
                                } else {
                                    break;
                                }
                            }

                            if (CheckUtil.isNotEmpty(activityVoucherId)) {
                                ActivityBase giveActivity = activityBaseRepository.get(activityVoucherId);

                                if (giveActivity == null) {
                                    throw new BusinessException(__("满返赠品活动信息不存在！"));
                                }
                                activityInfo.setGiveActivityName(giveActivity.getActivityName());
                                String activityRule = giveActivity.getActivityRule();

                                if (StrUtil.isNotEmpty(activityRule)) {
                                    ActivityRuleVo giveRule = JSONUtil.parseObject(activityRule, ActivityRuleVo.class);

                                    if (giveRule != null) {
                                        VoucherVo voucher = giveRule.getVoucher();

                                        if (voucher != null) {
                                            activityInfo.setGiveImage(voucher.getVoucherImage());
                                            activityInfo.setGiveVoucherPrice(voucher.getVoucherPrice());
                                            activityInfo.setGiveVoucherProductLimit(voucher.getVoucherProductLimit());
                                            activityInfo.setGiveVoucherStartDate(voucher.getVoucherStartDate());
                                            activityInfo.setGiveVoucherEndDate(voucher.getVoucherEndDate());
                                        }
                                        RequirementVo requirement = giveRule.getRequirement();

                                        if (requirement != null) {
                                            BuyVo buy = requirement.getBuy();

                                            if (buy != null) {
                                                activityInfo.setGiveVoucherSubtotal(buy.getSubtotal());
                                            }
                                        }
                                    }
                                }

                                storeItemVo.getManhuiActivityIds().add(activityVoucherId);
                                storeItemVo.getActivitys().getManhui().add(activityInfo);
                            }

                            for (ProductItemVo item : items) {
                                if (CheckUtil.isNotEmpty(item.getActivityId()) && item.getActivityId().equals(activityId)) {
                                    //是否触发满返
                                    if (!item.getCartSelect() || activityVoucherId == 0) {
                                        //未触发，清除活动标记
                                        item.setActivityInfo(null);
                                        item.setActivityId(null);
                                    } else {
                                        item.setGiveId(activityVoucherId);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 过滤掉不可使用的优惠券，
            Iterator<UserVoucherRes> iter = voucherItems.iterator();

            while (iter.hasNext()) {
                UserVoucherRes voucherItem = iter.next();
                //指定优惠券
                String itemIdStr = voucherItem.getItemId();
                BigDecimal voucherSubtotal = voucherItem.getVoucherSubtotal();

                if (StrUtil.isNotEmpty(itemIdStr)) {
                    List<Long> itemIdList = Convert.toList(Long.class, itemIdStr);

                    //指定优惠券itemIdList不包含购买的任何商品
                    if (Collections.disjoint(itemIdList, itemIds)) {
                        // 删除不符合条件的优惠券
                        iter.remove();
                        continue;
                    }

                    //使用优惠券的订单金额 （指定）
                    BigDecimal assignProductAmount = BigDecimal.ZERO;
                    for (ProductItemVo item : items) {

                        if (itemIdList.contains(item.getItemId())) {
                            BigDecimal itemSalSubtotal = item.getItemSalePrice().multiply(BigDecimal.valueOf(item.getCartQuantity()));
                            assignProductAmount = assignProductAmount.add(itemSalSubtotal);
                        }
                    }
                    if (ObjectUtil.compare(assignProductAmount, voucherSubtotal) < 0) {
                        iter.remove();
                        continue;
                    }
                }

                //使用优惠券的订单金额 （全部） - 店铺已经产生优惠的金额  discountAmount
                if (ObjectUtil.compare(NumberUtil.sub(itemAmount, discountAmount), voucherSubtotal) < 0) {
                    iter.remove();
                }
            }

            storeItemVo.setIsVirtual(isVirtual);
            storeItemVo.setKindId(kindId);
            storeItemVo.setItems(items);
            storeItemVo.setMoneyItemAmount(itemAmount);

            //店铺选中的使用代金券
            UserVoucherRes voucherItemSelected = null;

            for (UserVoucherRes voucherItem : voucherItems) {
                if (voucherItem.getStoreId().equals(storeId) && voucherItem.getUserId().equals(in.getUserId())) {
                    storeItemVo.getVoucherItems().add(voucherItem);

                    //判断是否为选中的
                    if (ObjectUtil.isNotEmpty(in.getUserVoucherIds()) && in.getUserVoucherIds().contains(voucherItem.getUserVoucherId())) {
                        voucherItemSelected = voucherItem;
                    }
                }
            }

            //配送地址

            //1、门店自提，不需要配送运费的。 is_delivery = false
            //2、虚拟商品，不需要配送运费的。 is_delivery = false


            // 是否需要配送地址
            if (isVirtual) {
                //todo 判断是否有需要上门服务，需要计算配送费
                isDelivery = false;
            }

            // 是否需要配送地址， 自提及虚拟到店服务，不需要配送地址。
            if (isDelivery) {
                // 计算运费
                // 如果没有配送地址，则忽略地址选择问题。
                if (deliveryAddress != null && deliveryAddress.getUdCityId() != null) {
                    Integer district_id = deliveryAddress.getUdCityId();
                    freightAmount = calTransportFreight(storeItemVo, district_id);// 配送检测
                } else {
                    //throw new BusinessException(__("请选择正确的收货地址！"));
                }
                // end 运费计算
            }

            if (userIsSale) {
                Float salePersonRate = configBaseService.getConfig("sale_person_rate", 100f);

                if (salePersonRate.compareTo(0f) <= 0) {
                    throw new BusinessException(__("销售员折扣比率不能小于等于零！"));
                }
                BigDecimal salePersonRateAmount = moneyAmount.multiply(BigDecimal.valueOf(salePersonRate)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal salePersonDiscount = NumberUtil.sub(moneyAmount, salePersonRateAmount);
                moneyAmount = salePersonRateAmount;
                storeItemVo.setSalePersonDiscount(salePersonDiscount);
                orderSalePersonDiscount = orderSalePersonDiscount.add(salePersonDiscount);
            }

            //优惠券 voucherItemSelected 修正最终付款价格： moneyAmount， 不修正itemAmount原价
            if (ObjectUtil.isNotEmpty(voucherItemSelected)) {
                storeItemVo.setUserVoucherId(voucherItemSelected.getUserVoucherId());
                storeItemVo.setVoucherAmount(voucherItemSelected.getVoucherPrice());
                //moneyAmount = moneyAmount.subtract(voucherItemSelected.getVoucherPrice());
            }

            // PLUS 自营店铺免运费
            if (freeShippingPrivilege(in.getUserId()) && (storeItemVo.getStoreIsSelfsupport() || storeItemVo.getStoreId().equals(0))) {
                storeItemVo.setPlusShippingDiscount(freightAmount);
                freightAmount = BigDecimal.ZERO;
            }

            moneyAmount = NumberUtil.sub(NumberUtil.add(moneyAmount, freightAmount), storeItemVo.getVoucherAmount());

            storeItemVo.setDiscountAmount(discountAmount);
            storeItemVo.setProductAmount(productAmount);
            storeItemVo.setMoneyAmount(moneyAmount); //下单时候运费及活动会修正此值
            storeItemVo.setMoneyItemAmount(itemAmount);
            storeItemVo.setPointsAmount(pointsAmount);
            storeItemVo.setFreightAmount(freightAmount);
            storeItemVo.setLevelDiscountAmount(levelDiscountAmount);
            storeItemVo.setPlusDiscountAmount(plusDiscountAmount);

            out.getItems().add(storeItemVo);

            orderProductAmount = orderProductAmount.add(productAmount);
            orderItemAmount = orderItemAmount.add(itemAmount);
            orderMoneyAmount = orderMoneyAmount.add(moneyAmount);
            orderFreightAmount = orderFreightAmount.add(freightAmount);
            orderDiscountAmount = orderDiscountAmount.add(discountAmount);
            orderPointsAmount = orderPointsAmount.add(pointsAmount);
            orderSpAmount = orderSpAmount.add(spAmount);
        }

        out.setOrderProductAmount(orderProductAmount);
        out.setOrderItemAmount(orderProductAmount);
        out.setOrderFreightAmount(orderFreightAmount);
        out.setOrderMoneyAmount(orderMoneyAmount);
        out.setOrderDiscountAmount(orderDiscountAmount);
        out.setOrderSalePersonDiscount(orderSalePersonDiscount);
        out.setOrderPointsAmount(orderPointsAmount);
        out.setOrderSpAmount(orderSpAmount);

        return out;
    }

    private void checkoutLevel(String activityUseLevel, Integer userLevelId) {
        if (StrUtil.isNotEmpty(activityUseLevel)) {
            List<Integer> userLevels = Convert.toList(Integer.class, activityUseLevel);

            if (CollectionUtil.isNotEmpty(userLevels)) {
                Collections.sort(userLevels);
                List<UserLevel> userLevelList = userLevelRepository.gets(userLevels);

                if (CollectionUtil.isEmpty(userLevelList)) {
                    throw new BusinessException(__("等级信息不存在！"));
                }

                if (!userLevels.contains(userLevelId)) {
                    throw new BusinessException(String.format(__("活动商品会员等级为 %s ，用户等级未达到"), CollUtil.join(CommonUtil.column(userLevelList, UserLevel::getUserLevelName), "、")));
                }
            }
        }
    }


    /**
     * 配送区域判断及运费, 并修正最终数据
     * <p>
     * 1、单品在一个订单中，运费规则计算
     * 2、配送区域问题
     *
     * @param storeItemVo 最总数据
     * @param district_id 配送地区
     */
    @Override
    public BigDecimal calTransportFreight(StoreItemVo storeItemVo, Integer district_id) {
        //运费模板  商品数量
        List transport_type_none_ids = new ArrayList();
        List delivery_item_none_row = new ArrayList();

        //todo 如果为礼包等等不计费运费商品，可以计算运费时候，从items中忽略

        // 运费
        BigDecimal freight = BigDecimal.ZERO;
        BigDecimal freightFreeAmountMax = BigDecimal.ZERO;

        List<Integer> ttIds = CommonUtil.column(storeItemVo.getItems(), ProductItemVo::getTransportTypeId);

        // 判断运费方式，如果发现同一个订单计费模式不一致，报错，禁止下单。
        if (CollectionUtil.isEmpty(ttIds)) {
            throw new BusinessException(__("商品运费设置有误！请联系商家检查商品设置！"));
        }

        List<StoreTransportType> storeTransportTypes = storeTransportTypeRepository.gets(ttIds);

        if (CollectionUtil.isEmpty(storeTransportTypes)) {
            throw new BusinessException(__("物流运费不存在！"));
        }
        List<Integer> transport_type_pricing_method = CommonUtil.column(storeTransportTypes, StoreTransportType::getTransportTypePricingMethod);

        if (transport_type_pricing_method.size() > 1) {
            throw new BusinessException(__("所选商品运费模式不统一，请拆分下单！"));
        }
        Integer transport_method = transport_type_pricing_method.get(0);
        if (transport_method == null) {
            throw new BusinessException(__("运费计费方式数据异常！"));
        }
        Map<Integer, StoreTransportType> transportTypeMap = storeTransportTypes.stream().collect(Collectors.toMap(StoreTransportType::getTransportTypeId, StoreTransportType -> StoreTransportType, (k1, k2) -> k1));

        List<ProductItemVo> items = storeItemVo.getItems();

        //处理 ttIdsMap
        Map<Integer, BigDecimal> ttIdsMap = new HashMap();

        for (ProductItemVo pi : items) {
            Integer transportTypeId = pi.getTransportTypeId();
            StoreTransportType storeTransportType = transportTypeMap.get(transportTypeId);

            if (storeTransportType == null) {
                throw new BusinessException(__("物流运费模板不存在！"));
            }
            Integer pricingMethod = storeTransportType.getTransportTypePricingMethod();
            BigDecimal quantity = ObjectUtil.defaultIfNull(ttIdsMap.get(transportTypeId), BigDecimal.ZERO);

            if (pricingMethod.equals(1)) {
                quantity = NumberUtil.add(quantity, pi.getCartQuantity());
            } else if (pricingMethod.equals(2)) {
                quantity = NumberUtil.add(quantity, NumberUtil.mul(pi.getItemWeight(), pi.getCartQuantity()));
            } else if (pricingMethod.equals(3)) {
                quantity = NumberUtil.add(quantity, NumberUtil.mul(pi.getItemVolume(), pi.getCartQuantity()));
            }
            ttIdsMap.put(pi.getTransportTypeId(), quantity);
        }

        BigDecimal moneyItemAmount = storeItemVo.getMoneyItemAmount();

        for (Integer tt_id : ttIds) {
            BigDecimal quantity = ttIdsMap.get(tt_id);
            Integer tt_transport_type_id = Convert.toInt(tt_id);

            //此处免运费，传递的是transport_type中的设置。
            Optional<StoreTransportType> transportTypeOpl = storeTransportTypes.stream().filter(s -> ObjectUtil.equal(s.getTransportTypeId(), tt_id)).findFirst();
            StoreTransportType transportType = transportTypeOpl.orElseGet(StoreTransportType::new);
            BigDecimal freightFreeAmount = transportType.getTransportTypeFreightFree();

            freightFreeAmountMax = NumberUtil.max(freightFreeAmountMax, freightFreeAmount);

            OrderFreightVo data = storeTransportTypeRepository.calFreight(tt_transport_type_id, district_id, quantity, moneyItemAmount, freightFreeAmount);

            boolean type_freight = data.getCanDelivery();
            BigDecimal _freight = data.getFreight();

            if (!type_freight) {
                // 配送不到这个区域，提示删除商品
                transport_type_none_ids.add(tt_transport_type_id);
            } else {
                freight = NumberUtil.add(freight, _freight);
            }
        }


        // 配送区域无货设置；
        if (CollUtil.isNotEmpty(transport_type_none_ids)) {
            for (ProductItemVo item_row : items) {
                // 配送区域 库存问题。
                Integer _transport_type_id = item_row.getTransportTypeId();
                boolean show_oos = transport_type_none_ids.contains(_transport_type_id);
                item_row.setIsOos(show_oos);

                delivery_item_none_row.add(item_row);
            }
        }

        if (storeItemVo.getMoneyItemAmount().compareTo(freightFreeAmountMax) < 0) {
            storeItemVo.setFreightFreeBalance(NumberUtil.sub(freightFreeAmountMax, storeItemVo.getMoneyItemAmount()).abs());
        } else {
            storeItemVo.setFreightFreeBalance(BigDecimal.ZERO);
        }

        return freight;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCart(CartAddInput in) {
        //流程可配置 节点触发api调用

        //todo  判断库存，提示加入购物车
        if (in.getCartQuantity() <= 0) {
            throw new BusinessException(__("最低备货数量 1 件，请确认！"));
        }

        ProductItem productItem = productItemRepository.get(in.getItemId());

        if (!Objects.equals(productItem.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL)) {
            throw new BusinessException(__("商品未上架，不可加入购物车！"));
        }

        // 店铺关闭时禁止加入购物车，防止关店后继续下单
        if (CheckUtil.isNotEmpty(productItem.getStoreId())) {
            StoreBase storeBase = storeBaseRepository.get(productItem.getStoreId());
            if (storeBase != null && !Boolean.TRUE.equals(storeBase.getStoreIsOpen())) {
                throw new BusinessException(__("店铺已关闭，不可加入购物车！"));
            }
        }

        //判断可用库存
        int availableQuantity = productItem.getItemQuantity() - productItem.getItemQuantityFrozen();

        QueryWrapper<UserCart> where = new QueryWrapper<UserCart>().eq("user_id", in.getUserId()).eq("item_id", in.getItemId());

        /*
        where.eq("store_id", productItem.getStoreId());

        if (in.getChainId() != null && in.getChainId() > 0) {
            where.eq("chain_id", in.getChainId());
        } else {
            where.eq("chain_id", 0); // 根据错误信息，chain_id 可能是 0
        }
        */

        UserCart cart = repository.findOne(where);

        if (ObjectUtil.isEmpty(cart)) {
            if (in.getCartQuantity() > availableQuantity) {
                throw new BusinessException(String.format(__("库存可用数量 %d 件，请确认！"), availableQuantity));
            }

            if (CheckUtil.isNotEmpty(in.getActivityId())) {
                ActivityBase activityBase = activityBaseRepository.get(in.getActivityId());

                if (activityBase == null) {
                    throw new BusinessException(__("商品参加活动信息不存在！"));
                }

                if (!in.getCartType().equals(3) && Objects.equals(activityBase.getActivityTypeId(), StateCode.ACTIVITY_TYPE_GIFT)) {
                    in.setActivityId(0);
                }
            }

            cart = BeanUtil.copyProperties(in, UserCart.class);
            cart.setProductId(productItem.getProductId());
            cart.setItemId(productItem.getItemId());
            cart.setStoreId(productItem.getStoreId());
        } else {
            if (in.getCartQuantity() + cart.getCartQuantity() > availableQuantity) {
                throw new BusinessException(String.format(__("库存可用数量 %d 件，请确认！"), availableQuantity));
            }

            cart.setCartQuantity(cart.getCartQuantity() + in.getCartQuantity());
        }

        //购物车当前商品
        QueryWrapper<UserCart> userCartQueryWrapper = new QueryWrapper<>();
        userCartQueryWrapper.eq("user_id", in.getUserId());
        userCartQueryWrapper.eq("cart_select", true);
        List<UserCart> userCarts = find(userCartQueryWrapper);

        if (CollectionUtil.isNotEmpty(userCarts)) {

            if (cart.getCartSelect()) {
                userCarts.add(cart);
            }
            CheckoutInput checkoutInput = new CheckoutInput();
            checkoutInput.setUserId(in.getUserId());
            checkoutInput.setItems(BeanUtil.copyToList(userCarts, CheckoutItemVo.class));
            checkout(checkoutInput);
        }

        // 判断是新增还是更新
        // 赠品只能选一个SKU
        if (cart.getCartType().equals(3)) {
            QueryWrapper<UserCart> userCartQuery = new QueryWrapper<>();
            userCartQuery.eq("user_id", cart.getUserId());
            userCartQuery.eq("activity_id", cart.getActivityId());
            UserCart userCart = findOne(userCartQuery);

            if (userCart != null) {
                cart.setCartId(userCart.getCartId());
                cart.setCartQuantity(in.getCartQuantity());
            }
        }

        return repository.saveOrUpdate(cart);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sel(UserCartSelectInput input) {
        Integer userId = input.getUserId();
        Long cartId = input.getCartId();
        Integer storeId = input.getStoreId();
        String action = input.getAction();

        List<Serializable> cartIds = new ArrayList<>();

        if ("all".equals(action)) {
            QueryWrapper<UserCart> cartQueryWrapper = new QueryWrapper<>();
            cartQueryWrapper.eq("user_id", userId);
            cartIds = findKey(cartQueryWrapper);
        } else if ("store".equals(action)) {
            QueryWrapper<UserCart> cartQueryWrapper = new QueryWrapper<>();
            cartQueryWrapper.eq("user_id", userId).eq("store_id", storeId);
            cartIds = findKey(cartQueryWrapper);
        } else {
            cartIds = Arrays.asList(cartId);
        }

        QueryWrapper<UserCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("cart_id", cartIds);

        UserCart cart = new UserCart();
        cart.setCartSelect(input.getCartSelect());

        if (!edit(cart, queryWrapper)) {
            throw new BusinessException(__("更改购物车选中状态失败"));
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean editQuantity(UserCart userCart, Integer userId) {
        UserCart cart = get(userCart.getCartId());
        boolean result = false;

        if (cart == null) {
            throw new BusinessException(__("购物车该数据不存在！"));
        }

        if (cart.getUserId().equals(userId)) {
            //减少
            if (userCart.getCartQuantity().equals(0)) {
                result = remove(userCart.getCartId());
            } else {
                //增加
                ProductItem productItem = productItemRepository.get(cart.getItemId());

                if (productItem == null) {
                    throw new BusinessException(__("该商品不存在！"));
                }
                //判断可用库存
                int availableQuantity = productItem.getItemQuantity() - productItem.getItemQuantityFrozen();

                if (userCart.getCartQuantity() > availableQuantity) {
                    throw new BusinessException(String.format(__("库存可用数量 %d 件，请确认！"), availableQuantity));
                }

                cart.setCartQuantity(userCart.getCartQuantity());
                result = edit(cart);
            }
        }

        return result;
    }

}
