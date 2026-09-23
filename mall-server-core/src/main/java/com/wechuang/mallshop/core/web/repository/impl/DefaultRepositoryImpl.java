package com.wechuang.mallshop.core.web.repository.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechuang.mallshop.core.web.repository.IDefaultRepository;

/**
 * 通用数据访问实现（自研重写，对齐 core-3.0.27908；业务侧未引用，保留占位）
 *
 * @param <M> Mapper 类型
 * @param <T> 实体类型
 * @since 3.1.0-healthmall
 */
public class DefaultRepositoryImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IDefaultRepository<T> {

}
