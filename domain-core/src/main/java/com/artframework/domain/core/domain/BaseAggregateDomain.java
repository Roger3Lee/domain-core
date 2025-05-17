package com.artframework.domain.core.domain;

import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.service.BaseDomainService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import java.util.List;


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
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    protected transient S _service;

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> D loadRelated(Class<T> tClass) {
        return loadRelated(tClass,null);
    }

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param filter
     * @param <T>
     * @return
     */
    public <T> D loadRelated(Class<T> tClass, LambdaQuery<T> filter) {
        throw new UnsupportedOperationException();
    }
}
