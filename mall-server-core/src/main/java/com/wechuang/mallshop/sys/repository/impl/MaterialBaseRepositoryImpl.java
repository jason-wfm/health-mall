package com.wechuang.mallshop.sys.repository.impl;

import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.sys.dao.MaterialBaseDao;
import com.wechuang.mallshop.sys.model.entity.MaterialBase;
import com.wechuang.mallshop.sys.repository.MaterialBaseRepository;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 素材表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Repository
public class MaterialBaseRepositoryImpl extends BaseRepositoryImpl<MaterialBaseDao, MaterialBase> implements MaterialBaseRepository {

}
