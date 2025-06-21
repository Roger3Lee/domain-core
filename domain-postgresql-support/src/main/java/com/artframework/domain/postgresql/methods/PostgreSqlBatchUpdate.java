package com.artframework.domain.postgresql.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PostgreSQL 批量更新方法实现
 */
public class PostgreSqlBatchUpdate extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public PostgreSqlBatchUpdate() {
        super("batchUpdate");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String keyProperty = tableInfo.getKeyProperty();
        String keyColumn = tableInfo.getKeyColumn();

        // 获取需要更新的字段（排除主键）
        List<TableFieldInfo> fieldList = tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())
                .collect(Collectors.toList());

        StringBuilder sql = new StringBuilder("<script>");
        sql.append("UPDATE ").append(tableInfo.getTableName()).append(" SET ");

        // 构建 SET 子句
        List<String> setClauses = fieldList.stream()
                .map(field -> field.getColumn() + " = v." + field.getColumn())
                .collect(Collectors.toList());
        sql.append(String.join(", ", setClauses));

        // 构建 FROM VALUES 子句
        sql.append(" FROM (VALUES ");
        sql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\">");
        sql.append("(");

        // 主键值
        sql.append("#{item.").append(keyProperty).append("}");

        // 其他字段值
        for (TableFieldInfo field : fieldList) {
            sql.append(", #{item.").append(field.getProperty()).append("}");
        }

        sql.append(")");
        sql.append("</foreach>");

        sql.append(") AS v(").append(keyColumn);
        for (TableFieldInfo field : fieldList) {
            sql.append(", ").append(field.getColumn());
        }
        sql.append(")");

        // WHERE 子句
        sql.append(" WHERE ").append(tableInfo.getTableName()).append(".").append(keyColumn)
                .append(" = v.").append(keyColumn);

        sql.append("</script>");

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "batchUpdate", sqlSource);
    }
}