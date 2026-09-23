package com.wechuang.mallshop.shop.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * [healthmall-ext] C端门店接入定位表（spec §3.3）
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("shop_store_access")
@Schema(name = "StoreAccess对象", description = "C端门店接入定位表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StoreAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "access_id", type = IdType.AUTO)
    private Integer accessId;

    @Schema(description = "店铺编号")
    @TableField("store_id")
    private Integer storeId;

    @Schema(description = "类型:domain-域名;appid-小程序appid")
    @TableField("access_type")
    private String accessType;

    @Schema(description = "接入键:域名(不含协议与端口)或小程序appid")
    @TableField("access_key")
    private String accessKey;

    @Schema(description = "状态:1-启用;0-停用")
    @TableField("access_status")
    private Integer accessStatus;

    @Schema(description = "创建时间")
    @TableField("access_ctime")
    private Date accessCtime;

    @Schema(description = "更新时间")
    @TableField("access_utime")
    private Date accessUtime;
}
