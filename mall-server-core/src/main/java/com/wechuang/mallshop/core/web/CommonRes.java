package com.wechuang.mallshop.core.web;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 统一响应体（自研重写，构造器与访问器对齐 core-3.0.27908 javap 核验；
 * 注意：只有 getMsg，没有 getMessage/isSuccess；setter 为链式返回）
 *
 * @param <T> 数据类型
 * @since 3.1.0-healthmall
 */
@Data
@Accessors(chain = true)
public class CommonRes<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer status;

    private String msg;

    private T data;

    private Integer code;

    private String error;

    public CommonRes() {
    }

    public CommonRes(Integer status) {
        this.status = status;
    }

    public CommonRes(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonRes(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonRes(Integer status, String msg, T data, Integer code) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.code = code;
    }

    public CommonRes(Integer status, String msg, T data, Integer code, String error) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.code = code;
        this.error = error;
    }
}
