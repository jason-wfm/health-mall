package com.wechuang.mallshop.shop.controller.front;

import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.shop.model.res.SearchInfoRes;
import com.wechuang.mallshop.shop.service.UserSearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商城移动端首页
 * </p>
 *
 * @author Xinze
 * @since 2021-05-28
 */
@Tag(name = "商城移动端首页")
@RestController
@RequestMapping("/front/shop/mobile")
public class MobileController extends BaseController {

    @Autowired
    private UserSearchHistoryService userSearchHistoryService;

    @Operation(summary = "返回搜索关键词", description = "用户最新搜索记录及系统推荐搜索关键词")
    @RequestMapping(value = "/getSearchInfo", method = RequestMethod.GET)
    public CommonRes<SearchInfoRes> getSearchInfo(@RequestParam(name = "source_lang", required = false) String sourceLang) {
        return success(userSearchHistoryService.getSearchInfo(sourceLang));
    }

}
