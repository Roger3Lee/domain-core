package com.artframework.domain.core.service;

import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.repository.BaseRepository;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface BaseDomainService {


    /**
     * 獲取倉儲
     * @param clazz
     * @return
     */
    <T> BaseRepository getRepository(Class<T> clazz);
    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
     <D extends BaseDomain> void merge(List<D> oldList, List<D> newList, Function<D, Serializable> keyWrap, BaseRepository repository);


    <D> Boolean deleteRelated(Class<D> clazz, LambdaQuery<D> lambdaQuery);

    <T> T queryOne(Class<T> clazz, LambdaQuery<T> lambdaQuery);

    <T> List<T> queryList(Class<T> clazz, LambdaQuery<T> lambdaQuery);


    <T> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain);

    <T> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain, LambdaQuery<T> lambdaQuery);
}
