package io.github.roger3lee.domain.core.domain;

import cn.hutool.core.collection.CollUtil;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;

import lombok.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/

@Data
public class BaseLoadFlag {
    /**
     * 各实体的查询条件（key 为实体简单类名，value 为该实体的 LambdaQuery）
     * <p>JSON 形状：{@code {"query": {"EntityName": {"filter": {...}, "order": [...]}}}}</p>
     */
    @Getter
    private Map<String, LambdaQuery<?>> query = new HashMap<>();

    /**
     * 为指定实体类设置 LambdaQuery 过滤条件。
     * <p>若该实体已存在查询条件，则将新条件通过 AND 连接合并进去。</p>
     *
     * <pre>
     * loadFlag.lambdaQuery(MemberDomain.class,
     *     LambdaQuery.of(MemberDomain.class).eq(MemberDomain::getStatus, "ACTIVE"));
     * </pre>
     *
     * @param entityClass 实体类
     * @param lambdaQuery LambdaQuery 实例
     * @return this，支持链式调用
     */
    public <T> BaseLoadFlag lambdaQuery(Class<T> entityClass, LambdaQuery<T> lambdaQuery) {
        if (entityClass == null || lambdaQuery == null) {
            return this;
        }
        String entityName = entityClass.getSimpleName();
        LambdaQuery<?> existing = query.get(entityName);
        if (existing == null || existing == lambdaQuery) {
            query.put(entityName, lambdaQuery);
            return this;
        }

        // 已存在查询条件，AND 合并过滤条件并追加排序
        if (lambdaQuery.hasFilter()) {
            existing.getFilter().addChild(lambdaQuery.getFilter());
        }
        if (CollUtil.isNotEmpty(lambdaQuery.getOrderItems())) {
            existing.getOrderItems().addAll(lambdaQuery.getOrderItems());
        }
        return this;
    }

    /**
     * 为指定实体类通过 Consumer 方式设置 LambdaQuery 过滤条件。
     *
     * <pre>
     * loadFlag.lambdaQuery(MemberDomain.class, q -&gt; q.eq(MemberDomain::getStatus, "ACTIVE"));
     * </pre>
     *
     * @param entityClass 实体类
     * @param consumer    LambdaQuery 构建器
     * @return this，支持链式调用
     */
    public <T> BaseLoadFlag lambdaQuery(Class<T> entityClass, Consumer<LambdaQuery<T>> consumer) {
        if (entityClass == null) {
            return this;
        }
        LambdaQuery<T> lambdaQuery = LambdaQuery.of(entityClass);
        if (consumer != null) {
            consumer.accept(lambdaQuery);
        }
        return lambdaQuery(entityClass, lambdaQuery);
    }
}
