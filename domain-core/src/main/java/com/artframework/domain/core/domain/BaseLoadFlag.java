package com.artframework.domain.core.domain;

import com.artframework.domain.core.constants.Op;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLoadFlag {
    private List<Filter> filters = new ArrayList<>();

    public void setFilters(Filter... filters) {
        List<Filter> filterList = new ArrayList<>(filters.length);
        Collections.addAll(filterList, filters);
        this.filters = filterList;
    }
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    @Data
    public static class Filter extends DOFilter {
        private String entity;
    }

    @Data
    public static class DOFilter{
        private String field;
        private String op = Op.EQ.getCode();
        private Object value;
    }
}
