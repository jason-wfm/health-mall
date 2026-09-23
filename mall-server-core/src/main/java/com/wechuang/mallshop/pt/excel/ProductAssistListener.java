package com.wechuang.mallshop.pt.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.WxHttpUtil;
import com.wechuang.mallshop.pt.model.entity.ProductAssist;
import com.wechuang.mallshop.pt.model.entity.ProductAssistItem;
import com.wechuang.mallshop.pt.repository.ProductAssistItemRepository;
import com.wechuang.mallshop.pt.repository.ProductAssistRepository;

import java.util.ArrayList;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


public class ProductAssistListener extends AnalysisEventListener<ProductAssistTemp> {

    private final ArrayList<ProductAssist> productAssists = new ArrayList<>();

    public static ProductAssistRepository productAssistRepository;

    public static ProductAssistItemRepository productAssistItemRepository;


    static {
        initDictionary();
    }

    static void initDictionary() {
        productAssistRepository = WxHttpUtil.getBean(ProductAssistRepository.class);
        productAssistItemRepository = WxHttpUtil.getBean(ProductAssistItemRepository.class);

    }

    /**
     * 每解析一行，回调该方
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ProductAssistTemp data, AnalysisContext context) {
        ProductAssist productAssist = BeanUtil.copyProperties(data, ProductAssist.class);

        if (StrUtil.isNotEmpty(productAssist.getAssistItem())) {
            List<String> items = Convert.toList(String.class, productAssist.getAssistItem());
            List<ProductAssistItem> productAssistItems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                ProductAssistItem productAssistItem = new ProductAssistItem();
                productAssistItem.setAssistItemName(items.get(i));
                productAssistItem.setAssistItemSort(i + 1);

                productAssistItems.add(productAssistItem);
            }

            productAssist.setItems(productAssistItems);
        }

        productAssists.add(productAssist);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtil.isEmpty(productAssists)) return;

        if (!productAssistRepository.saveOrUpdate(productAssists)) {
            throw new BusinessException(__("批量导入商品辅助属性失败！"));
        }
        List<ProductAssistItem> productAssistItems = new ArrayList<>();

        for (ProductAssist productAssist : productAssists) {
            List<ProductAssistItem> items = productAssist.getItems();

            if (CollectionUtil.isNotEmpty(items)) {
                for (ProductAssistItem item : items) {
                    item.setAssistId(productAssist.getAssistId());
                }

                productAssistItems.addAll(items);
            }
        }

        if (CollectionUtil.isNotEmpty(productAssistItems)) {

            if (!productAssistItemRepository.saveOrUpdate(productAssistItems)) {
                throw new BusinessException(__("批量导入商品辅助属性值失败！"));
            }
        }

        // 清除数据
        productAssists.clear();
    }
}
