package com.wechuang.mallshop.trade.model.vo;

import com.wechuang.mallshop.shop.model.entity.StoreAnalytics;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class StoreAnalyticsVo extends StoreAnalytics implements Serializable {

    private StoreCreditVo storeCredit;

}
