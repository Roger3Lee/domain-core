package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadFlagUtils {
    public static <T extends BaseLoadFlag> T addFilters(BaseLoadFlag loadFlag, List<LambdaFilter.Filter> filters) {
        if (CollectionUtil.isNotEmpty(filters)) {
            Map<String, List<LambdaFilter.Filter>> filterMap = filters.stream().collect(Collectors.groupingBy(LambdaFilter.Filter::getEntity));
            for (Map.Entry<String, List<LambdaFilter.Filter>> entry : filterMap.entrySet()) {
                loadFlag.getFilters().merge(entry.getKey(), entry.getValue(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return (T) loadFlag;
    }

    public static <T extends BaseLoadFlag, F> T addOrders(BaseLoadFlag loadFlag, LambdaOrder<F> orders) {
        if (null != orders) {
            return addOrders(loadFlag, orders.getOrderItems());
        }
        return (T) loadFlag;
    }

    public static <T extends BaseLoadFlag, E> T addOrder(BaseLoadFlag loadFlag, SFunction<E, Serializable> field, Order order) {
        LambdaOrder.LambdaOrderItem orderItem = LambdaOrder.buildItem(field, order);
        loadFlag.getOrders().merge(orderItem.getEntity(), ListUtil.toList(orderItem), (x, y) -> {
            x.addAll(y);
            return x;
        });
        return (T) loadFlag;
    }

    public static <T extends BaseLoadFlag, E> T addOrders(BaseLoadFlag loadFlag, Map<String, List<LambdaOrder.LambdaOrderItem>> orders) {
        for (Map.Entry<String, List<LambdaOrder.LambdaOrderItem>> item : orders.entrySet()) {
            loadFlag.getOrders().merge(item.getKey(), item.getValue(), (x, y) -> {
                x.addAll(y);
                return x;
            });
        }
        return (T) loadFlag;
    }

}
