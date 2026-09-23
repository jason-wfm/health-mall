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
package com.wechuang.mallshop.pt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserDeliveryAddressRepository;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.admin.model.entity.UserAdmin;
import com.wechuang.mallshop.admin.repository.UserAdminRepository;
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
import com.wechuang.mallshop.marketing.model.vo.*;
import com.wechuang.mallshop.marketing.repository.ActivityBaseRepository;
import com.wechuang.mallshop.marketing.repository.ActivityItemRepository;
import com.wechuang.mallshop.marketing.service.ActivityItemService;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.input.ProductDetailInput;
import com.wechuang.mallshop.pt.model.input.ProductIndexInput;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.output.ProductOutput;
import com.wechuang.mallshop.pt.model.req.ProductIndexListReq;
import com.wechuang.mallshop.pt.model.res.ActivityInfoRes;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.model.res.ProductDetailRes;
import com.wechuang.mallshop.pt.model.res.ProductListRes;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.pt.service.ProductAssistItemService;
import com.wechuang.mallshop.pt.service.ProductAssistService;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.wechuang.mallshop.shop.model.entity.*;
import com.wechuang.mallshop.shop.model.vo.StoreTransportItemVo;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.repository.StoreTransportTypeRepository;
import com.wechuang.mallshop.shop.service.StoreAnalyticsService;
import com.wechuang.mallshop.sys.model.entity.ContractType;
import com.wechuang.mallshop.sys.repository.ContractTypeRepository;
import com.wechuang.mallshop.sys.repository.DistrictBaseRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.MessageTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 产品索引表-不读取数据只读主键 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Service
public class ProductIndexServiceImpl extends BaseServiceImpl<ProductIndexRepository, ProductIndex, ProductIndexListReq> implements ProductIndexService {

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductTagRepository productTagRepository;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductAssistRepository productAssistRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Autowired
    private ActivityItemService activityItemService;

    @Autowired
    private StoreTransportTypeRepository storeTransportTypeRepository;

    @Autowired
    private DistrictBaseRepository districtBaseRepository;

    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ActivityBaseRepository activityBaseRepository;

    @Autowired
    private ActivityItemRepository activityItemRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private UserDeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private ProductBrandRepository productBrandRepository;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Autowired
    private StoreAnalyticsService storeAnalyticsService;

    @Autowired
    private ProductPricingPolicyRepository productPricingPolicyRepository;

    @Autowired
    private ProductIndexService productIndexService;

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private UserAdminRepository userAdminRepository;

    @Autowired
    private ProductAssistItemService productAssistItemService;

    @Autowired
    private ProductAssistService productAssistService;

