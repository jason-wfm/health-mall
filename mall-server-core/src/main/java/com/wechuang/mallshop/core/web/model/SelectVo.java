package com.wechuang.mallshop.core.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 下拉选项 VO（自研重写，对齐 core-3.0.27908）
 *
 * @since 3.1.0-healthmall
 */
@Data
public class SelectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer value;

    private String label;

    private Boolean enable;

    private String ext1;

    private String ext2;
}
