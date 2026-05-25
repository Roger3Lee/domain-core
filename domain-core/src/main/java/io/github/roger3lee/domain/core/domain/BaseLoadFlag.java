package io.github.roger3lee.domain.core.domain;

import io.github.roger3lee.domain.core.lambda.order.LambdaOrderItem;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.*;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/

@Data
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
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<LambdaOrderItem> order;

        /**
         * 字段选择（白名单）
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> select;

        /**
         * 字段排除（黑名单）
         */
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<String> exclude;
    }
}
