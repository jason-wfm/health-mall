package com.wechuang.mallshop.sys.controller.front;

import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.sys.model.req.DistrictBaseListReq;
import com.wechuang.mallshop.sys.model.res.DistrictBaseRes;
import com.wechuang.mallshop.sys.service.DistrictBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 地区管理 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-29
 */
@Tag(name = "地区管理")
@RestController
@RequestMapping("/front/sys/district")
public class DistrictController extends BaseController {
    @Autowired
    private DistrictBaseService districtBaseService;

    @Operation(summary = "区域管理", description = "获得地址区域")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public CommonRes<List<DistrictBaseRes>> tree(DistrictBaseListReq DistrictBaseListReq) {
        List<DistrictBaseRes> districtTree = districtBaseService.getTree(DistrictBaseListReq);

        return success(districtTree);
    }
}

