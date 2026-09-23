package com.wechuang.mallshop.common.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Jwt载体
 *
 * @author Xinze
 * @since 2021-09-03 00:11:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtSubject implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户编号")
    private Integer userId;

    @Schema(description = "用户盐值")
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

    @Schema(description = "后台管理:admin=1;移动端front=0")
    private Integer clientId;
}
