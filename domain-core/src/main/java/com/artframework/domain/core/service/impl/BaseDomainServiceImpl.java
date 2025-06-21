package com.artframework.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseDomain;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.repository.BaseRepository;
import com.artframework.domain.core.service.BaseDomainService;
import com.artframework.domain.core.utils.CompareUtil;
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

    // 用于保存实体和repository之间的关系
    protected final Map<String, BaseRepository<?, ?>> domainRepositoryMap = new HashMap<>();


    public  <T extends BaseDomain> void addRepository(Class<T> clazz, BaseRepository<?, ?> repository) {
        this.domainRepositoryMap.put(clazz.getCanonicalName(), repository);
    }
    /**
     * 获取指定类型的Repository
     * @param clazz 实体类型
     * @return 对应的Repository实例
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> BaseRepository<T, ?> getRepository(Class<T> clazz) {
        BaseRepository<?, ?> repository = domainRepositoryMap.get(clazz.getCanonicalName());
        if (repository == null) {
            throw new UnsupportedOperationException("未找到类型 " + clazz.getCanonicalName() + " 对应的Repository");
        }
        return (BaseRepository<T, ?>) repository;
    }

    /**
     * 合并新旧列表，自动处理增删改
     * @param oldList 旧列表
     * @param newList 新列表
     * @param keyWrap 主键提取函数
     * @param repository 仓储接口
     */
    @Override
    @SuppressWarnings("unchecked")
    public <D extends BaseDomain> void merge(List<D> oldList, List<D> newList,
                                           Function<D, Serializable> keyWrap, 
                                           BaseRepository<D,?> repository) {
        CompareUtil.CompareResult<D> compareResult = CompareUtil.compareList(oldList, newList, keyWrap);

        // 删除
        if (CollUtil.isNotEmpty(compareResult.getDeleteList())) {
            repository.delete(compareResult.getDeleteList());
        }

        // 新增
        if (CollUtil.isNotEmpty(compareResult.getAddList())) {
            repository.insert(compareResult.getAddList());
        }
        
        // 修改 - 只更新有变化的数据
        if (CollUtil.isNotEmpty(compareResult.getUpdateList())) {
            List<D> updateList = compareResult.getUpdateList().stream()
                    .filter(item -> ObjectUtil.isNull(item.getChanged()) || Boolean.TRUE.equals(item.getChanged()))
                    .collect(Collectors.toList());
            if (CollUtil.isNotEmpty(updateList)) {
                repository.update(updateList);
            }
        }
    }

    /**
     * 根据条件删除关联数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> Boolean deleteRelated(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允许不加过滤条件删除数据");
            return false;
        }

        BaseRepository<T, ?> repository = getRepository(clazz);
        int effect = repository.deleteByFilter(lambdaQuery);
        return effect > 0;
    }

    /**
     * 查询单条数据
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> T queryOne(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允许不加过滤条件查询数据");
            return null;
        }

        BaseRepository<T, ?> repository = getRepository(clazz);
        return repository.query(lambdaQuery);
    }

    /**
     * 查询数据列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> List<T> queryList(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        if (!lambdaQuery.hasFilter()) {
            log.warn("不允许不加过滤条件查询数据");
            return ListUtil.empty();
        }

        BaseRepository<T, ?> repository = getRepository(clazz);
        return repository.queryList(lambdaQuery);
    }

    /**
     * 分页查询（无条件）
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain) {
        return queryPage(clazz, pageDomain, null);
    }

    /**
     * 分页查询（带条件）
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> IPage<T> queryPage(Class<T> clazz, PageDomain pageDomain, LambdaQuery<T> lambdaQuery) {
        BaseRepository<T, ?> repository = getRepository(clazz);
        return repository.queryPage(pageDomain, lambdaQuery);
    }
}
    