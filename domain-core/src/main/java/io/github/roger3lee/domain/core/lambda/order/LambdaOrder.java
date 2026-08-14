package io.github.roger3lee.domain.core.lambda.order;

import io.github.roger3lee.domain.core.constants.Order;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/3
 **/
public class LambdaOrder<T> {
    @JsonIgnore
    private final Class<T> entityClass;

    /**
     * 无参构造，供 Jackson 反序列化使用（此时 entityClass 为 null）
     */
    protected LambdaOrder() {
        this.entityClass = null;
    }

    protected LambdaOrder(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    private final List<LambdaOrderItem> orderItems = new ArrayList<>();

    /**
     * 获取排序项列表（JSON 序列化为 order 属性）
     */
    @JsonProperty("order")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<LambdaOrderItem> getOrderItems() {
        return orderItems;
    }

    /**
     * 设置排序项列表（供 Jackson 反序列化使用）
     */
    @JsonProperty("order")
    public void setOrderItems(List<LambdaOrderItem> orderItems) {
        this.orderItems.clear();
        if (orderItems != null) {
            this.orderItems.addAll(orderItems);
        }
    }

    /**
     * 获取实体类名称（entityClass 为 null 时返回 null，如反序列化场景）
     */
    private String entityClassName() {
        return entityClass != null ? entityClass.getCanonicalName() : null;
    }

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
        orderItems.add(new LambdaOrderItem(entityClassName(), field, order));
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
        orderItems.add(new LambdaOrderItem(entityClassName(), field, order));
        return this;
    }
}
