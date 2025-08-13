package com.artframework.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadFlagUtils {

    public static <T extends BaseLoadFlag> T mergeEntityQuery(T loadFlag, T loadFlagSource, String entityName) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isNull(loadFlagSource) || ObjectUtil.isEmpty(entityName)) {
            return loadFlag;
        }

        // 获取源LoadFlag中指定实体的Query
        BaseLoadFlag.Query sourceQuery = loadFlagSource.getQuery().get(entityName);
        if (ObjectUtil.isNull(sourceQuery)) {
            return loadFlag;
        }

        // 获取或创建目标LoadFlag中指定实体的Query
        BaseLoadFlag.Query targetQuery = loadFlag.getQuery().computeIfAbsent(entityName, k -> new BaseLoadFlag.Query());

        // 合并过滤条件
        if (ObjectUtil.isNotNull(sourceQuery.getFilter())) {
            if (ObjectUtil.isNull(targetQuery.getFilter())) {
                targetQuery.setFilter(sourceQuery.getFilter());
            } else {
                // 合并现有过滤条件，将源条件作为AND条件添加到目标条件
                targetQuery.getFilter().addChild(sourceQuery.getFilter());
            }
        }

        // 合并排序条件
        if (CollUtil.isNotEmpty(sourceQuery.getOrder())) {
            if (CollUtil.isEmpty(targetQuery.getOrder())) {
                targetQuery.setOrder(new ArrayList<>(sourceQuery.getOrder()));
            } else {
                // 合并排序条件，避免重复
                for (LambdaOrderItem orderItem : sourceQuery.getOrder()) {
                    if (!targetQuery.getOrder().contains(orderItem)) {
                        targetQuery.getOrder().add(orderItem);
                    }
                }
            }
        }

        return loadFlag;
    }

    /**
     * 从 LambdaQuery 中提取条件并合并到 LoadFlag
     * 
     * @param loadFlag   目标 LoadFlag
     * @param query      源 LambdaQuery
     * @param entityName 实体名称
     */
    public static <T> void mergeQueryCondition(BaseLoadFlag loadFlag, LambdaQuery<T> query, String entityName) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isNull(query) || ObjectUtil.isEmpty(entityName)) {
            return;
        }

        // 提取过滤条件和排序条件
        LambdaQuery.ConditionGroup filter = LambdaQueryUtils.toFilters(query);
        List<LambdaOrderItem> orders = LambdaQueryUtils.toOrders(query);

        if ((ObjectUtil.isNotNull(filter) && CollUtil.isNotEmpty(filter.getCondition()))
                || CollUtil.isNotEmpty(orders)) {
            // 创建临时LoadFlag并合并
            BaseLoadFlag tempSource = createTempLoadFlag(filter, orders, entityName);
            mergeEntityQuery(loadFlag, tempSource, entityName);
        }
    }
    
    /**
     * 创建临时的LoadFlag用于合并操作
     * 
     * @param filter     过滤条件
     * @param orders     排序条件
     * @param entityName 实体名称
     * @return 临时LoadFlag
     */
    private static BaseLoadFlag createTempLoadFlag(LambdaQuery.ConditionGroup filter, List<LambdaOrderItem> orders, String entityName) {
        BaseLoadFlag tempLoadFlag = new BaseLoadFlag();
        BaseLoadFlag.Query query = new BaseLoadFlag.Query();
        
        if (ObjectUtil.isNotNull(filter) && CollUtil.isNotEmpty(filter.getCondition())) {
            query.setFilter(filter);
        }
        
        if (CollUtil.isNotEmpty(orders)) {
            query.setOrder(new ArrayList<>(orders));
        }
        
        tempLoadFlag.getQuery().put(entityName, query);
        return tempLoadFlag;
    }
}
