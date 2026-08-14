package io.github.roger3lee.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.roger3lee.domain.core.domain.BaseLoadFlag;
import io.github.roger3lee.domain.core.lambda.order.LambdaOrderItem;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;

public class LoadFlagUtils {

    /**
     * 合并源LoadFlag中指定实体的查询条件到目标LoadFlag
     *
     * @param loadFlag       目标LoadFlag
     * @param loadFlagSource 源LoadFlag
     * @param entityName     实体名称
     * @return 目标LoadFlag
     */
    public static <T extends BaseLoadFlag> T mergeEntityQuery(T loadFlag, T loadFlagSource, String entityName) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isNull(loadFlagSource) || ObjectUtil.isEmpty(entityName)) {
            return loadFlag;
        }

        LambdaQuery<?> sourceQuery = loadFlagSource.getQuery().get(entityName);
        if (ObjectUtil.isNull(sourceQuery)) {
            return loadFlag;
        }

        mergeLambdaQuery(loadFlag, sourceQuery, entityName);
        return loadFlag;
    }

    /**
     * 将 LambdaQuery 合并到 LoadFlag 中指定实体的查询条件
     *
     * @param loadFlag   目标 LoadFlag
     * @param query      源 LambdaQuery
     * @param entityName 实体名称
     */
    public static <T> void mergeQueryCondition(BaseLoadFlag loadFlag, LambdaQuery<T> query, String entityName) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isNull(query) || ObjectUtil.isEmpty(entityName)) {
            return;
        }

        mergeLambdaQuery(loadFlag, query, entityName);
    }

    /**
     * 合并 LambdaQuery：目标不存在时直接放入，否则过滤条件AND连接、排序去重追加
     */
    private static void mergeLambdaQuery(BaseLoadFlag loadFlag, LambdaQuery<?> source, String entityName) {
        LambdaQuery<?> target = loadFlag.getQuery().get(entityName);
        if (ObjectUtil.isNull(target)) {
            loadFlag.getQuery().put(entityName, source);
            return;
        }
        if (target == source) {
            return;
        }

        // 合并过滤条件（AND 连接）
        if (source.hasFilter()) {
            target.getFilter().addChild(source.getFilter());
        }

        // 合并排序条件，避免重复
        if (CollUtil.isNotEmpty(source.getOrderItems())) {
            for (LambdaOrderItem orderItem : source.getOrderItems()) {
                if (!target.getOrderItems().contains(orderItem)) {
                    target.getOrderItems().add(orderItem);
                }
            }
        }
    }
}
