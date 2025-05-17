package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.MPFieldLambda;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadFlagUtils {
    public static <T extends BaseLoadFlag> T addFilters(T loadFlag, List<LambdaQuery.Condition> filters) {
        if (CollectionUtil.isNotEmpty(filters)) {
            Map<String, List<LambdaQuery.Condition>> filterMap = filters.stream().collect(Collectors.groupingBy(LambdaQuery.Condition::getEntity));
            for (Map.Entry<String, List<LambdaQuery.Condition>> entry : filterMap.entrySet()) {
                loadFlag.getFilters().merge(entry.getKey(), entry.getValue(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return loadFlag;
    }

    public static <T extends BaseLoadFlag> T addOrders(T loadFlag, LambdaOrder<T> order) {
        if (null != order) {
            return addOrders(loadFlag, order.getOrderItems());
        }
        return loadFlag;
    }

    public static <T extends BaseLoadFlag, E> T addOrder(T loadFlag, MPFieldLambda.SSFunction<E, Serializable> field, Order order) {
        LambdaOrderItem orderItem = LambdaOrder.orderBy(field, order);
        loadFlag.getOrders().merge(orderItem.getEntity(), ListUtil.toList(orderItem), (x, y) -> {
            x.addAll(y);
            return x;
        });
        return (T) loadFlag;
    }

    public static <T extends BaseLoadFlag> T addOrders(T loadFlag, List<LambdaOrderItem> orders) {
        if (CollUtil.isNotEmpty(orders)) {
            for (Map.Entry<String, List<LambdaOrderItem>> item :
                    orders.stream().collect(Collectors.groupingBy(LambdaOrderItem::getEntity)).entrySet()) {
                loadFlag.getOrders().merge(item.getKey(), item.getValue(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return loadFlag;
    }

}
