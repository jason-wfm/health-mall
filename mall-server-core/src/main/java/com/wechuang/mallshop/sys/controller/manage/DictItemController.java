package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.DictItem;
import com.wechuang.mallshop.sys.model.req.DictItemAddReq;
import com.wechuang.mallshop.sys.model.req.DictItemEditReq;
import com.wechuang.mallshop.sys.model.req.DictItemListReq;
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
 * 字典项表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Tag(name = "字典项表")
@RestController
@RequestMapping("/manage/sys/dict")
public class DictItemController extends BaseController {
    @Autowired
    private DictItemService dictItemService;

    @PreAuthorize("hasAuthority('/manage/sys/dict/listItem')")
    @Operation(summary = "字典项表-分页列表查询", description = "字典项表-分页列表查询")
    @RequestMapping(value = "/listItem", method = RequestMethod.GET)
    public CommonRes<BaseListRes<DictItem>> list(DictItemListReq dictItemListReq) {
        IPage<DictItem> pageList = dictItemService.lists(dictItemListReq);

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/addItem')")
    @Operation(summary = "字典项表-添加", description = "字典项表-添加")
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public CommonRes<?> add(DictItemAddReq dictItemAddReq) {
        DictItem dictItem = BeanUtil.copyProperties(dictItemAddReq, DictItem.class);
        boolean success = dictItemService.add(dictItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/editItem')")
    @Operation(summary = "字典项表-编辑", description = "字典项表-编辑")
    @RequestMapping(value = "/editItem", method = RequestMethod.POST)
    public CommonRes<?> edit(DictItemEditReq dictItemEditReq) {
        DictItem dictItem = BeanUtil.copyProperties(dictItemEditReq, DictItem.class);
        boolean success = dictItemService.edit(dictItem);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/dict/removeItem')")
    @Operation(summary = "字典项表-通过dict_item_id删除", description = "字典项表-通过dict_item_id删除")
    @RequestMapping(value = "/removeItem", method = RequestMethod.POST)
    public CommonRes<?> removeItem(@RequestParam("dict_item_id") String dictItemId) {
        boolean success = dictItemService.remove(dictItemId);

        if (success) {
            return success();
        }

        return fail();
    }
}

