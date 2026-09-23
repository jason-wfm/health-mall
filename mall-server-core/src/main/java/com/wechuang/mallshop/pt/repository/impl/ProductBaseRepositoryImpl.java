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
package com.wechuang.mallshop.pt.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.repository.UserLevelRepository;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.RequestUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.marketing.model.vo.ActivityInfoVo;
import com.wechuang.mallshop.marketing.service.ActivityItemService;
import com.wechuang.mallshop.pt.dao.ProductBaseDao;
import com.wechuang.mallshop.pt.model.context.PriceContext;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.vo.ProductDataBundleVo;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.sys.model.entity.LangMeta;
import com.wechuang.mallshop.sys.repository.ConfigBaseRepository;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


/**
 * <p>
 * 商品基础表-SPU表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-03-20
 */
@Repository
public class ProductBaseRepositoryImpl extends BaseRepositoryImpl<ProductBaseDao, ProductBase> implements ProductBaseRepository {
    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ActivityItemService activityItemService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserLevelRepository userLevelRepository;

    @Autowired
    private LangMetaRepository langMetaRepository;

    @Autowired
    private ConfigBaseRepository configBaseRepository;

    @Autowired
    private ProductPricingPolicyRepository productPricingPolicyRepository;

    @Override
    public List<ProductItemVo> getItems(List<Long> itemIds, Integer userId) {

        if (CollUtil.isEmpty(itemIds)) {
            return Collections.emptyList();
        }

        // 1. 加载基础数据（无业务逻辑）
        ProductDataBundleVo dataBundle = loadBaseData(itemIds, userId);

        // 2. 构建上下文（用户 + 配置）
        PriceContext context = buildContext(userId, dataBundle);

        // 3. 价格计算
        List<ProductItemVo> itemVos = calculatePrice(dataBundle, context);

        // 4. 填充商品信息（图片 / 名称 / 标签等）
        fillProductInfo(itemVos, dataBundle, context);

        return itemVos;
    }

    // 1. 加载基础数据 只负责查数据 + 转 Map
    private ProductDataBundleVo loadBaseData(List<Long> itemIds, Integer userId) {

        ProductDataBundleVo bundle = new ProductDataBundleVo();

        // 1. SKU
        List<ProductItem> itemList = productItemRepository.gets(itemIds);
        bundle.setItemList(itemList);

        if (CollUtil.isEmpty(itemList)) {
            return bundle;
        }

        List<Long> productIds = CommonUtil.column(itemList, ProductItem::getProductId);

        // 2. 商品索引
        List<ProductIndex> productIndexs = productIndexRepository.gets(productIds);
        bundle.setProductIndexMap(productIndexs.stream()
                .collect(Collectors.toMap(ProductIndex::getProductId, Function.identity(), (a, b) -> a)));

        // 3. 商品基础信息
        List<ProductBase> productBases = gets(productIds);
        bundle.setProductBaseMap(productBases.stream()
                .collect(Collectors.toMap(ProductBase::getProductId, Function.identity(), (a, b) -> a)));

        // 4. 活动信息
        List<ActivityInfoVo> activityList = activityItemService.getActivityInfo(itemIds, userId);
        bundle.setActivityMap(activityList.stream()
                .collect(Collectors.toMap(ActivityInfoVo::getItemId, Function.identity(), (a, b) -> a)));

        // 5. 图片
        List<ProductImage> images = productImageRepository.find(
                new QueryWrapper<ProductImage>().in("product_id", productIds));

        bundle.setImageMap(images.stream()
                .collect(Collectors.toMap(
                        s -> s.getProductId() + "_" + s.getColorId(),
                        Function.identity(),
                        (a, b) -> a
                )));

        // 6. 等级配置
        bundle.setLevelDiscountMap(productIndexs.stream()
                .collect(Collectors.toMap(ProductIndex::getProductId, ProductIndex::getProductLevelMembership)));

        // 7. 一客一价
        List<ProductPricingPolicy> policyList = productPricingPolicyRepository.find(
                new QueryWrapper<ProductPricingPolicy>()
                        .in("item_id", itemIds)
                        .eq("user_id", userId)
                        .eq("policy_enable", true)
        );
        Map<Long, ProductPricingPolicy> policyMap = policyList.stream()
                .collect(Collectors.toMap(
                        ProductPricingPolicy::getItemId,
                        Function.identity(),
                        (a, b) -> a
                ));
        bundle.setPolicyMap(policyMap);

        return bundle;
    }

