package com.wechuang.mallshop.common.web;

import cn.hutool.core.util.ObjectUtil;
import com.wechuang.mallshop.common.consts.AuthConstant;
import com.wechuang.mallshop.common.utils.CheckUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * request context user
 *
 * @author Xinze
 * @since 2021-08-26 22:14:43
 */
@Data
public class ContextUser {
    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户账号")
    private String userAccount;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "salt值")
    private String userSalt;

    @Schema(description = "角色编号:0-用户;2-商家;3-门店;8-租户;9-平台;")
    private Integer roleId;

    @Schema(description = "分站编号:0-总账")
    private Integer siteId;

    @Schema(description = "店铺编号")
    private Integer storeId;

    @Schema(description = "门店编号")
    private Integer chainId;

    @Schema(description = "[healthmall-ext] 商家编号:0-平台/无归属")
    private Integer merchantId;

    @Schema(description = "行业编号")
    private Integer industryId;

    @Schema(description = "后台管理:admin=1;移动端front=0")
    private Integer clientId;

    public boolean isPlatform() {
        return this.roleId != null && this.roleId == 9;
    }

    public boolean isSubsite() {
        return this.roleId != null && this.roleId == 8 && this.siteId > 0;
    }

    public boolean isStore() {
        return CheckUtil.isNotEmpty(this.storeId) && this.roleId == 2;
    }

    public boolean isChain() {
        return CheckUtil.isNotEmpty(this.chainId);
    }

    public boolean isAdmin() {
        return ObjectUtil.equal(chainId, AuthConstant.ADMIN_CLIENT_ID);
    }

    public boolean isMobile() {
        return ObjectUtil.equal(clientId, AuthConstant.MOBILE_CLIENT_ID);
    }

}
