package com.artframework.domain.mysql.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * MySQL 批量更新方法实现
 */
public class MySqlBatchUpdate extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public MySqlBatchUpdate() {
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

        // 构建 CASE WHEN 子句（MySQL 风格）
        for (int i = 0; i < fieldList.size(); i++) {
            TableFieldInfo field = fieldList.get(i);
            if (i > 0) sql.append(", ");
            
            sql.append(field.getColumn()).append(" = CASE ");
            sql.append("<foreach collection=\"list\" item=\"item\" index=\"index\">");
            sql.append("WHEN ").append(keyColumn).append(" = #{item.").append(keyProperty).append("} ");
            sql.append("THEN #{item.").append(field.getProperty()).append("} ");
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

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql.toString(), modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "batchUpdate", sqlSource);
    }
} 