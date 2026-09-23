package com.wechuang.mallshop.core.web.repository.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.wechuang.mallshop.core.web.repository.IBaseRepository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据访问基类实现（自研重写，对齐 core-3.0.27908 行为规格：
 * 别名方法全部委托 MyBatis-Plus 标准能力；不含时间字段填充——原实现依赖数据库默认值，保持一致）
 *
 * @param <M> Mapper 类型
 * @param <T> 实体类型
 * @since 3.1.0-healthmall
 */
public class BaseRepositoryImpl<M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, T>
        extends ServiceImpl<M, T> implements IBaseRepository<T> {

    @Override
    public T get(Serializable id) {
        return getById(id);
    }

    @Override
    public List<T> gets(Collection<? extends Serializable> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return listByIds(ids);
    }

    @Override
    public List<T> gets(Serializable id) {
        if (id == null) {
            return Collections.emptyList();
        }
        return listByIds(Collections.singletonList(id));
    }

    @Override
    public T findOne(QueryWrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    @Override
    public T findOne(LambdaQueryWrapper<T> queryWrapper) {
        return getOne(queryWrapper, false);
    }

    @Override
    public List<T> find(QueryWrapper<T> queryWrapper) {
        return list(queryWrapper);
    }

    @Override
    public List<T> find(LambdaQueryWrapper<T> queryWrapper) {
        return list(queryWrapper);
    }

    @Override
    public List<Serializable> findKey(QueryWrapper<T> queryWrapper) {
        return toSerializableList(getBaseMapper().selectObjs(queryWrapper));
    }

    @Override
    public List<Serializable> findKey(LambdaQueryWrapper<T> queryWrapper) {
        return toSerializableList(getBaseMapper().selectObjs(queryWrapper));
    }

    @Override
    public Page<Serializable> listKey(QueryWrapper<T> queryWrapper, Integer page, Integer size) {
        Page<Map<String, Object>> mapPage = getBaseMapper().selectMapsPage(
                new Page<>(resolvePage(page), resolveSize(size)), queryWrapper);
        return toKeyPage(mapPage);
    }

    @Override
    public Page<Serializable> listKey(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size) {
        Page<Map<String, Object>> mapPage = getBaseMapper().selectMapsPage(
                new Page<>(resolvePage(page), resolveSize(size)), queryWrapper);
        return toKeyPage(mapPage);
    }

    private Page<Serializable> toKeyPage(Page<Map<String, Object>> mapPage) {
        Page<Serializable> result = new Page<>(mapPage.getCurrent(), mapPage.getSize(), mapPage.getTotal());
        result.setPages(mapPage.getPages());
        if (mapPage.getRecords() != null) {
            result.setRecords(mapPage.getRecords().stream()
                    .filter(map -> !map.isEmpty())
                    .map(map -> (Serializable) map.values().iterator().next())
                    .collect(Collectors.toList()));
        }
        return result;
    }

    @Override
    public Page<T> lists(QueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return page(new Page<>(resolvePage(page), resolveSize(size)), queryWrapper);
    }

    @Override
    public Page<T> lists(LambdaQueryWrapper<T> queryWrapper, Integer page, Integer size) {
        return page(new Page<>(resolvePage(page), resolveSize(size)), queryWrapper);
    }

    @Override
    public boolean edit(T entity) {
        return updateById(entity);
    }

    @Override
    public boolean edit(T entity, QueryWrapper<T> queryWrapper) {
        return update(entity, queryWrapper);
    }

    @Override
    public boolean edit(Collection<T> entitys) {
        return updateBatchById(entitys);
    }

    @Override
    public boolean add(T entity) {
        return save(entity);
    }

    /**
     * MP 3.5.7 的 ServiceImpl 不覆写 save(T)（IService default），
     * IBaseRepository 重新抽象后必须显式提供实现（与原 core 行为一致）
     */
    @Override
    public boolean save(T entity) {
        return SqlHelper.retBool(getBaseMapper().insert(entity));
    }

    @Override
    public boolean saveOrUpdate(Collection<T> entitys) {
        return saveOrUpdateBatch(entitys);
    }

    @Override
    public boolean saves(Collection<T> entitys) {
        return saveBatch(entitys);
    }

    @Override
    public boolean saves(T entity, Wrapper<T> updateWrapper) {
        return saveOrUpdate(entity, updateWrapper);
    }

    @Override
    public boolean remove(Serializable id) {
        return removeById(id);
    }

    @Override
    public boolean remove(Collection<? extends Serializable> ids) {
        return removeByIds(ids);
    }

    @Override
    public <E> Page<E> convertResult(IPage<?> page, Class<E> clazz) {
        Page<E> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (page.getRecords() != null) {
            result.setRecords(page.getRecords().stream()
                    .map(record -> BeanUtil.copyProperties(record, clazz))
                    .collect(Collectors.toList()));
        }
        return result;
    }

    private List<Serializable> toSerializableList(List<Object> objs) {
        if (objs == null) {
            return Collections.emptyList();
        }
        return objs.stream()
                .map(obj -> (Serializable) obj)
                .collect(Collectors.toList());
    }

    private long resolvePage(Integer page) {
        return page == null || page < 1 ? 1L : page;
    }

    private long resolveSize(Integer size) {
        return size == null || size < 1 ? 10L : size;
    }
}
