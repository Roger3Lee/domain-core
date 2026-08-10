package io.github.roger3lee.domain.mysql.methods;

import io.github.roger3lee.domain.core.batch.BatchOperationType;
import io.github.roger3lee.domain.core.batch.EnhancedBatchMethod;
import io.github.roger3lee.domain.core.batch.FieldStrategyHelper;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.List;

/**
 * 优化版本 - MySQL 批量操作方法（支持 FieldStrategy）
 *
 * 改进点：
 * 1. 支持 insertStrategy（ALWAYS, NOT_NULL, NOT_EMPTY, NEVER）
 * 2. 支持 updateStrategy（ALWAYS, NOT_NULL, NOT_EMPTY, NEVER）
 * 3. 使用 CASE WHEN 构建批量更新 SQL
 * 4. 完全遵循 MyBatis Plus 的字段策略配置
 *
 * @version 3.0
 */
public class EnhancedMySqlBatchMethodV3 extends EnhancedBatchMethod {

    public EnhancedMySqlBatchMethodV3(BatchOperationType operationType) {
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

    // ==================== 批量插入 ====================

    @Override
    protected String buildBatchInsertSql(TableInfo tableInfo) {
        // 使用 FieldStrategyHelper 获取插入字段（考虑 insertStrategy，排除 NEVER）
        List<TableFieldInfo> fields = FieldStrategyHelper.getInsertFields(tableInfo);

        String columns = "(" + buildColumnList(tableInfo, fields) + ")";
        String values = "(" + buildParameterList(tableInfo, fields, "item") + ")";

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("INSERT INTO ").append(tableInfo.getTableName()).append(" ").append(columns);
        sql.append(" VALUES ");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append(values);
        sql.append("</foreach>");
        sql.append("</script>");

        return sql.toString();
    }

    // ==================== 批量更新 ====================

    @Override
    protected String buildBatchUpdateSql(TableInfo tableInfo) {
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();

        // 使用 FieldStrategyHelper 获取更新字段（考虑 updateStrategy，排除 NEVER）
        List<TableFieldInfo> fields = FieldStrategyHelper.getUpdateFields(tableInfo);

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("UPDATE ").append(tableInfo.getTableName()).append(" ");

        // 使用 trim 包裹 SET 子句，suffixOverrides="," 自动去除末尾多余逗号
        // 当某个字段的所有 WHEN 条件都不满足时，该字段的 trim 不产生任何内容，避免生成无效的 CASE ELSE column END
        sql.append("<trim prefix=\"SET\" suffixOverrides=\",\">");

        for (TableFieldInfo field : fields) {
            buildUpdateCaseWhen(sql, field, keyProperty, keyColumn);
        }

        sql.append("</trim>");

        // WHERE 子句
        sql.append(" WHERE ").append(keyColumn).append(" IN (");
        sql.append("<foreach collection=\"list\" item=\"item\" separator=\",\">");
        sql.append("#{item.").append(keyProperty).append("}");
        sql.append("</foreach>");
        sql.append(")");
        sql.append("</script>");

        return sql.toString();
    }

    // ==================== 内部方法 ====================

    /**
     * 构建单个字段的 CASE WHEN 更新子句
     * 根据 updateStrategy 决定是否检查 null/empty
     */
    private void buildUpdateCaseWhen(StringBuilder sql, TableFieldInfo field,
                                      String keyProperty, String keyColumn) {
        FieldStrategy strategy = getEffectiveStrategy(field.getUpdateStrategy());
        String column = field.getColumn();
        String property = field.getProperty();

        // 使用 trim 包裹 CASE WHEN 表达式
        // 当所有 WHEN 条件都不满足时（foreach 内容为空），trim 不输出任何内容，避免生成无效的 CASE ELSE column END
        // suffix 中的逗号用于字段间分隔，由外层 trim 的 suffixOverrides 处理末尾多余逗号
        sql.append("<trim prefix=\"").append(column).append(" = CASE \" suffix=\" ELSE ")
           .append(column).append(" END,\">");

        sql.append("<foreach collection=\"list\" item=\"item\">");

        switch (strategy) {
            case ALWAYS:
                // 总是更新，不检查 null
                sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
                sql.append("THEN #{item.").append(property).append("} ");
                break;

            case NOT_NULL:
                // 仅当值非 null 时更新
                sql.append("<if test=\"item.").append(property).append(" != null\">");
                sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
                sql.append("THEN #{item.").append(property).append("} ");
                sql.append("</if>");
                break;

            case NOT_EMPTY:
                // 仅当值非 null 且非空字符串时更新
                sql.append("<if test=\"item.").append(property).append(" != null and item.")
                   .append(property).append(" != ''\">");
                sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
                sql.append("THEN #{item.").append(property).append("} ");
                sql.append("</if>");
                break;

            default:
                // 默认使用 NOT_NULL
                sql.append("<if test=\"item.").append(property).append(" != null\">");
                sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
                sql.append("THEN #{item.").append(property).append("} ");
                sql.append("</if>");
                break;
        }

        sql.append("</foreach>");
        sql.append("</trim>");
    }

    /**
     * 构建列名列表（考虑 insertStrategy，排除 NEVER 字段）
     */
    private String buildColumnList(TableInfo tableInfo, List<TableFieldInfo> fields) {
        StringBuilder columns = new StringBuilder();

        // 主键列（仅在非自增时包含）
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
            params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
        }

        // 使用 FieldStrategyHelper 构建参数，考虑 insertStrategy
        for (TableFieldInfo field : fields) {
            if (params.length() > 0) {
                params.append(", ");
            }
            params.append(FieldStrategyHelper.buildInsertParameter(field, itemName));
        }

        return params.toString();
    }

    /**
     * 判断是否应该包含主键列
     * MySQL 自增主键（AUTO_INCREMENT）应省略，由数据库自动生成
     */
    private boolean shouldIncludeKeyColumn(TableInfo tableInfo) {
        if (StringUtils.isBlank(tableInfo.getKeyProperty())) {
            return false;
        }
        // 自增主键省略（AUTO_INCREMENT）
        return tableInfo.getIdType() != IdType.AUTO;
    }

    /**
     * 获取有效策略（处理 DEFAULT → NOT_NULL）
     */
    private FieldStrategy getEffectiveStrategy(FieldStrategy strategy) {
        return strategy == FieldStrategy.DEFAULT ? FieldStrategy.NOT_NULL : strategy;
    }
}
