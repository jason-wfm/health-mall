package com.wechuang.mallshop.core.web.model;

import lombok.Data;

/**
 * 应用发布包 DTO（自研重写，对齐 core-3.0.27908；本地化后安装包类接口返回空数据，此模型仅保持兼容）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class ReleaseDto {

    private String appId;

    private String appName;

    private String url;

    private String version;

    private String primaryColor;

    private String serviceUserId;

    private String serviceAppKey;

    private Integer serviceAppId;
}
