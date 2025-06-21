package com.artframework.domain.core.constants;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/
public enum SaveState {
    INSERT("INSERT"),
    UPDATE("UPDATE")
    ;

    private final String code;

    SaveState(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
