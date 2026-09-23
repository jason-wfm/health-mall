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
package com.wechuang.mallshop.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.req.ProductCategoryListReq;
import com.wechuang.mallshop.pt.model.res.ProductCategoryRes;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.ProductBaseRepository;
import com.wechuang.mallshop.pt.service.ProductCategoryService;
import com.wechuang.mallshop.sys.model.entity.PageCategoryNav;
import com.wechuang.mallshop.sys.model.req.PageCategoryNavListReq;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import com.wechuang.mallshop.sys.repository.PageCategoryNavRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.PageCategoryNavService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PC分类导航表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-09-18
 */
@Service
public class PageCategoryNavServiceImpl extends BaseServiceImpl<PageCategoryNavRepository, PageCategoryNav, PageCategoryNavListReq> implements PageCategoryNavService {
    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private LangMetaRepository langMetaRepository;

    @Autowired
    private ConfigBaseService configBaseService;

    /**
     * 获取PC页面布局信息
     *
     * @return
     */
    @Cacheable(value = {"pcLayoutData"})
    @Override
    public Map getPcLayout(String sourceLang, Integer siteId) {
        List<PageCategoryNav> pageCategoryNavs = find(new QueryWrapper<PageCategoryNav>().eq("category_nav_enable", true).orderByAsc("category_nav_order"));
        Map resultMap = new HashMap<>();

        //无用数据较多，可以独立封装一个数据，只传必须数据
        ProductCategoryListReq productCategoryListReq = new ProductCategoryListReq();
        productCategoryListReq.setCategoryIsEnable(true);
        productCategoryListReq.setSourceLang(sourceLang);
        List<ProductCategoryRes> categoryRes = productCategoryService.getTree(productCategoryListReq.getCategoryParentId(), productCategoryListReq.getCategoryIsEnable(), "", productCategoryListReq.getSourceLang(), siteId);

        //读取所有商品信息
        List<Long> itemIds = new ArrayList<>();
        for (PageCategoryNav item : pageCategoryNavs) {
            //处理商品信息
            if (item.getCategoryNavType().equals(2)) {
                itemIds.addAll(Convert.toList(Long.class, item.getItemIds()));
            }
        }

        List<ProductItemVo> items = new ArrayList<>();

        if (CollUtil.isNotEmpty(itemIds)) {
            items = productBaseRepository.getItems(itemIds, null);
        }

        for (PageCategoryNav item : pageCategoryNavs) {
            //处理分类编号
            if (item.getCategoryNavType().equals(1)) {
                Integer categoryId = Convert.toInt(item.getCategoryIds());

                if (CheckUtil.isNotEmpty(categoryId)) {
                    ProductCategoryRes productCategoryRes = categoryRes.stream().filter(s -> s.getCategoryId().equals(categoryId)).findFirst().orElse(null);
                    if (productCategoryRes != null) {
                        item.setProductCategoryTree(productCategoryRes);
                    }
                }
            }

            //处理商品信息
            if (item.getCategoryNavType().equals(2)) {
                List<ProductItemVo> productItemVoList = new ArrayList<>();

                List<Long> itemIdList = Convert.toList(Long.class, item.getItemIds());
                for (Long itemId : itemIdList) {
                    ProductItemVo itemVo = items.stream().filter(s -> s.getItemId().equals(itemId)).findFirst().orElse(null);

                    if (itemVo != null) {
                        productItemVoList.add(itemVo);
                    }
                }

                item.setProductItems(productItemVoList);
            }
        }

        resultMap.put("category_nav", pageCategoryNavs);

        return resultMap;
    }

    @Override
    @Cacheable(value = {"getPcHelp"})
    public List<Map> getPcHelp(String sourceLang, Integer siteId) {
        List<Map> pcFooterList = new ArrayList<>();

        //底部站内帮助
        String keys = "page_pc_help";
        String pagePcHelp = configBaseService.getConfig(keys);

        if (StrUtil.isNotEmpty(pagePcHelp)) {
            pcFooterList = com.wechuang.mallshop.common.utils.JSONUtil.parseArray(pagePcHelp, Map.class);
        }

        String to = ContextUtil.getToLang();
        if (to == null) {
            return pcFooterList;
        }

        if (CollUtil.isNotEmpty(pcFooterList)) {
            try {
                for (Map spec : pcFooterList) {
                    Object name = spec.get("cat_name");
                    if (name != null) {
                        if (CheckUtil.isNotEmpty(name.toString())) {
                            String sepc = langMetaRepository.getTranslate(name.toString(), to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0);
                            if (CheckUtil.isNotEmpty(sepc)) {
                                spec.put("cat_name", sepc);
                            }
                        }
                    }

                    for (Map specItem : (List<Map>) spec.get("items")) {
                        Object specItemName = specItem.get("title");
                        if (specItemName != null) {
                            if (CheckUtil.isNotEmpty(specItemName.toString())) {
                                String itemName = langMetaRepository.getTranslate(specItemName.toString(), to, ConstantConfig.BASE_LANG, "diy_page", "pc", null, 0);
                                if (CheckUtil.isNotEmpty(itemName)) {
                                    specItem.put("title", itemName);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                //throw new BusinessException(e.getMessage());
            }
        }

        return pcFooterList;
    }

    @CacheEvict(value = {"pcLayoutData"}, allEntries = true)
    @Override
    public boolean add(PageCategoryNav a) {
        return super.add(a);
    }

    @CacheEvict(value = {"pcLayoutData"}, allEntries = true)
    @Override
    public boolean edit(PageCategoryNav a) {
        return super.edit(a);
    }

    @CacheEvict(value = {"pcLayoutData"}, allEntries = true)
    @Override
    public boolean remove(Serializable a) {
        return super.remove(a);
    }
}
