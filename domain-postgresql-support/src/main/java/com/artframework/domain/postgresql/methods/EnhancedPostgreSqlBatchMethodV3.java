package com.artframework.domain.postgresql.methods;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.core.batch.EnhancedBatchMethod;
import com.artframework.domain.core.batch.FieldStrategyHelper;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.List;

/**
 * 优化版本 - PostgreSQL 批量操作方法（支持 FieldStrategy）
 *
 * 改进点：
 * 1. 支持 insertStrategy（IGNORED, NOT_NULL, NOT_EMPTY, NEVER）
 * 2. 支持 updateStrategy（IGNORED, NOT_NULL, NOT_EMPTY, NEVER）
 * 3. 使用 COALESCE 简化 SQL
 * 4. 完全遵循 MyBatis Plus 的字段策略配置
 *
 * @author framework
 * @version 3.0
 */
public class EnhancedPostgreSqlBatchMethodV3 extends EnhancedBatchMethod {

    public EnhancedPostgreSqlBatchMethodV3(BatchOperationType operationType) {
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
        // 使用 FieldStrategyHelper 获取插入字段（考虑 insertStrategy）
        List<TableFieldInfo> fields = FieldStrategyHelper.getInsertFields(tableInfo);

        String columns = "(" + buildColumnList(tableInfo, fields) + ")";
        String values = "(" + buildParameterList(tableInfo, fields, "item") + ")";

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

        // 使用 FieldStrategyHelper 获取更新字段（考虑 updateStrategy）
        List<TableFieldInfo> fields = FieldStrategyHelper.getUpdateFields(tableInfo);

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("UPDATE ").append(tableInfo.getTableName()).append(" SET ");

        // 【改进】使用 FieldStrategyHelper 构建 SET 子句，考虑 updateStrategy
        for (int i = 0; i < fields.size(); i++) {
            TableFieldInfo field = fields.get(i);
            if (i > 0)
                sql.append(", ");

            // 根据 updateStrategy 生成不同的 SQL
            sql.append(FieldStrategyHelper.buildUpdateSetClausePostgreSQL(field, tableInfo.getTableName()));
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
     * 构建列名列表（考虑 insertStrategy，排除 NEVER 字段）
     */
    private String buildColumnList(TableInfo tableInfo, List<TableFieldInfo> fields) {
        StringBuilder columns = new StringBuilder();

        // 主键列
        if (shouldIncludeKeyColumn(tableInfo)) {
            columns.append(tableInfo.getKeyColumn());
        }

        // 其他字段（已通过 FieldStrategyHelper 过滤）
        for (TableFieldInfo field : fields) {
            if (columns.length() > 0) {
                columns.append(", ");
            }
            columns.append(field.getColumn());
        }

        return columns.toString();
    }

    /**
     * 构建参数列表（考虑 insertStrategy）
     */
    private String buildParameterList(TableInfo tableInfo, List<TableFieldInfo> fields, String itemName) {
        StringBuilder params = new StringBuilder();

        // 主键参数
        if (shouldIncludeKeyColumn(tableInfo)) {
            KeySequence keySequence = tableInfo.getKeySequence();
            if (keySequence != null) {
                // 序列主键：使用 nextval
                params.append("nextval('").append(keySequence.value()).append("')");
            } else {
                // 非自增非序列主键：直接使用值
                params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
            }
        }

        // 【改进】使用 FieldStrategyHelper 构建参数，考虑 insertStrategy
        for (TableFieldInfo field : fields) {
            if (params.length() > 0) {
                params.append(", ");
            }

            // 根据 insertStrategy 生成不同的参数表达式
            params.append(FieldStrategyHelper.buildInsertParameter(field, itemName));
        }

        return params.toString();
    }

    /**
     * 判断是否应该包含主键列
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
}
