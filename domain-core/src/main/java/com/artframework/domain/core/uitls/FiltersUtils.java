package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.lambda.query.LogicalOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/11
 * 用於通過實體過濾過濾條件
 **/
public class FiltersUtils {

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

    public static <DO> void buildWrapper(LambdaQueryWrapper<DO> wrapper, LambdaQuery.ConditionGroup filter, Class<DO> doClass) {
        Consumer<LambdaQueryWrapper<DO>> consumer = x -> {
        };
        for (Object child : filter.condition) {
            if (child instanceof LambdaQuery.Condition) {
                consumer = consumer.andThen(x -> applyCondition(x, (LambdaQuery.Condition) child, doClass));
            } else if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup childGroup = (LambdaQuery.ConditionGroup) child;
                if (childGroup.op.equals(LogicalOperator.OR)) {
                    consumer = consumer.andThen(x -> x.or(y -> buildWrapper(y, childGroup, doClass)));
                }
                if (childGroup.op.equals(LogicalOperator.AND)) {
                    consumer = consumer.andThen(x -> x.and(y -> buildWrapper(y, childGroup, doClass)));
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
