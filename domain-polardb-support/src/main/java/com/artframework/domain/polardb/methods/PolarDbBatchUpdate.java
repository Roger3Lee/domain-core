package com.artframework.domain.polardb.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * PolarDB 批量更新方法实现
 * PolarDB-O 兼容 Oracle，使用 Oracle MERGE 语法
 */
public class PolarDbBatchUpdate extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public PolarDbBatchUpdate() {
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

        // PolarDB-O 使用 Oracle 兼容的 MERGE 语法
        sql.append("MERGE INTO ").append(tableInfo.getTableName()).append(" target ");
        sql.append("USING (");

        // 构建数据源
        sql.append("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\" UNION ALL \">");
        sql.append("SELECT ");
        sql.append("#{item.").append(keyProperty).append("} AS ").append(keyColumn);

        for (TableFieldInfo field : fieldList) {
            sql.append(", #{item.").append(field.getProperty()).append("} AS ").append(field.getColumn());
        }

        sql.append(" FROM DUAL");
        sql.append("</foreach>");

        sql.append(") source ON (target.").append(keyColumn).append(" = source.").append(keyColumn).append(") ");

        // WHEN MATCHED 子句
        sql.append("WHEN MATCHED THEN UPDATE SET ");
        List<String> updateClauses = fieldList.stream()
                .map(field -> "target." + field.getColumn() + " = source." + field.getColumn())
                .collect(Collectors.toList());
        sql.append(String.join(", ", updateClauses));

        sql.append("</script>");

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "batchUpdate", sqlSource);
    }
}