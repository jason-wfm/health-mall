package com.wechuang.mallshop.pay.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.pay.dao.StoreSettlementDao;
import com.wechuang.mallshop.pay.model.entity.StoreSettlement;
import com.wechuang.mallshop.pay.repository.StoreSettlementRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StoreSettlementRepositoryImpl extends BaseRepositoryImpl<StoreSettlementDao, StoreSettlement> implements StoreSettlementRepository {

}
