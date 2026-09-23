package com.wechuang.mallshop.pt.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.wechuang.mallshop.marketing.model.vo.ActivityInfoVo;
import com.wechuang.mallshop.pt.model.entity.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductDataBundleVo {
    
    List<ProductItem> itemList;

    Map<Long, ProductIndex> productIndexMap;

    Map<Long, ProductBase> productBaseMap;

    Map<Long, ActivityInfoVo> activityMap;

    Map<String, ProductImage> imageMap;

    Map<Long, Integer> levelDiscountMap;

    Map<Long, ProductPricingPolicy> policyMap;

}
