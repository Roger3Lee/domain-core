package com.artframework.domain.core.lambda;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Getter;
import com.artframework.domain.core.constants.Op;

import java.io.Serializable;

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
    private LambdaFilter<T> or;

    private LambdaFilter(SFunction<T, Serializable> field, Object value, Op op, LambdaFilter<T> or) {
        this.field = field;
        this.value = value;
        this.op = op;
        this.or = or;
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value, Op op, LambdaFilter<T> orFilter) {
        return new LambdaFilter<>(field, value, op, orFilter);
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value, Op op) {
        return new LambdaFilter<>(field, value, op, null);
    }

    public static <T> LambdaFilter<T> build(SFunction<T, Serializable> field, Object value) {
        return new LambdaFilter<>(field, value, Op.EQ, null);
    }
}
