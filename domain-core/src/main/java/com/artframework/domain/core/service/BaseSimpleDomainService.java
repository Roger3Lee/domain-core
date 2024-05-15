package com.artframework.domain.core.service;

import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.artframework.domain.core.repository.BaseRepository;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/3/13
 **/
public interface BaseSimpleDomainService<T> {


    /**
     * 獲取倉儲
     *
     * @return
     */
    BaseRepository getRepository();

    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
    void merge(List<T> oldList, List<T> newList, Function<T, Serializable> keyWrap, BaseRepository repository);

    T queryOne(List<LambdaFilter<T>> lambdaFilters);

    T queryOne(List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders);

    List<T> queryList(List<LambdaFilter<T>> lambdaFilters);

    List<T> queryList(List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders);

    IPage<T> queryPage(PageDomain pageDomain);

    IPage<T> queryPage(PageDomain pageDomain, LambdaOrder<T> orders);

    IPage<T> queryPage(PageDomain pageDomain, List<LambdaFilter<T>> lambdaFilters, LambdaOrder<T> orders);

    Boolean batchUpdate(List<T> list);

    Boolean batchDelete(List<T> list);
}
