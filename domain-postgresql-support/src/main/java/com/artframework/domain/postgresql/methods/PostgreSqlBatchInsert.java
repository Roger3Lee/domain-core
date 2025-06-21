package com.artframework.domain.postgresql.methods;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostgreSQL 批量插入方法实现
 * 使用 PostgreSQL VALUES 语法进行批量插入
 */
public class PostgreSqlBatchInsert extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public PostgreSqlBatchInsert() {
        super("insertBatch");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        String keyProperty = null;
        String keyColumn = null;

        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            // 对于批量插入，使用Jdbc3KeyGenerator来处理RETURNING结果
            keyGenerator = Jdbc3KeyGenerator.INSTANCE;
            keyProperty = "list." + tableInfo.getKeyProperty(); // 批量插入时的key路径
            keyColumn = tableInfo.getKeyColumn();
        }

        String sql = buildSql(tableInfo);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource,
                keyGenerator, keyProperty, keyColumn);
    }

    private String buildSql(TableInfo tableInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("<script>");
        sql.append("INSERT INTO ").append(tableInfo.getTableName());

        // 使用动态列名，只插入非null字段
        sql.append(buildColumnsSql(tableInfo));
        sql.append(" VALUES ");
        sql.append("<foreach collection='list' item='item' separator=','>");
        sql.append(buildValuesSql(tableInfo));
        sql.append("</foreach>");
        sql.append(" RETURNING ").append(tableInfo.getKeyColumn()); // 只返回主键
        sql.append("</script>");

        return sql.toString();
    }

    /**
     * 构建列名SQL
     */
    private String buildColumnsSql(TableInfo tableInfo) {
        StringBuilder columns = new StringBuilder("(");

        // 添加主键列
        if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
            if (tableInfo.getIdType() != IdType.AUTO) {
                columns.append(tableInfo.getKeyColumn()).append(",");
            }
        }

        // 添加非主键列
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            columns.append(fieldInfo.getColumn()).append(",");
        }

        // 移除最后一个逗号
        if (columns.length() > 1 && columns.charAt(columns.length() - 1) == ',') {
            columns.deleteCharAt(columns.length() - 1);
        }

        columns.append(")");
        return columns.toString();
    }

    /**
     * 构建值SQL，支持null值时使用DEFAULT
     */
    private String buildValuesSql(TableInfo tableInfo) {
        StringBuilder values = new StringBuilder("(");

        // 处理主键值
        if (StringUtils.isNotBlank(tableInfo.getKeyColumn())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                // 自增主键不需要处理
            } else if (null != tableInfo.getKeySequence()) {
                values.append("nextval('").append(tableInfo.getKeySequence().value()).append("'),");
            } else {
                String keyProperty = tableInfo.getKeyProperty();
                values.append("<choose>");
                values.append("<when test='item.").append(keyProperty).append(" != null'>");
                values.append("#{item.").append(keyProperty).append("}");
                values.append("</when>");
                values.append("<otherwise>DEFAULT</otherwise>");
                values.append("</choose>,");
            }
        }

        // 处理非主键字段值，支持null时使用DEFAULT
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            String property = fieldInfo.getProperty();

            values.append("<choose>");
            values.append("<when test='item.").append(property).append(" != null'>");
            values.append("#{item.").append(property).append("}");
            values.append("</when>");
            values.append("<otherwise>DEFAULT</otherwise>");
            values.append("</choose>,");
        }

        // 移除最后一个逗号
        if (values.length() > 1 && values.charAt(values.length() - 1) == ',') {
            values.deleteCharAt(values.length() - 1);
        }

        values.append(")");
        return values.toString();
    }
}