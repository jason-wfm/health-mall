package com.wechuang.mallshop.sys.controller.manage;

import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 清理服务端缓存 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Tag(name = "清理服务端缓存")
@RestController
@RequestMapping("/manage/sys/cache")
public class CacleController extends BaseController {

    @Autowired
    private ConfigBaseService configBaseService;

    @PreAuthorize("hasAuthority('/manage/sys/cache/clean')")
    @Operation(summary = "清理服务端缓存", description = "清理服务端缓存")
    @RequestMapping(value = "/clean", method = RequestMethod.POST)
    public CommonRes<?> clean() {
        configBaseService.cleanCache();

        return success(__("缓存清理成功"));
    }

}
