package com.artframework.domain.core.uitls;

import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * 獲取當前實體的所有排序條件
 *
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/4
 **/
public class OrdersUtils {
    public static <T> List<LambdaOrder.LambdaOrderItem> getEntityOrders(Map<String, List<LambdaOrder.LambdaOrderItem>> orders, Class<T> tClass) {
        if (ObjectUtil.isNull(orders)) {
            return null;
        }
        return orders.get(tClass.getCanonicalName());
    }


    /**
     * 生成排序的
     *
     * @param wrapper
     * @param order
     * @param doClass
     * @param <DO>
     */

    public static <DO> void buildOrderWrapper(LambdaQueryWrapper<DO> wrapper, LambdaOrder.LambdaOrderItem order, Class<DO> doClass) {
        switch (order.getOrder()) {
            case DES:
                wrapper.orderByDesc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
            default:
                //EQ
                wrapper.orderByAsc(LambdaCache.DOLambda(doClass, order.getField()));
        }
    }
}
