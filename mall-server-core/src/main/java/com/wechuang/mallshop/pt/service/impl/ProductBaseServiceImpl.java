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


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.excel.EasyExcelUtil;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.common.web.service.MessageService;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.invoicing.model.entity.StockBillItem;
import com.wechuang.mallshop.invoicing.repository.StockBillItemRepository;
import com.wechuang.mallshop.pt.excel.ProductTemp;
import com.wechuang.mallshop.pt.excel.ProductTempListener;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.input.ProductSaveInput;
import com.wechuang.mallshop.pt.model.output.ProductDataOutput;
import com.wechuang.mallshop.pt.model.req.ProductBaseListReq;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.pt.service.ProductBaseService;
import com.wechuang.mallshop.pt.service.ProductItemService;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.entity.UserFavoritesItem;
import com.wechuang.mallshop.shop.model.entity.UserProductBrowse;
import com.wechuang.mallshop.shop.repository.UserFavoritesItemRepository;
import com.wechuang.mallshop.shop.repository.UserProductBrowseRepository;
import com.wechuang.mallshop.shop.repository.StoreBaseRepository;
import com.wechuang.mallshop.shop.service.StoreAnalyticsService;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.NumberSeqService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品基础表-SPU表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-03-13
 */
@Service
public class ProductBaseServiceImpl extends BaseServiceImpl<ProductBaseRepository, ProductBase, ProductBaseListReq> implements ProductBaseService {

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductValidPeriodRepository productValidPeriodRepository;

    @Autowired
    private NumberSeqService numberSeqService;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private StockBillItemRepository stockBillItemRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ProductItemService productItemService;

    @Autowired
    private ProductBaseService productBaseService;

    @Autowired
    private StoreAnalyticsService storeAnalyticsService;

    @Autowired
    private StoreBaseRepository storeBaseRepository;

    @Autowired
    private UserAdminService userAdminService;

    @Autowired
    private UserFavoritesItemRepository userFavoritesItemRepository;

    @Autowired
    private UserProductBrowseRepository userProductBrowseRepository;

    @Override
    @Transactional
    public boolean saveProduct(ProductSaveInput in) {
        boolean newAddFlag = false; //新增标记
        Long productId;

        ProductBase productBase = in.getProductBase();
        ProductIndex productIndex = in.getProductIndex();
        ProductInfo productInfo = in.getProductInfo();
        List<ProductItem> productItems = in.getProductItems();
        List<ProductImage> productImages = in.getProductImages();

        //商家身份强制归属本店,防止伪造store_id跨店建品;平台身份允许指定(缺省0=平台自营)
        ContextUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.isStore()) {
            productIndex.setStoreId(loginUser.getStoreId());
            productBase.setStoreId(loginUser.getStoreId());
        }

        // store_id=0 视为平台商品，默认自营；否则与店铺表 store_is_selfsupport 同步（对齐 golershop / SQL 约定）
        Integer storeId = productIndex.getStoreId();
        if (CheckUtil.isNotEmpty(storeId)) {
            StoreBase storeBase = storeBaseRepository.get(storeId);
            if (storeBase == null) {
                throw new BusinessException(__("店铺信息不存在！"));
            }
            productIndex.setStoreIsSelfsupport(storeBase.getStoreIsSelfsupport());
        } else {
            productIndex.setStoreIsSelfsupport(true);
        }

