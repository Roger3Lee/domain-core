package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.LambdaFilter;

import java.io.Serializable;
import java.util.ArrayList;
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
    public static BaseLoadFlag.Filter build(String entity, String field, Op op, Object value) {

        return build(entity, field, op, value, null);
    }

    public static BaseLoadFlag.Filter build(String entity, String field, Op op, Object value, BaseLoadFlag.DOFilter orFilter) {
        BaseLoadFlag.Filter filter = new BaseLoadFlag.Filter();
        filter.setEntity(entity);
        filter.setField(field);
        filter.setOp(op.getCode());
        filter.setValue(value);
        filter.setOrFilter(orFilter);
        return filter;
    }

    public static <D> BaseLoadFlag.Filter build(SFunction<D, Serializable> column, Object value, Op op) {
        return build(column, value, op, null);
    }

    public static <D> BaseLoadFlag.Filter build(SFunction<D, Serializable> column, Object value, Op op, BaseLoadFlag.DOFilter orFilter) {
        LambdaCache.LambdaInfo<D> lambdaInfo = LambdaCache.info(column);
        return build(getEntityName(lambdaInfo.getClazz()), lambdaInfo.getFieldName(), op, value, orFilter);
    }

    public static <D> BaseLoadFlag.Filter build(SFunction<D, Serializable> column, Object value) {
        return build(column, value, Op.EQ, null);
    }

    public static <D> List<BaseLoadFlag.Filter> buildLambdaFilter(List<LambdaFilter<D>> lambdaFilters) {
        if(CollectionUtil.isEmpty(lambdaFilters)){
            return ListUtil.empty();
        }

        //轉換
        List<BaseLoadFlag.Filter> filters = new ArrayList<>(lambdaFilters.size());
        for (LambdaFilter<D> filter : lambdaFilters) {
            if (ObjectUtil.isNotNull(filter.getOr())) {
                LambdaFilter<D> orFilter = filter.getOr();
                filters.add(build(filter.getField(), filter.getValue(), filter.getOp(), build(orFilter.getField(), orFilter.getValue(), orFilter.getOp())));
            } else {
                filters.add(build(filter.getField(), filter.getValue(), filter.getOp()));
            }
        }
        return filters;
    }

    public static <D> String getEntityName(Class<D> dtoClass) {
        return dtoClass.getCanonicalName();
    }

    /**
     * 原始過濾條件
     *
     * @param filters
     * @param entityClass
     * @return
     */
    public static <T> List<BaseLoadFlag.DOFilter> getEntityFilters(Map<String, List<BaseLoadFlag.Filter>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass)).stream().map(x -> (BaseLoadFlag.DOFilter) x).collect(Collectors.toList());
    }

    public static <T> List<BaseLoadFlag.Filter> getEntityFiltersEx(Map<String, List<BaseLoadFlag.Filter>> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    public static <T> List<BaseLoadFlag.Filter> getEntityFilters(Map<String, List<BaseLoadFlag.Filter>> filters, String entity) {
        List<BaseLoadFlag.Filter> list = filters.get(entity);
        if (ObjectUtil.isNull(list)) {
            list = ListUtil.empty();
        }
        return list;
    }

    public static <DO, T> LambdaQueryWrapper<DO> buildWrapper(LambdaQueryWrapper<DO> wrapper, BaseLoadFlag.DOFilter filter, Class<DO> doClass) {
        if (ObjectUtil.isNull(filter.getOrFilter())) {
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
                    wrapper.eq(LambdaCache.DOLambda(doClass, filter.getField()), filter.getValue());
            }
        } else {
            Consumer<LambdaQueryWrapper<DO>> consumer = x -> x.and(y -> buildWrapper(y, BaseLoadFlag.DOFilter.copy(filter), doClass))
                    .or(y -> buildWrapper(y, filter.getOrFilter(), doClass));
            wrapper.and(x -> x.and(consumer));
        }
        return wrapper;
    }
}
