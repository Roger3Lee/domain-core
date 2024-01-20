package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.LambdaMeta;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import mo.gov.dsaj.domain.core.MPFieldLambda;
import mo.gov.dsaj.domain.core.constants.Op;
import mo.gov.dsaj.domain.core.domain.BaseLoadFlag;
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

    public static <DO, T> LambdaQueryWrapper<DO> buildWrapper(LambdaQueryWrapper<DO> wrapper, BaseLoadFlag.DOFilter filter, Class<DO> doClass) {
        Op op = Op.getOp(filter.getOp());
        switch (op) {
            case IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.in(MPFieldLambda.fieldLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.in(MPFieldLambda.fieldLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case NOT_IN:
                if (filter.getValue() instanceof Iterable) {
                    wrapper.notIn(MPFieldLambda.fieldLambda(doClass, filter.getField()), ListUtil.toList((Iterable) filter.getValue()));
                } else {
                    wrapper.notIn(MPFieldLambda.fieldLambda(doClass, filter.getField()), ListUtil.toList(filter.getValue()));
                }
                break;
            case LIKE:
                wrapper.like(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NOT_LIKE:
                wrapper.notLike(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case NE:
                wrapper.ne(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GT:
                wrapper.gt(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case GE:
                wrapper.ge(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LT:
                wrapper.lt(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            case LE:
                wrapper.le(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
                break;
            default:
                //EQ
                wrapper.eq(MPFieldLambda.fieldLambda(doClass, filter.getField()), filter.getValue());
        }
        return wrapper;
    }
}
