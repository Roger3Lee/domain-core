package com.artframework.domain.core.lambda;

import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.constants.Op;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/2
 **/

public class LambdaFilter<T> {
    @Getter
    private SFunction<T, Serializable> field;
    @Getter
    private Op op;
    @Getter
    private Object value;
    @Getter
    private List<LambdaFilter<T>> or;

    private LambdaFilter(SFunction<T, Serializable> field, Object value, Op op, List<LambdaFilter<T>>  or) {
        this.field = field;
        this.value = value;
        this.op = op;
        this.or = or;
    }
    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value, Op op, LambdaFilter<T> orFilter) {
        return new LambdaFilter<>(field, value, op, ListUtil.toList(orFilter));
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value, Op op, LambdaFilter<T>... orFilter) {
        return new LambdaFilter<>(field, value, op, null != orFilter ? ListUtil.toList(orFilter) : ListUtil.empty());
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value, Op op) {
        return new LambdaFilter<>(field, value, op, ListUtil.empty());
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value) {
        return new LambdaFilter<>(field, value, Op.EQ, ListUtil.empty());
    }

    @Data
    @EqualsAndHashCode(callSuper=false)
    @JsonPropertyOrder({"field","op","value","orFilter"})
    public static class Filter extends DOFilter {
        @ApiModelProperty(hidden=true)
        @JsonIgnore
        private String entity;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DOFilter{
        @ApiModelProperty("字段")
        private String field;
        @ApiModelProperty("过滤条件规则， 默认是=")
        private String op = Op.EQ.getCode();
        @ApiModelProperty("值")
        private Object value;
        @ApiModelProperty("OR 条件")
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
