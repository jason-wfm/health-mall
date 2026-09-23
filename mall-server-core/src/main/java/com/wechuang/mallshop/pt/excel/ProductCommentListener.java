package com.wechuang.mallshop.pt.excel;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.WxHttpUtil;
import com.wechuang.mallshop.pt.model.entity.ProductComment;
import com.wechuang.mallshop.pt.model.entity.ProductIndex;
import com.wechuang.mallshop.pt.repository.ProductCommentRepository;
import com.wechuang.mallshop.pt.repository.ProductIndexRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


public class ProductCommentListener extends AnalysisEventListener<ProductCommentTemp> {

    private final ArrayList<ProductComment> productComments = new ArrayList<>();

    public static ProductCommentRepository productCommentRepository;

    public static ProductIndexRepository productIndexRepository;


    static {
        initDictionary();
    }

    static void initDictionary() {
        productCommentRepository = WxHttpUtil.getBean(ProductCommentRepository.class);

        productIndexRepository = WxHttpUtil.getBean(ProductIndexRepository.class);
    }

    /**
     * 每解析一行，回调该方
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ProductCommentTemp data, AnalysisContext context) {
        ProductComment productComment = BeanUtil.copyProperties(data, ProductComment.class);
        productComment.setOrderId(UUID.randomUUID().toString().replace("-", ""));
        productComment.setCommentScores(5);

        productComments.add(productComment);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtil.isEmpty(productComments)) return;

        if (!productCommentRepository.saveOrUpdate(productComments)) {
            throw new BusinessException(__("批量导入虚拟评论失败！"));
        }
        Map<Long, List<ProductComment>> lsitMap = productComments.stream().collect(Collectors.groupingBy(ProductComment::getProductId));

        for (Long productId : lsitMap.keySet()) {
            List<ProductComment> commentList = lsitMap.get(productId);
            ProductIndex shopProductIndex = productIndexRepository.get(productId);

            if (shopProductIndex != null) {
                shopProductIndex.setProductEvaluationNum(shopProductIndex.getProductEvaluationNum() + commentList.size());

                if (!productIndexRepository.edit(shopProductIndex)) {
                    throw new BusinessException(__("产品评论更新失败！"));
                }
            }
        }

        // 清除数据
        productComments.clear();
    }
}
