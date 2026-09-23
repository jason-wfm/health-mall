package com.wechuang.mallshop.sys.controller.front;

import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.sys.model.entity.ExpressBase;
import com.wechuang.mallshop.sys.service.ExpressBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 快递表 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "快递表")
@RestController
@RequestMapping("/front/sys/express")
public class ExpressController extends BaseController {

    @Autowired
    private ExpressBaseService expressBaseService;

    @Operation(summary = "快递表-获取列表数据", description = "快递表-获取列表数据")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<?> list() {
        List<ExpressBase> expressBaseList = expressBaseService.getList();
        return success(expressBaseList);
    }

}
