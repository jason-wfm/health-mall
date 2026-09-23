package com.wechuang.mallshop.account.controller.front;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.account.model.entity.UserMessage;
import com.wechuang.mallshop.account.model.input.UserMessageAddInput;
import com.wechuang.mallshop.account.model.req.UserMessageListReq;
import com.wechuang.mallshop.account.model.res.MessageRes;
import com.wechuang.mallshop.account.model.res.UserMessageRes;
import com.wechuang.mallshop.account.service.UserMessageService;
import com.wechuang.mallshop.admin.service.UserAdminService;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.controller.BaseController;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户站内信 前端控制器
 * </p>
 *
 * @author Xinze
 * @since 2022-11-27
 */
@Tag(name = "用户站内信")
@RestController
@RequestMapping("/front/account/userMessage")
public class MessageController extends BaseController {

    @Autowired
    private UserMessageService messageService;

    @Autowired
    private UserAdminService userAdminService;

    @Operation(summary = "IM配置", description = "IM配置接口")
    @RequestMapping(value = "/getImConfig", method = RequestMethod.GET)
    public CommonRes<?> getImConfig(@RequestParam(value = "user_other_id", required = false, defaultValue = "0") Integer userOtherId,
                                    @RequestParam(value = "chat_item_id", required = false, defaultValue = "0") Long chatItemId,
                                    @RequestParam(value = "store_id", required = false, defaultValue = "0") Integer storeId,
                                    @RequestParam(value = "chat_order_id", required = false, defaultValue = "") String chatOrderId) {
        ContextUser user = ContextUtil.getLoginUser();
        Integer userId = ObjectUtil.isNotNull(user) ? user.getUserId() : null;

        if (userId == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        // 根据店铺ID查询 管理员用户ID
        if (storeId != null && storeId > 0) {
            userOtherId = userAdminService.getUserIdByStoreId(storeId);
        }

        return success(messageService.getImConfig(userId, userOtherId, chatItemId, chatOrderId));
    }

    @Operation(summary = "客服配置", description = "客服配置接口")
    @RequestMapping(value = "/getKefuConfig", method = RequestMethod.GET)
    public CommonRes<?> getKefuConfig(@RequestParam(value = "store_id", required = false, defaultValue = "0") Integer storeId) {
        ContextUser user = ContextUtil.getLoginUser();
        Integer userId = ObjectUtil.isNotNull(user) ? user.getUserId() : null;

        if (userId == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }


        // 根据店铺ID查询 管理员用户ID
        Integer friendId = 0;
        if (storeId != null && storeId > 0) {
            friendId = userAdminService.getUserIdByStoreId(storeId);
        }

        return success(messageService.getKefuConfig(userId, storeId, friendId));
    }

    @Operation(summary = "用户通知消息", description = "用户消息列表接口")
    @RequestMapping(value = "/getNotice", method = RequestMethod.GET)
    public CommonRes<List<UserMessage>> getNotice() {
        Integer userId = ContextUtil.checkLoginUserId();
        List<UserMessage> userMessages = messageService.getNotice(userId);

        return success(userMessages);
    }

    @Operation(summary = "用户通知消息数量", description = "用户通知消息数量")
    @RequestMapping(value = "/getMsgCount", method = RequestMethod.GET)
    public CommonRes<UserMessageRes> getMsgCount(@RequestParam(value = "recently_flag", required = false) Boolean recentlyFlag) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserMessageRes userMessageRes = messageService.getMsgCount(recentlyFlag, userId);

        return success(userMessageRes);
    }

    @Operation(summary = "短消息列表数据", description = "短消息列表数据")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonRes<BaseListRes<UserMessageRes>> list(UserMessageListReq userMessageListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userMessageListReq.setUserId(userId);
        IPage<UserMessageRes> pageList = messageService.getList(userMessageListReq);

        return success(pageList);
    }

    @Operation(summary = "读取短消息", description = "读取短消息")
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonRes<UserMessage> get(@RequestParam(value = "message_id") Integer messageId) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserMessage userMessage = messageService.getById(messageId, userId);

        return success(userMessage);
    }

    @Operation(summary = "设置为已读", description = "设置为已读")
    @RequestMapping(value = "/setRead", method = RequestMethod.POST)
    public CommonRes<?> setRead(@RequestParam(value = "message_id", required = false) Integer messageId,
                                @RequestParam(value = "user_other_id", required = false) Integer userOtherId) {
        Integer userId = ContextUtil.checkLoginUserId();
        boolean success = messageService.setRead(messageId, userOtherId, userId);

        if (success) {
            return success();
        }

        return fail();
    }

    @Operation(summary = "添加短消息", description = "添加短消息")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonRes<UserMessageRes> add(UserMessageAddInput messageAddInput) {
        Integer userId = ContextUtil.checkLoginUserId();
        UserMessageRes userMessageRes = messageService.addMessage(messageAddInput, userId);

        return success(userMessageRes);
    }

    @Operation(summary = "读取分页列表", description = "读取分页列表")
    @RequestMapping(value = "/listChatMsg", method = RequestMethod.GET)
    public CommonRes<BaseListRes<MessageRes>> listChatMsg(UserMessageListReq userMessageListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userMessageListReq.setUserId(userId);
        IPage<MessageRes> pageList = messageService.listChatMsg(userMessageListReq);

        return success(pageList);
    }


    @Operation(summary = "消息中心-信息数", description = "消息中心-信息数")
    @RequestMapping(value = "/getMessageNum", method = RequestMethod.GET)
    public CommonRes<UserMessageRes> getMessageNum(UserMessageListReq userMessageListReq) {
        Integer userId = ContextUtil.checkLoginUserId();
        userMessageListReq.setUserId(userId);
        UserMessageRes userMessageRes = messageService.getMessageNum(userMessageListReq);

        return success(userMessageRes);
    }
}

