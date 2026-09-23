package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.MaterialGallery;
import com.wechuang.mallshop.sys.model.req.MaterialGalleryAddReq;
import com.wechuang.mallshop.sys.model.req.MaterialGalleryEditReq;
import com.wechuang.mallshop.sys.model.req.MaterialGalleryListReq;
import com.wechuang.mallshop.sys.service.MaterialGalleryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;

/**
 * <p>
 * 素材分类表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Tag(name = "素材分类表")
@RestController
@RequestMapping("/manage/sys/material")
public class MaterialGalleryController extends BaseController {
    @Autowired
    private MaterialGalleryService materialGalleryService;

    @PreAuthorize("hasAuthority('/manage/sys/material/list')")
    @Operation(summary = "素材分类表-分页列表查询", description = "素材分类表-分页列表查询")
    @RequestMapping(value = "/listGallery", method = RequestMethod.GET)
    public CommonRes<BaseListRes<MaterialGallery>> list(MaterialGalleryListReq materialGalleryListReq) {
        materialGalleryListReq.setSidx("gallery_sort");
        IPage<MaterialGallery> pageList = materialGalleryService.lists(materialGalleryListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/add')")
    @Operation(summary = "素材分类表-添加", description = "素材分类表-添加")
    @RequestMapping(value = "/addGallery", method = RequestMethod.POST)
    public CommonRes<?> add(MaterialGalleryAddReq materialGalleryAddReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        if (CheckUtil.isNotEmpty(loginUser.getStoreId())) {
            materialGalleryAddReq.setStoreId(loginUser.getStoreId());
        }

        MaterialGallery materialGallery = BeanUtil.copyProperties(materialGalleryAddReq, MaterialGallery.class);
        boolean success = materialGalleryService.add(materialGallery);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/edit')")
    @Operation(summary = "素材分类表-编辑", description = "素材分类表-编辑")
    @RequestMapping(value = "/editGallery", method = RequestMethod.POST)
    public CommonRes<?> edit(MaterialGalleryEditReq materialGalleryEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        MaterialGallery gallery = materialGalleryService.get(materialGalleryEditReq.getGalleryId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), gallery, MaterialGallery::getStoreId)) {
            MaterialGallery materialGallery = BeanUtil.copyProperties(materialGalleryEditReq, MaterialGallery.class);
            boolean success = materialGalleryService.edit(materialGallery);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/remove')")
    @Operation(summary = "素材分类表-通过gallery_id删除", description = "素材分类表-通过gallery_id删除")
    @RequestMapping(value = "/removeGallery", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("gallery_id") String galleryId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        MaterialGallery gallery = materialGalleryService.get(galleryId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), gallery, MaterialGallery::getStoreId)) {
            boolean success = materialGalleryService.remove(galleryId);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }
}

