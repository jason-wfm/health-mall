package com.wechuang.mallshop.sys.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.SmsDto;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.core.web.service.CloundService;
import com.wechuang.mallshop.sys.model.entity.ConfigBase;
import com.wechuang.mallshop.sys.model.req.*;
import com.wechuang.mallshop.sys.model.res.ConfigListRes;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import com.wechuang.mallshop.sys.service.ConfigTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 系统参数设置表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "系统参数设置表")
@RestController
@RequestMapping("/manage/sys/config")
public class ConfigBaseController extends BaseController {
    @Autowired
    private ConfigBaseService configBaseService;

    @Autowired
    private ConfigTypeService configTypeService;

    @Autowired
    private CloundService cloundService;

    @PreAuthorize("hasAuthority('/manage/sys/config/list')")
    @Operation(summary = "系统参数设置表-分页列表查询", description = "系统参数设置表-分页列表查询")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ConfigBase>> list(ConfigBaseListReq configBaseListReq) {
        IPage<ConfigBase> pageList = configBaseService.lists(new BaseQueryWrapper<ConfigBase, ConfigBaseListReq>(configBaseListReq).getWrapper().orderByAsc("config_sort"), configBaseListReq.getPage(), configBaseListReq.getSize());

        return success(pageList);
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/add')")
    @Operation(summary = "系统参数设置表-添加", description = "系统参数设置表-添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(ConfigBaseAddReq configBaseAddReq) {
        ConfigBase configBase = BeanUtil.copyProperties(configBaseAddReq, ConfigBase.class);
        boolean success = configBaseService.add(configBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/edit')")
    @Operation(summary = "系统参数设置表-编辑", description = "系统参数设置表-编辑")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public CommonRes<?> edit(ConfigBaseEditReq configBaseEditReq) {
        ConfigBase configBase = BeanUtil.copyProperties(configBaseEditReq, ConfigBase.class);
        boolean success = configBaseService.edit(configBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/edit')")
    @Operation(summary = "系统参数设置表-修改状态", description = "系统参数设置表-修改状态")
    @RequestMapping(value = "/editState", method = RequestMethod.POST)
    public CommonRes<?> editState(ConfigBaseStateEditReq configBaseStateEditReq) {
        ConfigBase configBase = BeanUtil.copyProperties(configBaseStateEditReq, ConfigBase.class);
        boolean success = configBaseService.edit(configBase);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/remove')")
    @Operation(summary = "系统参数设置表-通过config_key删除", description = "系统参数设置表-通过config_key删除")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public CommonRes<?> remove(@RequestParam("config_key") String configKey) {
        boolean success = configBaseService.remove(configKey);

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/removeBatch')")
    @Operation(summary = "系统参数设置表-批量删除", description = "系统参数设置表-批量删除")
    @RequestMapping(value = "/removeBatch", method = RequestMethod.GET)
    public CommonRes<?> removeBatch(@RequestParam("config_key") String configKeys) {
        boolean success = configBaseService.remove(Convert.toList(String.class, configKeys));

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/editSite')")
    @Operation(summary = "站点设置-保存更改", description = "站点设置-保存更改")
    @PostMapping(value = "/editSite")
    public CommonRes<?> editSite(@ModelAttribute ConfigBaseEditSiteReq objectMap) {
        boolean success = configBaseService.editSite(objectMap.getConfigs());

        if (success) {
            return success();
        }

        return fail();
    }

    @PreAuthorize("hasAuthority('/manage/sys/config/editSite')")
    @Operation(summary = "站点设置", description = "站点设置")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public CommonRes<ConfigListRes> index(ConfigBaseIndexReq configBaseIndexReq) {
        ConfigListRes configListRes = configBaseService.index(configBaseIndexReq);

        return success(configListRes);
    }

    @Operation(summary = "站点帮助", description = "站点帮助")
    @RequestMapping(value = "/savePcHelp", method = RequestMethod.POST)
    public CommonRes<?> savePcHelp(@RequestParam(name = "pc_help") String pcHelp) {
        ConfigBase configBase = new ConfigBase();
        configBase.setConfigKey("page_pc_help");
        configBase.setConfigValue(pcHelp);
        configBase.setConfigTypeId(0);
        configBase.setConfigDatatype("text");
        configBase.setConfigBuildin(true);
        configBaseService.save(configBase);
        return success();
    }

    @Operation(summary = "系统参数设置表-详细配置", description = "系统参数设置表-详细配置")
    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    public CommonRes<ConfigBase> getDetail(@RequestParam(name = "config_key") String configKey) {
        ConfigBase configBase = configBaseService.get(configKey);

        return success(configBase);
    }

    @Operation(summary = "消息模板表-分页列表查询", description = "消息模板表-分页列表查询")
    @RequestMapping(value = "/smsRecord", method = RequestMethod.GET)
    public CommonRes<?> smsRecord(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "rows", defaultValue = "10") Integer rows) throws Exception {
        String serviceUserId = configBaseService.getConfig("service_user_id", "");
        String serviceAppKey = configBaseService.getConfig("service_app_key", "");
        //从云服务器读取
        SmsDto smsDto = new SmsDto();
        smsDto.setServiceUserId(serviceUserId);
        smsDto.setServiceAppKey(serviceAppKey);

        return success(cloundService.listSmsRecords(smsDto, page, rows));
    }

}

