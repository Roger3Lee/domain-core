package com.artframework.domain.postgresql.methods;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.core.batch.EnhancedBatchMethod;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 增强的 PostgreSQL 批量操作方法实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 */
public class EnhancedPostgreSqlBatchMethod extends EnhancedBatchMethod {

    public EnhancedPostgreSqlBatchMethod(BatchOperationType operationType) {
        super(getMethodName(operationType), operationType);
    }

    private static String getMethodName(BatchOperationType operationType) {
        switch (operationType) {
            case INSERT:
                return "batchInsert";
            case UPDATE:
                return "batchUpdate";
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + operationType);
        }
    }

    @Override
    protected String buildBatchInsertSql(TableInfo tableInfo) {
        String columns = "(" + buildColumnList(tableInfo) + ")";
        String values = "(" + buildParameterList(tableInfo, "item") + ")";

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("INSERT INTO ").append(tableInfo.getTableName()).append(" ").append(columns);
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append(values);
        sql.append("</foreach>");

        // PostgreSQL的RETURNING子句支持主键回填
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            sql.append(" RETURNING ").append(tableInfo.getKeyColumn());
        }

        sql.append("</script>");
        return sql.toString();
    }

    @Override
    protected String buildBatchUpdateSql(TableInfo tableInfo) {
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();
        List<TableFieldInfo> fields = getValidFields(tableInfo);

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("UPDATE ").append(tableInfo.getTableName()).append(" SET ");

        // 构建SET子句 - 支持ignore null策略
        for (int i = 0; i < fields.size(); i++) {
            TableFieldInfo field = fields.get(i);
            if (i > 0)
                sql.append(", ");

            // 使用CASE WHEN实现ignore null策略
            sql.append(field.getColumn()).append(" = CASE WHEN v.").append(field.getColumn())
                    .append(" IS NOT NULL THEN v.").append(field.getColumn())
                    .append(" ELSE ").append(tableInfo.getTableName()).append(".").append(field.getColumn())
                    .append(" END");
        }

        // 构建FROM VALUES子句
        sql.append(" FROM (VALUES ");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append("(#{item.").append(keyProperty).append("}");

        for (TableFieldInfo field : fields) {
            sql.append(", #{item.").append(field.getProperty()).append("}");
        }

        sql.append(")");
        sql.append("</foreach>");
        sql.append(") AS v(").append(keyColumn);

        for (TableFieldInfo field : fields) {
            sql.append(", ").append(field.getColumn());
        }
        sql.append(")");

        // WHERE子句
        sql.append(" WHERE ").append(tableInfo.getTableName()).append(".").append(keyColumn)
                .append(" = v.").append(keyColumn);

        sql.append("</script>");
        return sql.toString();
    }

    /**
     * 构建列名列表（排除逻辑删除字段和自增主键）
     */
    private String buildColumnList(TableInfo tableInfo) {
        StringBuilder columns = new StringBuilder();

        // PostgreSQL 自增主键完全省略，让数据库自动生成
        // 只有非自增主键且有序列或有值时才包含
        if (shouldIncludeKeyColumn(tableInfo)) {
            columns.append(tableInfo.getKeyColumn());
        }

        // 添加其他字段（排除逻辑删除字段）
        List<TableFieldInfo> validFields = getValidFields(tableInfo);
        for (TableFieldInfo field : validFields) {
            if (columns.length() > 0) {
                columns.append(", ");
            }
            columns.append(field.getColumn());
        }

        return columns.toString();
    }

    /**
     * 构建参数列表（支持ignore null策略）
     */
    private String buildParameterList(TableInfo tableInfo, String itemName) {
        StringBuilder params = new StringBuilder();

        // PostgreSQL 自增主键完全省略
        if (shouldIncludeKeyColumn(tableInfo)) {
            KeySequence keySequence = tableInfo.getKeySequence();
            if (keySequence != null) {
                // 序列主键：使用 nextval
                params.append("nextval('").append(keySequence.value()).append("')");
            } else {
                // 非自增非序列主键：直接使用值（不为null时才会包含该列）
                params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
            }
        }

        // 添加其他字段参数（支持ignore null策略）
        List<TableFieldInfo> validFields = getValidFields(tableInfo);
        for (TableFieldInfo field : validFields) {
            if (params.length() > 0) {
                params.append(", ");
            }

            // 支持null值时使用DEFAULT
            params.append("<choose>");
            params.append("<when test=\"").append(itemName).append(".").append(field.getProperty())
                    .append(" != null\">");
            params.append("#{").append(itemName).append(".").append(field.getProperty()).append("}");
            params.append("</when>");
            params.append("<otherwise>DEFAULT</otherwise>");
            params.append("</choose>");
        }

        return params.toString();
    }

    /**
     * 判断是否应该包含主键列
     * PostgreSQL 中的处理策略：
     * 1. AUTO 类型：完全省略（数据库自增）
     * 2. INPUT + KeySequence：包含并使用序列
     * 3. INPUT 无序列：不包含（避免null值问题）
     */
    private boolean shouldIncludeKeyColumn(TableInfo tableInfo) {
        if (StringUtils.isBlank(tableInfo.getKeyProperty())) {
            return false;
        }

        // 自增主键完全省略（SERIAL、IDENTITY等）
        if (tableInfo.getIdType() == IdType.AUTO) {
            return false;
        }

        // INPUT 类型且有序列：包含并使用序列生成值
        return tableInfo.getIdType() == IdType.INPUT && tableInfo.getKeySequence() != null;
    }

    /**
     * 获取有效字段列表（排除逻辑删除字段）
     */
    public List<TableFieldInfo> getValidFields(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())
                .collect(Collectors.toList());
    }
}