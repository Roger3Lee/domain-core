package com.artframework.domain.core.domain;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/

public class BaseLoadFlag {

    @Getter
    @Setter
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Boolean ignoreDomainFilter = false;
    /**
     * filter信息
     */
    @Getter
    private Map<String, List<Filter>> filters = new HashMap<>();

    /**
     * 排序
     */
    @Getter
    private Map<String, List<LambdaOrder.LambdaOrderItem>> orders = new HashMap<>();


    public <T extends BaseLoadFlag> T setFilters(List<Filter> filters) {
        if(CollectionUtil.isEmpty(filters)){
            return (T) this;
        }

        this.filters = filters.stream().collect(Collectors.groupingBy(Filter::getEntity));
        return (T) this;
    }
    public  <T extends BaseLoadFlag> T addFilters(List<Filter> filters){
        if (CollectionUtil.isNotEmpty(filters)) {
            Map<String, List<Filter>> filterMap = filters.stream().collect(Collectors.groupingBy(Filter::getEntity));
            for (Map.Entry<String, List<Filter>> entry : filterMap.entrySet()) {
                this.filters.merge(entry.getKey(), entry.getValue(), (x, y) -> {
                    x.addAll(y);
                    return x;
                });
            }
        }
        return (T) this;
    }
    public <T extends BaseLoadFlag, F> T setOrder(LambdaOrder<F> orders) {
        if (null != orders) {
            return addOrders(orders.getOrderItems());
        }
        return (T) this;
    }

    public <T extends BaseLoadFlag, E> T addOrder(SFunction<E, Serializable> field, Order order) {
        LambdaOrder.LambdaOrderItem orderItem = LambdaOrder.buildItem(field, order);
        this.orders.merge(orderItem.getEntity(), ListUtil.toList(orderItem), (x, y) -> {
            x.addAll(y);
            return x;
        });
        return (T) this;
    }

    public <T extends BaseLoadFlag, E> T addOrders(Map<String, List<LambdaOrder.LambdaOrderItem>> orders) {
        for (Map.Entry<String, List<LambdaOrder.LambdaOrderItem>> item:orders.entrySet()) {
            this.orders.merge(item.getKey(), item.getValue(),(x, y) -> {
                x.addAll(y);
                return x;
            });
        }
        return (T) this;
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    public static class Filter extends DOFilter {
        @JsonIgnore
        private String entity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DOFilter{
        private String field;
        private String op = Op.EQ.getCode();
        private Object value;

        private List<DOFilter> orFilter;

        /**
         * 拷貝基礎信息， 補拷貝orFilter
         * @param filter
         * @return
         */
        public static DOFilter copy(DOFilter filter) {
            DOFilter doFilter = new DOFilter();
            doFilter.setField(filter.getField());
            doFilter.setOp(filter.getOp());
            doFilter.setValue(filter.getValue());
            return doFilter;
        }
    }
}
