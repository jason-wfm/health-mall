package com.wechuang.mallshop.pay.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pay.dao.StoreSettlementOrderDao;
import com.wechuang.mallshop.pay.model.entity.StoreSettlementOrder;
import com.wechuang.mallshop.pay.repository.StoreSettlementOrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StoreSettlementOrderRepositoryImpl extends BaseRepositoryImpl<StoreSettlementOrderDao, StoreSettlementOrder> implements StoreSettlementOrderRepository {

}
