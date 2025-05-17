package com.artframework.domain.core.domain;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.lambda.LambdaFilter;
import com.artframework.domain.core.lambda.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2023/12/18
 **/

public class BaseLoadFlag {
    /**
     * filter信息
     */
    @Getter
    private Map<String, List<LambdaFilter.Filter>> filters = new HashMap<>();

    /**
     * 排序
     */
    @Getter
    private Map<String, List<LambdaOrder.LambdaOrderItem>> orders = new HashMap<>();

}
