package com.wechuang.mallshop.core.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.core.web.BaseQueryWrapper;
import com.wechuang.mallshop.core.web.model.req.BaseListReq;
import com.wechuang.mallshop.core.web.repository.IBaseRepository;
import com.wechuang.mallshop.core.web.service.IBaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 业务服务基类实现（自研重写，对齐 core-3.0.27908 行为规格）
 * 子类声明为 @Service 并继承本类后，按泛型自动注入对应 Repository
 *
 * @param <R> 数据访问类型
 * @param <T> 实体类型
 * @param <L> 分页查询请求类型
 * @since 3.1.0-healthmall
 */
public class BaseServiceImpl<R extends IBaseRepository<T>, T, L extends BaseListReq> implements IBaseService<T, L> {

    @Autowired
    protected R repository;

    public R getRepository() {
        return repository;
    }

    @Override
    public Page<T> lists(L req) {
        QueryWrapper<T> wrapper = new BaseQueryWrapper<T, L>(req).getWrapper();

        Integer page = req == null ? null : req.getPage();
        Integer size = req == null ? null : req.getSize();
        return repository.lists(wrapper, page, size);
    }

    @Override
    public Page<T> lists(QueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return repository.lists(queryWrapper, page, size);
    }

    @Override
    public Page<T> lists(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return repository.lists(queryWrapper, page, size);
    }

    @Override
    public String getParameter(String key) {
        HttpServletRequest request = currentRequest();
        if (request == null || StrUtil.isBlank(key)) {
            return null;
        }
        String value = request.getParameter(key);
        return StrUtil.isBlank(value) ? null : value;
    }

    @Override
    public <E> E getParameter(String key, Class<E> clazz) {
        String value = getParameter(key);
        return value == null ? null : Convert.convert(clazz, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E getParameter(String key, E defaultValue) {
        String value = getParameter(key);
        if (value == null) {
            return defaultValue;
        }
        if (defaultValue != null) {
            return (E) Convert.convert(defaultValue.getClass(), value);
        }
        return (E) value;
    }

    @Override
    public T get(Serializable id) {
        return repository.get(id);
    }

    @Override
    public boolean remove(Serializable id) {
        return repository.remove(id);
    }

    @Override
    public boolean remove(Collection<? extends Serializable> ids) {
        return repository.remove(ids);
    }

    @Override
    public List<T> find(LambdaQueryWrapper<T> queryWrapper) {
        return repository.find(queryWrapper);
    }

    @Override
    public List<T> find(QueryWrapper<T> queryWrapper) {
        return repository.find(queryWrapper);
    }

    @Override
    public T findOne(QueryWrapper<T> queryWrapper) {
        return repository.findOne(queryWrapper);
    }

    @Override
    public T findOne(LambdaQueryWrapper<T> queryWrapper) {
        return repository.findOne(queryWrapper);
    }

    @Override
    public long count(LambdaQueryWrapper<T> queryWrapper) {
        return repository.count(queryWrapper);
    }

    @Override
    public long count(Wrapper<T> queryWrapper) {
        return repository.count(queryWrapper);
    }

    @Override
    public List<Serializable> findKey(QueryWrapper<T> queryWrapper) {
        return repository.findKey(queryWrapper);
    }

    @Override
    public List<Serializable> findKey(LambdaQueryWrapper<T> queryWrapper) {
        return repository.findKey(queryWrapper);
    }

    @Override
    public Page<Serializable> listKey(QueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return repository.listKey(queryWrapper, page, size);
    }

    @Override
    public Page<Serializable> listKey(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return repository.listKey(queryWrapper, page, size);
    }

    @Override
    public boolean edit(T entity, QueryWrapper<T> queryWrapper) {
        return repository.edit(entity, queryWrapper);
    }

    @Override
    public boolean edit(Collection<T> entitys) {
        return repository.edit(entitys);
    }

    @Override
    public boolean edit(T entity) {
        return repository.edit(entity);
    }

    @Override
    public List<T> gets(Collection<? extends Serializable> ids) {
        return repository.gets(ids);
    }

    @Override
    public boolean save(T entity) {
        return repository.save(entity);
    }

    @Override
    public boolean add(T entity) {
        return repository.save(entity);
    }

    private HttpServletRequest currentRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        return null;
    }
}
