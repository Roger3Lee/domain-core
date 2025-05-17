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
    private Map<String, LambdaQuery.ConditionGroup> filters = new HashMap<>();

    /**
     * 排序
     */
    @Getter
    private Map<String, List<LambdaOrderItem>> orders = new HashMap<>();

}
