package com.wechuang.mallshop.sys.controller.front;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import com.wechuang.mallshop.sys.model.entity.FeedbackBase;
import com.wechuang.mallshop.sys.model.req.FeedbackBaseAddReq;
import com.wechuang.mallshop.sys.model.req.FeedbackBaseListReq;
import com.wechuang.mallshop.sys.model.res.FeedbackTypeRes;
import com.wechuang.mallshop.sys.service.FeedbackBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户反馈反馈")
@RestController
@RequestMapping("/front/sys/feedback")
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackBaseService feedbackService;

    @Operation(summary = "平台反馈-举报", description = "平台反馈-举报")
    @RequestMapping(value = "/getCategory", method = RequestMethod.GET)
    public CommonRes<List<FeedbackTypeRes>> getCategory() {
        List<FeedbackTypeRes> feedbackTypeResList = feedbackService.getCategory();

        return success(feedbackTypeResList);
    }

    @Operation(summary = "平台反馈-反馈列表", description = "平台反馈-反馈列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<FeedbackBase>> lists(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(name = "size", defaultValue = "10") Integer size) {
        FeedbackBaseListReq feedbackBaseListReq = new FeedbackBaseListReq();
        feedbackBaseListReq.setPage(page);
        feedbackBaseListReq.setSize(size);
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        feedbackBaseListReq.setUserId(user.getUserId());
        IPage<FeedbackBase> pageList = feedbackService.lists(feedbackBaseListReq);

        return success(pageList);
    }

    @Operation(summary = "添加平台反馈-举报", description = "添加平台反馈-举报")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<?> add(FeedbackBaseAddReq feedbackBaseAddReq) {
        FeedbackBase feedbackBase = BeanUtil.copyProperties(feedbackBaseAddReq, FeedbackBase.class);
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        feedbackBase.setUserId(user.getUserId());
        feedbackBase.setUserNickname(user.getUserNickname());

        boolean result = feedbackService.add(feedbackBase);

        if (result) {
            return success();
        }

        return fail();
    }

}