    // 获取查询条件
    private void getQueryWrapper(ProductIndexInput in, QueryWrapper<ProductIndex> wrapper) {
        //指定优惠券跳转商品列表
        if (StrUtil.isNotEmpty(in.getItemIds())) {
            List<ProductItem> productItems = productItemRepository.gets(Convert.toList(Long.class, in.getItemIds()));

            if (CollectionUtil.isNotEmpty(productItems)) {
                List<Long> productIds = productItems.stream().map(ProductItem::getProductId).distinct().collect(Collectors.toList());
                wrapper.in("product_id", productIds);
            }
        }

        if (in.getStoreId() != null) {
            wrapper.eq("store_id", in.getStoreId());
        } else {
            ContextUser loginUser = ContextUtil.getLoginUser();

            if (loginUser != null) {
                Integer storeId = loginUser.getStoreId();

                if (CheckUtil.isNotEmpty(storeId) && loginUser.isStore()) {
                    if (ContextUtil.isManage()) {
                        wrapper.eq("store_id", storeId);
                    } else {
                        wrapper.ne("store_id", storeId);
                    }
                }
            }
        }

        if (CollectionUtil.isNotEmpty(in.getProductIds())) {
            wrapper.in("product_id", in.getProductIds());
        }

        wrapper.ne("kind_id", StateCode.PRODUCT_KIND_EDU);
        wrapper.orderByAsc("product_order");
        wrapper.orderByDesc("product_id");

        //处理辅助属性assist
        if (CheckUtil.isNotEmpty(in.getAssist())) {
            Map<String, List<Integer>> productAssistMap = new HashMap<>();

            // 创建ObjectMapper对象
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                productAssistMap = objectMapper.readValue(in.getAssist(), new TypeReference<Map<String, List<Integer>>>() {
                });
            } catch (Exception e) {
                throw new BusinessException(e.getMessage());
            }

            productAssistMap.forEach((assistId, assistItemIdList) -> {
                //AND 关系添加查询条件
                if (assistItemIdList.size() > 0) {
                    CheckUtil.handleFindInSet(assistItemIdList, "product_assist_data", wrapper);
                }
            });
        }

    }

    // 查询商品列表
    private ProductListRes queryProductPage(ProductIndexInput in) {

        ProductListRes output = new ProductListRes();

        QueryWrapper<ProductIndex> wrapper =
                new BaseQueryWrapper<ProductIndex, ProductIndexInput>(in).getWrapper();

        getQueryWrapper(in, wrapper);

        IPage<ProductIndex> lists = lists(wrapper, in.getPage(), in.getSize());

        output.setRecords(Convert.toInt(lists.getTotal()));
        output.setSize(Convert.toInt(lists.getSize()));
        output.setPage(Convert.toInt(lists.getCurrent()));
        output.setTotal(Convert.toInt(lists.getPages()));

        List<ProductOutput> records =
                BeanUtil.copyToList(lists.getRecords(), ProductOutput.class);

        output.setItems(records);

        return output;
    }

    /**
     * 按商品列表中的店铺编号加载店铺表（用于自营等与店铺表同步）
     */
    private Map<Integer, StoreBase> loadStoreMap(List<ProductOutput> records) {

        Map<Integer, StoreBase> storeBaseMap = new HashMap<>();

        List<Integer> storeIds = CommonUtil.column(records, ProductOutput::getStoreId);

        if (CollUtil.isEmpty(storeIds)) {
            return storeBaseMap;
        }

        List<StoreBase> storeBases = storeBaseRepository.gets(storeIds);

        storeBaseMap = storeBases.stream()
                .collect(Collectors.toMap(StoreBase::getStoreId, Function.identity()));

        return storeBaseMap;
    }

    /**
     * 处理虚拟销量 product_virtual_salenum
     * 管理端返回真实销量，前台返回叠加虚拟销量。
     */
    private void applyVirtualSaleNum(List<ProductOutput> records) {
        if (CollUtil.isEmpty(records)) {
            return;
        }

        if (ContextUtil.isManage()) {
            return;
        }

        Integer productVirtualSalenum = configBaseService.getConfig("product_virtual_salenum", 0);
        if (ObjectUtil.isEmpty(productVirtualSalenum) || productVirtualSalenum <= 0) {
            return;
        }

        for (ProductOutput vo : records) {
            if (vo == null || vo.getProductId() == null) {
                continue;
            }

            int addNumId = CheckUtil.bkdrHash(Convert.toStr(vo.getProductId()));
            int addNum = addNumId % productVirtualSalenum;
            Integer saleNum = ObjectUtil.defaultIfNull(vo.getProductSaleNum(), 0);
            vo.setProductSaleNum(saleNum + addNum);
        }
    }

    @Override
    public ProductListRes listItem(ProductIndexInput in) {
        // 1. 查询商品分页数据
        ProductListRes output = queryProductPage(in);
        List<ProductOutput> records = output.getItems();

        if (CollUtil.isEmpty(records)) {
            return output;
        }

        List<Long> productIds = CommonUtil.column(records, ProductOutput::getProductId);

        if (CollUtil.isNotEmpty(productIds)) {
            //基础表数据
            List<ProductBase> productBases = productBaseRepository.gets(productIds);
            List<ProductInfo> productInfos = productInfoRepository.gets(productIds);

            // 获取店铺信息
            Map<Integer, StoreBase> storeBaseMap = loadStoreMap(records);

            /*
            //读取图片
            List<ProductImage> productImages = productImageRepository.find(new QueryWrapper<ProductImage>().in("product_id", productIds));

            //处理为Map
            Map<Long, String> imageMap = new HashMap<>();

            for (ProductImage image : productImages) {
                if (!imageMap.containsKey(image.getColorId())) {
                    imageMap.put(image.getColorId(), image.getItemImageDefault());
                }
            }
             */

            //读取SKU
            List<ProductItem> productItems = productItemRepository.find(new QueryWrapper<ProductItem>().in("product_id", productIds).orderByAsc("item_id"));


            // 处理为map
            Map<Long, List<ProductItem>> itemMap = new HashMap<>();
            Map<Long, Long> defaultItemMap = new HashMap<>();

            if (CollUtil.isNotEmpty(productItems)) {

                for (ProductItem item : productItems) {
                    //
                    if (!itemMap.containsKey(item.getProductId())) {
                        itemMap.put(item.getProductId(), new ArrayList<>());
                    }

                    itemMap.get(item.getProductId()).add(item);

                    //默认item
                    if (item.getItemIsDefault()) {
                        if (!defaultItemMap.containsKey(item.getProductId())) {
                            defaultItemMap.put(item.getProductId(), item.getItemId());
                        }
                    }
                }
            }

            for (ProductOutput vo : records) {

                // 是否自营：store_id=0 默认自营；否则以店铺表为准
                Integer storeId = vo.getStoreId();
                if (CheckUtil.isNotEmpty(storeId)) {
                    StoreBase storeBase = storeBaseMap.get(storeId);
                    if (storeBase != null) {
                        vo.setStoreIsSelfsupport(Boolean.TRUE.equals(storeBase.getStoreIsSelfsupport()));
                    }
                } else {
                    vo.setStoreIsSelfsupport(true);
                }

                for (ProductBase item : productBases) {
                    if (vo.getProductId().equals(item.getProductId())) {
                        vo.setProductTips(item.getProductTips());
                        vo.setProductImage(item.getProductImage());
                        vo.setProductVideo(item.getProductVideo());
                        vo.setTransportTypeId(item.getTransportTypeId());
                        vo.setProductBuyLimit(item.getProductBuyLimit());
                        vo.setProductCommissionRate(item.getProductCommissionRate());
                    }
                }

                for (ProductInfo info : productInfos) {
                    if (vo.getProductId().equals(info.getProductId())) {
                        vo.setProductSpec(info.getProductSpec());
                        vo.setProductUniqid(info.getProductUniqid());
                    }
                }

                //默认
                //默认商品为下架状态 改为下个上架商品
                Long defaultItemId = defaultItemMap.get(vo.getProductId());
                List<ProductItem> productItemList = itemMap.get(vo.getProductId());

                if (CollectionUtil.isNotEmpty(productItemList)) {
                    Long finalDefaultItemId = defaultItemId;
                    Optional<ProductItem> firstItemOptional = productItemList.stream()
                            .filter(item -> item.getItemId().equals(finalDefaultItemId))
                            .findFirst();

                    if (firstItemOptional.isPresent()) {
                        ProductItem productItem = firstItemOptional.get();

                        if (!Objects.equals(productItem.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL)) {
                            for (ProductItem item : productItemList) {

                                if (Objects.equals(item.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL)) {
                                    defaultItemId = item.getItemId();
                                    vo.setItemPlusDiscountPrice(item.getItemPlusDiscountPrice());
                                    vo.setItemPlusPrice(item.getItemPlusPrice());
                                    vo.setItemPolicyPrice(item.getItemPolicyPrice());
                                    vo.setItemSalePrice(item.getItemSalePrice());
                                    vo.setItemSavePrice(item.getItemSavePrice());
                                    vo.setItemUnitPrice(item.getItemUnitPrice());

                                    break;
                                }
                            }
                        }
                    }
                }

                vo.setItemId(defaultItemId);
                vo.setItems(productItemList);
            }

            List<Long> itemIds = records.stream().map(ProductOutput::getItemId).distinct().collect(Collectors.toList());
            Map<Long, BigDecimal> pricingPolicyMap = new HashMap<>();
            Map<Long, BigDecimal> itemPriceMap = new HashMap<>();
            //活动价格
            QueryWrapper<ActivityItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.in("item_id", itemIds)
                    .eq("activity_item_state", StateCode.ACTIVITY_STATE_NORMAL);
            List<ActivityItem> activityItemList = activityItemService.find(itemQueryWrapper);

            if (CollectionUtil.isNotEmpty(activityItemList)) {
                itemPriceMap = activityItemList.stream().collect(Collectors.toMap(ActivityItem::getItemId, ActivityItem::getActivityItemPrice, (k1, k2) -> k1));
            }

            boolean productPricingPolicyFlag = configBaseService.getConfig("product_pricing_policy", false);
            Integer userId = ContextUtil.getLoginUserId();

            if (productPricingPolicyFlag && CheckUtil.isNotEmpty(userId)) {
                QueryWrapper<ProductPricingPolicy> policyQueryWrapper = new QueryWrapper<>();
                policyQueryWrapper.in("item_id", itemIds);
                policyQueryWrapper.eq("user_id", userId);
                policyQueryWrapper.eq("policy_enable", true);
                List<ProductPricingPolicy> pricingPolicyList = productPricingPolicyRepository.find(policyQueryWrapper);

                if (CollectionUtil.isNotEmpty(pricingPolicyList)) {
                    pricingPolicyMap = pricingPolicyList.stream().collect(Collectors.toMap(ProductPricingPolicy::getItemId, ProductPricingPolicy::getPolicyPrice, (k1, k2) -> k1));
                }
            }

            for (ProductOutput productOutput : records) {
                Long itemId = productOutput.getItemId();

                if (itemPriceMap.containsKey(itemId)) {
                    productOutput.setProductUnitPriceMin(itemPriceMap.get(itemId));
                }

                if (pricingPolicyMap.containsKey(itemId)) {
                    productOutput.setItemPolicyPrice(pricingPolicyMap.get(itemId));
                }
            }
        }

        //判断是否固定分类读取数据
        /*
        if (CheckUtil.isNotEmpty(in.getCategoryId())) {
            List<Integer> ids = Convert.toList(Integer.class, in.getCategoryId());
            if (ids.size() == 1) {
                Integer categoryId = ids.get(0);

                if (CheckUtil.isNotEmpty(categoryId)) {
                    ProductCategory productCategory = productCategoryRepository.get(categoryId);

                    if (ObjectUtil.isNotEmpty(productCategory)) {
                        ProductType productType = productTypeRepository.get(productCategory.getTypeId());

                        if (ObjectUtil.isNotEmpty(productType)) {
                            List<ProductAssistOutput> assists = productAssistRepository.getAssists(productType.getAssistIds());
                            output.setAssists(assists);
                        }
                    }
                }
            }
        }
         */

        applyVirtualSaleNum(records);

        return output;
    }

    @Override
    public ProductDetailRes detail(Long itemId) {
        return detail(itemId, null, null);
    }

    /**
     * 商品详情
     *
     * @param input
     * @return
     */
    @Override
    public ProductDetailRes detail(ProductDetailInput input) {
        ProductDetailRes out = new ProductDetailRes();
        Long itemId = input.getItemId();
        Integer districtId = input.getDistrictId();
        Integer gbId = input.getGbId();
        Integer loginUserId = input.getUserId();

        out.setItemId(itemId);

        // 用 getItems 统一价格计算
        List<ProductItemVo> itemVos = productBaseRepository.getItems(
                Convert.toList(Long.class, itemId),
                loginUserId
        );

        if (CollUtil.isEmpty(itemVos)) {
            throw new BusinessException(__("商品SKU不存在或不可用！"));
        }

        ProductItemVo itemVo = itemVos.get(0);
        if (itemVo == null) {
            throw new BusinessException(__("商品SKU不存在或不可用！"));
        }
        out.setItemRow(itemVo);

        Long productId = itemVo.getProductId();
        ProductIndex productIndex = get(productId);
        if (productIndex == null) {
            throw new BusinessException(__("产品索引信息不存在！"));
        }

        if (!Objects.equals(productIndex.getProductVerifyId(), StateCode.PRODUCT_VERIFY_PASSED)) {
            throw new BusinessException(__("商品审核未通过！"));
        }

        ProductBase productBase = productBaseRepository.get(productId);
        if (productBase == null) {
            throw new BusinessException(__("商品基础信息不存在！"));
        }

        ProductInfo productInfo = productInfoRepository.get(productId);
        if (productInfo == null) {
            throw new BusinessException(__("商品信息不存在！"));
        }

        // 处理虚拟销量（对齐 golershop：详情场景直接叠加配置虚拟销量）
        Integer productVirtualSalenum = configBaseService.getConfig("product_virtual_salenum", 0);
        if (ObjectUtil.isNotEmpty(productVirtualSalenum) && productVirtualSalenum > 0) {
            int addNumId = CheckUtil.bkdrHash(Convert.toStr(productIndex.getProductId()));
            int addNum = addNumId % productVirtualSalenum;
            Integer saleNum = ObjectUtil.defaultIfNull(productIndex.getProductSaleNum(), 0);
            productIndex.setProductSaleNum(saleNum + addNum);
        }

        BeanUtils.copyProperties(productIndex, out);
        BeanUtils.copyProperties(productBase, out);
        BeanUtils.copyProperties(productInfo, out);

        if (StrUtil.isNotEmpty(productBase.getProductFile())) {
            out.setProductFiles(Convert.toList(String.class, productBase.getProductFile()));
        }

        if (CheckUtil.isNotEmpty(productIndex.getBrandId())) {
            ProductBrand productBrand = productBrandRepository.get(productIndex.getBrandId());
            if (productBrand != null) {
                out.setBrandName(productBrand.getBrandName());
            }
        }

        //SKU图片
        ProductImage image = productImageRepository.findOne(
                new QueryWrapper<ProductImage>().eq("product_id", itemVo.getProductId()).
                        eq("color_id", itemVo.getColorId())
        );
        out.setImage(image);
        out.setProductImage(image.getItemImageDefault());

        out.setProductItemName(itemVo.getProductItemName());

        //是否可销售
        if (itemVo.getAvailableQuantity() > 0) {
            out.setIfStore(true);

            //可售区域
            if (CheckUtil.isNotEmpty(districtId)) {

                /* //商品也，默认三级分类
                if (CheckUtil.isNotEmpty(districtId)) {
                    // 读取上级分类信息
                    DistrictBase districtBase = districtBaseRepository.get(districtId);

                    if (districtBase != null) {
                        districtId = districtBase.getDistrictParentId();
                    }
                } */

                StoreTransportItemVo storeTransportItemVo = storeTransportTypeRepository.getFreight(productBase.getTransportTypeId(), districtId);

                if (storeTransportItemVo == null) {
                    out.setIfStore(false);
                } else {
                    if (!storeTransportItemVo.getTransportTypeFree() && ObjectUtil.isEmpty(storeTransportItemVo.getItem())) {
                        out.setIfStore(false);
                    }
                    StoreTransportItem transportItem = storeTransportItemVo.getItem();

                    if (transportItem != null) {
                        out.setFreight(transportItem.getTransportItemDefaultPrice());
                    }
                }
            }
        } else {
            out.setIfStore(false);
        }

        Integer categoryId = out.getCategoryId();
        if (ObjectUtil.isNotNull(categoryId)) {
            // 读取上级分类信息
            List<ProductCategory> parentCategoryListById = productCategoryRepository.getParentCategory(categoryId);
            out.setProductCategorys(parentCategoryListById);
        }

        //服务
        QueryWrapper<ContractType> contractTypeQueryWrapper = new QueryWrapper<>();
        contractTypeQueryWrapper.eq("contract_type_enable", true);
        contractTypeQueryWrapper.orderByAsc("contract_type_order");
        List<ContractType> contractTypes = contractTypeRepository.find(contractTypeQueryWrapper);

        if (CollectionUtil.isNotEmpty(contractTypes)) {
            out.setContracts(contractTypes);
        }

        // 商品评论
        QueryWrapper<ProductComment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("product_id", productId).eq("comment_enable", 1).orderByDesc("comment_id");
        List<ProductComment> productCommentList = productCommentRepository.lists(commentQueryWrapper, 1, 5).getRecords();
        if (CollUtil.isNotEmpty(productCommentList)) {

            List<Integer> userIds = productCommentList.stream().map(ProductComment::getUserId).distinct().collect(Collectors.toList());
            List<UserInfo> userInfos = CollUtil.emptyIfNull(userInfoRepository.gets(userIds));
            productCommentList.forEach(comment -> {
                comment.setCommentContent(StrUtil.isEmpty(comment.getCommentContent()) ? "无评论" : comment.getCommentContent());
                userInfos.stream()
                        .filter(userInfo -> userInfo != null && userInfo.getUserId() != null && userInfo.getUserId().equals(comment.getUserId()))
                        .findFirst()
                        .ifPresent(userInfo -> comment.setUserAvatar(userInfo.getUserAvatar()));
                String commentImage = comment.getCommentImage();
                if (StrUtil.isNotEmpty(commentImage)) {
                    String replaceImg = commentImage.replace("[", "").replace("]", "");
                    comment.setCommentImages(Convert.toList(String.class, replaceImg));
                }
            });

            out.setLastComments(productCommentList);
            out.setLastComment(productCommentList.get(0));
        }

        //辅助属性读取
        List<Integer> productAssistIds = Convert.toList(Integer.class, out.getProductAssistData());
        List<ProductAssistItem> productAssistItemList = productAssistItemService.gets(productAssistIds);

        List<Integer> assistIds = CommonUtil.column(productAssistItemList, ProductAssistItem::getAssistId);
        List<ProductAssist> assistList = productAssistService.gets(assistIds);
        List<Map<String, Object>> productAssistMapList = new ArrayList<>();
        for (ProductAssist assist : assistList) {
            Map<String, Object> productAssistMap = new HashMap<>();
            productAssistMap.put("assist_id", assist.getAssistId());
            productAssistMap.put("assist_name", assist.getAssistName());
            List<Map<String, Object>> assistRow = new ArrayList<>();
            productAssistMap.put("assist_items", assistRow);
            for (ProductAssistItem productAssistItem : productAssistItemList) {
                if (productAssistItem.getAssistId().equals(assist.getAssistId())) {
                    Map<String, Object> rowMap = new HashMap<>();
                    rowMap.put("assist_item_id", productAssistItem.getAssistItemId());
                    rowMap.put("assist_item_name", productAssistItem.getAssistItemName());
                    assistRow.add(rowMap);
                }
            }
            productAssistMapList.add(productAssistMap);
        }
        out.setProductAssist(productAssistMapList);

        return out;
    }

    /**
     * 商品详情
     *
     * @param itemId
     * @param districtId
     * @param gbId
     * @return
     */
    @Override
    public ProductDetailRes detail(Long itemId, Integer districtId, Integer gbId) {
        ProductDetailInput input = new ProductDetailInput();
        input.setItemId(itemId);
        input.setDistrictId(districtId);
        input.setGbId(gbId);

        return detail(input);
    }

    /**
     * 商品活动信息
     *
     * @param itemId
     * @return
     */
    @Override
    public ActivityInfoRes getActivityInfo(Long itemId) {
        //读取活动信息
        List<ActivityInfoVo> activityInfoVoList = activityItemService.getActivityInfo(Convert.toList(Long.class, itemId), null);
        if (CollUtil.isNotEmpty(activityInfoVoList)) {
            ActivityInfoVo activityInfoVo = activityInfoVoList.get(0);
        }

        ActivityInfoRes res = new ActivityInfoRes();
        res.setItems(activityInfoVoList);

        return res;
    }

    @Override
    public ItemListRes listItem(ProductItemInput productItemListReq) {
        ItemListRes output = new ItemListRes();

        //参加活动 产品及数量 - 活动信息使用
        Map<Long, ItemNumVo> itemNumVoMap = new HashMap<>();
        if (CheckUtil.isNotEmpty(productItemListReq.getActivityId())) {
            ActivityBase activityBase = activityBaseRepository.get(productItemListReq.getActivityId());

            if (activityBase != null) {
                String activityRule = activityBase.getActivityRule();

                if (StrUtil.isNotEmpty(activityRule)) {
                    itemNumVoMap = activityBaseRepository.getActivityItemNum(activityBase);
                }
                Integer activityTypeId = activityBase.getActivityTypeId();
                String rule = activityBase.getActivityRule();

                if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_GIFT)
                        || Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_REDUCTION)
                        || Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_MANHUI)) {
                    QueryWrapper<ActivityItem> activityItemQueryWrapper = new QueryWrapper<>();
                    activityItemQueryWrapper.eq("activity_id", activityBase.getActivityId());
                    List<ActivityItem> activityItemList = activityItemRepository.find(activityItemQueryWrapper);

                    if (CollectionUtil.isNotEmpty(activityItemList)) {
                        productItemListReq.setItemId(CommonUtil.column(activityItemList, ActivityItem::getItemId));
                    }

                    if (StrUtil.isNotEmpty(rule)) {
                        ActivityRuleVo activityRuleVo = JSONUtil.parseObject(rule, ActivityRuleVo.class);

                        if (activityRuleVo != null) {
                            List<RuleVo> ruleVos = activityRuleVo.getRule();

                            if (CollectionUtil.isNotEmpty(ruleVos)) {
                                if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_GIFT)) {
                                    for (RuleVo ruleVo : ruleVos) {
                                        List<ItemNumVo> item = ruleVo.getItem();

                                        if (CollectionUtil.isNotEmpty(item)) {
                                            List<ProductItemVo> productItemVos = productBaseRepository.getItems(CommonUtil.column(item, ItemNumVo::getItemId), null);

                                            if (CollectionUtil.isEmpty(productItemVos)) {
                                                throw new BusinessException(__("商品SKU集合为空！"));
                                            }
                                            Map<Long, String> nameMap = productItemVos.stream().collect(Collectors.toMap(ProductItemVo::getItemId, ProductItemVo::getProductItemName, (k1, k2) -> k1));
                                            for (ItemNumVo itemNumVo : item) {
                                                itemNumVo.setProductItemName(nameMap.get(itemNumVo.getItemId()));
                                            }
                                        }
                                    }
                                } else if (Objects.equals(activityTypeId, StateCode.ACTIVITY_TYPE_MANHUI)) {
                                    for (RuleVo ruleVo : ruleVos) {
                                        Integer maxNum = ruleVo.getMaxNum();
                                        ActivityBase giveActivity = activityBaseRepository.get(maxNum);

                                        if (giveActivity == null) {
                                            throw new BusinessException(__("满返优惠券不存在！"));
                                        }
                                        ruleVo.setGiveVoucherName(giveActivity.getActivityName());
                                        String giveRule = giveActivity.getActivityRule();

                                        if (StrUtil.isNotEmpty(giveRule)) {
                                            ActivityRuleVo giveRuleVo = JSONUtil.parseObject(giveRule, ActivityRuleVo.class);

                                            if (giveRuleVo != null) {
                                                RequirementVo requirement = giveRuleVo.getRequirement();

                                                if (requirement != null) {
                                                    BuyVo buy = requirement.getBuy();

                                                    if (buy != null) {
                                                        ruleVo.setGiveVoucherSubtotal(buy.getSubtotal());
                                                    }
                                                }
                                                VoucherVo voucher = giveRuleVo.getVoucher();

                                                if (voucher != null) {
                                                    ruleVo.setGiveVoucherPrice(voucher.getVoucherPrice());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            output.setActivityRuleJson(activityRuleVo);
                        }
                    }
                } else {
                    if (StrUtil.isNotEmpty(rule)) {
                        ActivityRuleVo activityRuleVo = JSONUtil.parseObject(rule, ActivityRuleVo.class);
                        output.setActivityRuleJson(activityRuleVo);
                    }

                    productItemListReq.setItemId(Convert.toList(Long.class, activityBase.getActivityItemIds()));
                }
                output.setActivityBase(activityBase);
            }
        }

        if (productItemListReq.getItemId() != null && productItemListReq.getItemId().isEmpty()) {
            productItemListReq.setItemId(null);
        }

        // 防止sql注入
        if (StrUtil.isNotBlank(productItemListReq.getSidx())) {
            String sidx = productItemListReq.getSidx();
            sidx = sidx.replace("`", "");

            if (CheckUtil.hasField(ProductItem.class, sidx)) {
                productItemListReq.setSidx(sidx);

                if ("DESC".equals(productItemListReq.getSort())) {
                    productItemListReq.setSort("DESC");
                } else {
                    productItemListReq.setSort("ASC");
                }
            } else {
                productItemListReq.setSidx("");
                productItemListReq.setSort("DESC");
            }
        }

        IPage<Long> lists = productItemRepository.listItemKey(new Page<>(productItemListReq.getPage(), productItemListReq.getSize()), productItemListReq);
        List<Long> itemIds = lists.getRecords();

        output.setRecords(Convert.toInt(lists.getTotal()));
        output.setSize(Convert.toInt(lists.getSize()));
        output.setPage(Convert.toInt(lists.getCurrent()));
        output.setTotal(Convert.toInt(lists.getPages()));

        List<ItemOutput> records = new ArrayList<>();
        output.setItems(records);

        if (CollUtil.isNotEmpty(itemIds)) {
            List<ProductItem> productItems = productItemRepository.gets(itemIds);
            List<Long> productIds = CommonUtil.column(productItems, ProductItem::getProductId);

            records = BeanUtil.copyToList(productItems, ItemOutput.class);
            output.setItems(records);

            //SKU图片
            List<ProductImage> productImages = productImageRepository.find(new QueryWrapper<ProductImage>().in("product_id", productIds));

            //基础表数据
            List<ProductBase> productBases = productBaseRepository.gets(productIds);

            //产品状态
            List<ProductIndex> productIndices = productIndexRepository.gets(productIds);
            Map<Long, ProductIndex> productIndexMap = new HashMap<>();

            if (CollectionUtil.isNotEmpty(productIndices)) {
                productIndexMap = productIndices.stream().collect(Collectors.toMap(ProductIndex::getProductId, ProductIndex -> ProductIndex, (k1, k2) -> k1));
            }

            for (ItemOutput vo : records) {
                //ProductImage productImage = productImages.stream().filter(s -> {return s.getProductId().equals(vo.getProductId()) && s.getColorId().equals(vo.getColorId());}).findFirst().orElse(new ProductImage());
                ProductImage productImage = productImages.stream().filter(s -> {
                    return s.getProductId().equals(vo.getProductId()) && s.getColorId().equals(vo.getColorId());
                }).findFirst().orElse(new ProductImage());

                for (ProductBase base : productBases) {
                    if (vo.getProductId().equals(base.getProductId())) {
                        String itemName = vo.getItemName();
                        String itemSpecName = StrUtil.replaceChars(itemName, ",", " ");

                        String productItemName = base.getProductName() + " " + itemSpecName;
                        vo.setItemSpecName(productItemName);
                        vo.setProductItemName(productItemName);

                        vo.setProductName(base.getProductName());
                        vo.setProductTips(base.getProductTips());
                        vo.setProductVideo(base.getProductVideo());
                        vo.setTransportTypeId(base.getTransportTypeId());
                        vo.setProductBuyLimit(base.getProductBuyLimit());

                        vo.setProductImage(base.getProductImage());
                        if (ObjectUtil.isNotEmpty(productImage.getItemImageDefault())) {
                            vo.setProductImage(productImage.getItemImageDefault());
                        }

                        //商品状态
                        if (!productIndexMap.isEmpty()) {
                            ProductIndex productIndex = productIndexMap.get(base.getProductId());

                            if (productIndex != null) {
                                vo.setProductStateId(productIndex.getProductStateId());
                            }
                        }
                    }
                }
                //活动产品数量
                if (CollUtil.isNotEmpty(itemNumVoMap)) {
                    ItemNumVo itemNumVo = itemNumVoMap.get(vo.getItemId());

                    if (itemNumVo != null) {
                        vo.setActivityItemNum(itemNumVo.getNum());
                    }
                }

            }
        }

        return output;
    }

    @Override
    public void autoSaleProduct() {
        QueryWrapper<ProductIndex> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_verify_id", StateCode.PRODUCT_VERIFY_PASSED);
        queryWrapper.eq("product_state_id", StateCode.PRODUCT_STATE_OFF_THE_SHELF);
        queryWrapper.le("product_sale_time", new Date().getTime());
        List<ProductIndex> productIndices = productIndexRepository.find(queryWrapper);

        if (CollectionUtil.isNotEmpty(productIndices)) {
            List<Long> productIds = CommonUtil.column(productIndices, ProductIndex::getProductId);

            QueryWrapper<ProductItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.in("product_id", productIds);
            List<ProductItem> productItems = productItemRepository.find(itemQueryWrapper);

            if (CollectionUtil.isEmpty(productItems)) {
                throw new BusinessException(__("商品SKU集合为空！"));
            }
            List<ProductIndex> indices = new ArrayList<>();

            for (ProductIndex productIndex : productIndices) {
                Long productId = productIndex.getProductId();
                List<ProductItem> productItemList = productItems.stream().filter(item -> item.getProductId().equals(productId)).collect(Collectors.toList());

                if (CollectionUtil.isEmpty(productItemList)) {
                    throw new BusinessException(String.format(__("商品编号: %s ,SKU数据为空！"), productId));
                }
                boolean itemEnable = productItemList.stream()
                        .anyMatch(item -> ObjectUtil.equal(item.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL));

                if (!itemEnable) {
                    continue;
                }

                ProductIndex editIndex = new ProductIndex();
                editIndex.setProductId(productId);
                editIndex.setProductStateId(StateCode.PRODUCT_STATE_NORMAL);
                editIndex.setProductSaleTime(new Date().getTime());
                editIndex.setProductEvaluationNum(productIndex.getProductEvaluationNum());
                indices.add(editIndex);
            }

            if (CollectionUtil.isNotEmpty(indices)) {
                if (!productIndexRepository.saveOrUpdate(indices)) {
                    throw new BusinessException(__("商品定时上架失败！"));
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean editVerify(ProductIndex productIndex) {
        ProductIndex product = get(productIndex.getProductId());

        if (product == null) {
            throw new BusinessException(__("该商品不存在！"));
        }
        productIndex.setProductStateId(StateCode.PRODUCT_STATE_OFF_THE_SHELF);

        if (Objects.equals(productIndex.getProductVerifyId(), StateCode.PRODUCT_VERIFY_PASSED)) {
            long nowTime = new Date().getTime();

            if (product.getProductSaleTime() <= nowTime) {
                QueryWrapper<ProductItem> productItemQueryWrapper = new QueryWrapper<>();
                productItemQueryWrapper.eq("product_id", productIndex.getProductId());
                List<ProductItem> productItems = productItemRepository.find(productItemQueryWrapper);

                if (CollectionUtil.isNotEmpty(productItems)) {
                    boolean itemEnable = productItems.stream()
                            .anyMatch(item -> ObjectUtil.equal(item.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL));

                    if (itemEnable) {
                        productIndex.setProductStateId(StateCode.PRODUCT_STATE_NORMAL);
                    }
                }
            }
        }

        if (!edit(productIndex)) {
            throw new BusinessException(__("修改商品信息失败！"));
        }

        if (CheckUtil.isNotEmpty(product.getStoreId())) {
            if (!storeAnalyticsService.saveProductAnalyticsNum(product.getStoreId())) {
                throw new BusinessException(__("更新商品数量统计失败！"));
            }

            QueryWrapper<UserAdmin> userAdminQueryWrapper = new QueryWrapper<>();
            userAdminQueryWrapper.eq("store_id", product.getStoreId());
            UserAdmin userAdmin = userAdminRepository.findOne(userAdminQueryWrapper);

            if (userAdmin == null) {
                throw new BusinessException(__("店铺用户信息不存在！"));
            }
            //消息通知
            String messageId = "commodity-audit-remind";
            Map<String, Object> args = new HashMap<>();
            args.put("des", StrUtil.isNotEmpty(productIndex.getProductVerifyRemark()) ? productIndex.getProductVerifyRemark() : "无");
            args.put("product_id", productIndex.getProductId());
            args.put("passed", productIndex.getProductVerifyId() == StateCode.PRODUCT_VERIFY_REFUSED ? "没有" : "");

            messageTemplateService.send(userAdmin.getUserId(), messageId, args);
        }

        return true;
    }

    private List<ProductItemVo> loadProductItems(List<Long> productIds) {

        if (CollUtil.isEmpty(productIds)) {
            return new ArrayList<>();
        }

        // 1. 查找可用商品 并 排序
        List<ProductItem> productItems = productItemRepository.find(
                new QueryWrapper<ProductItem>()
                        .in("product_id", productIds)
                        .eq("item_enable", StateCode.PRODUCT_STATE_NORMAL)
                        .gt("item_quantity", 0)
                        .orderByDesc("item_is_default")
                        .orderByAsc("item_id")
        );

        if (CollUtil.isEmpty(productItems)) {
            return new ArrayList<>();
        }

        // 2. 每个商品取第一个
        Map<Long, ProductItem> availableItemMap = new HashMap<>();

        for (ProductItem item : productItems) {
            availableItemMap.putIfAbsent(item.getProductId(), item);
        }

        // 3. 只计算必要 SKU
        List<Long> itemIds = availableItemMap.values().stream()
                .map(ProductItem::getItemId)
                .collect(Collectors.toList());

        Integer userId = ContextUtil.getLoginUserId();

        return productBaseRepository.getItems(itemIds, userId);
    }

    private void fillProductOutput(
            List<ProductOutput> records,
            Map<Integer, StoreBase> storeBaseMap,
            Map<Long, ProductItemVo> availableItemMap, Map<Long, ProductInfo> productInfoMap) {

        for (ProductOutput vo : records) {

            // 是否自营：store_id=0 默认自营；否则以店铺表为准
            Integer sid = vo.getStoreId();
            if (CheckUtil.isNotEmpty(sid)) {
                if (CollUtil.isNotEmpty(storeBaseMap)) {
                    StoreBase storeBase = storeBaseMap.get(sid);
                    if (storeBase != null) {
                        vo.setStoreIsSelfsupport(Boolean.TRUE.equals(storeBase.getStoreIsSelfsupport()));
                    }
                }
            } else {
                vo.setStoreIsSelfsupport(true);
            }

            // 可用SKU
            ProductItemVo item = availableItemMap.get(vo.getProductId());

            if (item != null) {
                vo.setItemId(item.getItemId());
                vo.setProductName(item.getProductName());
                vo.setProductImage(item.getProductImage());
                vo.setItemUnitPrice(item.getItemUnitPrice());
                vo.setItemPolicyPrice(item.getItemPolicyPrice());
                vo.setItemSalePrice(item.getItemSalePrice());
                vo.setItemPlusPrice(item.getItemPlusPrice());
                vo.setItemPlusDiscountPrice(item.getItemPlusDiscountPrice());
                vo.setItemSavePrice(item.getItemSavePrice());
                vo.setPriceTag(item.getPriceTag());
                vo.setPriceType(item.getPriceType());
                vo.setItemAvailableQuantity(item.getAvailableQuantity());
                vo.setItemSpec(item.getItemSpec());

                ProductInfo productInfo = productInfoMap.get(vo.getProductId());
                if (productInfo != null) {
                    vo.setProductSpec(productInfo.getProductSpec());
                    vo.setProductUniqid(productInfo.getProductUniqid());
                }
            }
        }
    }

    @Override
    public ProductListRes listProduct(ProductIndexInput in) {

        // 1. 查询商品分页
        ProductListRes output = queryProductPage(in);
        List<ProductOutput> records = output.getItems();

        if (CollUtil.isEmpty(records)) {
            return output;
        }

        // 2. 商品ID
        List<Long> productIds = CommonUtil.column(records, ProductOutput::getProductId);

        // 3. 店铺
        Map<Integer, StoreBase> storeBaseMap = loadStoreMap(records);

        // 4. SKU
        List<ProductItemVo> productItemVos = loadProductItems(productIds);

        // 5. 直接转 Map
        Map<Long, ProductItemVo> availableItemMap = productItemVos.stream()
                .collect(Collectors.toMap(
                        ProductItemVo::getProductId,
                        Function.identity(),
                        (a, b) -> a
                ));

        // 6. 商品信息
        List<ProductInfo> productInfos = productInfoRepository.gets(productIds);
        // 转成 Map（key: product_id）
        Map<Long, ProductInfo> productInfoMap = productInfos.stream()
                .collect(Collectors.toMap(
                        ProductInfo::getProductId,
                        v -> v,
                        (k1, k2) -> k1 // 防止重复key
                ));

        // 7. 填充商品数据
        fillProductOutput(records, storeBaseMap, availableItemMap, productInfoMap);

        applyVirtualSaleNum(records);

        return output;
    }

    // 获取当前商品SKU
    @Override
    public ProductItemVo skuItem(Long itemId, Integer userId) {
        // 用 getItems 统一价格计算
        List<ProductItemVo> itemVos = productBaseRepository.getItems(
                Convert.toList(Long.class, itemId),
                userId
        );

        if (CollUtil.isEmpty(itemVos)) {
            throw new BusinessException(__("商品SKU不存在或不可用！"));
        }

        ProductItemVo first = itemVos.get(0);
        if (first == null) {
            throw new BusinessException(__("商品SKU不存在或不可用！"));
        }
        return first;
    }

}
