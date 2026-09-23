package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.ConfigType;
import com.wechuang.mallshop.sys.model.req.ConfigTypeAddReq;
import com.wechuang.mallshop.sys.model.req.ConfigTypeEditReq;
import com.wechuang.mallshop.sys.model.req.ConfigTypeListReq;
import com.wechuang.mallshop.sys.service.ConfigTypeService;
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
 * 配置分组表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "配置分组表")
@RestController
@RequestMapping("/manage/sys/config")
public class ConfigTypeController extends BaseController {
    @Autowired
    private ConfigTypeService configTypeService;

    @PreAuthorize("hasAuthority('/manage/sys/config/list')")
    @Operation(summary = "配置分组表-分页列表查询", description = "配置分组表-分页列表查询")
    @RequestMapping(value = "/listType", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ConfigType>> list(ConfigTypeListReq configTypeListReq) {
        IPage<ConfigType> pageList = configTypeService.lists(new BaseQueryWrapper<ConfigType, ConfigTypeListReq>(configTypeListReq).getWrapper().orderByAsc("config_type_id"), configTypeListReq.getPage(), configTypeListReq.getSize());

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/add')")
    @Operation(summary = "配置分组表-添加", description = "配置分组表-添加")
    @RequestMapping(value = "/addType", method = RequestMethod.POST)
    public CommonRes<?> add(ConfigTypeAddReq configTypeAddReq) {
        ConfigType configType = BeanUtil.copyProperties(configTypeAddReq, ConfigType.class);
        boolean success = configTypeService.add(configType);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/edit')")
    @Operation(summary = "配置分组表-编辑", description = "配置分组表-编辑")
    @RequestMapping(value = "/editType", method = RequestMethod.POST)
    public CommonRes<?> edit(ConfigTypeEditReq configTypeEditReq) {
        ConfigType configType = BeanUtil.copyProperties(configTypeEditReq, ConfigType.class);
        boolean success = configTypeService.edit(configType);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/remove')")
    @Operation(summary = "配置分组表-通过config_type_id删除", description = "配置分组表-通过config_type_id删除")
    @RequestMapping(value = "/removeType", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("config_type_id") Integer configTypeId) {
        boolean success = configTypeService.remove(configTypeId);

        if (success) {
            return success();
        }

        return fail();
    }
}

