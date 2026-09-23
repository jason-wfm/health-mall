package com.wechuang.mallshop.core.web.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 业务服务基类接口（自研重写，方法集对齐 core-3.0.27908 javap 核验）
 *
 * @param <T> 实体类型
 * @param <L> 分页查询请求类型
 * @since 3.1.0-healthmall
 */
public interface IBaseService<T, L extends BaseListReq> {

    Page<T> lists(L req);

    Page<T> lists(QueryWrapper<T> queryWrapper, Integer page, Integer size);

    Page<T> lists(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size);

    String getParameter(String key);

    <T> T getParameter(String key, Class<T> clazz);

    <T> T getParameter(String key, T defaultValue);

    T get(Serializable id);

    boolean remove(Serializable id);

    boolean remove(Collection<? extends Serializable> ids);

    List<T> find(LambdaQueryWrapper<T> queryWrapper);

    List<T> find(QueryWrapper<T> queryWrapper);

    T findOne(QueryWrapper<T> queryWrapper);

    T findOne(LambdaQueryWrapper<T> queryWrapper);

    long count(LambdaQueryWrapper<T> queryWrapper);

    long count(Wrapper<T> queryWrapper);

    List<Serializable> findKey(QueryWrapper<T> queryWrapper);

    List<Serializable> findKey(LambdaQueryWrapper<T> queryWrapper);

    Page<Serializable> listKey(QueryWrapper<T> queryWrapper, Integer page, Integer size);

    Page<Serializable> listKey(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size);

    boolean edit(T entity, QueryWrapper<T> queryWrapper);

    boolean edit(Collection<T> entitys);

    boolean edit(T entity);

    List<T> gets(Collection<? extends Serializable> ids);

    boolean save(T entity);

    boolean add(T entity);
}
