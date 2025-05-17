package com.artframework.domain.core.lambda.order;

import com.artframework.domain.core.MPFieldLambda;
import com.artframework.domain.core.constants.Order;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/3
 **/
public class LambdaOrder<T> {
    private final Class<T> entityClass;

    protected LambdaOrder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Getter
    private final List<LambdaOrderItem> orderItems = new ArrayList<>();

    /**
     * 構造排序的Item
     * @param field
     * @param order
     * @return
     * @param <T>
     */
    public LambdaOrder<T> orderBy(SFunction<T, Serializable> field) {
        orderBy(field, Order.ASC);
        return this;
    }
    /**
     * 構造排序的Item
     * @param field
     * @param order
     * @return
     * @param <T>
     */
    public LambdaOrder<T> orderBy(SFunction<T, Serializable> field, Order order) {
        orderItems.add(new LambdaOrderItem(field, order));
        return this;
    }

    public LambdaOrder<T> orderBy(String field, Order order) {
        orderItems.add(new LambdaOrderItem(entityClass.getCanonicalName(),field, order));
        return this;
    }

    /**
     * 多字段排序, 默認ASC
     * @param field
     * @return
     */
    public LambdaOrder<T> thenBy(SFunction<T, Serializable> field) {
       orderBy(field, Order.ASC);
        return this;
    }

    public LambdaOrder<T> thenBy(SFunction<T, Serializable> field, Order order) {
        orderBy(field, order);
        return this;
    }

    public LambdaOrder<T> thenBy(String field, Order order) {
        orderItems.add(new LambdaOrderItem(entityClass.getCanonicalName(),field, order));
        return this;
    }
}
