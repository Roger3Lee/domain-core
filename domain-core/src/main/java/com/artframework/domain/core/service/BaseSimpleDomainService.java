package com.artframework.domain.core.service;

import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.query.LambdaQuery;
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

    T queryOne(LambdaQuery<T> lambdaQuery);

    List<T> queryList(LambdaQuery<T> lambdaQuery);

    IPage<T> queryPage(PageDomain pageDomain);

    IPage<T> queryPage(PageDomain pageDomain, LambdaQuery<T> lambdaQuery);

    Boolean batchUpdate(List<T> list);

    Boolean batchDelete(List<T> list);
}
