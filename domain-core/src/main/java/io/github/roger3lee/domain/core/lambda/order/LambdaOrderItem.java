package io.github.roger3lee.domain.core.lambda.order;


import io.github.roger3lee.domain.core.MPFieldLambda;
import io.github.roger3lee.domain.core.constants.Order;
import io.github.roger3lee.domain.core.lambda.LambdaCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"field", "order"})
public class LambdaOrderItem {
    public <T> LambdaOrderItem(SFunction<T, Serializable> field, Order order) {
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
    @Setter
    @JsonIgnore
    private String entity;
    @Getter
    @Setter
    private String field;
    @Getter
    @Setter
    private Order order;
}