    // 2. 构建上下文 配置信息
    private PriceContext buildContext(Integer userId, ProductDataBundleVo bundle) {

        PriceContext ctx = new PriceContext();
        ctx.setUserId(userId);

        if (CheckUtil.isNotEmpty(userId)) {
            UserInfo userInfo = userInfoRepository.get(userId);

            if (userInfo != null) {
                ctx.setUserLevelId(userInfo.getUserLevelId());
                ctx.setUserLevelRate(
                        userLevelRepository.getUserLevelRateMap()
                                .getOrDefault(userInfo.getUserLevelId(), 100)
                );
            }

            ctx.setPlusMember(false);
        }

        ctx.setPlusEnable(false);
        ctx.setPricingPolicyEnable(configBaseRepository.getConfigValue("product_pricing_policy", false));

        return ctx;
    }

    private List<ProductItemVo> calculatePrice(ProductDataBundleVo bundle, PriceContext ctx) {

        List<ProductItemVo> result = new ArrayList<>();

        for (ProductItem item : bundle.getItemList()) {

            ProductItemVo vo = new ProductItemVo();
            BeanUtils.copyProperties(item, vo);

            vo.setItemSalePrice(vo.getItemUnitPrice());

            ProductIndex productIndex = bundle.getProductIndexMap().get(item.getProductId());

            // todo 目前一客一价优先级最高 直接跳过后续所有价格逻辑
            ProductPricingPolicy policy = bundle.getPolicyMap().get(item.getItemId());
            if (policy != null) {
                BigDecimal policyPrice = policy.getPolicyPrice();
                vo.setItemPolicyPrice(policyPrice);
                vo.setItemSalePrice(policyPrice);
                vo.setItemSavePrice(vo.getItemUnitPrice().subtract(policyPrice));

                vo.setPriceType(StateCode.PRICE_TYPE_POLICY);
                vo.setPriceTag(__("专属价"));

                result.add(vo);

                continue;
            }

            // 2. 等级价
            applyLevelPrice(vo, item, productIndex, ctx);

            // 3. 活动价
            applyActivityPrice(vo, bundle.getActivityMap().get(item.getItemId()));

            result.add(vo);
        }

        return result;
    }

    // 等级价
    private void applyLevelPrice(ProductItemVo vo, ProductItem item, ProductIndex productIndex, PriceContext ctx) {

        Integer membership = productIndex.getProductLevelMembership();

        if (membership.equals(2) && ctx.getUserLevelId() != null) {

            String discountJson = item.getItemLevelDiscount();

            if (StrUtil.isNotEmpty(discountJson)) {
                Map map = JSONUtil.toBean(discountJson, Map.class);
                Integer discount = Convert.toInt(
                        map.get(Convert.toStr(ctx.getUserLevelId())), 100);

                calLevelDiscount(vo, discount);
            }

        } else if (ctx.getUserLevelRate() < 100) {
            calLevelDiscount(vo, ctx.getUserLevelRate());
        }
    }

    // 活动价
    private void applyActivityPrice(ProductItemVo vo, ActivityInfoVo act) {

        if (act == null) return;

        // 设置活动信息
        vo.setActivityId(act.getActivityId());
        vo.setActivityInfo(act);

        if (!checkSingleActivity(act.getActivityTypeId())) return;

        BigDecimal activityPrice = act.getActivityItemPrice();
        if (activityPrice == null) return;

        if (activityPrice.compareTo(vo.getItemSalePrice()) < 0) {

            vo.setItemSavePrice(vo.getItemSalePrice().subtract(activityPrice));
            vo.setItemSalePrice(activityPrice);

            vo.setPriceType(StateCode.PRICE_TYPE_ACTIVITY);
            vo.setPriceTag(__("活动价"));
        }
    }

    // 填充商品信息
    private void fillProductInfo(List<ProductItemVo> list, ProductDataBundleVo bundle, PriceContext ctx) {

        for (ProductItemVo vo : list) {

            Long productId = vo.getProductId();

            ProductBase base = bundle.getProductBaseMap().get(productId);
            ProductIndex index = bundle.getProductIndexMap().get(productId);

            if (base == null || index == null) continue;

            vo.setProductName(base.getProductName()); // 商品名称
            vo.setProductTips(base.getProductTips()); // 商品卖点

            String itemSpecName = StrUtil.replaceChars(vo.getItemName(), ",", " ");
            String productItemName = base.getProductName() + " " + itemSpecName;
            vo.setProductItemName(productItemName); // 商品名称 + 规格名称

            BigDecimal productCommissionRate = Optional.ofNullable(base.getProductCommissionRate()).orElse(BigDecimal.ZERO);
            vo.setProductCommissionRate(productCommissionRate); // 商品佣金比例
            vo.setTransportTypeId(base.getTransportTypeId()); // 运费模板

            vo.setProductTags(index.getProductTags());  // 标签
            vo.setProductDistEnable(index.getProductDistEnable()); // 分销标记
            vo.setProductInventoryLock(index.getProductInventoryLock()); // 锁定库存方式
            vo.setKindId(index.getKindId()); // 商品类型
            vo.setIndustryIds(index.getIndustryIds());
            vo.setProductPlusEnable(index.getProductPlusEnable()); // 是否参与PLUS折扣

            Integer productStateId = index.getProductStateId();
            //SKU是否启用
            if (vo.getItemEnable() == StateCode.PRODUCT_STATE_NORMAL) {
                //可用库存不足，设为下架状态
                if (vo.getItemQuantity() - vo.getItemQuantityFrozen() > 0) {

                } else {
                    productStateId = StateCode.PRODUCT_STATE_OFF_THE_SHELF;
                }
            } else {
                productStateId = StateCode.PRODUCT_STATE_OFF_THE_SHELF;
            }

            vo.setProductStateId(productStateId); // 设置商品状态
            vo.setProductMinimumOrder(base.getProductMinimumOrder());  //起订量

            // 商品图片
            String key = productId + "_" + vo.getColorId();
            ProductImage img = bundle.getImageMap().get(key);

            if (img != null && StrUtil.isNotEmpty(img.getItemImageDefault())) {
                vo.setProductImage(img.getItemImageDefault());
            } else {
                vo.setProductImage(base.getProductImage());
            }
        }
    }

