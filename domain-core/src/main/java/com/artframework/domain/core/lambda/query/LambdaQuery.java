package com.artframework.domain.core.lambda.query;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.core.constants.Op;
import com.artframework.domain.core.lambda.LambdaCache;
import com.artframework.domain.core.lambda.order.LambdaOrder;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
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
    private ConditionGroup currentGroup = rootGroup;

    protected LambdaQuery(Class<T> entityClass) {
        super(entityClass);
        this.entityClass = entityClass;
    }

    public static <T> LambdaQuery<T> of(Class<T> entityClass) {
        return new LambdaQuery<>(entityClass);
    }

    /**
     * 检查是否有过滤条件
     */
    public Boolean hasFilter() {
        return CollUtil.isNotEmpty(rootGroup.getCondition());
    }

    /**
     * 获取根过滤条件组
     */
    public ConditionGroup getFilter() {
        return rootGroup;
    }

    /**
     * AND 嵌套条件组
     */
    public LambdaQuery<T> and(Consumer<LambdaQuery<T>> consumer) {
        return nestGroup(LogicalOperator.AND, consumer);
    }

    /**
     * OR 嵌套条件组
     */
    public LambdaQuery<T> or(Consumer<LambdaQuery<T>> consumer) {
        return nestGroup(LogicalOperator.OR, consumer);
    }

    /**
     * 嵌套逻辑组的核心方法
     */
    private LambdaQuery<T> nestGroup(LogicalOperator operator, Consumer<LambdaQuery<T>> consumer) {
        // 创建新逻辑组
        ConditionGroup newGroup = new ConditionGroup(operator);
        currentGroup.addChild(newGroup);

        // 保存当前组上下文，进入新组
        ConditionGroup previousGroup = currentGroup;
        currentGroup = newGroup;

        // 执行用户定义的子条件
        consumer.accept(this);

        // 恢复上下文
        currentGroup = previousGroup;
        return this;
    }

    /**
     * 添加条件到当前组
     */
    protected LambdaQuery<T> addCondition(SFunction<T, Serializable> column, Op op, Object value) {
        // 直接添加条件到当前组，不创建不必要的ConditionGroup包装
        if (value != null || op == Op.ISNULL || op == Op.NOTNULL) {
            this.currentGroup.addChild(new Condition(column, op, value));
        }
        return this;
    }

    // ==================== 条件方法 ====================

    public LambdaQuery<T> eq(SFunction<T, Serializable> column, Object value) {
        return addCondition(column, Op.EQ, value);
    }

    public LambdaQuery<T> ne(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.NE, val);
    }

    public LambdaQuery<T> gt(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.GT, val);
    }

    public LambdaQuery<T> ge(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.GE, val);
    }

    public LambdaQuery<T> lt(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.LT, val);
    }

    public LambdaQuery<T> le(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.LE, val);
    }

    public LambdaQuery<T> like(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.LIKE, val);
    }

    public LambdaQuery<T> notLike(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.NOT_LIKE, val);
    }

    public LambdaQuery<T> likeLeft(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.LIKE_LEFT, val);
    }

    public LambdaQuery<T> likeRight(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.LIKE_RIGHT, val);
    }

    public LambdaQuery<T> in(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.IN, val);
    }

    public LambdaQuery<T> notIn(SFunction<T, Serializable> column, Object val) {
        return addCondition(column, Op.NOT_IN, val);
    }

    public LambdaQuery<T> isNull(SFunction<T, Serializable> column) {
        return addCondition(column, Op.ISNULL, null);
    }

    public LambdaQuery<T> notNull(SFunction<T, Serializable> column) {
        return addCondition(column, Op.NOTNULL, null);
    }

    // ==================== 内部类 ====================

    /**
     * 逻辑组定义（包含子条件）
     */
    @Data
    @NoArgsConstructor
    public static class ConditionGroup {
        @JsonProperty("logic")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public LogicalOperator getLogic() {
            if(ObjectUtil.equal(this.logic, LogicalOperator.AND)){
                return null;
            }
            return logic;
        }

        @JsonProperty("logic")
        private LogicalOperator logic = LogicalOperator.AND;

        @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "@type",
            defaultImpl = Object.class
        )
        @JsonSubTypes({
            @JsonSubTypes.Type(value = Condition.class, name = "condition"),
            @JsonSubTypes.Type(value = ConditionGroup.class, name = "conditionGroup")
        })
        private List<Object> condition = new ArrayList<>();

        public ConditionGroup(LogicalOperator operator) {
            this.logic = operator;
        }

        public void addChild(Object child) {
            if (child != null) {
                condition.add(child);
            }
        }

        /**
         * 设置条件列表，支持从Map反序列化
         */
        public void setCondition(List<Object> condition) {
            if (CollUtil.isEmpty(condition)) {
                this.condition = new ArrayList<>();
                return;
            }

            // 处理反序列化的Map对象
            List<Object> processedConditions = new ArrayList<>();
            for (Object item : condition) {
                if (item instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) item;
                    if (map.containsKey("condition")) {
                        // 转换为 ConditionGroup
                        item = BeanUtil.mapToBean(map, ConditionGroup.class, true, CopyOptions.create());
                    } else if (map.containsKey("field")) {
                        // 转换为 Condition
                        item = BeanUtil.mapToBean(map, Condition.class, true, CopyOptions.create());
                    }
                }
                processedConditions.add(item);
            }
            this.condition = processedConditions;
        }
    }

    /**
     * 条件定义（字段 + 操作符 + 值）
     */
    @Data
    @NoArgsConstructor
    public static class Condition {
        @JsonIgnore
        @ApiModelProperty(hidden = true)
        private SFunction<?, Serializable> columnFunction;

        @ApiModelProperty(hidden = true)
        @JsonIgnore
        private String entity;

        @ApiModelProperty("字段")
        private String field;

        @ApiModelProperty("过滤条件规则，默认是=")
        private Op op = Op.EQ;

        @ApiModelProperty("值")
        private Object value;

        @SuppressWarnings("rawtypes")
        public Condition(SFunction<?, Serializable> column, Op op, Object value) {
            LambdaCache.LambdaInfo lambdaInfo = LambdaCache.info(column);
            this.entity = lambdaInfo.getClazzName();
            this.field = lambdaInfo.getFieldName();
            this.columnFunction = column;
            this.op = op != null ? op : Op.EQ;
            this.value = value;
        }

        public Condition(String entity, String field, Op op, Object value) {
            this.entity = entity;
            this.field = field;
            this.op = op != null ? op : Op.EQ;
            this.value = value;
        }
    }
}