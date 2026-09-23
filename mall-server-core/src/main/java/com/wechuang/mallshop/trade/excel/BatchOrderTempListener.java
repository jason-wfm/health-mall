package com.wechuang.mallshop.trade.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.api.StateCode;

import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.utils.WxHttpUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.pt.model.entity.ProductItem;
import com.wechuang.mallshop.pt.model.vo.ProductItemVo;
import com.wechuang.mallshop.pt.repository.ProductBaseRepository;
import com.wechuang.mallshop.pt.repository.ProductItemRepository;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.trade.model.entity.BatchOrder;
import com.wechuang.mallshop.trade.repository.BatchOrderRepository;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


public class BatchOrderTempListener extends AnalysisEventListener<BatchOrderTemp> {

    private final ArrayList<ProductItemVo> itemTemps = new ArrayList<>();


    public static ProductItemRepository productItemRepository;

    public static BatchOrderRepository batchOrderRepository;

    public static ProductBaseRepository productBaseRepository;

    public static ConfigBaseService configBaseService;


    static {
        initDictionary();
    }

    static void initDictionary() {
        productItemRepository = WxHttpUtil.getBean(ProductItemRepository.class);
        batchOrderRepository = WxHttpUtil.getBean(BatchOrderRepository.class);
        productBaseRepository = WxHttpUtil.getBean(ProductBaseRepository.class);
        configBaseService = WxHttpUtil.getBean(ConfigBaseService.class);
    }

    /**
     * 每解析一行，回调该方
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(BatchOrderTemp data, AnalysisContext context) {
        initDictionary();
        ProductItemVo productItemVo = new ProductItemVo();
        productItemVo.setItemNumber(data.getItemNumber());
        productItemVo.setCartQuantity(data.getCartQuantity());

        itemTemps.add(productItemVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //删除历史导入数据
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        Integer userId = user.getUserId();
        batchOrderRepository.remove(new QueryWrapper<BatchOrder>().eq("user_id", userId));

        //查询封装商品item
        if (CollectionUtil.isNotEmpty(itemTemps)) {
            Set<String> seenItemNumbers = new HashSet<>();
            List<ProductItemVo> productItemVos = new ArrayList<>();

            for (ProductItemVo productItemVo : itemTemps) {
                if (seenItemNumbers.add(productItemVo.getItemNumber())) {
                    productItemVos.add(productItemVo);
                }
            }

            Map<String, ProductItemVo> productItemMap = new HashMap<>();
            List<String> numbers = CommonUtil.column(productItemVos, ProductItemVo::getItemNumber);

            QueryWrapper<ProductItem> itemQueryWrapper = new QueryWrapper<>();
            itemQueryWrapper.in("item_number", numbers);
            List<ProductItem> productItemList = productItemRepository.find(itemQueryWrapper);

            if (CollectionUtil.isNotEmpty(productItemList)) {
                List<Long> itemIds = CommonUtil.column(productItemList, ProductItem::getItemId);
                List<ProductItemVo> itemVos = productBaseRepository.getItems(itemIds, null);

                if (CollectionUtil.isNotEmpty(itemVos)) {
                    itemVos.removeIf(item -> !Objects.equals(item.getProductStateId(), StateCode.PRODUCT_STATE_NORMAL));

                    if (configBaseService.ifIndustry()) {
                        itemVos.removeIf(item -> !Convert.toList(Integer.class, item.getIndustryIds()).contains(user.getIndustryId()));
                    }

                    productItemMap = itemVos.stream().collect(Collectors.toMap(ProductItemVo::getItemNumber, ProductItemVo -> ProductItemVo, (k1, k2) -> k1));
                }
            }

            List<BatchOrder> batchOrders = new ArrayList<>();

            for (ProductItemVo itemTemp : productItemVos) {
                BatchOrder batchOrder = new BatchOrder();
                BeanUtils.copyProperties(itemTemp, batchOrder);
                batchOrder.setUserId(userId);
                String itemNumber = itemTemp.getItemNumber();

                if (productItemMap.containsKey(itemNumber)) {
                    ProductItemVo productItemVo = productItemMap.get(itemNumber);

                    batchOrder.setItemId(productItemVo.getItemId());

                    if (CheckUtil.isNotEmpty(productItemVo.getItemUnitPrice())) {
                        batchOrder.setBatchMatchStatus(1);
                    } else {
                        batchOrder.setBatchMatchStatus(2);
                    }
                }else {
                    batchOrder.setBatchMatchStatus(3);
                }
                batchOrders.add(batchOrder);
            }

            if (!batchOrderRepository.saveOrUpdate(batchOrders)) {
                throw new BusinessException(__("保存批量下单数据失败！"));
            }
            // 清除数据
            itemTemps.clear();
        }
    }
}
