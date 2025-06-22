package com.artframework.domain.polardb.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.ArrayList;
import java.util.List;

/**
 * PolarDB 批量插入方法实现
 * PolarDB-O 兼容 Oracle，使用 Oracle INSERT ALL 语法
 */
public class PolarDbBatchInsert extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public PolarDbBatchInsert() {
        super("insertBatch");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = buildSql(tableInfo);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource,
                new NoKeyGenerator(), null, null);
    }

    private String buildSql(TableInfo tableInfo) {
        StringBuilder sql = new StringBuilder();
        sql.append("<script>");
        sql.append("INSERT INTO ").append(tableInfo.getTableName());

        String columnScript = SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf((String)null, true  ), "(", ")", (String)null, ",");
        String valuesScript = "(\n" + SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf("item", true), (String)null, (String)null, (String)null, ",") + "\n" + ")";
        sql.append(columnScript);
        sql.append(" VALUES ");
        sql.append("<foreach collection='list' item='item' separator=','>");
        sql.append(valuesScript);
        sql.append("</foreach>");
        sql.append(" RETURNING id"); // PostgreSQL特有语法，返回自增ID
        sql.append("</script>");

        return sql.toString();
    }
}