    /**
     * 计算会员等级优惠
     *
     * @param itVo
     * @param userLevelRate
     */
    private void calLevelDiscount(ProductItemVo itVo, Integer userLevelRate) {
        BigDecimal originalPrice = itVo.getItemUnitPrice();
        BigDecimal discountRate = BigDecimal.valueOf(userLevelRate);

        // 会员折扣价
        BigDecimal itemLevelPrice = originalPrice.multiply(discountRate).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        itVo.setItemSalePrice(itemLevelPrice);

        // 单品会员优惠金额
        BigDecimal itemLevelDiscountPrice = originalPrice.subtract(itemLevelPrice).setScale(2, RoundingMode.HALF_UP);
        itVo.setItemLevelDiscountPrice(itemLevelDiscountPrice);
        itVo.setItemSavePrice(itemLevelDiscountPrice);

        itVo.setPriceType(StateCode.PRICE_TYPE_LEVEL);
        itVo.setPriceTag(__("会员价"));
    }

    /**
     * 单品直接购买活动，判断是否通过活动
     *
     * @param activityTypeId
     * @return
     */
    private boolean checkSingleActivity(Integer activityTypeId) {
        return true;
    }

    @Override
    public List<ProductBase> gets(Collection<? extends Serializable> a) {
        List<ProductBase> list = super.gets(a);

        String to = ContextUtil.getToLang();
        if (to == null) {
            return list;
        }

        for (ProductBase it : list) {
            if (ObjectUtil.isNotEmpty(it)) {
                String translate = langMetaRepository.getTranslate(it.getProductName(), to, ConstantConfig.BASE_LANG, "pt_product_base", Convert.toStr(it.getProductId()), "product_name", 0);
                it.setProductName(translate);

                if (StringUtils.isNotEmpty(it.getProductTips())) {
                    it.setProductTips(langMetaRepository.getTranslate(it.getProductTips(), to, ConstantConfig.BASE_LANG, "pt_product_base", Convert.toStr(it.getProductId()), "product_tips", 0));
                }
                // 处理商品索引字段信息
                // 不能放在此处，区分不出新翻译还是已经翻译过。
                /*
                ProductIndex productIndexRow = productIndexRepository.get(it.getProductId());

                if (productIndexRow != null) {
                    String[] productNameIndexRow = productIndexRow.getProductNameIndex().split(" -|- ");

                    //重新设置索引
                    ProductIndex productIndex = new ProductIndex();

                    String productNameIndex = initProductLangIndex(it.getProductId(), productNameIndexRow[0]);

                    productIndex.setProductId(Convert.toLong(it.getProductId()));
                    productIndex.setProductNameIndex(productNameIndex);
                    productIndexRepository.save(productIndex);
                }
                 */
            }
        }

        return list;
    }

    public String initProductLangIndex(Long productId, String productNameIndex) {
        QueryWrapper<LangMeta> queryParams = new QueryWrapper<>();
        queryParams.eq("table_name", "pt_product_base");
        queryParams.eq("column_name", "product_name");
        queryParams.eq("primary_key", productId);

        List<LangMeta> langmetaRows = langMetaRepository.find(queryParams);

        // 处理唯一的meta_value列
        List<String> langNameIndexRow = CommonUtil.column(langmetaRows, LangMeta::getMetaValue);

        if (!langNameIndexRow.isEmpty()) {
            productNameIndex = String.format("%s -|- %s", productNameIndex, String.join(" ", langNameIndexRow));
        }

        return productNameIndex;
    }
}
