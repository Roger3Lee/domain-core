package com.artframework.domain.core.lambda.query;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 自定义 LambdaQuery，支持 AND/OR 嵌套查询
 *
 * @param <T> 实体类型
 */
public class LambdaQuery<T> extends LambdaOrder<T> {
    @Getter
    private final Class<T> entityClass;
    // 根逻辑组（默认 AND 连接）
    private final ConditionGroup rootGroup = new ConditionGroup(LogicalOperator.AND);
    // 当前处理的逻辑组（用于嵌套）
    @Getter
    private ConditionGroup condition = rootGroup;

    protected LambdaQuery(Class<T> entityClass) {
        super(entityClass);
        this.entityClass = entityClass;
    }

    public static <T> LambdaQuery<T> of(Class<T> entityClass) {
        return new LambdaQuery<T>(entityClass);
    }

    public Boolean hasFilter() {
        return CollUtil.isNotEmpty(condition.condition);
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
        condition.addChild(newGroup);

        // 保存当前组上下文，进入新组
        ConditionGroup previousGroup = condition;
        condition = newGroup;

        // 执行用户定义的子条件
        consumer.accept(this);

        // 恢复上下文
        condition = previousGroup;
        return this;
    }

    protected LambdaQuery<T> addCondition(SFunction<T, Serializable> column, Op op, Object value) {
        // 创建新逻辑组
        Condition newGroup = new Condition(column, op, value);
        condition.addChild(newGroup);
        return this;
    }

    public ConditionGroup getFilter() {
        return condition;
    }


    // 逻辑组定义（包含子条件）
    @Data
    @NoArgsConstructor
    public static class ConditionGroup {
        private LogicalOperator op = LogicalOperator.AND;
        private List<Object> condition = new ArrayList<>();

        ConditionGroup(LogicalOperator operator) {
            this.op = operator;
        }

        public void addChild(Object child) {
            condition.add(child);
        }

        /**
         * @param condition
         */
        public void setCondition(List<Object> condition) {
            if (CollUtil.isNotEmpty(condition)) {
                for (int i = 0; i < condition.size(); i++) {
                    Object item = condition.get(i);
                    if (item instanceof Map) {
                        if (((Map<?, ?>) item).containsKey("condition")) {
                            item = BeanUtil.mapToBean((Map<?, ?>) item, ConditionGroup.class, true, CopyOptions.create());
                        } else if (((Map<?, ?>) item).containsKey("field")) {
                            item = BeanUtil.mapToBean((Map<?, ?>) item, Condition.class, true, CopyOptions.create());
                        }
                        condition.set(i, item);
                    }
                }
            }
            this.condition = condition;
        }
    }

    // 条件定义（字段 + 操作符 + 值）
    @Data
    @NoArgsConstructor
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
        private Op op = Op.EQ;
        @ApiModelProperty("值")
        private Object value;

        Condition(SFunction<?, Serializable> column, Op op, Object value) {
            LambdaCache.LambdaInfo lambdaInfo = LambdaCache.info(column);
            this.entity = lambdaInfo.getClazzName();
            this.field = lambdaInfo.getFieldName();
            this.columnFunction = column;
            this.op = op;
            this.value = value;
        }

        Condition(String entity, String field, Op op, Object value) {
            this.entity = entity;
            this.field = field;
            this.op = op;
            this.value = value;
        }
    }

    public LambdaQuery<T> eq(SFunction<T, Serializable> column, Object value) {
        return this.addCondition(column, Op.EQ, value);
    }

    public LambdaQuery<T> ne(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.NE, val);
    }

    public LambdaQuery<T> gt(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.GT, val);
    }

    public LambdaQuery<T> ge(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.GE, val);
    }

    public LambdaQuery<T> lt(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.LT, val);
    }

    public LambdaQuery<T> le(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.LE, val);
    }

    public LambdaQuery<T> like(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.LIKE, val);
    }

    public LambdaQuery<T> notLike(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.NOT_LIKE, val);
    }

    public LambdaQuery<T> likeLeft(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.LIKE_LEFT, val);
    }

    public LambdaQuery<T> likeRight(SFunction<T, Serializable> column, Object val) {
        return this.addCondition(column, Op.LIKE_RIGHT, val);
    }

    public LambdaQuery<T> in(SFunction<T, Serializable> column, Object val) {
        return this.addCondition( column, Op.IN, val);
    }
    public LambdaQuery<T> notIn(SFunction<T, Serializable> column, Object val) {
        return this.addCondition( column, Op.NOT_IN, val);
    }
    public LambdaQuery<T> isNull(SFunction<T, Serializable> column) {
        return this.addCondition( column, Op.ISNULL, null);
    }
    public LambdaQuery<T> notNull(SFunction<T, Serializable> column) {
        return this.addCondition( column, Op.NOTNULL, null);
    }
}