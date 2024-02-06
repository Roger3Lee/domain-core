package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.service.BaseDomainService;
import com.artframework.domain.core.uitls.CompareUtil;
import com.artframework.domain.core.uitls.FiltersUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseDomainServiceImpl implements BaseDomainService {

    //用於保存實體和repository之間的關係
    public final Map<String, BaseRepository> _DomainRepositoryMap = new HashMap<>();

    public <T> BaseRepository getRepository(Class<T> clazz) {
        return this._DomainRepositoryMap.get(clazz.getCanonicalName());
    }

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
        if (CollUtil.isNotEmpty(compareList.getAddList())) {
            repository.insert(compareList.getAddList());
        }
        //删除
        if (CollUtil.isNotEmpty(compareList.getDeleteList())) {
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> Boolean deleteRelated(Class<T> clazz, List<LambdaFilter<T>> lambdaFilters) {
        if (CollUtil.isEmpty(lambdaFilters)) {
            return false;
        }

        int totalEffect = 0;
        BaseRepository repository = _DomainRepositoryMap.get(clazz.getCanonicalName());
        if (null == repository) {
            throw new UnsupportedOperationException("刪除不支持的實體:" + clazz.getCanonicalName());
        }

        totalEffect += repository.deleteByFilter(FiltersUtils.buildLambdaFilter(lambdaFilters));

        return totalEffect > 0;
    }
}
    