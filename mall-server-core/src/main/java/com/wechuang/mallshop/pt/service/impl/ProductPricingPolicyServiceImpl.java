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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.JSONUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.entity.ProductPricingPolicy;
import com.wechuang.mallshop.pt.model.output.ProductPricingPolicyOutput;
import com.wechuang.mallshop.pt.model.req.ProductPricingPolicyAddReq;
import com.wechuang.mallshop.pt.model.req.ProductPricingPolicyListReq;
import com.wechuang.mallshop.pt.repository.ProductIndexRepository;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.pt.repository.ProductPricingPolicyRepository;
import com.wechuang.mallshop.pt.service.ProductPricingPolicyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 价格策略表-按客户定价 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2025-02-19
 */
@Service
public class ProductPricingPolicyServiceImpl extends BaseServiceImpl<ProductPricingPolicyRepository, ProductPricingPolicy, ProductPricingPolicyListReq> implements ProductPricingPolicyService {

    @Autowired
    private ProductIndexRepository productIndexRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * 获取列表数据
     *
     * @param productPricingPolicyListReq
     * @return
     */
    @Override
    public IPage<ProductPricingPolicyOutput> listItem(ProductPricingPolicyListReq productPricingPolicyListReq) {

        // 根据商品名称查询 queryProductIds 合集
        if (productPricingPolicyListReq.getProductName() != null && !productPricingPolicyListReq.getProductName().isEmpty()) {
            QueryWrapper<ProductIndex> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("product_name", productPricingPolicyListReq.getProductName());
            List<ProductIndex> IndexRes = productIndexRepository.find(queryWrapper);
            List<Long> queryProductIds = IndexRes.stream().map(ProductIndex::getProductId).collect(Collectors.toList());
            String productIdsStr = "-1";
            if (!queryProductIds.isEmpty()) {
                productIdsStr = queryProductIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            }

            // 设置到查询条件
            productPricingPolicyListReq.setProductId(productIdsStr);
            productPricingPolicyListReq.setProductName(null);
        }

        Page<ProductPricingPolicy> itemPage = lists(productPricingPolicyListReq);
        List<ProductPricingPolicy> itemList = itemPage.getRecords();

        // 获取 productIds、itemIds、userIds 列表
        List<Long> productIds = itemList.stream().map(ProductPricingPolicy::getProductId).distinct().collect(Collectors.toList());
        List<Long> itemIds = itemList.stream().map(ProductPricingPolicy::getItemId).distinct().collect(Collectors.toList());
        List<Integer> userIds = itemList.stream().map(ProductPricingPolicy::getUserId).distinct().collect(Collectors.toList());

        // 获取 ProductIndex 并转为 Map 以便快速查找
        List<ProductIndex> indexList = productIndexRepository.gets(productIds);
        Map<Long, ProductIndex> productIndexMap = indexList.stream().collect(Collectors.toMap(ProductIndex::getProductId, productIndex -> productIndex));

        // 获取 productItemMap
/*
        List<Long> itemIdRow = new ArrayList<>();
        for (Long itemId : itemIds){
            if(!itemIdRow.contains(itemId)){
                itemIdRow.add(itemId);
            }

        }
*/
        List<ProductItem> productItems = productItemRepository.gets(itemIds);

        Map<Long, ProductItem> productItemMap = productItems.stream().collect(Collectors.toMap(ProductItem::getItemId, ProductItem -> ProductItem));

        // 获取 userInfoMap
        List<UserInfo> userInfos = userInfoRepository.gets(userIds);
        Map<Integer, UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getUserId, UserInfo -> UserInfo));

        // 准备 ProductPricingPolicyOutput 列表
        List<ProductPricingPolicyOutput> outputList = itemList.stream()
                .map(item -> {
                    ProductPricingPolicyOutput output = new ProductPricingPolicyOutput();
                    BeanUtils.copyProperties(item, output);

                    // 获取对应的 ProductIndex
                    ProductIndex productIndex = productIndexMap.get(item.getProductId());
                    if (productIndex != null) {
                        output.setProductName(productIndex.getProductName());
                    }

                    // 获取对应的 ProductItem
                    ProductItem productItem = productItemMap.get(item.getItemId());
                    if (productItem != null) {
                        output.setItemSpecName(productItem.getItemName());
                        output.setItemUnitPrice(productItem.getItemUnitPrice());
                    }

                    // 获取对应的 UserInfo
                    UserInfo userInfo = userInfoMap.get(item.getUserId());
                    if (userInfo != null) {
                        output.setUserAccount(userInfo.getUserAccount());
                    }

                    return output;
                })
                .filter(Objects::nonNull)  // 过滤掉 null 值
                .collect(Collectors.toList());

        // 返回分页结果
        IPage<ProductPricingPolicyOutput> outputIPage = new Page<>();
        outputIPage.setPages(itemPage.getPages());
        outputIPage.setCurrent(itemPage.getCurrent());
        outputIPage.setSize(itemPage.getSize());
        outputIPage.setTotal(itemPage.getTotal());
        outputIPage.setRecords(outputList);

        return outputIPage;
    }


    @Transactional(rollbackFor = Exception.class)
    public Boolean addPolicyItem(ProductPricingPolicyAddReq productPricingPolicyAddReq) {
        ContextUser loginUser = getLoginUser();
        Integer storeId = null;

        if (loginUser != null) {

            if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
                storeId = loginUser.getStoreId();
            }
        }

        Integer userId = productPricingPolicyAddReq.getUserId();
        if (userId == null) {
            throw new BusinessException(__("请选择用户！"));
        }

        // 将 items 字符串解析为 List<Item>
        List<ProductPricingPolicyAddReq.Item> itemList = BeanUtil.copyToList(JSONUtil.parseArray(productPricingPolicyAddReq.getItems(), Map.class), ProductPricingPolicyAddReq.Item.class);

        if (itemList != null && !itemList.isEmpty()) {

            // 获取所有 item_id 的集合
            List<Long> itemIds = itemList.stream().map(ProductPricingPolicyAddReq.Item::getItemId).collect(Collectors.toList());

            // 查询数据库中已存在的 item_id
            QueryWrapper<ProductPricingPolicy> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("item_id", itemIds);
            queryWrapper.eq("user_id", userId);
            List<ProductPricingPolicy> existingItems = find(queryWrapper);
            List<Long> existingItemIds = existingItems.stream().map(ProductPricingPolicy::getItemId).collect(Collectors.toList());

            boolean success = true;
            for (ProductPricingPolicyAddReq.Item item : itemList) {

                // 如果 item_id 已经存在，则跳过该项的插入
                if (existingItemIds.contains(item.getItemId())) {
                    continue;
                }

                ProductPricingPolicy pricingPolicyRow = new ProductPricingPolicy();
                pricingPolicyRow.setUserId(userId);
                pricingPolicyRow.setProductId(item.getProductId());
                pricingPolicyRow.setItemId(item.getItemId());
                pricingPolicyRow.setPolicyEnable(item.getPolicyEnable());
                pricingPolicyRow.setPolicyPrice(item.getPolicyPrice());

                if (CheckUtil.isNotEmpty(storeId)) {
                    pricingPolicyRow.setStoreId(storeId);
                }

                // 调用服务层插入商品定价项
                boolean itemSuccess = add(pricingPolicyRow);
                if (!itemSuccess) {
                    success = false;
                    break;
                }
            }

            if (success) {
                return true;
            }
        }

        return false;
    }

}
