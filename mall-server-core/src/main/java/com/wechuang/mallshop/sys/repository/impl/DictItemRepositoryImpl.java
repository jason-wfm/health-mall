package com.wechuang.mallshop.sys.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.sys.dao.DictItemDao;
import com.wechuang.mallshop.sys.model.entity.DictItem;
import com.wechuang.mallshop.sys.repository.DictItemRepository;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 字典项表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Repository
public class DictItemRepositoryImpl extends BaseRepositoryImpl<DictItemDao, DictItem> implements DictItemRepository {

}
