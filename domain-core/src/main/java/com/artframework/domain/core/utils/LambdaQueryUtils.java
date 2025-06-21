package com.artframework.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.lambda.query.LogicalOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Lambda 查询工具类
 * 提供查询条件和排序的构建工具方法
 */
public class LambdaQueryUtils {

    /**
     * 合并查询条件和排序
     * 
     * @param lambdaQuery 目标查询对象
     * @param filter      过滤条件组
     * @param orderItems  排序项列表
     * @return 合并后的查询对象
     */
    public static <T> LambdaQuery<T> combine(LambdaQuery<T> lambdaQuery,
            LambdaQuery.ConditionGroup filter,
            List<LambdaOrderItem> orderItems) {
        if (ObjectUtil.isNotNull(filter)) {
            combineFilter(lambdaQuery, filter);
        }

        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(orderItem -> lambdaQuery.orderBy(orderItem.getField(), orderItem.getOrder()));
        }
        return lambdaQuery;
    }

    /**
     * 合并过滤条件到查询对象
     * 智能合并：避免不必要的嵌套
     */
    private static <T> void combineFilter(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter) {
        if (filter == null || CollUtil.isEmpty(filter.getCondition())) {
            return;
        }

        LambdaQuery.ConditionGroup rootFilter = lambdaQuery.getFilter();
        List<Object> conditionList = new ArrayList<>();
        if (isAndLogic(filter, conditionList)) {
            conditionList.forEach(rootFilter::addChild);
        } else {
            // 3. 其他情况保持原有逻辑，但会生成嵌套结构
            rootFilter.addChild(filter);
        }
    }

    /**
     * 获取指定实体类的排序条件
     */
    public static <T> List<LambdaOrderItem> getEntityOrders(Map<String, List<LambdaOrderItem>> orders,
            Class<T> entityClass) {
        if (ObjectUtil.isNull(orders) || entityClass == null) {
            return null;
        }
        return orders.get(entityClass.getCanonicalName());
    }

    /**
     * 构建排序条件到 MyBatis Plus 查询包装器
     * 
     * @param wrapper 查询包装器
     * @param order   排序项
     * @param doClass DO 类型
     */
    public static <DO> void buildOrderWrapper(LambdaQueryWrapper<DO> wrapper,
            LambdaOrderItem order,
            Class<DO> doClass) {
        if (wrapper == null || order == null || doClass == null) {
            return;
        }

        switch (order.getOrder()) {
            case DESC:
                wrapper.orderByDesc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
            case ASC:
            default:
                wrapper.orderByAsc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
        }
    }

    /**
     * 提取查询对象的排序条件
     */
    public static <T> List<LambdaOrderItem> toOrders(LambdaQuery<T> query) {
        if (query == null) {
            return ListUtil.empty();
        }
        return query.getOrderItems();
    }

    /**
     * 获取实体类简单名称
     */
    public static <D> String getEntityName(Class<D> entityClass) {
        return entityClass != null ? entityClass.getSimpleName() : null;
    }

    /**
     * 根据实体类获取过滤条件
     */
    public static <T> LambdaQuery.ConditionGroup getEntityFilters(Map<String, LambdaQuery.ConditionGroup> filters,
            Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    /**
     * 根据实体名称获取过滤条件
     */
    public static LambdaQuery.ConditionGroup getEntityFilters(Map<String, LambdaQuery.ConditionGroup> filters,
            String entityName) {
        if (filters == null || entityName == null) {
            return null;
        }
        return filters.get(entityName);
    }


    private static Boolean isAndLogic(LambdaQuery.ConditionGroup rootGroup, List<Object> conditionList) {
        if (!LogicalOperator.AND.equals(rootGroup.getOp())) {
            return false;
        }

        // OR子组的组合
        boolean isAndGroup = true;
        for (Object child : rootGroup.getCondition()) {
            if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup group = (LambdaQuery.ConditionGroup) child;
                if (LogicalOperator.OR.equals(group.getOp())) {
                    isAndGroup = false;
                } else {
                    if (!isAndLogic(group, conditionList)) {
                        conditionList.add(group);
                    }
                }
            } else {
                conditionList.add(child);
            }
        }

        //都是and条件
        return isAndGroup;
    }


    /**
     * 构建过滤条件到 MyBatis Plus 查询包装器
     * 
     * @param wrapper 查询包装器
     * @param filter  过滤条件组
     * @param doClass DO 类型
     */
    public static <DO> void buildFilterWrapper(LambdaQueryWrapper<DO> wrapper,
            LambdaQuery.ConditionGroup filter,
            Class<DO> doClass) {
        if (wrapper == null || filter == null || doClass == null || CollUtil.isEmpty(filter.getCondition())) {
            return;
        }

        for (Object child : filter.getCondition()) {
            if (child instanceof LambdaQuery.Condition) {
                applyCondition(wrapper, (LambdaQuery.Condition) child, doClass);
            } else if (child instanceof LambdaQuery.ConditionGroup) {
                List<Object> conditionList = new ArrayList<>();
                if (isAndLogic((LambdaQuery.ConditionGroup) child, conditionList)) {
                    conditionList.forEach(x -> {
                        if (x instanceof LambdaQuery.Condition) {
                            applyCondition(wrapper, (LambdaQuery.Condition) x, doClass);
                        } else {
                            buildFilterWrapper(wrapper, (LambdaQuery.ConditionGroup) x, doClass);
                        }
                    });
                } else {
                    if (LogicalOperator.AND.equals(((LambdaQuery.ConditionGroup) child).getOp())) {
                        wrapper.and(w -> buildFilterWrapper(w, (LambdaQuery.ConditionGroup) child, doClass));
                    } else {
                        wrapper.or(w -> buildFilterWrapper(w, (LambdaQuery.ConditionGroup) child, doClass));
                    }
                }
            }
        }
    }

    /**
     * 应用单个条件到查询包装器
     */
    private static <F> void applyCondition(LambdaQueryWrapper<F> wrapper,
            LambdaQuery.Condition condition,
            Class<F> doClass) {
        if (wrapper == null || condition == null || doClass == null) {
            return;
        }

        Op op = condition.getOp();
        Object value = condition.getValue();
        String field = condition.getField();

        if (field == null) {
            return;
        }

        switch (op) {
            case IN:
                handleInCondition(wrapper, field, value, doClass, false);
                break;
            case NOT_IN:
                handleInCondition(wrapper, field, value, doClass, true);
                break;
            case LIKE:
                wrapper.like(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LIKE_LEFT:
                wrapper.likeLeft(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LIKE_RIGHT:
                wrapper.likeRight(LambdaCache.DOLambda(doClass, field), value);
                break;
            case NOT_LIKE:
                wrapper.notLike(LambdaCache.DOLambda(doClass, field), value);
                break;
            case NE:
                wrapper.ne(LambdaCache.DOLambda(doClass, field), value);
                break;
            case GT:
                wrapper.gt(LambdaCache.DOLambda(doClass, field), value);
                break;
            case GE:
                wrapper.ge(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LT:
                wrapper.lt(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LE:
                wrapper.le(LambdaCache.DOLambda(doClass, field), value);
                break;
            case ISNULL:
                wrapper.isNull(LambdaCache.DOLambda(doClass, field));
                break;
            case NOTNULL:
                wrapper.isNotNull(LambdaCache.DOLambda(doClass, field));
                break;
            case EQ:
            default:
                // 等于条件，特殊处理 null 值
                if (value == null) {
                    wrapper.isNull(LambdaCache.DOLambda(doClass, field));
                } else {
                    wrapper.eq(LambdaCache.DOLambda(doClass, field), value);
                }
                break;
        }
    }

    /**
     * 处理 IN 和 NOT IN 条件
     */
    private static <F> void handleInCondition(LambdaQueryWrapper<F> wrapper,
            String field,
            Object value,
            Class<F> doClass,
            boolean isNotIn) {
        if (value == null) {
            return;
        }

        List<?> valueList;
        if (value instanceof Iterable) {
            valueList = ListUtil.toList((Iterable<?>) value);
        } else {
            valueList = ListUtil.toList(value);
        }

        if (CollUtil.isNotEmpty(valueList)) {
            if (isNotIn) {
                wrapper.notIn(LambdaCache.DOLambda(doClass, field), valueList);
            } else {
                wrapper.in(LambdaCache.DOLambda(doClass, field), valueList);
            }
        }
    }

    /**
     * 提取查询对象的过滤条件
     */
    public static <T> LambdaQuery.ConditionGroup toFilters(LambdaQuery<T> query) {
        if (query == null) {
            return null;
        }
        return query.getFilter();
    }
}
