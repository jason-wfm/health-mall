package com.wechuang.mallshop.sys.service;

import com.wechuang.mallshop.core.web.service.IBaseService;
import com.wechuang.mallshop.sys.model.entity.MaterialBase;
import com.wechuang.mallshop.sys.model.req.MaterialBaseListReq;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 素材表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
public interface MaterialBaseService extends IBaseService<MaterialBase, MaterialBaseListReq> {
    /**
     * 异步删除文件
     *
     * @param files 文件数组
     */
    void deleteFileAsync(List<File> files);
}
