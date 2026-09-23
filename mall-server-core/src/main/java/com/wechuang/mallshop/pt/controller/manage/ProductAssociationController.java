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
package com.wechuang.mallshop.pt.controller.manage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.pt.model.entity.ProductAssociation;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.output.ItemOutput;
import com.wechuang.mallshop.pt.model.req.ProductAssociationListReq;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.repository.ProductAssociationRepository;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 关联商品表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2025-08-12
 */
@Tag(name = "关联商品表")
@RestController
@RequestMapping("/manage/pt/productAssociation")
public class ProductAssociationController extends BaseController {

    @Autowired
    private ProductIndexService productIndexService;

    @Autowired
    private ProductAssociationRepository productAssociationRepository;

    @Autowired
    private ProductItemRepository productItemRepository;


    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "关联商品表-分页列表查询", description = "关联商品表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<ItemListRes> list(ProductAssociationListReq productAssociationListReq) {
        QueryWrapper<ProductAssociation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_parent_id", productAssociationListReq.getProductParentId());
        List<ProductAssociation> items = productAssociationRepository.find(queryWrapper);

        List<Long> item_id_row = CommonUtil.column(items, ProductAssociation::getItemId);
        ProductItemInput input = new ProductItemInput();
        ItemListRes pageList = new ItemListRes();
        if (!item_id_row.isEmpty()) {
            input.setItemId(item_id_row);
            input.setPage(productAssociationListReq.getPage());
            input.setSize(productAssociationListReq.getSize());
            pageList = productIndexService.listItem(input);
            List<ItemOutput> itemRows = pageList.getItems();
            for (ItemOutput itemRow : itemRows) {
                for (ProductAssociation item : items) {
                    if (item.getItemId().equals(itemRow.getItemId())) {
                        itemRow.setAssociationId(item.getAssociationId());
                    }
                }
            }

        }

        return success(pageList);
    }


    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "关联商品表-添加", description = "关联商品表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(@RequestParam("product_parent_id") Long productParentId, @RequestParam("item_ids") String itemIds) {

        List<Long> item_ids = Convert.toList(Long.class, itemIds);
        QueryWrapper<ProductAssociation> queryWrapper = new QueryWrapper<>();
        List<ProductAssociation> lists = new ArrayList<>();
        for (Long itemId : item_ids) {
            queryWrapper.eq("item_id", itemId);
            queryWrapper.eq("product_parent_id", productParentId);
            if (productAssociationRepository.count(queryWrapper) == 0) {
                ProductAssociation productAssociation = new ProductAssociation();
                ProductItem productItem = productItemRepository.get(itemId);
                ProductIndex productIndex = productIndexService.get(productItem.getProductId());
                productAssociation.setItemId(itemId);
                productAssociation.setProductId(productItem.getProductId());
                productAssociation.setProductParentId(productParentId);
                productAssociation.setCategoryId(productIndex.getCategoryId());
                lists.add(productAssociation);
            }
        }

        if (CollUtil.isNotEmpty(lists)) {
            productAssociationRepository.saves(lists);
        }
        return success();
    }


    @PreAuthorize("hasAuthority('/manage/pt/productBase/list')")
    @Operation(summary = "关联商品表-通过association_id删除", description = "关联商品表-通过association_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("association_id") Integer associationId) {
        boolean success = productAssociationRepository.remove(associationId);

        if (success) {
            return success();
        }

        return fail();
    }

}

