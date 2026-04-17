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

    // 下一个条件的逻辑运算符（用于链式 OR）
    private LogicalOperator nextConditionOperator = null;

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
     * 链式 OR - 标记下一个条件使用 OR 连接
     *
     * 用法示例：
     * <pre>
     * query.eq(field1, value1)
     *      .or().eq(field2, value2)
     *      .or().eq(field3, value3);
     *
     * 生成：(field1 = ? OR field2 = ? OR field3 = ?)
     * </pre>
     *
     * @return this
     */
    public LambdaQuery<T> or() {
        this.nextConditionOperator = LogicalOperator.OR;
        return this;
    }

    /**
     * AND 嵌套条件组（保留用于复杂逻辑）
     *
     * 用法示例：
     * <pre>
     * query.and(q -> q.eq(field1, value1).eq(field2, value2))
     *
     * 生成：(field1 = ? AND field2 = ?)
     * </pre>
     */
    public LambdaQuery<T> and(Consumer<LambdaQuery<T>> consumer) {
        return nestGroup(LogicalOperator.AND, consumer);
    }

    /**
     * OR 一个 AND 组 - 将一组 AND 条件用 OR 连接到前面的条件
     *
     * 用法示例：
     * <pre>
     * query.eq(field1, value1)
     *      .orGroup(q -> q.eq(field2, value2).eq(field3, value3));
     *
     * 生成：field1 = ? OR (field2 = ? AND field3 = ?)
     * </pre>
     *
     * @param consumer AND 条件组的定义
     * @return this
     */
    public LambdaQuery<T> or(Consumer<LambdaQuery<T>> consumer) {
        List<Object> conditions = currentGroup.getCondition();

        if (CollUtil.isEmpty(conditions)) {
            // 当前组为空，直接创建 AND 组
            return nestGroup(LogicalOperator.AND, consumer);
        }

        // 取出最后一个元素
        Object lastElement = conditions.get(conditions.size() - 1);

        // 情况1：最后一个元素已经是 OR group
        if (lastElement instanceof ConditionGroup) {
            ConditionGroup lastGroup = (ConditionGroup) lastElement;
            if (LogicalOperator.OR.equals(lastGroup.getLogic())) {
                // 创建 AND 组并添加到现有 OR group
                ConditionGroup andGroup = new ConditionGroup(LogicalOperator.AND);

                // 保存上下文，进入 AND 组
                ConditionGroup previousGroup = currentGroup;
                currentGroup = andGroup;
                consumer.accept(this);
                currentGroup = previousGroup;

                // 将 AND 组添加到 OR group
                lastGroup.addChild(andGroup);
                return this;
            }
        }

        // 情况2：最后一个元素是普通条件，创建新的 OR group
        conditions.remove(conditions.size() - 1);

        // 创建 OR group
        ConditionGroup orGroup = new ConditionGroup(LogicalOperator.OR);
        orGroup.addChild(lastElement);

        // 创建 AND 组
        ConditionGroup andGroup = new ConditionGroup(LogicalOperator.AND);

        // 保存上下文，进入 AND 组
        ConditionGroup previousGroup = currentGroup;
        currentGroup = andGroup;
        consumer.accept(this);
        currentGroup = previousGroup;

        // 将 AND 组添加到 OR group
        orGroup.addChild(andGroup);

        // 将 OR group 添加到 currentGroup
        currentGroup.addChild(orGroup);

        return this;
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
        // 跳过 null 值（除非是 IS NULL / IS NOT NULL）
        if (value == null && op != Op.ISNULL && op != Op.NOTNULL) {
            return this;
        }

        Condition newCondition = new Condition(column, op, value);

        // 处理链式 OR
        if (nextConditionOperator == LogicalOperator.OR) {
            handleChainedOr(newCondition);
            nextConditionOperator = null;  // 重置标记
        } else {
            // 默认 AND 连接，直接添加
            currentGroup.addChild(newCondition);
        }

        return this;
    }

    /**
     * 处理链式 OR 逻辑
     *
     * @param newCondition 新条件
     */
    private void handleChainedOr(Condition newCondition) {
        List<Object> conditions = currentGroup.getCondition();

        if (CollUtil.isEmpty(conditions)) {
            // 当前组为空，直接添加（忽略 OR）
            currentGroup.addChild(newCondition);
            return;
        }

        Object lastElement = conditions.get(conditions.size() - 1);

        // 情况1：最后一个元素已经是 OR group，继续追加到该 group
        if (lastElement instanceof ConditionGroup) {
            ConditionGroup lastGroup = (ConditionGroup) lastElement;
            if (LogicalOperator.OR.equals(lastGroup.getLogic())) {
                lastGroup.addChild(newCondition);
                return;
            }
        }

        // 情况2：最后一个元素是普通条件，创建新的 OR group
        // 从 currentGroup 移除最后一个元素
        conditions.remove(conditions.size() - 1);

        // 创建 OR group，包含上一个条件和新条件
        ConditionGroup orGroup = new ConditionGroup(LogicalOperator.OR);
        orGroup.addChild(lastElement);
        orGroup.addChild(newCondition);

        // 将 OR group 添加到 currentGroup
        currentGroup.addChild(orGroup);
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