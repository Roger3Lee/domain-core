package com.artframework.domain.core.constants;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/2/3
 **/
public enum Order {
    ASC("ASC"),
    DESC("DESC"),;

    private final String code;


    Order(String code) {
        this.code = code;
    }
    @JsonValue
    public String getCode() {
        return code;
    }

    public static Order getOrder(String order) {
        for (Order item : Order.values()) {
            if (item.getCode().equals(order)) {
                return item;
            }
        }
        return Order.ASC;
    }
}
