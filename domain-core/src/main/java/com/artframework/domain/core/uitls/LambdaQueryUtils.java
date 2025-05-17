package com.artframework.domain.core.uitls;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;

import java.util.List;

public class LambdaQueryUtils {

    public static <T> LambdaQuery<T> combine(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter, List<LambdaOrderItem> orderItems) {
        if (ObjectUtil.isNotEmpty(filter)) {
            combineFilter(lambdaQuery, filter);
        }

        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(x -> lambdaQuery.orderBy(x.getField(), x.getOrder()));
        }
        return lambdaQuery;
    }

    private static <T> void combineFilter(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter) {
        lambdaQuery.getCondition().addChild(filter);
    }
}
