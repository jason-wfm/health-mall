package com.wechuang.mallshop.core.web.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 数据访问基类接口（自研重写，别名方法集对齐 core-3.0.27908 javap 核验；
 * 标准 CRUD 由父接口 IService 提供，此处补充 get/gets/find/findKey/lists/edit 等别名）
 *
 * @param <T> 实体类型
 * @since 3.1.0-healthmall
 */
public interface IBaseRepository<T> extends IService<T> {

    T get(Serializable id);

    List<T> gets(Collection<? extends Serializable> ids);

    List<T> gets(Serializable id);

    T findOne(QueryWrapper<T> queryWrapper);

    T findOne(LambdaQueryWrapper<T> queryWrapper);

    List<T> find(QueryWrapper<T> queryWrapper);

    List<T> find(LambdaQueryWrapper<T> queryWrapper);

    List<Serializable> findKey(QueryWrapper<T> queryWrapper);

    List<Serializable> findKey(LambdaQueryWrapper<T> queryWrapper);

    Page<Serializable> listKey(QueryWrapper<T> queryWrapper, Integer page, Integer size);

    Page<Serializable> listKey(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size);

    Page<T> lists(QueryWrapper<T> queryWrapper, Integer page, Integer size);

    Page<T> lists(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size);

    boolean add(T entity);

    boolean edit(T entity);

    boolean edit(T entity, QueryWrapper<T> queryWrapper);

    boolean edit(Collection<T> entitys);

    boolean save(T entity);

    boolean saveOrUpdate(T entity);

    boolean saveOrUpdate(Collection<T> entitys);

    boolean saves(Collection<T> entitys);

    boolean saves(T entity, Wrapper<T> updateWrapper);

    boolean remove(Serializable id);

    boolean remove(Collection<? extends Serializable> ids);

    <T> Page<T> convertResult(IPage<?> page, Class<T> clazz);
}