        if (CollectionUtil.isNotEmpty(productItems)) {
            List<String> numbers = productItems.stream().map(ProductItem::getItemNumber).filter(StrUtil::isNotEmpty).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(numbers)) {
                QueryWrapper<ProductItem> productItemQueryWrapper = new QueryWrapper<>();
                productItemQueryWrapper.in("item_number", numbers);

                if (CheckUtil.isNotEmpty(productIndex.getProductId())) {
                    productItemQueryWrapper.ne("product_id", productIndex.getProductId());
                }

                if (productItemRepository.count(productItemQueryWrapper) > 0) {
                    throw new BusinessException(__("商品编码重复！"));
                }
            }
        }

        //todo 过滤替换词汇

        //todo 是否需要审核

        //判断新增 or 修改
        if (CheckUtil.isEmpty(productBase.getProductId())) {
            newAddFlag = true;
            productId = numberSeqService.getNextSeqInt("product_id");

            //初始化默认值
            if (CheckUtil.isEmpty(productIndex.getProductStateId())) {
                productIndex.setProductStateId(StateCode.PRODUCT_STATE_NORMAL);
            }

            if (Objects.equals(productIndex.getProductStateId(), StateCode.PRODUCT_STATE_OFF_THE_SHELF)) {
                //in.ProductIndex.ProductSaleTime
            }

            //上架时间
            if (Objects.equals(productIndex.getProductStateId(), StateCode.PRODUCT_STATE_NORMAL)) {
                productIndex.setProductSaleTime(new Date().getTime());
            }

            productIndex.setProductSpEnable(false);//供应商是否允许批发市场分销
            productIndex.setProductDistEnable(true);//是否允许三级分销
            productIndex.setProductAddTime(new Date().getTime());
            productIndex.setProductFrom(1000);
        } else {
            productId = productBase.getProductId();

            ProductBase productBaseOld = get(productId);

            if (productBaseOld == null) {
                throw new BusinessException(__("商品基础信息不存在！"));
            }

            if (CheckUtil.isNotEmpty(productBase.getStoreId())) {

                if (!productBase.getStoreId().equals(productBaseOld.getStoreId())) {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            }
        }

        //默认商品设置判断
        boolean isSetDefault = false; //设置了默认SKU

        for (ProductItem v : productItems) {

            if (CheckUtil.isNotEmpty(productBase.getStoreId())) {
                v.setStoreId(productBase.getStoreId());
            }

            if (v.getItemIsDefault()) {
                isSetDefault = true;
                break;
            }
        }

        //如果未设置， 将第SKU设置为默认
        if (!isSetDefault) {
            productItems.get(0).setItemIsDefault(true);
        }

        //处理主图
        String productImage = "";

        //根据默认商品获取默认主图
        for (ProductItem productItem : productItems) {
            if (productItem.getItemIsDefault()) {
                isSetDefault = true;

                for (ProductImage image : productImages) {
                    if (image.getColorId().equals(productItem.getColorId())) {
                        //
                        if (CheckUtil.isEmpty(image.getItemImageDefault())) {
                            image.setItemImageDefault(configBaseService.getDefaultImage());
                        }

                        productImage = image.getItemImageDefault();
                        break;
                    }
                }

                break;
            }
        }

        //商品价格最大值及最小值
        BigDecimal productUnitPriceMin = new BigDecimal(-1);
        BigDecimal productUnitPriceMax = BigDecimal.ZERO;
        BigDecimal productUnitPointsMin = new BigDecimal(-1);
        BigDecimal productUnitPointsMax = BigDecimal.ZERO;


        for (ProductItem v : productItems) {
            if (CheckUtil.isEmpty(v.getItemUnitPrice())) {
                v.setItemUnitPrice(BigDecimal.ZERO);
            }

            //价格
            if (productUnitPriceMin.equals(new BigDecimal(-1))) {
                productUnitPriceMin = v.getItemUnitPrice();
            }

            if (v.getItemUnitPrice().compareTo(productUnitPriceMin) < 0) {
                productUnitPriceMin = v.getItemUnitPrice();
            }


            if (v.getItemUnitPrice().compareTo(productUnitPriceMax) > 0) {
                productUnitPriceMax = v.getItemUnitPrice();
            }

            //积分
            if (productUnitPointsMin.equals(new BigDecimal(-1))) {
                productUnitPointsMin = v.getItemUnitPoints();
            }

            if (v.getItemUnitPoints().compareTo(productUnitPointsMin) < 0) {
                productUnitPointsMin = v.getItemUnitPoints();
            }


            if (v.getItemUnitPoints().compareTo(productUnitPointsMax) > 0) {
                productUnitPointsMax = v.getItemUnitPoints();
            }

        }

        productIndex.setProductUnitPriceMin(productUnitPriceMin);
        productIndex.setProductUnitPriceMax(productUnitPriceMax);
        productIndex.setProductUnitPointsMin(productUnitPointsMin);
        productIndex.setProductUnitPointsMax(productUnitPointsMax);

        //初始化商品状态
        if (Objects.equals(productIndex.getProductStateId(), StateCode.PRODUCT_STATE_NORMAL)) {
            boolean itemEnable = productItems.stream()
                    .anyMatch(item -> ObjectUtil.equal(item.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL));

            if (!itemEnable) {
                productIndex.setProductStateId(StateCode.PRODUCT_STATE_OFF_THE_SHELF);
            }
        }

        //开启事务

        //商品基础表ProductBase
        productBase.setProductId(productId);
        productBase.setProductImage(productImage);

        boolean flag = repository.save(productBase);

        if (!flag) {
            throw new BusinessException(__("商品基数数据错误"));
        }

        //商品辅助属性 Product_AssistIndexModel
        //处理辅助属性
        Map<String, List<Integer>> productAssistMap = new HashMap<>();

        // 读取JSON字符串
        // 创建ObjectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            productAssistMap = objectMapper.readValue(productInfo.getProductAssist(), new TypeReference<Map<String, List<Integer>>>() {
            });
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        List<Integer> productAssistData = new ArrayList<>();

        productAssistMap.forEach((assistId, assistItemIdList) -> {
            //g.Log().Info(ctx, assistId, assistItemIdList)
            //System.out.print(assistId);
            //System.out.print(assistItemIdList);
            productAssistData.addAll(assistItemIdList);
        });

        //商品索引表
        //product_assist_data unit_price_min/max
        productIndex.setProductId(productId);

        boolean productVerifyFlag = configBaseService.getConfig("product_verify_flag", false);

        if (productVerifyFlag) {
            productIndex.setProductVerifyId(StateCode.PRODUCT_VERIFY_WAITING);
            productIndex.setProductStateId(StateCode.PRODUCT_STATE_OFF_THE_SHELF);
        } else {
            productIndex.setProductVerifyId(StateCode.PRODUCT_VERIFY_PASSED);
        }
        productIndex.setProductVerifyRemark("");
        productIndex.setProductStateRemark("");
        productIndex.setProductNameIndex(productIndex.getProductName());

        //商品状态 product_state_id 商品状态判断修正： 是否需要审核等
        ProductCategory productCategory = productCategoryRepository.get(productIndex.getCategoryId());
        productIndex.setTypeId(productCategory == null ? 0 : productCategory.getTypeId());
        productIndex.setProductAssistData(CollUtil.join(productAssistData, ","));
        productIndex.setIndustryIds(productCategory == null ? "" : productCategory.getIndustryIds());

        flag = productIndexRepository.save(productIndex);
        if (!flag) {
            throw new BusinessException(__("商品索引数据错误"));
        }

        //商品SKU Product_ItemModel
        //读取已经存在的SKU, 需要删除的记录
        List<ProductItem> oldProductItems = productItemRepository.find(new QueryWrapper<ProductItem>().eq("product_id", productId));
        //List<Serializable> itemIds = productItemRepository.findKey(new QueryWrapper<ProductItem>().eq("product_id", productId));
        List<Long> itemIds = CommonUtil.column(oldProductItems, ProductItem::getItemId);

        for (ProductItem v : productItems) {
            v.setProductId(productId);
            v.setCategoryId(productIndex.getCategoryId());

            if (itemIds.contains(v.getItemId())) {
                itemIds.removeIf(s -> v.getItemId().equals(s));
            }
        }

        if (CollUtil.isNotEmpty(itemIds)) {
            productItemRepository.remove(itemIds);
        }

        //处理ItemName
        for (ProductItem v : productItems) {
            List<String> itemNames = new ArrayList<>();
            List<String> itemIdList = new ArrayList<>();

            List<Map> specs = new ArrayList<>();

            specs = JSONUtil.parseArray(v.getItemSpec(), Map.class);

            for (Map spec : specs) {
                //ISpecVo
                Map item = (Map) spec.get("item");
                itemNames.add(Convert.toStr(item.get("name")));
                itemIdList.add(Convert.toStr(item.get("id")));
            }
            v.setItemName(CollUtil.join(itemNames, " "));
            v.setSpecItemIds(CollUtil.join(itemIdList, ","));
        }

        flag = productItemRepository.saves(productItems);

        if (!flag) {
            throw new BusinessException(__("商品SKU数据错误"));
        }

        //处理product_uniqid
        Map<String, List<Object>> productUniqid = new HashMap<>();

        for (ProductItem v : productItems) {
            List<Integer> specItemIds = new ArrayList<>();

            List<Map> specs = new ArrayList<>();

            specs = JSONUtil.parseArray(v.getItemSpec(), Map.class);

            for (Map spec : specs) {
                //ISpecVo
                Map item = (Map) spec.get("item");
                specItemIds.add(Convert.toInt(item.get("id")));
            }
            /*
            List<ISpecVo> specs = new ArrayList<>();

            specs = JSONUtil.parseArray(v.getItemSpec(), ISpecVo.class);

            for (ISpecVo spec : specs) {
                //ISpecVo
                ISpecItemVo item = spec.getItem();
                specItemIds.add(item.getId());
                itemNames.add(item.getId());
            }
             */

            // Sort the slice in ascending order
            Collections.sort(specItemIds);

            String colorImage = "";

            for (ProductImage image : productImages) {
                if (image.getColorId().equals(v.getColorId())) {
                    colorImage = image.getItemImageDefault();
                    break;
                }
            }

            //[]interface{}{"ItemId", "item_unit_price", "item_quantity", "ItemEnable", "color_id", "color_img", "item_name"}
            productUniqid.put(CollUtil.join(specItemIds, "-"), new ArrayList<Object>(Arrays.asList(v.getItemId(), v.getItemUnitPrice(), v.getItemQuantity(), v.getItemEnable(), v.getColorId(), colorImage, v.getItemName())));
        }

        //商品图片 图片和规格属性一起保存  !如果是编辑，允许增加SKU，需要删除不使用的记录
        for (ProductImage v : productImages) {
            v.setProductId(productId);
        }

        flag = productImageRepository.saves(productImages);

        if (!flag) {
            throw new BusinessException(__("商品图片数据错误"));
        }


        if (productIndex.getKindId() == StateCode.PRODUCT_KIND_FUWU
                || productIndex.getKindId() == StateCode.PRODUCT_KIND_EDU) {
            ProductValidPeriod productValidPeriod = in.getProductValidPeriod();
            productValidPeriod.setProductId(productId);

            flag = productValidPeriodRepository.save(productValidPeriod);

            if (!flag) {
                throw new BusinessException(__("商品虚拟商品数据错误"));
            }
        }

        //商品信息表
        productInfo.setProductId(productId);
        productInfo.setProductUniqid(JSONUtil.toJSONString(productUniqid));

        List<Map> productSpecList = new ArrayList<>();
        productSpecList = JSONUtil.parseArray(productInfo.getProductSpec(), Map.class);

        try {
            List<Integer> column = new ArrayList<>();
            for (Map spec : productSpecList) {
                column.add(Convert.toInt(spec.get("id")));
            }

            productInfo.setSpecIds(CollUtil.join(column, ","));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        flag = productInfoRepository.save(productInfo);

        if (!flag) {
            throw new BusinessException(__("商品信息数据错误"));
        }


        List<Long> oldItemIds = CommonUtil.column(oldProductItems, ProductItem::getItemId);

        // 添加商品，设置期初库存
        // 编辑商品，设置库存变动
        List<StockBillItem> stockBillItems = new ArrayList<>();
        for (ProductItem v : productItems) {
            StockBillItem stockBillItem = new StockBillItem();
            stockBillItem.setProductId(v.getProductId());
            stockBillItem.setProductName(productBase.getProductName());
            stockBillItem.setItemId(v.getItemId());
            stockBillItem.setItemName(v.getItemName());

            //oldItemIds 已经存在的SKU, 修改
            if (oldItemIds.contains(v.getItemId())) {
                //查找
                ProductItem findItem = oldProductItems.stream().filter(s -> {
                    return s.getItemId().equals(v.getItemId());
                }).findFirst().orElse(null);

                if (findItem != null) {
                    int i = v.getItemQuantity() - findItem.getItemQuantity();

                    if (i == 0) {
                        continue;
                    } else if (i > 0) {
                        stockBillItem.setBillTypeId(StateCode.BILL_TYPE_IN);
                        stockBillItem.setStockTransportTypeId(StateCode.STOCK_IN_OTHER);
                        stockBillItems.add(stockBillItem);
                    } else {
                        if (findItem.getAvailableQuantity().intValue() < Math.abs(i)) {
                            throw new BusinessException(__("出库数量不能大于总库存！"));
                        }

                        stockBillItem.setBillTypeId(StateCode.BILL_TYPE_OUT);
                        stockBillItem.setStockTransportTypeId(StateCode.STOCK_OUT_OTHER);
                        stockBillItems.add(stockBillItem);
                    }

                    stockBillItem.setBillItemQuantity(Math.abs(i));
                    stockBillItem.setWarehouseItemQuantity(findItem.getItemQuantity());
                }
            } else {
                stockBillItem.setBillTypeId(StateCode.BILL_TYPE_IN);
                stockBillItem.setStockTransportTypeId(StateCode.STOCK_IN_INIT);
                stockBillItem.setBillItemQuantity(v.getItemQuantity());
                stockBillItems.add(stockBillItem);
                stockBillItem.setWarehouseItemQuantity(0);
            }

            stockBillItem.setBillItemUnitPrice(v.getItemUnitPrice());
            stockBillItem.setBillItemSubtotal(v.getItemUnitPrice().multiply(Convert.toBigDecimal(stockBillItem.getBillItemQuantity())));
        }

        if (CollUtil.isNotEmpty(stockBillItems)) {
            boolean add = stockBillItemRepository.saves(stockBillItems);
        }


        return true;
    }

    @Override
    public ProductDataOutput getProduct(Long productId) {
        ProductDataOutput productDataOutput = new ProductDataOutput();

        //基础表
        ProductBase productBase = repository.get(productId);

        if (ObjectUtil.isEmpty(productBase)) {
            throw new BusinessException(__("商品基础数据有误！"));
        }

        productDataOutput.setProductBase(productBase);

        //索引表
        ProductIndex productIndex = productIndexRepository.get(productId);

        if (ObjectUtil.isEmpty(productIndex)) {
            throw new BusinessException(__("商品索引数据有误！"));
        }

        productDataOutput.setProductIndex(productIndex);

        //信息表
        ProductInfo productInfo = productInfoRepository.get(productId);

        if (ObjectUtil.isEmpty(productInfo)) {
            throw new BusinessException(__("商品信息数据有误！"));
        }

        productDataOutput.setProductInfo(productInfo);

        //SKU表
        List<ProductItem> productItems = productItemRepository.find(new QueryWrapper<ProductItem>().eq("product_id", productId));
        if (CollUtil.isEmpty(productItems)) {
            LogUtil.error(ConstantLog.DEFAULT, String.format(__("商品 %d SKU数据有误！"), productId));
            //throw new BusinessException(__("商品SKU数据有误！"));
        }

        productDataOutput.setProductItem(productItems);

        //图片表
        List<ProductImage> productImages = productImageRepository.find(new QueryWrapper<ProductImage>().eq("product_id", productId));
        if (CollUtil.isEmpty(productImages)) {
            LogUtil.error(ConstantLog.DEFAULT, String.format(__("商品 %d 图片数据有误！"), productId));
            //throw new BusinessException(__("商品图片数据有误！"));
        }

        // 按 color_id 分组，只保留一个（假设一个颜色一个图）
        Map<Long, ProductImage> specImg = productImages.stream()
                .filter(img -> img.getColorId() != null)
                .collect(Collectors.toMap(
                        ProductImage::getColorId,
                        img -> img,
                        (oldVal, newVal) -> newVal
                ));
        productDataOutput.setSpecImg(specImg);

        productDataOutput.setProductImage(productImages);

        if (productIndex.getKindId().intValue() == StateCode.PRODUCT_KIND_FUWU) {
            ProductValidPeriod productValidPeriod = productValidPeriodRepository.get(productId);
            productDataOutput.setProductValidPeriod(productValidPeriod);
        }

        return productDataOutput;
    }

    @Override
    @Transactional
    public boolean removeProduct(Long productId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        ProductBase productBase = get(productId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), productBase, ProductBase::getStoreId)) {
            //todo 是否可以删除判断
            // 1、生效或者待生效 活动商品不可删除
            // 2、装修商品 提示
            if (true) {

            }

            // RemoveProdcut 删除商品
            // 对齐需求：删除商品后同步清理用户收藏/用户足迹
            List<ProductItem> productItems = productItemRepository.find(new QueryWrapper<ProductItem>().eq("product_id", productId));
            List<Long> itemIds = CommonUtil.column(productItems, ProductItem::getItemId);
            userFavoritesItemRepository.remove(new QueryWrapper<UserFavoritesItem>().eq("product_id", productId));
            if (CollUtil.isNotEmpty(itemIds)) {
                userProductBrowseRepository.remove(new QueryWrapper<UserProductBrowse>().in("item_id", itemIds));
            }

            productInfoRepository.remove(productId);
            productIndexRepository.remove(productId);

            productImageRepository.remove(new QueryWrapper<ProductImage>().eq("product_id", productId));
            productItemRepository.remove(new QueryWrapper<ProductItem>().eq("product_id", productId));

            productValidPeriodRepository.remove(productId);

            repository.remove(productId);

            Integer sellerId = userAdminService.getNoticeUserId(productBase.getStoreId());

            String messageId = "notice-of-deleting-goods";
            Map<String, Object> args = new HashMap<>();
            args.put("product_id", productId);
            args.put("des", String.format("商品: %s 被平台删除,如有疑问请联系平台。", productId));

            messageService.sendNoticeMsg(sellerId, messageId, args);

            return true;
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @Transactional
    @Override
    public boolean batchEditState(List<Long> productIds, Integer productStateId, String productStateRemark) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        Date productSaleTime = null;
        for (Long productId : productIds) {
            ProductBase productBase = get(productId);

            if (productBase == null) {
                throw new BusinessException(__("商品不存在！"));
            }

            if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), productBase, ProductBase::getStoreId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }

            ProductIndex productIndex = productIndexRepository.get(productId);
            if (productIndex == null) {
                throw new BusinessException(__("商品索引数据有误！"));
            }
            Integer storeId = productIndex.getStoreId();

            if (Objects.equals(productStateId, StateCode.PRODUCT_STATE_NORMAL)) {
                Integer productVerifyId = productIndex.getProductVerifyId();

                if (Arrays.asList(StateCode.PRODUCT_VERIFY_WAITING, StateCode.PRODUCT_VERIFY_REFUSED).contains(productVerifyId)) {
                    throw new BusinessException(String.format(__("商品编号: %s 尚未审核通过，无法上架！"), productIndex.getProductId()));
                }
                QueryWrapper<ProductItem> productItemQueryWrapper = new QueryWrapper<>();
                productItemQueryWrapper.eq("product_id", productId);
                List<ProductItem> productItemList = productItemRepository.find(productItemQueryWrapper);

                if (CollectionUtil.isNotEmpty(productItemList)) {
                    boolean itemEnable = productItemList.stream()
                            .anyMatch(item -> ObjectUtil.equal(item.getItemEnable(), StateCode.PRODUCT_STATE_NORMAL));

                    if (!itemEnable) {
                        throw new BusinessException(String.format(__("SPU编号: %s， 由于SKU商品都处于下架仓库中，无法上架！"), productId));
                    }
                }
                productSaleTime = new Date();
            } else if (Objects.equals(productStateId, StateCode.PRODUCT_STATE_OFF_THE_SHELF)) {
                productSaleTime = DateUtil.offsetMonth(new Date(), 12 * 10);// 待上架时间
            } else if (productStateId.equals((StateCode.PRODUCT_STATE_ILLEGAL)) && CheckUtil.isNotEmpty(storeId)) {
                // 违规下架
                Integer sellerId = userAdminService.getNoticeUserId(storeId);

                String messageId = "illegal-commodity-shelves";
                Map<String, Object> args = new HashMap<>();
                args.put("des", "违规下架禁售");
                args.put("product_id", productId);
                args.put("product_name", productIndex.getProductName());

                messageService.sendNoticeMsg(sellerId, messageId, args);
            }
            productIndex.setProductStateId(productStateId);

            if (productSaleTime != null) {
                productIndex.setProductSaleTime(productSaleTime.getTime());
            }

            if (StrUtil.isNotEmpty(productStateRemark)) {
                productIndex.setProductStateRemark(productStateRemark);
            }

            if (!productIndexRepository.edit(productIndex)) {
                throw new BusinessException(ResultCode.FAILED);
            }

            if (CheckUtil.isNotEmpty(storeId)) {
                if (!storeAnalyticsService.saveProductAnalyticsNum(storeId)) {
                    throw new BusinessException(__("更新商品数量统计失败！"));
                }
            }
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
            WriteSheet writeSheet = EasyExcelUtil.writeSelectedSheet(ProductTemp.class, 0, "导入单规格商品模版");
            excelWriter.write(new ArrayList<ProductTemp>(), writeSheet);
            excelWriter.finish();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException(__("导出Excel编码异常"));
        } catch (IOException e) {
            throw new BusinessException(__("导出Excel文件异常"));
        }
    }

    @Override
    public void importTemp(MultipartFile file) throws Exception {
        AnalysisEventListener productTempListener = new ProductTempListener();
        Class<?> tempClass = ProductTemp.class;

        InputStream inputStream = file.getInputStream();
        EasyExcel.read(inputStream)
                // 注册监听器，可以在这里校验字段
                .registerReadListener(productTempListener)
                .head(tempClass)
                // 设置sheet,默认读取第一个
                .sheet()
                // 设置标题所在行数
                .headRowNumber(1)
                .doReadSync();
    }

    @Override
    public long getProductNum(Integer productStateId, Integer productVerifyId, Integer storeId, Integer dayFlag, Integer subsiteId) {
        QueryWrapper<ProductIndex> queryWrapper = new QueryWrapper<>();

        if (productStateId != null) {
            queryWrapper.eq("product_state_id", productStateId);
        }

        if (storeId != null) {
            queryWrapper.eq("store_id", storeId);
        }

        if (CheckUtil.isNotEmpty(productVerifyId)) {
            queryWrapper.eq("product_verify_id", productVerifyId);
        }

        if (dayFlag != null) {
            // 获得今日0时
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            Date todayZeroTime = calendar.getTime();
            // 查询大于日期 - day_flag零时的所有数据
            queryWrapper.gt("product_add_time", DateUtil.offsetDay(todayZeroTime, dayFlag).getTime());
        }

        if (CheckUtil.isNotEmpty(subsiteId)) {
            queryWrapper.eq("subsite_id", subsiteId);
        }

        return productIndexRepository.count(queryWrapper);
    }

}
