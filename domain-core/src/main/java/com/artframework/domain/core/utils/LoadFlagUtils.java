package com.artframework.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.domain.BaseLoadFlag;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LoadFlagUtils {
    public static <T extends BaseLoadFlag> T addFilters(T loadFlag, LambdaQuery.ConditionGroup filter ,String entityName) {
        if (ObjectUtil.isNotEmpty(filter)) {
                loadFlag.getFilters().merge(entityName, filter, (x, y) -> {
                    x.addChild(y);
                    return x;
                });
        }
        return loadFlag;
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

    public static  <T extends BaseLoadFlag> T addOrders(T loadFlag, Map<String, List<LambdaOrderItem>> orders) {
        if (CollUtil.isNotEmpty(orders)) {
            for (Map.Entry<String, List<LambdaOrderItem>> item : orders.entrySet()) {
                loadFlag.getOrders().merge(item.getKey(), item.getValue(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return loadFlag;
    }
}
