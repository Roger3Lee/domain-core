package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.lambda.query.LogicalOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public static <T> List<LambdaQuery.Condition> getEntityFilters(Map<String, List<LambdaQuery.Condition>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    @SafeVarargs
    public static List<LambdaQuery.Condition> combine(List<LambdaQuery.Condition>... filters) {
        return Arrays.stream(filters).flatMap(List::stream).collect(Collectors.toList());
    }

    public static <T> List<LambdaQuery.Condition> getEntityFiltersEx(Map<String, List<LambdaQuery.Condition>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    public static <T> List<LambdaQuery.Condition> getEntityFilters(Map<String, List<LambdaQuery.Condition>> filters, String entity) {
        List<LambdaQuery.Condition> list = filters.get(entity);
        if (ObjectUtil.isNull(list)) {
            list = ListUtil.empty();
        }
        return list;
    }

    public static <DO> void buildWrapper(LambdaQueryWrapper<DO> wrapper, LambdaQuery.ConditionGroup filter, Class<DO> doClass) {
        for (Object child : filter.children) {
            if (child instanceof LambdaQuery.Condition) {
                applyCondition(wrapper, (LambdaQuery.Condition) child, doClass);
            } else if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup childGroup =   (LambdaQuery.ConditionGroup) child ;
                if (childGroup.operator.equals(LogicalOperator.OR)) {
                    wrapper.or(x -> buildWrapper(wrapper, childGroup, doClass));
                }
                if(childGroup.operator.equals(LogicalOperator.AND)){
                    wrapper.and(x -> buildWrapper(wrapper, childGroup, doClass));
                }
            }
        }
    }

    private static <F> void applyCondition(
            LambdaQueryWrapper<F> wrapper,
            LambdaQuery.Condition filter, Class<F> doClass
    ) {
        switch (filter.getOperator()) {
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
    }
}
