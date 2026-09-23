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
package com.wechuang.mallshop.shop.controller.front;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.pt.model.entity.*;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.repository.*;
import com.wechuang.mallshop.pt.service.ProductAssistService;
import com.wechuang.mallshop.pt.service.ProductBrandService;
import com.wechuang.mallshop.shop.model.entity.UserContrastItem;
import com.wechuang.mallshop.shop.model.req.UserContrastItemAddReq;
import com.wechuang.mallshop.shop.model.req.UserContrastItemEditReq;
import com.wechuang.mallshop.shop.model.req.UserContrastItemListReq;
import com.wechuang.mallshop.shop.service.UserContrastItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品对比表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2025-09-11
 */
@Tag(name = "商品对比表")
@RestController
@RequestMapping("/front/shop/userContrastItem")
public class ContrastItemController extends BaseController {
    @Autowired
    private UserContrastItemService userContrastItemService;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductAssistItemRepository productAssistItemRepository;

    @Autowired
    private ProductAssistService productAssistService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductBrandService productBrandService;

    @Autowired
    private ProductSpecRepository productSpecRepository;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes <BaseListRes<UserContrastItem>> list(UserContrastItemListReq userContrastItemListReq) {
        IPage<UserContrastItem> pageList = userContrastItemService.lists(userContrastItemListReq);

        return success(pageList);
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<?> get() {
        Integer userId = ContextUtil.checkLoginUserId();
        UserContrastItem userContrastItem = userContrastItemService.get(userId);
        List<Map<String, Object>> resultData = new ArrayList<>();
        if(userContrastItem != null){
            List<Integer> itemIds = Convert.toList(Integer.class, userContrastItem.getItemIds());
            List<ProductItem> productItems = productItemRepository.gets(itemIds);

            List<Long> productIds = productItems.stream().map(ProductItem::getProductId).distinct().toList();
            List<ProductIndex> productIndexList = productIndexRepository.gets(productIds);

            Map<Long, ProductIndex> productIndexMap = productIndexList.stream().collect(Collectors.toMap(ProductIndex::getProductId, Function.identity()));

            List<Integer> brandIds = productIndexList.stream().map(ProductIndex::getBrandId).distinct().toList();
            List<ProductBrand> brandList = productBrandService.gets(brandIds);
            Map<Integer, ProductBrand> brandMap =brandList.stream().collect(Collectors.toMap(ProductBrand::getBrandId, Function.identity()));

            // 将product_assist_data转成数组并合并去重
            List<String> mergedAssistData = productIndexList.stream()
                    .map(ProductIndex::getProductAssistData)
                    .filter(data -> data != null && !data.isEmpty())
                    .flatMap(data -> Arrays.stream(data.split(",")))
                    .map(String::trim)
                    .distinct()
                    .collect(Collectors.toList());
            //将item_spec转成数组
            Map<Long, JSONArray> itemSpecArrays = new HashMap<>();
            List<Integer> itemSpecIds = new ArrayList<>();
            for (ProductItem item : productItems) {
                JSONArray specData = JSONUtil.parseArray(item.getItemSpec());
                if(CollectionUtil.isNotEmpty(specData)){
                    for (Object specDatum : specData) {
                        JSONObject spec = JSONUtil.parseObj(specDatum);
                        if (!itemSpecIds.contains(spec.getInt("id"))){
                            itemSpecIds.add(spec.getInt("id"));
                        }

                    }
                    itemSpecArrays.put(item.getItemId(), specData);
                }
            }

            List<ProductSpec> productSpecList = productSpecRepository.gets(itemSpecIds);
            List<ProductAssistItem> baseAssistItemRows = productAssistItemRepository.gets(mergedAssistData);
            List<Integer> assistIds = baseAssistItemRows.stream().map(ProductAssistItem::getAssistId).distinct().toList();
            List<ProductAssist> productAssistList = productAssistService.gets(assistIds);



            // 初始化每个商品的 assist 数据为 "-"
            productItems.forEach(item -> {
                Map<Integer, String> itemAssistData = new HashMap<>();
                productAssistList.forEach(assist -> itemAssistData.put(assist.getAssistId(), "-"));
            });

            Map<Long, List<?>> indexData = new HashMap<>();
            productIndexList.forEach(index -> {
                List<Map<String, Object>> AssistData = new ArrayList<>();

                productAssistList.forEach(assist -> {
                    Map<String, Object> assistRow = new HashMap<>();
                    assistRow.put("id", assist.getAssistId());
                    assistRow.put("name", assist.getAssistName());
                    assistRow.put("sub",new ArrayList<>());
                    if (index.getProductAssistData() != null && !index.getProductAssistData().isEmpty()) {
                        // 解析 product_assist_data，格式为 "assistId:value,assistId:value"
                        List<Integer>  productAssistItemId = Convert.toList(Integer.class, index.getProductAssistData());
                        List<Map<String, Object>> sub = new ArrayList<>();
                        for (int i = 0; i < baseAssistItemRows.size(); i++) {
                            if(assist.getAssistId().equals(baseAssistItemRows.get(i).getAssistId())){
                                for (int j = 0; j < productAssistItemId.size(); j++) {
                                    if (baseAssistItemRows.get(i).getAssistItemId().equals(productAssistItemId.get(j))){
                                        Map<String, Object> row = new HashMap<>();
                                        row.put("id", baseAssistItemRows.get(i).getAssistItemId());
                                        row.put("name", baseAssistItemRows.get(i).getAssistItemName());
                                        sub.add(row);
                                    }
                                }
                            }

                        }
                        assistRow.put("sub", sub);
                    }
                    AssistData.add(assistRow);
                });

                indexData.put(index.getProductId(), AssistData);
            });

            // 填充实际的 assist 数据
            List<Map<String, Object>> resultItems = new ArrayList<>();
            for (int i = 0; i < productItems.size(); i++) {
                ProductItem item = productItems.get(i);
                Map<String, Object> itemWithComparison = new HashMap<>();
                itemWithComparison.put("item_id", item.getItemId());
                itemWithComparison.put("product_id", item.getProductId());
                itemWithComparison.put("color_id", item.getColorId());
                itemWithComparison.put("item_name", item.getItemName());
                itemWithComparison.put("item_number", item.getItemNumber());
                itemWithComparison.put("item_unit_price", item.getItemUnitPrice());
                itemWithComparison.put("item_quantity", item.getItemQuantity());
                itemWithComparison.put("item_enable", item.getItemEnable());
                if(brandMap.get(productIndexMap.get(item.getProductId()).getBrandId()) != null){
                    itemWithComparison.put("brand_name",brandMap.get(productIndexMap.get(item.getProductId()).getBrandId()).getBrandName());
                }

                if(productIndexMap.get(item.getProductId()) != null){
                    itemWithComparison.put("product_name",productIndexMap.get(item.getProductId()).getProductName());
                    itemWithComparison.put("product_state_id",productIndexMap.get(item.getProductId()).getProductStateId());
                }

                if(itemSpecArrays.get(item.getItemId()) != null){
                    itemWithComparison.put("item_spec", itemSpecArrays.get(item.getItemId()));
                }


                itemWithComparison.put("assist_data", indexData.get(item.getProductId()));
                ProductImage image = productImageRepository.findOne(new QueryWrapper<ProductImage>().eq("product_id", item.getProductId()).eq("color_id", item.getColorId()));
                itemWithComparison.put("product_image",image.getItemImageDefault());
                resultItems.add(itemWithComparison);
            }



            // 构建最终返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("items", resultItems);
            result.put("assists", productAssistList);
            result.put("specs", productSpecList);

            return success(result);

        }
        else
        {
            return success();
        }


    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(UserContrastItemAddReq userContrastItemAddReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserContrastItem userContrastItem = userContrastItemService.get(userId);
        Integer itemId = userContrastItemAddReq.getItemId();
        if (userContrastItemAddReq.getItemNumber() != null){
            ProductItem productItem = productItemRepository.findOne(new QueryWrapper<ProductItem>().eq("item_number", userContrastItemAddReq.getItemNumber()));
            if (productItem != null){
                itemId = Math.toIntExact(productItem.getItemId());
            }

        }

        if (itemId == null){
            throw new BusinessException(__("商品不存在！"));
        }

        List< Integer> itemIds = new ArrayList<>();
        if(userContrastItem == null){
            userContrastItem = new UserContrastItem();
            userContrastItem.setUserId(userId);
        }
        else
        {
            itemIds = Convert.toList(Integer.class, userContrastItem.getItemIds());
            if (itemIds.contains(itemId)) {
                return fail("商品已存在");
            }
        }
        if (itemIds.size() >= 9){
            throw new BusinessException(__("最多只能添加9个商品！"));
        }

        itemIds.add(itemId);
        String ids = itemIds.stream().distinct().map(String::valueOf).collect(Collectors.joining(","));
        userContrastItem.setItemIds(ids);
        boolean success = userContrastItemService.save(userContrastItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(UserContrastItemEditReq userContrastItemEditReq) {
        UserContrastItem userContrastItem = BeanUtil.copyProperties(userContrastItemEditReq, UserContrastItem.class);
        boolean success = userContrastItemService.edit(userContrastItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("item_id") Integer itemId) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserContrastItem userContrastItem = userContrastItemService.get(userId);
        if (userContrastItem == null){
            throw new BusinessException(__("用户不存在！"));
        }

        List<Integer>  itemIds = Convert.toList(Integer.class, userContrastItem.getItemIds());
        if (!itemIds.contains(itemId)){
            throw new BusinessException(__("用户不存在该商品！"));
        }
        else
        {
            itemIds.remove(itemId);
        }
        UserContrastItem userContrastItemEdit = new UserContrastItem();
        userContrastItemEdit.setUserId(userId);
        userContrastItemEdit.setItemIds(itemIds.stream().distinct().map(String::valueOf).collect(Collectors.joining(",")));
        boolean success = userContrastItemService.edit(userContrastItemEdit);

        if (success) {
            return success();
        }

        return fail();
    }

    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("user_id") String userIds) {
        boolean success = userContrastItemService.remove(Convert.toList(Integer.class, userIds));

        if (success) {
            return success();
        }

        return fail();
    }
}

