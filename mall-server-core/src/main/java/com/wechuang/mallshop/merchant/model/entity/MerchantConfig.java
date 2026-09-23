package com.wechuang.mallshop.merchant.model.entity;

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

/**
 * 商家配置表
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mch_merchant_config")
@Schema(name = "MerchantConfig对象", description = "商家配置表")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MerchantConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置编号")
    @TableId(value = "config_id", type = IdType.AUTO)
    private Integer configId;

    @Schema(description = "商家编号")
    @TableField("merchant_id")
    private Integer merchantId = 0;

    @Schema(description = "配置键")
    @TableField("config_key")
    private String configKey;

    @Schema(description = "配置值")
    @TableField("config_value")
    private String configValue;

    @Schema(description = "租户编号")
    @TableField("subsite_id")
    private Integer subsiteId;
}
