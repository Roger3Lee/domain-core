package com.artframework.domain.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.dto.BaseDTO;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.repository.impl.BaseRepositoryImpl;
import com.artframework.domain.core.uitls.CompareUtil;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface BaseDomainService {

    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
     <T extends BaseDTO> void merge(List<T> oldList, List<T> newList, Function<T, Object> keyWrap, BaseRepository repository);
}
