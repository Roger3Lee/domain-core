package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.SaveState;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.mapper.BatchBaseMapper;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.utils.LambdaQueryUtils;
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

    private final Class<D> domainClass;
    private final Class<DO> doClass;

    @SuppressWarnings("unchecked")
    public BaseRepositoryImpl() {
        this.domainClass = GenericsUtils.getSuperClassGenericType(this.getClass(), 0);
        this.doClass = GenericsUtils.getSuperClassGenericType(this.getClass(), 1);
    }

    /**
     * 构建查询条件包装器
     */
    private LambdaQueryWrapper<DO> buildQueryWrapper(LambdaQuery<D> lambdaQuery) {
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNull(lambdaQuery)) {
            return wrapper;
        }

        // 构建过滤条件
        if (lambdaQuery.hasFilter()) {
            LambdaQueryUtils.buildFilterWrapper(wrapper, lambdaQuery.getFilter(), this.doClass);
        }

        // 构建排序条件
        List<LambdaOrderItem> orderItems = lambdaQuery.getOrderItems();
        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(order -> LambdaQueryUtils.buildOrderWrapper(wrapper, order, this.doClass));
        }
        return wrapper;
    }

    /**
     * 验证查询条件不为空
     */
    private void validateQueryCondition(LambdaQuery<D> lambdaQuery) {
        if (ObjectUtil.isNull(lambdaQuery) || !lambdaQuery.hasFilter()) {
            throw new IllegalArgumentException("不允许在不加任何过滤条件的情况下执行操作");
        }
    }

    /**
     * 通过字段查询唯一一条数据
     */
    @Override
    public D query(Serializable value, SFunction<D, Serializable> valueWarp) {
        return query(LambdaQuery.of(domainClass).eq(valueWarp, value));
    }

    @Override
    public D query(LambdaQuery<D> lambdaQuery) {
        validateQueryCondition(lambdaQuery);

        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        // 使用 selectOne 替代 selectList + limit 1
        DO doEntity = this.baseMapper.selectOne(wrapper.last("limit 1"));
        if (doEntity == null) {
            return null;
        }

        List<D> result = convert2DTO(Collections.singletonList(doEntity));
        return CollUtil.isEmpty(result) ? null : result.get(0);
    }

    @Override
    public List<D> queryList(Serializable value, SFunction<D, Serializable> valueWarp) {
        return queryList(LambdaQuery.of(domainClass).eq(valueWarp, value));
    }

    @Override
    public List<D> queryList(LambdaQuery<D> lambdaQuery) {
        validateQueryCondition(lambdaQuery);

        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        List<DO> doList = this.baseMapper.selectList(wrapper);
        return convert2DTO(doList);
    }

    @Override
    public IPage<D> queryPage(PageDomain pageDomain, LambdaQuery<D> lambdaQuery) {
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(lambdaQuery);
        Page<DO> page = new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize());

        // 优化：直接转换单个对象，避免创建不必要的列表
        return this.baseMapper.selectPage(page, wrapper)
                .convert(this::convertSingleDO2DTO);
    }

    /**
     * 转换单个DO为DTO
     */
    private D convertSingleDO2DTO(DO doEntity) {
        List<D> result = convert2DTO(Collections.singletonList(doEntity));
        return CollUtil.isEmpty(result) ? null : result.get(0);
    }

    /**
     * 插入一条数据
     */
    @Override
    public D insert(D item) {
        if (item == null) {
            throw new IllegalArgumentException("插入对象不能为空");
        }
        List<D> insertResponse = insert(Collections.singletonList(item));
        return insertResponse.get(0);
    }

    /**
     * 批量新增
     */
    @Override
    public List<D> insert(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        List<DO> doList = convert2DO(list);

        // 执行插入操作
        if (doList.size() == 1) {
            this.baseMapper.insert(doList.get(0));
        } else {
            // 优化：使用 instanceof 替代反射
            if (this.baseMapper instanceof BatchBaseMapper) {
                ((BatchBaseMapper<DO>) this.baseMapper).batchInsert(doList);
            } else {
                // 批量插入退化为循环插入
                doList.forEach(this.baseMapper::insert);
            }
        }

        // 回填自增字段并触发回调
        for (int i = 0; i < list.size(); i++) {
            convert2DTO(doList.get(i), list.get(i));
            list.get(i).afterSave(SaveState.INSERT);
        }

        return list;
    }

    /**
     * 批量删除
     */
    @Override
    public int delete(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }

        Function<D, Serializable> keyFunc = keyLambda();
        List<Serializable> keyList = list.stream()
                .map(keyFunc)
                .filter(ObjectUtil::isNotNull)
                .collect(Collectors.toList());

        return CollUtil.isEmpty(keyList) ? 0 : this.baseMapper.deleteBatchIds(keyList);
    }

    /**
     * 通过过滤条件删除数据
     */
    @Override
    public int deleteByFilter(LambdaQuery<D> lambdaQuery) {
        validateQueryCondition(lambdaQuery);

        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<>();
        LambdaQueryUtils.buildFilterWrapper(wrapper, lambdaQuery.getFilter(), this.doClass);
        return this.baseMapper.delete(wrapper);
    }

    /**
     * 根据ID删除
     */
    @Override
    public int deleteById(Serializable id) {
        if (id == null) {
            return 0;
        }
        return this.baseMapper.deleteById(id);
    }

    /**
     * 更新一条数据
     */
    @Override
    public int update(D item) {
        if (item == null) {
            return 0;
        }
        return update(Collections.singletonList(item));
    }

    /**
     * 批量更新
     */
    @Override
    public int update(List<D> list) {
        if (CollUtil.isEmpty(list)) {
            return 0;
        }

        int totalEffect = 0;
        List<DO> doList = convert2DO(list);

        if (doList.size() == 1) {
            totalEffect = this.baseMapper.updateById(doList.get(0));
        } else {
            // 优化：使用 instanceof 替代反射
            if (this.baseMapper instanceof BatchBaseMapper) {
                totalEffect = ((BatchBaseMapper<DO>) this.baseMapper).batchUpdate(doList);
            } else {
                // 批量更新退化为循环更新
                for (DO doEntity : doList) {
                    totalEffect += this.baseMapper.updateById(doEntity);
                }
            }
        }

        // 触发保存后的回调
        list.forEach(item -> item.afterSave(SaveState.UPDATE));

        return totalEffect;
    }

    public abstract List<DO> convert2DO(List<D> list);

    public abstract List<D> convert2DTO(List<DO> list);

    public abstract void convert2DTO(DO item, D targetItem);

    public abstract Function<D, Serializable> keyLambda();
}
