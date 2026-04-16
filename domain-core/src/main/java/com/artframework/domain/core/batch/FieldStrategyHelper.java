package com.artframework.domain.core.batch;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字段策略工具类
 * 处理 MyBatis Plus 的 insertStrategy 和 updateStrategy
 *
 * @author framework
 * @version 1.0
 */
public class FieldStrategyHelper {

    /**
     * 获取插入操作的有效字段（考虑 insertStrategy）
     *
     * @param tableInfo 表信息
     * @return 有效字段列表
     */
    public static List<TableFieldInfo> getInsertFields(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())  // 排除逻辑删除字段
                .filter(field -> !isNeverStrategy(field.getInsertStrategy()))  // 排除 NEVER 策略
                .collect(Collectors.toList());
    }

    /**
     * 获取更新操作的有效字段（考虑 updateStrategy）
     *
     * @param tableInfo 表信息
     * @return 有效字段列表
     */
    public static List<TableFieldInfo> getUpdateFields(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())  // 排除逻辑删除字段
                .filter(field -> !isNeverStrategy(field.getUpdateStrategy()))  // 排除 NEVER 策略
                .collect(Collectors.toList());
    }

    /**
     * 判断是否为 NEVER 策略
     */
    private static boolean isNeverStrategy(FieldStrategy strategy) {
        return strategy == FieldStrategy.NEVER;
    }

    /**
     * 构建插入参数（考虑 insertStrategy）
     *
     * @param field 字段信息
     * @param itemName 集合中的项名称（如 "item"）
     * @return MyBatis 参数表达式
     */
    public static String buildInsertParameter(TableFieldInfo field, String itemName) {
        FieldStrategy strategy = getEffectiveStrategy(field.getInsertStrategy());
        String property = field.getProperty();
        String param = "#{" + itemName + "." + property + "}";

        switch (strategy) {
            case IGNORED:
                // 总是包含，不判断 NULL
                return param;

            case NOT_NULL:
                // 非 NULL 判断
                return buildNullCheckParameter(itemName, property, param);

            case NOT_EMPTY:
                // 非空判断（字符串/集合）
                return buildEmptyCheckParameter(itemName, property, param);

            case NEVER:
                throw new IllegalStateException("NEVER strategy should be filtered out in getInsertFields");

            default:
                // 默认使用 NOT_NULL
                return buildNullCheckParameter(itemName, property, param);
        }
    }

    /**
     * 构建更新 SET 子句（考虑 updateStrategy）
     * 适用于 PostgreSQL
     *
     * @param field 字段信息
     * @param tableName 表名
     * @return SQL SET 子句片段
     */
    public static String buildUpdateSetClausePostgreSQL(TableFieldInfo field, String tableName) {
        FieldStrategy strategy = getEffectiveStrategy(field.getUpdateStrategy());
        String column = field.getColumn();

        switch (strategy) {
            case IGNORED:
                // 直接更新，允许 NULL 覆盖
                return column + " = v." + column;

            case NOT_NULL:
                // 非 NULL 判断，使用 COALESCE
                return column + " = COALESCE(v." + column + ", " + tableName + "." + column + ")";

            case NOT_EMPTY:
                // 非空判断（字符串/集合）
                return column + " = CASE WHEN v." + column + " IS NOT NULL AND v." + column + " != '' " +
                       "THEN v." + column + " ELSE " + tableName + "." + column + " END";

            case NEVER:
                throw new IllegalStateException("NEVER strategy should be filtered out in getUpdateFields");

            default:
                // 默认使用 NOT_NULL
                return column + " = COALESCE(v." + column + ", " + tableName + "." + column + ")";
        }
    }

    /**
     * 构建更新 SET 子句（考虑 updateStrategy）
     * 适用于 MySQL
     *
     * @param field 字段信息
     * @param column 列名
     * @return SQL SET 子句片段
     */
    public static String buildUpdateSetClauseMySQL(TableFieldInfo field, String column) {
        FieldStrategy strategy = getEffectiveStrategy(field.getUpdateStrategy());

        switch (strategy) {
            case IGNORED:
                // 直接更新，允许 NULL
                return column + " = VALUES(" + column + ")";

            case NOT_NULL:
                // 非 NULL 判断，使用 IFNULL
                return column + " = IFNULL(VALUES(" + column + "), " + column + ")";

            case NOT_EMPTY:
                // 非空判断
                return column + " = IF(VALUES(" + column + ") IS NOT NULL AND VALUES(" + column + ") != '', " +
                       "VALUES(" + column + "), " + column + ")";

            case NEVER:
                throw new IllegalStateException("NEVER strategy should be filtered out in getUpdateFields");

            default:
                // 默认使用 NOT_NULL
                return column + " = IFNULL(VALUES(" + column + "), " + column + ")";
        }
    }

    /**
     * 构建更新 SET 子句（考虑 updateStrategy）
     * 适用于 Oracle/PolarDB
     *
     * @param field 字段信息
     * @param column 列名
     * @param sourceAlias 源表别名（如 "v"）
     * @param targetAlias 目标表别名（如 "t"）
     * @return SQL SET 子句片段
     */
    public static String buildUpdateSetClauseOracle(TableFieldInfo field, String column,
                                                    String sourceAlias, String targetAlias) {
        FieldStrategy strategy = getEffectiveStrategy(field.getUpdateStrategy());

        switch (strategy) {
            case IGNORED:
                // 直接更新，允许 NULL
                return column + " = " + sourceAlias + "." + column;

            case NOT_NULL:
                // 非 NULL 判断，使用 NVL
                return column + " = NVL(" + sourceAlias + "." + column + ", " +
                       targetAlias + "." + column + ")";

            case NOT_EMPTY:
                // 非空判断
                return column + " = CASE WHEN " + sourceAlias + "." + column +
                       " IS NOT NULL AND " + sourceAlias + "." + column + " != '' " +
                       "THEN " + sourceAlias + "." + column + " ELSE " +
                       targetAlias + "." + column + " END";

            case NEVER:
                throw new IllegalStateException("NEVER strategy should be filtered out in getUpdateFields");

            default:
                // 默认使用 NOT_NULL
                return column + " = NVL(" + sourceAlias + "." + column + ", " +
                       targetAlias + "." + column + ")";
        }
    }

    /**
     * 获取有效策略（处理 DEFAULT）
     *
     * @param strategy 原始策略
     * @return 有效策略（将 DEFAULT 转换为 NOT_NULL）
     */
    private static FieldStrategy getEffectiveStrategy(FieldStrategy strategy) {
        // DEFAULT 策略转换为 NOT_NULL（跟随全局默认配置）
        return strategy == FieldStrategy.DEFAULT ? FieldStrategy.NOT_NULL : strategy;
    }

    /**
     * 构建 NULL 检查参数
     *
     * @param itemName 项名称
     * @param property 属性名
     * @param param 参数表达式
     * @return MyBatis 动态 SQL
     */
    private static String buildNullCheckParameter(String itemName, String property, String param) {
        return "<choose>" +
               "<when test=\"" + itemName + "." + property + " != null\">" +
               param +
               "</when>" +
               "<otherwise>DEFAULT</otherwise>" +
               "</choose>";
    }

    /**
     * 构建空值检查参数（字符串）
     *
     * @param itemName 项名称
     * @param property 属性名
     * @param param 参数表达式
     * @return MyBatis 动态 SQL
     */
    private static String buildEmptyCheckParameter(String itemName, String property, String param) {
        return "<choose>" +
               "<when test=\"" + itemName + "." + property + " != null and " +
               itemName + "." + property + " != ''\">" +
               param +
               "</when>" +
               "<otherwise>DEFAULT</otherwise>" +
               "</choose>";
    }
}
