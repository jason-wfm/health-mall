package com.wechuang.mallshop.marketing.controller.front;

import cn.hutool.core.convert.Convert;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.marketing.model.req.ActivityBaseListReq;
import com.wechuang.mallshop.marketing.model.res.ActivityBaseRes;
import com.wechuang.mallshop.marketing.service.ActivityBaseService;
import com.wechuang.mallshop.pt.model.input.ProductItemInput;
import com.wechuang.mallshop.pt.model.req.ProductItemListReq;
import com.wechuang.mallshop.pt.model.res.ItemListRes;
import com.wechuang.mallshop.pt.service.ProductIndexService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 开源版营销活动：仅优惠券列表、活动列表（优惠券+弹窗）、活动商品详情。
 */
@Tag(name = "营销活动-开源版")
@RestController
@RequestMapping("/front/marketing/activityBase")
public class ActivityController extends BaseController {

    @Autowired
    private ActivityBaseService activityBaseService;

    @Resource
    private ProductIndexService productIndexService;

    @Operation(summary = "活动表-优惠券列表", description = "活动表-优惠券列表")
    @RequestMapping(value = "/listVoucher", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ActivityBaseRes>> listVoucher(ActivityBaseListReq activityBaseListReq) {
        Integer userId = ContextUtil.getLoginUserId();
        activityBaseListReq.setUserId(userId);
        IPage<ActivityBaseRes> activityPage = activityBaseService.listVoucher(activityBaseListReq);

        return success(activityPage);
    }

    @Operation(summary = "活动表", description = "活动表（开源版仅优惠券与弹窗）")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<ActivityBaseRes>> list(ActivityBaseListReq activityBaseListReq) {
        Integer userId = ContextUtil.getLoginUserId();
        activityBaseListReq.setUserId(userId);
        IPage<ActivityBaseRes> activityPage = activityBaseService.getList(activityBaseListReq);

        return success(activityPage);
    }

    @Operation(summary = "活动商品详情", description = "活动商品详情")
    @RequestMapping(value = "/getActivityInfo", method = RequestMethod.GET)
    public CommonRes<ItemListRes> getActivityInfo(ProductItemListReq req) {
        ProductItemInput input = new ProductItemInput();
        BeanUtils.copyProperties(req, input);

        input.setItemId(Convert.toList(Long.class, req.getItemId()));

        ItemListRes pageList = productIndexService.listItem(input);

        return success(pageList);
    }
}
