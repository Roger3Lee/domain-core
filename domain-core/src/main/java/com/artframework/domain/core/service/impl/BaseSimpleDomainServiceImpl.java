package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.service.BaseSimpleDomainService;
import com.artframework.domain.core.uitls.CompareUtil;
import com.artframework.domain.core.uitls.FiltersUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public abstract class BaseSimpleDomainServiceImpl<T extends BaseDomain> implements BaseSimpleDomainService<T> {

    public abstract BaseRepository getRepository();

    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
    @Override
    public void merge(List<T> oldList, List<T> newList, Function<T, Serializable> keyWrap, BaseRepository repository) {
        CompareUtil.CompareResult<T> compareList = CompareUtil.compareList(oldList, newList, keyWrap);

        //删除
        if (CollUtil.isNotEmpty(compareList.getDeleteList())) {
            repository.delete(compareList.getDeleteList());
        }

        //新增
        if (CollUtil.isNotEmpty(compareList.getAddList())) {
            repository.insert(compareList.getAddList());
        }
        //修改
        if (CollUtil.isNotEmpty(compareList.getUpdateList())) {
            List<T> updateList = compareList.getUpdateList().stream()
                    .filter(x -> ObjectUtil.isNull(x.getChanged()) || x.getChanged())
                    .collect(Collectors.toList());
            repository.update(updateList);
        }
    }

    @Override
    public T queryOne(List<LambdaFilter<T>> lambdaFilters) {
        return queryOne(lambdaFilters, null);
    }

    @Override
    public T queryOne(List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders) {
        return (T) getRepository().query(null, null, FiltersUtils.buildLambdaFilter(lambdaFilters), ObjectUtil.isNotNull(orders) ? orders.toOrderItems() : ListUtil.empty(), false);
    }


    @Override
    public List<T> queryList(List<LambdaFilter<T>> lambdaFilters) {
        return queryList(lambdaFilters, null);
    }

    @Override
    public List<T> queryList(List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders) {
        return getRepository().queryList(FiltersUtils.buildLambdaFilter(lambdaFilters), ObjectUtil.isNotNull(orders) ? orders.toOrderItems() : ListUtil.empty());
    }

    @Override
    public IPage<T> queryPage(PageDomain pageDomain) {
        return queryPage(pageDomain, null, null);
    }

    @Override
    public IPage<T> queryPage(PageDomain pageDomain, LambdaOrder<T> orders) {
        return queryPage(pageDomain, null, orders);
    }

    @Override
    public IPage<T> queryPage(PageDomain pageDomain, List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders) {
        return getRepository().queryPage(pageDomain, FiltersUtils.buildLambdaFilter(lambdaFilters), ObjectUtil.isNotNull(orders) ? orders.toOrderItems() : ListUtil.empty());
    }

    @Override
    public Boolean batchUpdate(List<T> list) {
        return getRepository().update(list) > 0;
    }
    @Override
    public Boolean batchDelete(List<T> list){
        return getRepository().delete(list) > 0;
    }
}
    