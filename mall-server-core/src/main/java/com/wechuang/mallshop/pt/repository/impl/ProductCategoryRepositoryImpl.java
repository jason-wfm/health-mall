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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.common.consts.ConstantConfig;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pt.dao.ProductCategoryDao;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.repository.ProductCategoryRepository;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-05-06
 */
@Repository
public class ProductCategoryRepositoryImpl extends BaseRepositoryImpl<ProductCategoryDao, ProductCategory> implements ProductCategoryRepository {
    @Autowired
    private LangMetaRepository langMetaRepository;

    /**
     * 添加数据
     *
     * @param entity 实体
     * @return boolean
     */
    @Override
    @CacheEvict(value = {"productCategoryTree"}, allEntries = true)
    public boolean add(ProductCategory entity) {
        return super.add(entity);
    }

    /**
     * 更新数据
     *
     * @param entity 实体
     * @return boolean
     */
    @Override
    @CacheEvict(value = {"productCategoryTree"}, allEntries = true)
    public boolean edit(ProductCategory entity) {
        return super.edit(entity);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return boolean
     */
    @Override
    @CacheEvict(value = {"productCategoryTree"}, allEntries = true)
    public boolean remove(Serializable id) {
        return super.remove(id);
    }

    /**
     * 根据ids集合删除数据
     *
     * @param ids
     * @return boolean
     */
    @Override
    @CacheEvict(value = {"productCategoryTree"}, allEntries = true)
    public boolean remove(Collection<? extends Serializable> ids) {
        return super.remove(ids);
    }

    /**
     * 根据id 查询所有父级（包含自身）
     *
     * @param categoryId
     * @return
     */
    public List<ProductCategory> getParentCategory(Integer categoryId) {
        List<ProductCategory> categoryList = new ArrayList<>();
        int i = 5;

        while (CheckUtil.isNotEmpty(categoryId) && i != 0) {
            ProductCategory category = get(categoryId);

            if (ObjectUtil.isNotNull(category)) {
                categoryList.add(category);
                categoryId = category.getCategoryParentId();
            } else {
                break;
            }

            i--;
        }

        return categoryList;
    }


    @Override
    public List<ProductCategory> gets(Collection<? extends Serializable> a) {
        List<ProductCategory> list = super.gets(a);

        String to = ContextUtil.getToLang();
        if (to == null) {
            return list;
        }

        for (ProductCategory it : list) {
            if (ObjectUtil.isNotEmpty(it)) {
                it.setCategoryName(langMetaRepository.getTranslate(it.getCategoryName(), to, ConstantConfig.BASE_LANG, "pt_product_category", Convert.toStr(it.getCategoryId()), "category_name", 0));
            }
        }

        return list;
    }
}
