package com.artframework.domain.core.lambda.query;

import cn.hutool.core.collection.CollUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 自定义 LambdaQuery，支持 AND/OR 嵌套查询
 *
 * @param <T> 实体类型
 */
public class LambdaQuery<T> extends LambdaOrder<T> {
    private final Class<T> entityClass;
    // 根逻辑组（默认 AND 连接）
    private final ConditionGroup rootGroup = new ConditionGroup(LogicalOperator.AND);
    // 当前处理的逻辑组（用于嵌套）
    private ConditionGroup root = rootGroup;

    private LambdaQuery(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public static <T> LambdaQuery<T> of(Class<T> entityClass) {
        return new LambdaQuery<T>(entityClass);
    }

    public Boolean hasFilter() {
        return CollUtil.isNotEmpty(root.children);
    }

    public LambdaQuery<T> and(Consumer<LambdaQuery<T>> consumer) {
        return nestGroup(LogicalOperator.AND, consumer);
    }

    public LambdaQuery<T> or(Consumer<LambdaQuery<T>> consumer) {
        return nestGroup(LogicalOperator.OR, consumer);
    }

    /**
     * 嵌套逻辑组的核心方法
     */
    private LambdaQuery<T> nestGroup(LogicalOperator operator, Consumer<LambdaQuery<T>> consumer) {
        // 创建新逻辑组
        ConditionGroup newGroup = new ConditionGroup(operator);
        root.addChild(newGroup);

        // 保存当前组上下文，进入新组
        ConditionGroup previousGroup = root;
        root = newGroup;

        // 执行用户定义的子条件
        consumer.accept(this);

        // 恢复上下文
        root = previousGroup;
        return this;
    }

    public LambdaQuery<T> eq(SFunction<T, Serializable> valueWarp, Serializable value) {
        return addCondition(valueWarp, Op.EQ, value);
    }

    protected LambdaQuery<T> addCondition(SFunction<T, Serializable> column, Op op, Object value) {
        // 创建新逻辑组
        Condition newGroup = new Condition(column, op, value);
        root.addChild(newGroup);
        return this;
    }

    public ConditionGroup getFilter() {
        return root;
    }


    // 逻辑组定义（包含子条件）
    public static class ConditionGroup {
        public final LogicalOperator operator;
        public final List<Object> children = new ArrayList<>();

        ConditionGroup(LogicalOperator operator) {
            this.operator = operator;
        }

        void addChild(Object child) {
            children.add(child);
        }
    }

    // 条件定义（字段 + 操作符 + 值）
    @Data
    public static class Condition {
        @JsonIgnore
        @ApiModelProperty(hidden = true)
        private SFunction<?, ?> columnFunction;
        @ApiModelProperty(hidden = true)
        @JsonIgnore
        private String entity;
        @ApiModelProperty("字段")
        private String field;
        @ApiModelProperty("过滤条件规则， 默认是=")
        private Op operator = Op.EQ;
        @ApiModelProperty("值")
        private Object value;

        Condition(SFunction<?, Serializable> column, Op operator, Object value) {
            LambdaCache.LambdaInfo lambdaInfo = LambdaCache.info(column);
            this.entity = lambdaInfo.getClazzName();
            this.field = lambdaInfo.getFieldName();
            this.columnFunction = column;
            this.operator = operator;
            this.value = value;
        }

        Condition(String entity, String field, Op operator, Object value) {
            this.entity = entity;
            this.field = field;
            this.operator = operator;
            this.value = value;
        }
    }
}