package io.github.roger3lee.domain.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import io.github.roger3lee.domain.core.constants.Op;
import io.github.roger3lee.domain.core.domain.BaseLoadFlag;
import io.github.roger3lee.domain.core.lambda.LambdaCache;
import io.github.roger3lee.domain.core.lambda.order.LambdaOrderItem;
import io.github.roger3lee.domain.core.lambda.query.LambdaQuery;
import io.github.roger3lee.domain.core.lambda.query.LogicalOperator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Lambda 查询工具类
 * 提供查询条件和排序的构建工具方法
 */
public class LambdaQueryUtils {

    /**
     * 合并查询条件和排序
     * 
     * @param lambdaQuery 目标查询对象
     * @param filter      过滤条件组
     * @param orderItems  排序项列表
     * @return 合并后的查询对象
     */
    private static <T> LambdaQuery<T> combine(LambdaQuery<T> lambdaQuery,
            LambdaQuery.ConditionGroup filter,
            List<LambdaOrderItem> orderItems) {
        if (ObjectUtil.isNotNull(filter)) {
            combineFilter(lambdaQuery, filter);
        }

        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(orderItem -> lambdaQuery.orderBy(orderItem.getField(), orderItem.getOrder()));
        }
        return lambdaQuery;
    }

    /**
     * 合并查询条件和排序（从BaseLoadFlag中提取）
     * <p>将 LoadFlag 中指定实体的 LambdaQuery 的过滤条件（AND 连接）和排序合并到目标查询。</p>
     * 
     * @param lambdaQuery 目标查询对象
     * @param loadFlag    LoadFlag对象
     * @param entityClass 实体类
     * @return 合并后的查询对象
     */
    public static <T> LambdaQuery<T> combine(LambdaQuery<T> lambdaQuery,
            BaseLoadFlag loadFlag,
            Class<T> entityClass) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isNull(entityClass)) {
            return lambdaQuery;
        }

        LambdaQuery<?> entityQuery = getEntityLambdaQuery(loadFlag, entityClass);
        if (ObjectUtil.isNull(entityQuery)) {
            return lambdaQuery;
        }

        return combine(lambdaQuery, entityQuery.getFilter(), entityQuery.getOrderItems());
    }

    /**
     * 合并过滤条件到查询对象
     * 确保外键条件和用户条件通过 AND 连接
     *
     * 预期行为：
     * - 外键条件：family_id = ?
     * - 用户条件：type = ? OR type = ?
     * - 最终结果：family_id = ? AND (type = ? OR type = ?)
     */
    private static <T> void combineFilter(LambdaQuery<T> lambdaQuery, LambdaQuery.ConditionGroup filter) {
        if (filter == null || CollUtil.isEmpty(filter.getCondition())) {
            return;
        }

        LambdaQuery.ConditionGroup rootFilter = lambdaQuery.getFilter();

        // 展开用户 filter 的逻辑：
        // 如果用户 filter 是 AND 组且包含的元素可以直接展开，就展开它们
        // 注意：getLogic() 对 AND 返回 null（JSON 精简），null 也视为 AND
        if (filter.getLogic() == null || LogicalOperator.AND.equals(filter.getLogic())) {
            // 遍历用户 filter 的所有子元素，直接添加到 rootFilter
            for (Object child : filter.getCondition()) {
                rootFilter.addChild(child);
            }
        } else {
            // 用户 filter 是 OR 组，作为整体添加
            rootFilter.addChild(filter);
        }
    }

    /**
     * 从BaseLoadFlag中获取指定实体类的排序条件
     */
    private static <T> List<LambdaOrderItem> getEntityOrders(BaseLoadFlag loadFlag, Class<T> entityClass) {
        return getEntityOrders(loadFlag, getEntityName(entityClass));
    }

    /**
     * 从BaseLoadFlag中获取指定实体名称的排序条件
     */
    private static List<LambdaOrderItem> getEntityOrders(BaseLoadFlag loadFlag, String entityName) {
        LambdaQuery<?> query = getEntityLambdaQuery(loadFlag, entityName);
        return query != null ? query.getOrderItems() : null;
    }

    /**
     * 构建排序条件到 MyBatis Plus 查询包装器
     * 
     * @param wrapper 查询包装器
     * @param order   排序项
     * @param doClass DO 类型
     */
    public static <DO> void buildOrderWrapper(LambdaQueryWrapper<DO> wrapper,
            LambdaOrderItem order,
            Class<DO> doClass) {
        if (wrapper == null || order == null || doClass == null) {
            return;
        }

        switch (order.getOrder()) {
            case DESC:
                wrapper.orderByDesc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
            case ASC:
            default:
                wrapper.orderByAsc(LambdaCache.DOLambda(doClass, order.getField()));
                break;
        }
    }

    /**
     * 构建字段选择到 MyBatis Plus 查询包装器
     * <p>支持白名单（select）和黑名单（exclude）两种模式：</p>
     * <ul>
     *   <li>select 不为空时，仅查询指定字段</li>
     *   <li>exclude 不为空时（且 select 为空），查询所有字段但排除 exclude 中的字段</li>
     * </ul>
     *
     * @param wrapper     查询包装器
     * @param lambdaQuery 查询对象
     * @param doClass     DO 类型
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <DO> void buildSelectWrapper(LambdaQueryWrapper<DO> wrapper,
            LambdaQuery<?> lambdaQuery,
            Class<DO> doClass) {
        if (wrapper == null || lambdaQuery == null || doClass == null) {
            return;
        }

        List<String> select = lambdaQuery.getSelect();
        List<String> exclude = lambdaQuery.getExclude();

        if (CollUtil.isNotEmpty(select)) {
            // 白名单模式：只选择指定字段
            List<SFunction<DO, Serializable>> lambdas = new ArrayList<>();
            for (String f : select) {
                lambdas.add(LambdaCache.DOLambda(doClass, f));
            }
            wrapper.select(lambdas.toArray(new SFunction[0]));
        } else if (CollUtil.isNotEmpty(exclude)) {
            // 黑名单模式：选择所有字段，排除指定字段
            List<String> allFields = getAllFieldNames(doClass);
            List<SFunction<DO, Serializable>> lambdas = new ArrayList<>();
            for (String f : allFields) {
                if (!exclude.contains(f)) {
                    lambdas.add(LambdaCache.DOLambda(doClass, f));
                }
            }
            // 仅当确实排除了某些字段时才设置 select
            if (CollUtil.isNotEmpty(lambdas) && lambdas.size() < allFields.size()) {
                wrapper.select(lambdas.toArray(new SFunction[0]));
            }
        }
    }

    /**
     * 提取查询对象的排序条件
     */
    public static <T> List<LambdaOrderItem> toOrders(LambdaQuery<T> query) {
        if (query == null) {
            return ListUtil.empty();
        }
        return query.getOrderItems();
    }

    /**
     * 获取实体类简单名称
     */
    public static <D> String getEntityName(Class<D> entityClass) {
        return entityClass != null ? entityClass.getSimpleName() : null;
    }

    /**
     * 从BaseLoadFlag中获取指定实体类的过滤条件
     */
    public static <T> LambdaQuery.ConditionGroup getEntityFilters(BaseLoadFlag loadFlag, Class<T> entityClass) {
        return getEntityFilters(loadFlag, getEntityName(entityClass));
    }

    /**
     * 从BaseLoadFlag中获取指定实体名称的过滤条件
     */
    public static LambdaQuery.ConditionGroup getEntityFilters(BaseLoadFlag loadFlag, String entityName) {
        LambdaQuery<?> query = getEntityLambdaQuery(loadFlag, entityName);
        return query != null ? query.getFilter() : null;
    }

    /**
     * 从BaseLoadFlag中获取指定实体类的 LambdaQuery
     */
    public static <T> LambdaQuery<?> getEntityLambdaQuery(BaseLoadFlag loadFlag, Class<T> entityClass) {
        return getEntityLambdaQuery(loadFlag, getEntityName(entityClass));
    }

    /**
     * 从BaseLoadFlag中获取指定实体名称的 LambdaQuery
     */
    public static LambdaQuery<?> getEntityLambdaQuery(BaseLoadFlag loadFlag, String entityName) {
        if (ObjectUtil.isNull(loadFlag) || ObjectUtil.isEmpty(entityName)) {
            return null;
        }
        return loadFlag.getQuery().get(entityName);
    }


    private static Boolean isAndLogic(LambdaQuery.ConditionGroup rootGroup, List<Object> conditionList) {
        if (!LogicalOperator.AND.equals(rootGroup.getLogic())) {
            return false;
        }

        // OR子组的组合
        boolean isAndGroup = true;
        for (Object child : rootGroup.getCondition()) {
            if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup group = (LambdaQuery.ConditionGroup) child;
                if (LogicalOperator.OR.equals(group.getLogic())) {
                    isAndGroup = false;
                } else {
                    if (!isAndLogic(group, conditionList)) {
                        conditionList.add(group);
                    }
                }
            } else {
                conditionList.add(child);
            }
        }

        //都是and条件
        return isAndGroup;
    }


    /**
     * 构建过滤条件到 MyBatis Plus 查询包装器
     *
     * @param wrapper 查询包装器
     * @param filter  过滤条件组
     * @param doClass DO 类型
     */
    public static <DO> void buildFilterWrapper(LambdaQueryWrapper<DO> wrapper,
            LambdaQuery.ConditionGroup filter,
            Class<DO> doClass) {
        if (wrapper == null || filter == null || doClass == null || CollUtil.isEmpty(filter.getCondition())) {
            return;
        }

        LogicalOperator groupLogic = filter.getLogic() != null ? filter.getLogic() : LogicalOperator.AND;
        boolean isOrGroup = LogicalOperator.OR.equals(groupLogic);

        for (int i = 0; i < filter.getCondition().size(); i++) {
            Object child = filter.getCondition().get(i);
            boolean isFirst = (i == 0);

            if (child instanceof LambdaQuery.Condition) {
                // 简单条件
                if (isOrGroup && !isFirst) {
                    wrapper.or();
                }
                applyCondition(wrapper, (LambdaQuery.Condition) child, doClass);

            } else if (child instanceof LambdaQuery.ConditionGroup) {
                LambdaQuery.ConditionGroup childGroup = (LambdaQuery.ConditionGroup) child;
                LogicalOperator childLogic = childGroup.getLogic() != null ? childGroup.getLogic() : LogicalOperator.AND;

                // 判断子组是否需要括号包裹：
                // 1. 子组是 OR 组 - 总是需要括号
                // 2. 子组是 AND 组但父组是 OR - 也需要括号以保证运算优先级
                boolean childNeedsWrapper = LogicalOperator.OR.equals(childLogic) ||
                                          (LogicalOperator.AND.equals(childLogic) && isOrGroup);

                if (isOrGroup && !isFirst) {
                    // 父组是 OR，需要 OR 连接子组
                    if (childNeedsWrapper) {
                        // 子组需要括号
                        wrapper.or(w -> w.and(w2 -> buildFilterWrapper(w2, childGroup, doClass)));
                    } else {
                        wrapper.or(w -> buildFilterWrapper(w, childGroup, doClass));
                    }
                } else {
                    // 父组是 AND（或第一个元素）
                    if (childNeedsWrapper) {
                        // 子组需要括号
                        wrapper.and(w -> buildFilterWrapper(w, childGroup, doClass));
                    } else {
                        // AND 组在 AND 上下文中可以展开，不需要括号
                        buildFilterWrapper(wrapper, childGroup, doClass);
                    }
                }
            }
        }
    }

    /**
     * 应用单个条件到查询包装器
     */
    private static <F> void applyCondition(LambdaQueryWrapper<F> wrapper,
            LambdaQuery.Condition condition,
            Class<F> doClass) {
        if (wrapper == null || condition == null || doClass == null) {
            return;
        }

        Op op = condition.getOp();
        Object value = condition.getValue();
        String field = condition.getField();

        if (field == null) {
            return;
        }

        switch (op) {
            case IN:
                handleInCondition(wrapper, field, value, doClass, false);
                break;
            case NOT_IN:
                handleInCondition(wrapper, field, value, doClass, true);
                break;
            case LIKE:
                wrapper.like(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LIKE_LEFT:
                wrapper.likeLeft(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LIKE_RIGHT:
                wrapper.likeRight(LambdaCache.DOLambda(doClass, field), value);
                break;
            case NOT_LIKE:
                wrapper.notLike(LambdaCache.DOLambda(doClass, field), value);
                break;
            case NOT_LIKE_LEFT:
                wrapper.notLikeLeft(LambdaCache.DOLambda(doClass, field), value);
                break;
            case NOT_LIKE_RIGHT:
                wrapper.notLikeRight(LambdaCache.DOLambda(doClass, field), value);
                break;
            case BETWEEN: {
                Object[] pairVals = extractPairValues(value);
                if (pairVals != null) {
                    wrapper.between(LambdaCache.DOLambda(doClass, field), pairVals[0], pairVals[1]);
                }
                break;
            }
            case NOT_BETWEEN: {
                Object[] pairVals = extractPairValues(value);
                if (pairVals != null) {
                    wrapper.notBetween(LambdaCache.DOLambda(doClass, field), pairVals[0], pairVals[1]);
                }
                break;
            }
            case NE:
                wrapper.ne(LambdaCache.DOLambda(doClass, field), value);
                break;
            case GT:
                wrapper.gt(LambdaCache.DOLambda(doClass, field), value);
                break;
            case GE:
                wrapper.ge(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LT:
                wrapper.lt(LambdaCache.DOLambda(doClass, field), value);
                break;
            case LE:
                wrapper.le(LambdaCache.DOLambda(doClass, field), value);
                break;
            case ISNULL:
                wrapper.isNull(LambdaCache.DOLambda(doClass, field));
                break;
            case NOTNULL:
                wrapper.isNotNull(LambdaCache.DOLambda(doClass, field));
                break;
            case CUSTOM:
                handleCustomCondition(wrapper, field, condition.getSqlTemplate(), value, doClass);
                break;
            case EQ:
            default:
                // 等于条件，特殊处理 null 值
                if (value == null) {
                    wrapper.isNull(LambdaCache.DOLambda(doClass, field));
                } else {
                    wrapper.eq(LambdaCache.DOLambda(doClass, field), value);
                }
                break;
        }
    }

    /**
     * 处理 IN 和 NOT IN 条件
     */
    private static <F> void handleInCondition(LambdaQueryWrapper<F> wrapper,
            String field,
            Object value,
            Class<F> doClass,
            boolean isNotIn) {
        if (value == null) {
            return;
        }

        List<?> valueList;
        if (value instanceof Iterable) {
            valueList = ListUtil.toList((Iterable<?>) value);
        } else {
            valueList = ListUtil.toList(value);
        }

        if (CollUtil.isNotEmpty(valueList)) {
            if (isNotIn) {
                wrapper.notIn(LambdaCache.DOLambda(doClass, field), valueList);
            } else {
                wrapper.in(LambdaCache.DOLambda(doClass, field), valueList);
            }
        }
    }

    /**
     * 从值对象中提取两个值（用于 BETWEEN / NOT_BETWEEN）
     * <p>支持 Object[] 和 Iterable 两种格式，兼容编程构建和 JSON 反序列化场景。</p>
     *
     * @param value 值对象
     * @return 包含两个值的数组，或 null 如果无法提取
     */
    private static Object[] extractPairValues(Object value) {
        if (value instanceof Object[]) {
            Object[] arr = (Object[]) value;
            if (arr.length >= 2) {
                return new Object[]{arr[0], arr[1]};
            }
        } else if (value instanceof Iterable) {
            List<?> list = ListUtil.toList((Iterable<?>) value);
            if (list.size() >= 2) {
                return new Object[]{list.get(0), list.get(1)};
            }
        }
        return null;
    }

    /**
     * 处理自定义操作符条件
     * <p>将 sqlTemplate 中的 {0}, {1} 替换为 MyBatis-Plus 参数占位符，
     * 列名通过 TableInfo 自动解析并拼接到模板前面。</p>
     */
    private static <F> void handleCustomCondition(LambdaQueryWrapper<F> wrapper,
            String field,
            String sqlTemplate,
            Object value,
            Class<F> doClass) {
        if (sqlTemplate == null || sqlTemplate.isEmpty()) {
            return;
        }

        // 解析列名
        String columnName = resolveColumnName(doClass, field);

        // 组装完整 SQL: column_name + sqlTemplate
        String fullSql = columnName + " " + sqlTemplate;

        // 将 {0}, {1} 等替换为 MyBatis-Plus 的 {0}, {1} 参数占位符
        // MyBatis-Plus wrapper.apply 使用 {0}, {1} 作为占位符，与我们的模板一致
        if (value instanceof Object[]) {
            wrapper.apply(fullSql, (Object[]) value);
        } else {
            wrapper.apply(fullSql, value);
        }
    }

    /**
     * 通过 TableInfo 解析 DO 类的字段对应的数据库列名
     */
    private static <F> String resolveColumnName(Class<F> doClass, String fieldName) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(doClass);
        if (tableInfo != null) {
            // 检查是否是主键字段
            if (fieldName.equals(tableInfo.getKeyProperty())) {
                return tableInfo.getKeyColumn();
            }
            // 检查普通字段
            for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (fieldName.equals(fieldInfo.getProperty())) {
                    return fieldInfo.getColumn();
                }
            }
        }
        // 回退：使用字段名作为列名
        return fieldName;
    }

    /**
     * 通过 TableInfo 获取 DO 类的所有字段属性名列表（含主键）
     *
     * @param doClass DO 类型
     * @return 字段属性名列表，如果 TableInfo 未初始化则返回空列表
     */
    private static <F> List<String> getAllFieldNames(Class<F> doClass) {
        List<String> fields = new ArrayList<>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(doClass);
        if (tableInfo != null) {
            // 主键字段
            if (tableInfo.getKeyProperty() != null) {
                fields.add(tableInfo.getKeyProperty());
            }
            // 普通字段
            for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                fields.add(fieldInfo.getProperty());
            }
        }
        return fields;
    }

    /**
     * 提取查询对象的过滤条件
     */
    public static <T> LambdaQuery.ConditionGroup toFilters(LambdaQuery<T> query) {
        if (query == null) {
            return null;
        }
        return query.getFilter();
    }
}
