package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import mo.gov.dsaj.domain.core.domain.BaseDomain;
import mo.gov.dsaj.domain.core.repository.BaseRepository;
import mo.gov.dsaj.domain.core.service.BaseDomainService;
import mo.gov.dsaj.domain.core.uitls.CompareUtil;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseDomainServiceImpl implements BaseDomainService {

    /**
     * @param oldList
     * @param newList
     * @param keyWrap
     * @param repository
     * @param
     */
    @Override
    public <D extends BaseDomain> void merge(List<D> oldList, List<D> newList, Function<D, Serializable> keyWrap, BaseRepository repository) {
        CompareUtil.CompareResult<D> compareList = CompareUtil.compareList(oldList, newList, keyWrap);

        //新增
        if(CollUtil.isNotEmpty(compareList.getAddList())){
            repository.insert(compareList.getAddList());
        }
        //删除
        if(CollUtil.isNotEmpty(compareList.getDeleteList())){
            repository.delete(compareList.getDeleteList());
        }
        //修改
        if (CollUtil.isNotEmpty(compareList.getUpdateList())) {
            List<D> updateList = compareList.getUpdateList().stream()
                    .filter(x -> ObjectUtil.isNull(x.getChanged()) || x.getChanged())
                    .collect(Collectors.toList());
            repository.update(updateList);
        }
    }
}
    