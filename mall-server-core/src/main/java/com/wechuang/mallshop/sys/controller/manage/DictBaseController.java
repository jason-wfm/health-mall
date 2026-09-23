package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.DictBase;
import com.wechuang.mallshop.sys.model.req.DictBaseAddReq;
import com.wechuang.mallshop.sys.model.req.DictBaseEditReq;
import com.wechuang.mallshop.sys.model.req.DictBaseListReq;
import com.wechuang.mallshop.sys.service.DictBaseService;
import com.wechuang.mallshop.sys.service.DictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 字典类型表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Tag(name = "字典类型表")
@RestController
@RequestMapping("/manage/sys/dict")
public class DictBaseController extends BaseController {
    @Autowired
    private DictBaseService dictBaseService;

    @Autowired
    private DictItemService dictItemService;

    @PreAuthorize("hasAuthority('/manage/sys/dict/list')")
    @Operation(summary = "字典类型表-分页列表查询", description = "字典类型表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<DictBase>> list(DictBaseListReq dictBaseListReq) {
        IPage<DictBase> pageList = dictBaseService.lists(dictBaseListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/add')")
    @Operation(summary = "字典类型表-添加", description = "字典类型表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(DictBaseAddReq dictBaseAddReq) {
        DictBase dictBase = BeanUtil.copyProperties(dictBaseAddReq, DictBase.class);
        boolean success = dictBaseService.add(dictBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/edit')")
    @Operation(summary = "字典类型表-编辑", description = "字典类型表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(DictBaseEditReq dictBaseEditReq) {
        DictBase dictBase = BeanUtil.copyProperties(dictBaseEditReq, DictBase.class);
        boolean success = dictBaseService.edit(dictBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/remove')")
    @Operation(summary = "字典类型表-通过dict_id删除", description = "字典类型表-通过dict_id删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("dict_id") String dictId) {
        boolean success = dictBaseService.remove(dictId);

        if (success) {
            return success();
        }

        return fail();
    }
}

