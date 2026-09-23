package com.wechuang.mallshop.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.common.consts.ConstantLog;
import com.wechuang.mallshop.common.utils.LogUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.MaterialBase;
import com.wechuang.mallshop.sys.model.req.MaterialBaseListReq;
import com.wechuang.mallshop.sys.repository.MaterialBaseRepository;
import com.wechuang.mallshop.sys.service.MaterialBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 素材表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Service
public class MaterialBaseServiceImpl extends BaseServiceImpl<MaterialBaseRepository, MaterialBase, MaterialBaseListReq> implements MaterialBaseService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialBaseServiceImpl.class);

    @Override
    public boolean remove(Serializable materialId) {
        return super.remove(materialId);
    }

    @Override
    public Page<MaterialBase> lists(QueryWrapper<MaterialBase> wrapper, Integer pageNum, Integer pageSize) {
        wrapper.orderByAsc("material_sort");
        return super.lists(wrapper, pageNum, pageSize);
    }

    @Async
    @Override
    public void deleteFileAsync(List<File> files) {
        for (File file : files) {
            try {
                file.delete();
            } catch (Exception e) {
                LogUtil.error(ConstantLog.UPLOAD, e);
            }
        }
    }
}
