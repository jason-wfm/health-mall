package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.MaterialBase;
import com.wechuang.mallshop.sys.model.req.MaterialBaseEditReq;
import com.wechuang.mallshop.sys.model.req.MaterialBaseListReq;
import com.wechuang.mallshop.sys.service.MaterialBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wechuang.mallshop.common.utils.ContextUtil.getLoginUser;
import static com.wechuang.mallshop.common.utils.UploadUtil.getUploadBaseDir;
import static com.wechuang.mallshop.common.utils.UploadUtil.getUploadSmDir;

/**
 * <p>
 * 素材表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Tag(name = "素材表")
@RestController
@RequestMapping("/manage/sys/material")
public class MaterialBaseController extends BaseController {
    @Autowired
    private MaterialBaseService materialBaseService;

    @PreAuthorize("hasAuthority('/manage/sys/material/list')")
    @Operation(summary = "素材表-分页列表查询", description = "素材表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<MaterialBase>> list(MaterialBaseListReq materialBaseListReq) {
        IPage<MaterialBase> pageList = materialBaseService.lists(materialBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/edit')")
    @Operation(summary = "素材表-编辑", description = "素材表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(MaterialBaseEditReq materialBaseEditReq) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        MaterialBase material = materialBaseService.get(materialBaseEditReq.getMaterialId());

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), material, MaterialBase::getStoreId)) {
            MaterialBase materialBase = BeanUtil.copyProperties(materialBaseEditReq, MaterialBase.class);
            boolean success = materialBaseService.edit(materialBase);

            if (success) {
                return success();
            }

            return fail();
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/remove')")
    @Operation(summary = "素材表-通过material_id删除", description = "素材表-通过material_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("material_id") String materialId) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        MaterialBase record = materialBaseService.get(materialId);

        if (loginUser.isPlatform() || CheckUtil.checkDataRights(loginUser.getStoreId(), record, MaterialBase::getStoreId)) {

            if (materialBaseService.remove(materialId)) {
                if (StrUtil.isNotBlank(record.getMaterialPath())) {
                    materialBaseService.deleteFileAsync(Arrays.asList(
                            new File(getUploadBaseDir(), record.getMaterialPath()),
                            new File(getUploadSmDir(), record.getMaterialPath())
                    ));
                }
                return success("删除成功");
            }
            return fail("删除失败");
        } else {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('/manage/sys/material/removeBatch')")
    @Operation(summary = "素材表-批量删除", description = "素材表-批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.POST)
    public CommonRes<?> removeBatch(@RequestParam("material_id") String materialIds) {
        ContextUser loginUser = getLoginUser();

        if (loginUser == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        List<Long> ids = Convert.toList(Long.class, materialIds);

        List<MaterialBase> MaterialBases = materialBaseService.gets(ids);

        if (CollectionUtil.isNotEmpty(MaterialBases)) {
            for (MaterialBase materialBase : MaterialBases) {
                if (!loginUser.isPlatform() && !CheckUtil.checkDataRights(loginUser.getStoreId(), materialBase, MaterialBase::getStoreId)) {
                    throw new BusinessException(ResultCode.FORBIDDEN);
                }
            }
        }

        if (materialBaseService.remove(ids)) {
            List<File> files = new ArrayList<>();
            for (MaterialBase record : MaterialBases) {
                if (StrUtil.isNotBlank(record.getMaterialPath())) {
                    files.add(new File(getUploadBaseDir(), record.getMaterialPath()));
                    files.add(new File(getUploadSmDir(), record.getMaterialPath()));
                }
            }
            materialBaseService.deleteFileAsync(files);
            return success("删除成功");
        }
        return fail("删除失败");
    }
}

