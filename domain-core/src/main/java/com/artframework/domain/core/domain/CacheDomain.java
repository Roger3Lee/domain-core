package com.artframework.domain.core.domain;

import lombok.Getter;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/1/25
 **/
public class CacheDomain<T> {
    public CacheDomain(T value) {
        this.value = value;
    }

    @Getter
    private final T value;
}
