package com.artframework.domain.core.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.constants.SaveState;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.artframework.domain.core.mapper.BatchBaseMapper;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.uitls.FiltersUtils;
import com.artframework.domain.core.uitls.OrdersUtils;
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

    private LambdaQueryWrapper<DO> buildQueryWrapper(List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders,
                                                     boolean noConditionThrowException) {
        boolean hasFilter = false;
        LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>();
        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            hasFilter = true;
            //儅不需要查詢出數據時，返回空
            if (filters.stream().anyMatch(x -> Op.NIL.getCode().equals(x.getOp()))) {
                return null;
            }

            for (LambdaFilter.Filter filter : filters) {
                FiltersUtils.buildWrapper(wrapper, filter, this.getDOClass());
            }
        }

        if (noConditionThrowException && !hasFilter) {
            throw new IllegalArgumentException("不允許不加過濾條件查詢數據");
        }

        //排序
        if (ObjectUtil.isNotNull(orders)) {
            for (LambdaOrder.LambdaOrderItem order : orders) {
                OrdersUtils.buildOrderWrapper(wrapper, order, this.getDOClass());
            }
        }
        return wrapper;
    }

    /**
     * 通過字段查詢唯一一條數據
     *
     * @param value
     * @param valueWarp
     * @return
     */
    @Override
    public D query(Serializable value, SFunction<D, Serializable> valueWarp) {
        return query(FiltersUtils.toFilters(ListUtil.toList(LambdaFilter.build(LambdaCache.DOLambda(this.getDOClass(), valueWarp), value)))
                , null);
    }

    @Override
    public D query(List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders) {
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(filters, orders,true);
        wrapper = wrapper.last("limit 1");
        List<D> list = convert2DTO(this.baseMapper.selectList(wrapper));
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<D> queryList(Serializable value, SFunction<D, Serializable> valueWarp) {
        return queryList(FiltersUtils.toFilters(ListUtil.toList(LambdaFilter.build(LambdaCache.DOLambda(this.getDOClass(), valueWarp), value)))
                , null);
    }

    @Override
    public List<D> queryList(List<LambdaFilter.Filter> filters) {
        return queryList(filters, null);
    }

    @Override
    public List<D> queryList(List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders) {
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(filters, orders, true);
        return convert2DTO(this.baseMapper.selectList(wrapper));
    }

    @Override
    public IPage<D> queryPage(PageDomain pageDomain, List<LambdaFilter.Filter> filters) {
        return queryPage(pageDomain, filters, null);
    }

    @Override
    public IPage<D> queryPage(PageDomain pageDomain, List<LambdaFilter.Filter> filters, List<LambdaOrder.LambdaOrderItem> orders) {
        LambdaQueryWrapper<DO> wrapper = buildQueryWrapper(filters, orders, false);
        return this.baseMapper.selectPage(new Page<>(pageDomain.getPageNum(), pageDomain.getPageSize()), wrapper).convert(x -> convert2DTO(ListUtil.toList(x)).get(0));
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
        //覆蓋列表， 目的是將自賦值的字段的值返回
        for (int i = 0; i < list.size(); i++) {
            convert2DTO(tList.get(i), list.get(i));
            //觸發保存後的回調
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
     * 通過實體的過濾條件刪除數據
     *
     * @param filters
     * @return
     */
    @Override
    public int deleteByFilter(List<LambdaFilter.Filter> filters) {
        //額外的filter
        if (ObjectUtil.isNotNull(filters)) {
            LambdaQueryWrapper<DO> wrapper = new LambdaQueryWrapper<DO>();
            for (LambdaFilter.Filter filter : filters) {
                FiltersUtils.buildWrapper(wrapper, filter, this.getDOClass());
            }

            return this.baseMapper.delete(wrapper);
        } else {
            log.error("無過濾條件刪除數據，系統忽略此刪除操作");
            return 0;
        }
    }

    /**
     * 删除一行数据BYid
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
        Integer effect = 0;
        List<DO> tList = convert2DO(list);
        if (list.size() == 1) {
            effect = this.baseMapper.updateById(tList.get(0));
        } else {
            if (BatchBaseMapper.class.isAssignableFrom(this.baseMapper.getClass())) {
                return ((BatchBaseMapper) this.baseMapper).batchUpdate(tList);
            } else {
                for (DO d : tList) {
                    this.baseMapper.updateById(d);
                }
                effect = tList.size();
            }
        }

        //觸發保存後的回調
        for (D item : list) {
            item.afterSave(SaveState.UPDATE);
        }
        return effect;
    }

    public abstract List<DO> convert2DO(List<D> list);

    public abstract List<D> convert2DTO(List<DO> list);

    public abstract void convert2DTO(DO item, D targetItem);

    public abstract Function<D, Serializable> keyLambda();

    public abstract Class<DO> getDOClass();
}
