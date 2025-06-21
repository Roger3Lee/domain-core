package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.SaveState;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.mapper.BatchBaseMapper;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.uitls.LambdaQueryUtils;
import com.artframework.domain.core.utils.GenericsUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseRepositoryImpl<D extends BaseDomain, DO> implements BaseRepository<D, DO> {
    @Autowired
    protected BaseMapper<DO> baseMapper;

    private Class<D> domainClass;

    private Class<DO> doClass;

    public BaseRepositoryImpl() {
        this.domainClass = GenericsUtils.getSuperClassGenericType(this.getClass(), 0);
        this.doClass = GenericsUtils.getSuperClassGenericType(this.getClass(), 1);
    }

    private LambdaQueryWrapper<DO> buildQueryWrapper(LambdaQuery<D> lambdaQuery) {
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNull(lambdaQuery)) {
            return wrapper;
        }

        // 额外的filter
        if (lambdaQuery.hasFilter()) {
            LambdaQueryUtils.buildFilterWrapper(wrapper, lambdaQuery.getFilter(), this.doClass);
        }

        // 排序
        if (ObjectUtil.isNotNull(lambdaQuery.getOrderItems())) {
            for (LambdaOrderItem order : lambdaQuery.getOrderItems()) {
                LambdaQueryUtils.buildOrderWrapper(wrapper, order, this.doClass);
            }
        }
        return wrapper;
    }

    /**
     * 通过字段查询唯一一条数据
     *
     * @param value
     * @param valueWarp
     * @return
     */
    @Override
    public D query(Serializable value, SFunction<D, Serializable> valueWarp) {
        return query(LambdaQuery.of(domainClass).eq(valueWarp, value));
    }

    @Override
    public D query(LambdaQuery<D> lambdaQuery) {
        if (ObjectUtil.isNull(lambdaQuery) || !lambdaQuery.hasFilter()) {
            throw new IllegalArgumentException("不允许在不加任何过滤条件的情况下查询数据");
        }
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        wrapper = wrapper.last("limit 1");
        List<D> list = convert2DTO(this.baseMapper.selectList(wrapper));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<D> queryList(Serializable value, SFunction<D, Serializable> valueWarp) {
        return queryList(LambdaQuery.of(domainClass).eq(valueWarp, value));
    }

    @Override
    public List<D> queryList(LambdaQuery<D> lambdaQuery) {
        if (ObjectUtil.isNull(lambdaQuery) || !lambdaQuery.hasFilter()) {
            throw new IllegalArgumentException("不允许在不加任何过滤条件的情况下查询数据");
        }
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        return convert2DTO(this.baseMapper.selectList(wrapper));
    }

    @Override
    public IPage<D> queryPage(PageDomain pageDomain, LambdaQuery<D> lambdaQuery) {
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        return this.baseMapper.selectPage(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), wrapper)
                .convert(x -> convert2DTO(ListUtil.toList(x)).get(0));
    }

    /**
     * 插入一条数据
     *
     * @param item
     * @return 新增后数据
     */
    @Override
    public D insert(D item) {
        List<D> insertResponse = insert(CollUtil.newArrayList(item));
        return insertResponse.get(0);
    }

    /**
     * 新增列表
     *
     * @param list
     * @return 新增后数据列表
     */
    @Override
    public List<D> insert(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<DO> tList = convert2DO(list);
        if (list.size() == 1) {
            this.baseMapper.insert(tList.get(0));
        } else {
            if (BatchBaseMapper.class.isAssignableFrom(this.baseMapper.getClass())) {
                ((BatchBaseMapper) this.baseMapper).insertBatch(tList);
            } else {
                for (DO d : tList) {
                    this.baseMapper.insert(d);
                }
            }
        }
        // 覆盖列表， 目的是将自赋值的字段的值返回
        for (int i = 0; i < list.size(); i++) {
            convert2DTO(tList.get(i), list.get(i));
            // 触发保存后的回调
            list.get(i).afterSave(SaveState.INSERT);
        }
        return list;
    }

    /**
     * 批量删除
     *
     * @param list
     * @return 受影响行数
     */
    @Override
    public int delete(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        List<Serializable> keyList = list.stream().map(x -> keyLambda().apply(x)).collect(Collectors.toList());
        return this.baseMapper.deleteBatchIds(keyList);
    }

    /**
     * 通过实体的过滤条件删除数据
     *
     * @param lambdaQuery
     * @return
     */
    @Override
    public int deleteByFilter(LambdaQuery<D> lambdaQuery) {
        if (ObjectUtil.isNull(lambdaQuery) || !lambdaQuery.hasFilter()) {
            throw new IllegalArgumentException("不允许在不加任何过滤条件的情况下删除数据");
        }
        // 额外的filter
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryUtils.buildFilterWrapper(wrapper, lambdaQuery.getFilter(), this.doClass);
        return this.baseMapper.delete(wrapper);
    }

    /**
     * 根据ID删除一行数据
     *
     * @param id
     * @return 受影响行数
     */
    @Override
    public int deleteById(Serializable id) {
        return this.baseMapper.deleteById(id);
    }

    /**
     * 更新一条数据
     *
     * @param item
     * @return 更新后数据
     */
    @Override
    public int update(D item) {
        return update(CollUtil.newArrayList(item));
    }

    /**
     * 更新列表
     *
     * @param list
     * @return 更新后数据列表
     */
    @Override
    public int update(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }
        int effect = 0;
        List<DO> tList = convert2DO(list);
        if (list.size() == 1) {
            effect = this.baseMapper.updateById(tList.get(0));
        } else {
            if (BatchBaseMapper.class.isAssignableFrom(this.baseMapper.getClass())) {
                return ((BatchBaseMapper) this.baseMapper).batchUpdate(tList);
            } else {
                for (DO d : tList) {
                    effect += this.baseMapper.updateById(d);
                }
            }
        }

        // 触发保存后的回调
        for (D item : list) {
            item.afterSave(SaveState.UPDATE);
        }
        return effect;
    }

    public abstract List<DO> convert2DO(List<D> list);

    public abstract List<D> convert2DTO(List<DO> list);

    public abstract void convert2DTO(DO item, D targetItem);

    public abstract Function<D, Serializable> keyLambda();
}
