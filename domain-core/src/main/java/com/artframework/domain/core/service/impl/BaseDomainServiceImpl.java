package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.dto.BaseDTO;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.repository.impl.BaseRepositoryImpl;
import com.artframework.domain.core.service.BaseDomainService;
import com.artframework.domain.core.uitls.CompareUtil;

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
    public <T extends BaseDTO> void merge(List<T> oldList, List<T> newList, Function<T, Serializable> keyWrap, BaseRepository repository) {
        CompareUtil.CompareResult<T> compareList = CompareUtil.compareList(oldList, newList, keyWrap);

        //新增
        repository.insert(compareList.getAddList());
        //删除
        repository.delete(compareList.getDeleteList());
        //修改
        if (CollUtil.isNotEmpty(compareList.getUpdateList())) {
            List<T> updateList = compareList.getUpdateList().stream()
                    .filter(x -> ObjectUtil.isNull(x.getChanged()) || x.getChanged())
                    .collect(Collectors.toList());
            repository.update(updateList);
        }
    }
}
