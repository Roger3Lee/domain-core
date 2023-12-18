package com.artframework.domain.core.dto;

import cn.hutool.core.collection.ListUtil;
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
    private List<FilterDTO> filters = new ArrayList<>();

    @Data
    public static class FilterDTO {
        private String entity;
        private String field;
        private String op = Op.EQ.getCode();
        private Object value;
    }
}
