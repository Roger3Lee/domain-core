package com.artframework.domain.core.domain;

import cn.hutool.core.collection.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.artframework.domain.core.service.BaseDomainService;

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
    @JsonIgnore
    @Setter
    public S _service;

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> D loadRelated(Class<T> tClass) {
        return loadRelated(tClass, ListUtil.empty());
    }

    /**
     * 加載關聯數據
     *
     * @param tClass
     * @param filter
     * @param <T>
     * @return
     */
    public <T> D loadRelated(Class<T> tClass, LambdaFilter<T> filter) {
        return loadRelated(tClass, ListUtil.toList(filter));
    }

    public <T> D loadRelated(Class<T> tClass, List<LambdaFilter<T>> filters) {
        return loadRelated(tClass, filters, null);
    }


    public <T> D loadRelated(Class<T> tClass, LambdaFilter<T> filter, LambdaOrder<T> orders){
        return loadRelated(tClass, ListUtil.toList(filter), orders);
    }

    /**
     * 帶排序的加載關聯數據
     * @param tClass
     * @param filters
     * @param orders
     * @return
     * @param <T>
     */
    public abstract <T> D loadRelated(Class<T> tClass, List<LambdaFilter<T>> filters, LambdaOrder<T> orders);


}
