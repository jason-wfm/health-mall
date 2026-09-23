package com.wechuang.mallshop.account.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.UserInfo;
import com.wechuang.mallshop.account.model.output.UserInfoOutput;
import com.wechuang.mallshop.account.model.req.UserInfoListReq;
import com.wechuang.mallshop.account.model.res.UserInfoRes;
import com.wechuang.mallshop.account.model.res.UserSaleRes;
import com.wechuang.mallshop.core.web.service.IBaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户详细信息表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-08
 */
public interface UserInfoService extends IBaseService<UserInfo, UserInfoListReq> {

    Page<UserInfo> getList(QueryWrapper<UserInfo> queryWrapper, Integer page, Integer size);

    /**
     * 用户详情
     *
     * @param userId
     * @return
     */
    UserInfoOutput getUserData(Integer userId);

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     */
    boolean editUser(UserInfo userInfo);

    /**
     * 修改密码
     *
     * @param userId
     * @param userPassword
     * @return
     */
    boolean passWordEdit(Integer userId, String userPassword);

    /**
     * 批量设置标签
     *
     * @param userIds
     * @param tagIds
     * @return
     */
    boolean addTags(String userIds, String tagIds);

    /**
     * 删除用户账号
     *
     * @param userId
     * @return
     */
    boolean removeUser(Integer userId);

    /**
     * 导出模版
     *
     * @param response
     */
    void exportTemp(HttpServletResponse response);

    /**
     * 导出指定用户详细信息
     *
     * @param response
     * @param userIds
     */
    void exportFile(HttpServletResponse response, List<Integer> userIds);

    /**
     * 导入用户信息
     *
     * @param file
     */
    void importTemp(MultipartFile file) throws Exception;

    /**
     * 批量发放优惠券
     *
     * @param userIds
     * @param activityId
     */
    void addVouchers(List<Integer> userIds, Integer activityId);

    Map fixUserAvatar(Map row, Boolean fix_user_account);

    List<Map> fixUserAvatar(List<Map> rows, Boolean fix_user_account);

    /**
     * 添加客户
     *
     * @param saleId
     * @param userAccount
     * @return
     */
    boolean addCustomer(Integer saleId, String userAccount);

    /**
     * 客户列表
     * @param userInfoListReq
     * @return
     */
    IPage<UserInfoRes> getList(UserInfoListReq userInfoListReq);

    /**
     * 销售数据统计
     * @param userId
     * @return
     */
    UserSaleRes saleDataStatistics(Integer userId);

    /**
     * 客户信息
     * @param saleId
     * @param userId
     * @return
     */
    UserInfo getCustomer(Integer saleId, String userId);
}
