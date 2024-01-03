package com.artframework.domain.core.domain;

import com.artframework.domain.core.constants.Op;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/
@Data
public class BaseLoadFlag {
    private List<Filter> filters = new ArrayList<>();

    @Data
    public static class Filter {
        private String entity;
        private String field;
        private String op = Op.EQ.getCode();
        private Object value;
    }
}
