package com.artframework.domain.polardb.methods;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.core.batch.EnhancedBatchMethod;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.List;

/**
 * 增强的 PolarDB 批量操作方法实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 * PolarDB-O 兼容 Oracle，使用 Oracle 语法；PolarDB-MySQL 兼容 MySQL 语法
 */
public class EnhancedPolarDbBatchMethod extends EnhancedBatchMethod {

    public EnhancedPolarDbBatchMethod(BatchOperationType operationType) {
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

        // PolarDB-O 使用 Oracle 兼容的 INSERT ALL 语法
        sql.append("INSERT ALL ");
        sql.append("<foreach collection=\"list\" item=\"item\">");
        sql.append("INTO ").append(tableInfo.getTableName()).append(" ").append(columns);
        sql.append(" VALUES ").append(values).append(" ");
        sql.append("</foreach>");
        sql.append("SELECT 1 FROM DUAL");

        sql.append("</script>");
        return sql.toString();
    }

    @Override
    protected String buildBatchUpdateSql(TableInfo tableInfo) {
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();
        List<TableFieldInfo> fields = getValidFields(tableInfo);

        StringBuilder sql = new StringBuilder("<script>");

        // PolarDB-O 使用 Oracle 兼容的 MERGE 语法，支持 ignore null 策略
        sql.append("MERGE INTO ").append(tableInfo.getTableName()).append(" target ");
        sql.append("USING (");

        // 构建数据源
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\" UNION ALL \">");
        sql.append("SELECT ");
        sql.append("#{item.").append(keyProperty).append("} AS ").append(keyColumn);

        for (TableFieldInfo field : fields) {
            sql.append(", #{item.").append(field.getProperty()).append("} AS ").append(field.getColumn());
        }

        sql.append(" FROM DUAL");
        sql.append("</foreach>");

        sql.append(") source ON (target.").append(keyColumn).append(" = source.").append(keyColumn).append(") ");

        // WHEN MATCHED 子句 - 支持 ignore null 策略
        sql.append("WHEN MATCHED THEN UPDATE SET ");
        for (int i = 0; i < fields.size(); i++) {
            TableFieldInfo field = fields.get(i);
            if (i > 0)
                sql.append(", ");

            // 使用 NVL2 实现 ignore null 策略（Oracle 兼容）
            sql.append("target.").append(field.getColumn())
                    .append(" = NVL2(source.").append(field.getColumn())
                    .append(", source.").append(field.getColumn())
                    .append(", target.").append(field.getColumn()).append(")");
        }

        sql.append("</script>");
        return sql.toString();
    }

    /**
     * 构建列名列表（排除逻辑删除字段）
     */
    private String buildColumnList(TableInfo tableInfo) {
        StringBuilder columns = new StringBuilder();

        // 添加主键列（非自增主键）
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty()) && tableInfo.getIdType() != IdType.AUTO) {
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

        // 添加主键参数（非自增主键）
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty()) && tableInfo.getIdType() != IdType.AUTO) {
            if (tableInfo.getKeySequence() != null) {
                // 序列主键（Oracle 兼容语法）
                params.append(tableInfo.getKeySequence().value()).append(".NEXTVAL");
            } else {
                // 普通主键，支持null值时使用默认值
                params.append("<choose>");
                params.append("<when test=\"").append(itemName).append(".").append(tableInfo.getKeyProperty())
                        .append(" != null\">");
                params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
                params.append("</when>");
                params.append("<otherwise>NULL</otherwise>");
                params.append("</choose>");
            }
        }

        // 添加其他字段参数（支持ignore null策略）
        List<TableFieldInfo> validFields = getValidFields(tableInfo);
        for (TableFieldInfo field : validFields) {
            if (params.length() > 0) {
                params.append(", ");
            }

            // 支持null值
            params.append("<choose>");
            params.append("<when test=\"").append(itemName).append(".").append(field.getProperty())
                    .append(" != null\">");
            params.append("#{").append(itemName).append(".").append(field.getProperty()).append("}");
            params.append("</when>");
            params.append("<otherwise>NULL</otherwise>");
            params.append("</choose>");
        }

        return params.toString();
    }
}