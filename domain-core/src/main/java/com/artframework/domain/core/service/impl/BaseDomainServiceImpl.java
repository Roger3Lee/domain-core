package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.service.BaseDomainService;
import com.artframework.domain.core.uitls.CompareUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
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
            List<D> updateList = compareList.getUpdateList().stream()
                    .filter(x -> ObjectUtil.isNull(x.getChanged()) || x.getChanged())
                    .collect(Collectors.toList());
            repository.update(updateList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> Boolean deleteRelated(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允許不加過濾條件刪除數據");
            return false;
        }

        int totalEffect = 0;
        BaseRepository repository = _DomainRepositoryMap.get(clazz.getCanonicalName());
        if (null == repository) {
            throw new UnsupportedOperationException("刪除不支持的實體:" + clazz.getCanonicalName());
        }

        totalEffect += repository.deleteByFilter(lambdaQuery);

        return totalEffect > 0;
    }

    @Override
    public <T> T queryOne(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允許不加過濾條件刪除數據");
            return null;
        }

        BaseRepository repository = _DomainRepositoryMap.get(clazz.getCanonicalName());
        if (null == repository) {
            throw new UnsupportedOperationException("查詢不支持的實體:" + clazz.getCanonicalName());
        }

        return (T) repository.query(lambdaQuery);
    }

    @Override
    public <T> List<T> queryList(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允許不加過濾條件加載數據");
            return ListUtil.empty();
        }

        BaseRepository repository = _DomainRepositoryMap.get(clazz.getCanonicalName());
        if (null == repository) {
            throw new UnsupportedOperationException("查詢不支持的實體:" + clazz.getCanonicalName());
        }

        return repository.queryList(lambdaQuery);
    }

    @Override
    public <T> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain) {
        return queryPage(clazz, pageDomain, null);
    }


    @Override
    public <T> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain, LambdaQuery<T> lambdaQuery) {
        BaseRepository repository = _DomainRepositoryMap.get(clazz.getCanonicalName());
        if (null == repository) {
            throw new UnsupportedOperationException("查詢不支持的實體:" + clazz.getCanonicalName());
        }

        return repository.queryPage(pageDomain, lambdaQuery);
    }
}
    