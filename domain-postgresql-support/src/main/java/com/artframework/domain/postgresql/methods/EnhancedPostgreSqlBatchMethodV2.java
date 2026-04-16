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
 * 优化版本 - PostgreSQL 批量操作方法
 * 使用 COALESCE 简化 ignore null 策略
 */
public class EnhancedPostgreSqlBatchMethodV2 extends EnhancedBatchMethod {

    public EnhancedPostgreSqlBatchMethodV2(BatchOperationType operationType) {
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

        // 【优化】使用 COALESCE 简化 ignore null 策略
        for (int i = 0; i < fields.size(); i++) {
            TableFieldInfo field = fields.get(i);
            if (i > 0)
                sql.append(", ");

            // COALESCE(v.column, table.column) - 如果v.column为NULL，使用原值
            sql.append(field.getColumn())
                    .append(" = COALESCE(v.").append(field.getColumn())
                    .append(", ").append(tableInfo.getTableName()).append(".").append(field.getColumn())
                    .append(")");
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

    private String buildColumnList(TableInfo tableInfo) {
        StringBuilder columns = new StringBuilder();

        if (shouldIncludeKeyColumn(tableInfo)) {
            columns.append(tableInfo.getKeyColumn());
        }

        List<TableFieldInfo> validFields = getValidFields(tableInfo);
        for (TableFieldInfo field : validFields) {
            if (columns.length() > 0) {
                columns.append(", ");
            }
            columns.append(field.getColumn());
        }

        return columns.toString();
    }

    private String buildParameterList(TableInfo tableInfo, String itemName) {
        StringBuilder params = new StringBuilder();

        if (shouldIncludeKeyColumn(tableInfo)) {
            KeySequence keySequence = tableInfo.getKeySequence();
            if (keySequence != null) {
                params.append("nextval('").append(keySequence.value()).append("')");
            } else {
                params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
            }
        }

        List<TableFieldInfo> validFields = getValidFields(tableInfo);
        for (TableFieldInfo field : validFields) {
            if (params.length() > 0) {
                params.append(", ");
            }

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

    private boolean shouldIncludeKeyColumn(TableInfo tableInfo) {
        if (StringUtils.isBlank(tableInfo.getKeyProperty())) {
            return false;
        }

        if (tableInfo.getIdType() == IdType.AUTO) {
            return false;
        }

        return tableInfo.getIdType() == IdType.INPUT && tableInfo.getKeySequence() != null;
    }

    public List<TableFieldInfo> getValidFields(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())
                .collect(Collectors.toList());
    }
}
