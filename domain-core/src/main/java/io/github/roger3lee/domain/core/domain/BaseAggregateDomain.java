package io.github.roger3lee.domain.core.domain;

import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import io.github.roger3lee.domain.core.service.BaseDomainService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

import java.util.function.Consumer;


/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/3
 **/
public abstract class BaseAggregateDomain<D extends BaseDomain, S extends BaseDomainService> extends BaseDomain {

    /**
     * 領域服務
     */
    @Setter
    @Schema(hidden = true)
    @JsonIgnore
    protected transient S _service;

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param <T>
     * @return 當前聚合根實例，支持鏈式調用
     */
    public <T> D loadRelated(Class<T> tClass) {
        LambdaQuery<T> query = LambdaQuery.of(tClass);
        loadRelated(tClass, query);
        return (D) this;
    }

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param consumer
     * @param <T>
     * @return 當前聚合根實例，支持鏈式調用
     */
    public <T> D loadRelated(Class<T> tClass, Consumer<LambdaQuery<T>> consumer) {
        LambdaQuery<T> query = LambdaQuery.of(tClass);
        if (null != consumer) {
            consumer.accept(query);
        }
        this.loadRelated(tClass, query);
        return (D) this;
    }

    public <T> D loadRelated(Class<T> tClass, LambdaQuery<T> consumer) {
        throw new UnsupportedOperationException();
    }
}
