package com.artframework.domain.core.lambda.order;

import com.artframework.domain.core.MPFieldLambda;
import com.artframework.domain.core.constants.Order;
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

    @Getter
    private final List<LambdaOrderItem> orderItems = new ArrayList<>();

    /**
     * 構造排序的Item
     * @param field
     * @param order
     * @return
     * @param <T>
     */
    public static <T> LambdaOrderItem orderBy(MPFieldLambda.SSFunction<T, Serializable> field, Order order) {
        return new LambdaOrderItem(field, order);
    }

    /**
     * 多字段排序, 默認ASC
     * @param field
     * @return
     */
    public LambdaOrder<T> thenBy(MPFieldLambda.SSFunction<T, Serializable> field) {
        return then(field, Order.ASC);
    }
    /**
     * 多字段排序
     * @param field
     * @param order
     * @return
     */
    public LambdaOrder<T> then(MPFieldLambda.SSFunction<T, Serializable> field, Order order) {
        this.orderItems.add(orderBy(field,order));
        return this;
    }
}
