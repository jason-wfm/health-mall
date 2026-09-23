package com.wechuang.mallshop.shop.controller.front;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.shop.model.req.StoreBaseEditReq;
import com.wechuang.mallshop.shop.model.req.StoreBaseListReq;
import com.wechuang.mallshop.shop.model.req.StoreCategoryListReq;
import com.wechuang.mallshop.shop.model.res.StoreBaseRes;
import com.wechuang.mallshop.shop.model.res.StoreCategoryRes;
import com.wechuang.mallshop.shop.model.vo.StoreDetailVo;
import com.wechuang.mallshop.shop.service.StoreBaseService;
import com.wechuang.mallshop.shop.service.StoreCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "店铺")
@RestController
@RequestMapping("/front/shop/store")
public class StoreController extends BaseController {

    @Autowired
    private StoreBaseService storeBaseService;

    @Autowired
    private StoreCategoryService storeCategoryService;

    @Operation(summary = "店铺列表", description = "店铺列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<StoreBaseRes>> list(StoreBaseListReq storeBaseListReq) {
        IPage<StoreBaseRes> storeResIPage = storeBaseService.getStoreList(storeBaseListReq);

        return success(storeResIPage);
    }

    @Operation(summary = "店铺详情", description = "店铺详情")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public CommonRes<StoreDetailVo> info(@RequestParam(value = "store_id") Integer storeId) {
        StoreDetailVo storeDetail = storeBaseService.getStoreDetail(storeId);

        return success(storeDetail);
    }

    @Operation(summary = "商家入驻", description = "商家入驻")
    @RequestMapping(value = "/storeEnter", method = RequestMethod.POST)
    public CommonRes<?> storeEnter(StoreBaseEditReq storeBaseEditReq) {

        return success(storeBaseService.storeEnter(storeBaseEditReq));
    }

    @Operation(summary = "店铺信息", description = "店铺信息")
    @RequestMapping(value = "/getStoreInfo", method = RequestMethod.GET)
    public CommonRes<StoreDetailVo> getStoreInfo() {
        StoreDetailVo storeDetail = storeBaseService.getStoreInfo();

        return success(storeDetail);
    }

    @Operation(summary = "市场分类", description = "市场分类")
    @RequestMapping(value = "/getStoreCategoryTree", method = RequestMethod.GET)
    public CommonRes <List<StoreCategoryRes>> getStoreCategoryTree(StoreCategoryListReq storeCategoryListReq) {
        storeCategoryListReq.setStoreCategoryIsEnable(true);
        List<StoreCategoryRes> list = storeCategoryService.getTree(storeCategoryListReq);

        return success(list);
    }

    @Operation(summary = "店铺街过滤", description = "店铺街过滤")
    @RequestMapping(value = "/getFilterOpt", method = RequestMethod.GET)
    public CommonRes <List<Map>> getFilterOpt() {
        List<Map> result = storeBaseService.getFilterOpt();

        return success(result);
    }

}
