package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.lambda.query.LogicalOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LambdaQueryUtils {

    public static <T> LambdaQuery<T> combine(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter, List<LambdaOrderItem> orderItems) {
        if (ObjectUtil.isNotEmpty(filter)) {
            combineFilter(lambdaQuery, filter);
        }

        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(x -> lambdaQuery.orderBy(x.getField(), x.getOrder()));
        }
        return lambdaQuery;
    }

    private static <T> void combineFilter(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter) {
        lambdaQuery.getCondition().addChild(filter);
    }


    public static <T> List<LambdaOrderItem> getEntityOrders(Map<String, List<LambdaOrderItem>> orders, Class<T> tClass) {
        if (ObjectUtil.isNull(orders)) {
            return null;
        }
        return orders.get(tClass.getCanonicalName());
    }


    /**
     * 生成排序的
     *
     * @param wrapper
     * @param order
     * @param doClass
     * @param <DO>
     */

    public static <DO> void buildOrderWrapper(LambdaQueryWrapper<DO> wrapper, LambdaOrderItem order, Class<DO> doClass) {
        switch (order.getOrder()) {
            case DES:
                wrapper.orderByDesc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
            default:
                //EQ
                wrapper.orderByAsc(LambdaCache.DOLambda(doClass, order.getField()));
        }
    }

    public static <T> List<LambdaOrderItem> toOrders(LambdaQuery<T> query) {
        if(null==query){
            return ListUtil.empty();
        }
        return query.getOrderItems();
    }


    public static <D> String getEntityName(Class<D> dtoClass) {
        return dtoClass.getSimpleName();
    }

    /**
     * 原始過濾條件
     *
     * @param filters
     * @param entityClass
     * @return
     */
    public static <T> LambdaQuery.ConditionGroup getEntityFilters(Map<String, LambdaQuery.ConditionGroup> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }


    public static <T> LambdaQuery.ConditionGroup getEntityFiltersEx(Map<String, LambdaQuery.ConditionGroup> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    public static <T> LambdaQuery.ConditionGroup getEntityFilters(Map<String, LambdaQuery.ConditionGroup> filters, String entity) {
        return filters.get(entity);
    }

    public static <DO> void buildFilterWrapper(LambdaQueryWrapper<DO> wrapper, LambdaQuery.ConditionGroup filter, Class<DO> doClass) {
        Consumer<LambdaQueryWrapper<DO>> consumer = x -> {
        };
        for (Object child : filter.condition) {
            if (child instanceof LambdaQuery.Condition) {
                consumer = consumer.andThen(x -> applyCondition(x, (LambdaQuery.Condition) child, doClass));
            } else if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup childGroup = (LambdaQuery.ConditionGroup) child;
                if (childGroup.op.equals(LogicalOperator.OR)) {
                    consumer = consumer.andThen(x -> x.or(y -> buildFilterWrapper(y, childGroup, doClass)));
                }
                if (childGroup.op.equals(LogicalOperator.AND)) {
                    consumer = consumer.andThen(x -> x.and(y -> buildFilterWrapper(y, childGroup, doClass)));
                }
            }
        }
        Consumer<LambdaQueryWrapper<DO>> finalConsumer = consumer;
        if (filter.op.equals(LogicalOperator.AND)) {
            consumer.accept(wrapper);
        }
        if (filter.op.equals(LogicalOperator.OR)) {
            wrapper.or(finalConsumer);
        }
    }

    private static <F> LambdaQueryWrapper<F> applyCondition(
            LambdaQueryWrapper<F> wrapper,
            LambdaQuery.Condition filter, Class<F> doClass
    ) {
        switch (filter.getOp()) {
            case IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.in(LambdaCache.DOLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.in(LambdaCache.DOLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case NOT_IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.notIn(LambdaCache.DOLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.notIn(LambdaCache.DOLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case LIKE:
                wrapper.like(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LIKE_LEFT:
                wrapper.likeLeft(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LIKE_RIGHT:
                wrapper.likeRight(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NOT_LIKE:
                wrapper.notLike(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NE:
                wrapper.ne(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GT:
                wrapper.gt(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GE:
                wrapper.ge(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LT:
                wrapper.lt(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LE:
                wrapper.le(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case ISNULL:
                wrapper.isNull(LambdaCache.DOLambda(doClass, filter.getField()));
                break;
            case NOTNULL:
                wrapper.isNotNull(LambdaCache.DOLambda(doClass, filter.getField()));
                break;
            default:
                //EQ
                if (null == filter.getValue()) {
                    //為null的情況下使用is null 過濾
                    wrapper.isNull(LambdaCache.DOLambda(doClass, filter.getField()));
                } else {
                    wrapper.eq(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
                }
        }
        return wrapper;
    }

    public static <T> LambdaQuery.ConditionGroup toFilters(LambdaQuery<T> query) {
        if (null == query) {
            return null;
        }
        return query.getCondition();
    }
}
