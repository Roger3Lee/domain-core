package com.artframework.domain.core.lambda;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.artframework.domain.core.constants.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/3
 **/
public class LambdaOrder<T> {
    @Getter
    private final Map<String, List<LambdaOrderItem>> orderItems = new HashMap<>();

    private LambdaOrder() {
    }

    private LambdaOrder(LambdaOrderItem orderItem) {
        this.orderItems.merge(orderItem.getEntity(), ListUtil.toList(orderItem), (x, y) -> {
            x.addAll(y);
            return x;
        });
    }

    public List<LambdaOrderItem> toOrderItems() {
        List<LambdaOrderItem> list = new ArrayList<>();
        orderItems.forEach((x, y) -> list.addAll(y));
        return list;
    }

    public static <T> LambdaOrder<T> build() {
        return new LambdaOrder<T>();
    }

    /**
     * 構造排序，默認ASC
     * @param field
     * @return
     * @param <T>
     */
    public static <T> LambdaOrder<T> build(SFunction<T, Serializable> field) {
        return build(field,Order.ASC);
    }

    /**
     * 構造排序
     * @param field
     * @param order
     * @return
     * @param <T>
     */
    public static <T> LambdaOrder<T> build(SFunction<T, Serializable> field, Order order) {
        LambdaOrderItem orderItem = new LambdaOrderItem(field, order);
        return new LambdaOrder<T>(orderItem);
    }

    public static <T> LambdaOrder<T> buildByFieldName(Class<T> clazz, String field, Order order) {
        LambdaOrderItem orderItem = new LambdaOrderItem(clazz, field, order);
        return new LambdaOrder<T>(orderItem);
    }
    /**
     * 構造排序的Item
     * @param field
     * @param order
     * @return
     * @param <T>
     */
    public static <T> LambdaOrderItem buildItem(SFunction<T, Serializable> field, Order order) {
        return new LambdaOrderItem(field, order);
    }

    /**
     * 多字段排序, 默認ASC
     * @param field
     * @return
     */
    public LambdaOrder<T> then(SFunction<T, Serializable> field) {
        return then(field, Order.ASC);
    }
    /**
     * 多字段排序
     * @param field
     * @param order
     * @return
     */
    public LambdaOrder<T> then(SFunction<T, Serializable> field, Order order) {
        this.addItem(buildItem(field,order));
        return this;
    }

    private void addItem(LambdaOrderItem orderItem){
        this.orderItems.merge(orderItem.getEntity(), ListUtil.toList(orderItem), (x, y) -> {
            x.addAll(y);
            return x;
        });
    }

    @AllArgsConstructor
    public static class LambdaOrderItem {
        public <T> LambdaOrderItem(SFunction<T, Serializable> field, Order order) {
            LambdaCache.LambdaInfo<T> lambdaInfo = LambdaCache.info(field);
            this.entity = lambdaInfo.getClazzName();
            this.field = lambdaInfo.getFieldName();
            this.order = order;
        }

        public <T> LambdaOrderItem(Class<T> clazz, String field, Order order) {
            this.entity = clazz.getCanonicalName();
            this.field = field;
            this.order = order;
        }

        @Getter
        @JsonIgnore
        private String entity;
        @Getter
        private String field;
        @Getter
        private com.artframework.domain.core.constants.Order order;
    }
}
