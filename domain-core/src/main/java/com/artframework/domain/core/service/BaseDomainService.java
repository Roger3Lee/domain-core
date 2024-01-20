package com.artframework.domain.core.service;

import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.repository.BaseRepository;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

public interface BaseDomainService {

    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
     <D extends BaseDomain> void merge(List<D> oldList, List<D> newList, Function<D, Serializable> keyWrap, BaseRepository repository);
}
