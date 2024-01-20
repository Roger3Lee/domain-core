package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.artframework.domain.core.MPFieldLambda;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.domain.BaseLoadFlag;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        BaseLoadFlag.Filter filter = new BaseLoadFlag.Filter();
        filter.setEntity(entity);
        filter.setField(field);
        filter.setOp(op.getCode());
        filter.setValue(value);
        return filter;
    }

    public static <D> BaseLoadFlag.Filter build(SFunction<D, Serializable> column, Object value, Op op) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return FiltersUtils.build(getEntityName(meta.getInstantiatedClass()), fieldName, op, value);
    }

    public static <D> BaseLoadFlag.Filter build(SFunction<D, Serializable> column, Object value) {
        return build(column, value, Op.EQ);
    }

    public static <D> String getEntityName(Class<D> dtoClass) {
        return dtoClass.getSimpleName().replace("Domain", "").replace("DO", "");
    }

    /**
     * 原始過濾條件
     *
     * @param filters
     * @param entityClass
     * @return
     */
    public static <T> List<BaseLoadFlag.DOFilter> getEntityFilters(List<BaseLoadFlag.Filter> filters, Class<T> entityClass) {
        return getEntityFilters(filters, getEntityName(entityClass));
    }

    public static <T> List<BaseLoadFlag.DOFilter> getEntityFilters(List<BaseLoadFlag.Filter> filters, String entity) {
        List<BaseLoadFlag.DOFilter> filterDTOS = new ArrayList<>();
        if (ObjectUtil.isNotNull(filters)) {
            for (BaseLoadFlag.Filter entry : filters) {
                if (entity.equals(entry.getEntity())) {
                    filterDTOS.add(entry);
                }
            }
        }
        return filterDTOS;
    }

    public static <DO, D, R> MPFieldLambda.SSFunction<DO, R> DOLambda(Class<DO> doClass, SFunction<D, Serializable> column) {
        LambdaMeta meta = LambdaUtils.extract(column);
        String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
        return DOLambda(doClass, fieldName);
    }

    public static <DO, R> MPFieldLambda.SSFunction<DO, R> DOLambda(Class<DO> doClass,String field) {
        return MPFieldLambda.fieldLambda(doClass, field);
    }

    public static <DO, T> LambdaQueryWrapper<DO> buildWrapper(LambdaQueryWrapper<DO> wrapper, BaseLoadFlag.DOFilter filter, Class<DO> doClass) {
        Op op = Op.getOp(filter.getOp());
        switch (op) {
            case IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.in(DOLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.in(DOLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case NOT_IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.notIn(DOLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.notIn(DOLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case LIKE:
                wrapper.like(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NOT_LIKE:
                wrapper.notLike(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NE:
                wrapper.ne(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GT:
                wrapper.gt(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GE:
                wrapper.ge(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LT:
                wrapper.lt(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LE:
                wrapper.le(DOLambda(doClass, filter.getField()), filter.getValue());
                break;
            case ISNULL:
                wrapper.isNull(DOLambda(doClass, filter.getField()));
            case NOTNULL:
                wrapper.isNotNull(DOLambda(doClass, filter.getField()));
            default:
                //EQ
                wrapper.eq(DOLambda(doClass, filter.getField()), filter.getValue());
        }
        return wrapper;
    }
}
