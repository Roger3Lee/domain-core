package com.artframework.domain.core.service;

import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.repository.BaseRepository;

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


    <D> Boolean deleteRelated(Class<D> clazz, List<LambdaFilter<D>> lambdaFilters);
}
