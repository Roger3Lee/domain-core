package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/11
 * 用於通過實體過濾過濾條件
 **/
public class FiltersUtils {

    /**
     * @param entity
     * @param field
     * @param op
     * @param value
     * @return
     */
    public static LambdaFilter.Filter build(String entity, String field, Op op, Object value) {

        return build(entity, field, op, value, null);
    }

    public static LambdaFilter.Filter build(String entity, String field, Op op, Object value, List<LambdaFilter.DOFilter> orFilterList) {
        LambdaFilter.Filter filter = new LambdaFilter.Filter();
        filter.setEntity(entity);
        filter.setField(field);
        filter.setOp(op.getCode());
        filter.setValue(value);
        filter.setOrFilter(orFilterList);
        return filter;
    }

    public static <D> LambdaFilter.Filter build(SFunction<D, Serializable> column, Object value, Op op) {
        return build(column, value, op, null);
    }

    public static <D> LambdaFilter.Filter build(SFunction<D, Serializable> column, Object value, Op op, List<LambdaFilter.DOFilter> orFilterList) {
        LambdaCache.LambdaInfo<D> lambdaInfo = LambdaCache.info(column);
        return build(getEntityName(lambdaInfo.getClazz()), lambdaInfo.getFieldName(), op, value, orFilterList);
    }

    public static <D> LambdaFilter.Filter build(SFunction<D, Serializable> column, Object value) {
        return build(column, value, Op.EQ, null);
    }

    public static <D> LambdaFilter.Filter toFilter(LambdaFilter<D> filter) {
        if (CollectionUtil.isNotEmpty(filter.getOr())) {
            List<LambdaFilter<D>> orFilterList = filter.getOr();
            return build(filter.getField(), filter.getValue(), filter.getOp(),
                    orFilterList.stream().map(orFilter -> build(orFilter.getField(), orFilter.getValue(), orFilter.getOp())).collect(Collectors.toList()));
        } else {
            return build(filter.getField(), filter.getValue(), filter.getOp());
        }
    }

    public static <D> List<LambdaFilter.Filter> toFilters(List<LambdaFilter<D>> lambdaFilters) {
        if (CollectionUtil.isEmpty(lambdaFilters)) {
            return ListUtil.empty();
        }

        //轉換
        List<LambdaFilter.Filter> filters = new ArrayList<>(lambdaFilters.size());
        for (LambdaFilter<D> filter : lambdaFilters) {
            filters.add(toFilter(filter));
        }
        return filters;
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
    public static <T> List<LambdaFilter.Filter> getEntityFilters(Map<String, List<LambdaFilter.Filter>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    @SafeVarargs
    public static List<LambdaFilter.Filter> combine(List<LambdaFilter.Filter>... filters) {
        return Arrays.stream(filters).flatMap(List::stream).collect(Collectors.toList());
    }

    public static <T> List<LambdaFilter.Filter> getEntityFiltersEx(Map<String, List<LambdaFilter.Filter>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    public static <T> List<LambdaFilter.Filter> getEntityFilters(Map<String, List<LambdaFilter.Filter>> filters, String entity) {
        List<LambdaFilter.Filter> list = filters.get(entity);
        if (ObjectUtil.isNull(list)) {
            list = ListUtil.empty();
        }
        return list;
    }

    public static <DO, T> LambdaQueryWrapper<DO> buildWrapper(LambdaQueryWrapper<DO> wrapper, LambdaFilter.DOFilter filter, Class<DO> doClass) {
        if (CollectionUtil.isEmpty(filter.getOrFilter())) {
            //沒有OR過濾條件時
            Op op = Op.getOp(filter.getOp());
            switch (op) {
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
        } else {
            Consumer<LambdaQueryWrapper<DO>> consumer = x -> x.and(y -> buildWrapper(y, LambdaFilter.DOFilter.copy(filter), doClass));
            for (LambdaFilter.DOFilter filterItem : filter.getOrFilter()) {
                consumer = consumer.andThen(x -> x.or(y -> buildWrapper(y, filterItem, doClass)));
            }
            Consumer<LambdaQueryWrapper<DO>> finalConsumer = consumer;
            wrapper.and(x -> x.and(finalConsumer));
        }
        return wrapper;
    }
}
