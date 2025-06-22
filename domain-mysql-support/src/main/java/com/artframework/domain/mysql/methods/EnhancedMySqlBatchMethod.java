package com.artframework.domain.mysql.methods;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.core.batch.EnhancedBatchMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;

/**
 * 增强的 MySQL 批量操作方法实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 */
public class EnhancedMySqlBatchMethod extends EnhancedBatchMethod {

    public EnhancedMySqlBatchMethod(BatchOperationType operationType) {
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
        String columns = "(" + buildColumnList(tableInfo, true, true) + ")";
        String values = "(" + buildParameterList(tableInfo, true, true, "item") + ")";

        return "<script>" +
                "INSERT INTO " + tableInfo.getTableName() + " " + columns + " VALUES " +
                "<foreach collection=\"list\" item=\"item\" separator=\",\">" +
                values +
                "</foreach>" +
                "</script>";
    }

    @Override
    protected String buildBatchUpdateSql(TableInfo tableInfo) {
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();
        List<TableFieldInfo> fields = getValidFields(tableInfo);

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("UPDATE ").append(tableInfo.getTableName()).append(" SET ");

        // 构建 CASE WHEN 子句，支持ignore null
        for (int i = 0; i < fields.size(); i++) {
            TableFieldInfo field = fields.get(i);
            if (i > 0)
                sql.append(", ");

            sql.append(field.getColumn()).append(" = CASE ");
            sql.append("<foreach collection=\"list\" item=\"item\">");
            sql.append("<if test=\"item.").append(field.getProperty()).append(" != null\">");
            sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
            sql.append("THEN #{item.").append(field.getProperty()).append("} ");
            sql.append("</if>");
            sql.append("</foreach>");
            sql.append("ELSE ").append(field.getColumn()).append(" END");
        }

        // WHERE 子句
        sql.append(" WHERE ").append(keyColumn).append(" IN (");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append("#{item.").append(keyProperty).append("}");
        sql.append("</foreach>");
        sql.append(")");
        sql.append("</script>");

        return sql.toString();
    }

}