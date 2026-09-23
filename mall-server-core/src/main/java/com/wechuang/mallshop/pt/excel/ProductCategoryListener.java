package com.wechuang.mallshop.pt.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.WxHttpUtil;
import com.wechuang.mallshop.pt.model.entity.ProductCategory;
import com.wechuang.mallshop.pt.repository.ProductCategoryRepository;

import java.util.ArrayList;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


public class ProductCategoryListener extends AnalysisEventListener<ProductCategoryTemp> {

    private final ArrayList<ProductCategory> productCategories = new ArrayList<>();

    public static ProductCategoryRepository productCategoryRepository;


    static {
        initDictionary();
    }

    static void initDictionary() {
        productCategoryRepository = WxHttpUtil.getBean(ProductCategoryRepository.class);

    }

    /**
     * 每解析一行，回调该方
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ProductCategoryTemp data, AnalysisContext context) {
        ProductCategory productCategory = BeanUtil.copyProperties(data, ProductCategory.class);

        if (StrUtil.isNotEmpty(productCategory.getCategoryName())) {
            productCategories.add(productCategory);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtil.isEmpty(productCategories)) return;

        if (!productCategoryRepository.saveOrUpdate(productCategories)) {
            throw new BusinessException(__("批量导入商品分类失败！"));
        }

        // 清除数据
        productCategories.clear();
    }
}
