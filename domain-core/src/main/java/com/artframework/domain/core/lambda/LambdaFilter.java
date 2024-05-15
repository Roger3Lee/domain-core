package com.artframework.domain.core.lambda;

import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.constants.Op;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;

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
}
