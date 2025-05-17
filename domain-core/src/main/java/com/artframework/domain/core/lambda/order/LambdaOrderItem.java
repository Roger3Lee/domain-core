package com.artframework.domain.core.lambda.order;


import com.artframework.domain.core.MPFieldLambda;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.lambda.LambdaCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class LambdaOrderItem {
    public <T> LambdaOrderItem(MPFieldLambda.SSFunction<T, Serializable> field, Order order) {
        LambdaCache.LambdaInfo<T> lambdaInfo = LambdaCache.info(field);
        this.entity = lambdaInfo.getClazzName();
        this.field = lambdaInfo.getFieldName();
        this.order = order;
    }

    public <T> LambdaOrderItem(Class<T> clazz, String field, Order order) {
        this.entity = clazz.getCanonicalName();
        this.field = field;
        this.order = order;
    }

    @Getter
    @JsonIgnore
    private String entity;
    @Getter
    private String field;
    @Getter
    private Order order;
}