package com.wechuang.mallshop.core.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.consts.Constants;
import com.wechuang.mallshop.core.web.CommonRes;
import com.wechuang.mallshop.core.web.model.res.BaseListRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 控制器基类（自研重写，success/fail 家族对齐 core-3.0.27908 javap 核验）
 *
 * @since 3.1.0-healthmall
 */
public class BaseController {

    protected final transient Logger logger = LoggerFactory.getLogger(getClass());

    public CommonRes<?> success() {
        return new CommonRes<>(Constants.RESULT_OK_STATUS, Constants.RESULT_OK_MSG);
    }

    public CommonRes<?> success(String message) {
        return new CommonRes<>(Constants.RESULT_OK_STATUS, message);
    }

    public <T> CommonRes<T> success(T data) {
        return new CommonRes<>(Constants.RESULT_OK_STATUS, Constants.RESULT_OK_MSG, data);
    }

    public <T> CommonRes<T> success(String message, T data) {
        return new CommonRes<>(Constants.RESULT_OK_STATUS, message, data);
    }

    public <T> CommonRes<BaseListRes<T>> success(IPage<T> page) {
        BaseListRes<T> listRes = new BaseListRes<>(
                page.getRecords(),
                (int) page.getTotal(),
                (int) page.getCurrent(),
                (int) page.getSize(),
                (int) page.getPages());
        return new CommonRes<>(Constants.RESULT_OK_STATUS, Constants.RESULT_OK_MSG, listRes);
    }

    public <T> CommonRes<BaseListRes<T>> success(List<T> records, Integer total, Integer size, Integer page, Integer pageCount) {
        BaseListRes<T> listRes = new BaseListRes<>(records, total, page, size, pageCount);
        return new CommonRes<>(Constants.RESULT_OK_STATUS, Constants.RESULT_OK_MSG, listRes);
    }

    public CommonRes<?> fail() {
        return new CommonRes<>(Constants.RESULT_ERROR_STATUS, Constants.RESULT_ERROR_MSG);
    }

    public <T> CommonRes<T> fail(Integer status) {
        return new CommonRes<>(status, Constants.RESULT_ERROR_MSG);
    }

    public CommonRes<?> fail(String message) {
        return new CommonRes<>(Constants.RESULT_ERROR_STATUS, message);
    }

    public <T> CommonRes<T> fail(String message, Integer status) {
        return new CommonRes<>(status, message);
    }

    public <T> CommonRes<T> fail(String message, T data) {
        return new CommonRes<>(Constants.RESULT_ERROR_STATUS, message, data);
    }

    public <T> CommonRes<T> fail(T data) {
        return new CommonRes<>(Constants.RESULT_ERROR_STATUS, Constants.RESULT_ERROR_MSG, data);
    }
}
