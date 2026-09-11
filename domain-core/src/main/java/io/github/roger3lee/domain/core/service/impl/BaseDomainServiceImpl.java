package io.github.roger3lee.domain.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.roger3lee.domain.core.domain.BaseAggregateDomain;
import io.github.roger3lee.domain.core.domain.BaseDomain;
import io.github.roger3lee.domain.core.domain.PageDomain;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import io.github.roger3lee.domain.core.repository.BaseRepository;
import io.github.roger3lee.domain.core.service.BaseDomainService;
import io.github.roger3lee.domain.core.utils.CompareUtil;
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
     * 为聚合根领域对象绑定当前领域服务
     * 查询返回后可直接调用 loadRelated 加载关联数据，无需再手动调用 set_service
     *
     * @param domain 领域对象
     * @return 绑定服务后的领域对象
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <T extends BaseDomain> T bindService(T domain) {
        if (domain instanceof BaseAggregateDomain) {
            ((BaseAggregateDomain) domain).set_service(this);
        }
        return domain;
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
        return bindService(repository.query(lambdaQuery));
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
        List<T> list = repository.queryList(lambdaQuery);
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(this::bindService);
        }
        return list;
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
        IPage<T> page = repository.queryPage(pageDomain, lambdaQuery);
        if (ObjectUtil.isNotNull(page) && CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(this::bindService);
        }
        return page;
    }

    /**
     * 统计查询符合条件的数据总量
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseDomain> Long queryCount(Class<T> clazz, LambdaQuery<T> lambdaQuery) {
        BaseRepository<T, ?> repository = getRepository(clazz);
        return repository.queryCount(lambdaQuery);
    }
}
    