package com.artframework.domain.core.domain;

import com.artframework.domain.core.lambda.order.LambdaOrderItem;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import lombok.*;

import java.util.*;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/

public class BaseLoadFlag {
    /**
     * filter信息
     */
    @Getter
    private Map<String, Query> query = new HashMap<>();

    @Data
    public static class Query {
        /**
         * 过滤条件
         */
        private LambdaQuery.ConditionGroup filter;
        /**
         * 排序
         */
        private List<LambdaOrderItem> order;
    }
}
