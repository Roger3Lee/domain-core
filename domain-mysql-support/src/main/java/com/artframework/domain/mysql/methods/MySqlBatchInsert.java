package com.artframework.domain.mysql.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;

/**
 * MySQL 批量插入方法实现
 * 使用 MySQL VALUES 语法进行批量插入
 */
public class MySqlBatchInsert extends AbstractMethod {

    /**
     * 构造方法，指定方法名
     */
    public MySqlBatchInsert() {
        super("insertBatch");
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;

        // 获取所有需要插入的字段（包括主键）
        StringBuilder columns = new StringBuilder();
        StringBuilder properties = new StringBuilder();

        // 添加主键字段
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            columns.append(tableInfo.getKeyColumn());
            properties.append("#{item.").append(tableInfo.getKeyProperty()).append("}");
        }

        // 添加其他字段
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        for (TableFieldInfo field : fieldList) {
            if (!field.isLogicDelete()) {
                if (columns.length() > 0) {
                    columns.append(", ");
                    properties.append(", ");
                }
                columns.append(field.getColumn());
                properties.append("#{item.").append(field.getProperty()).append("}");
            }
        }

        String columnScript = LEFT_BRACKET + columns.toString() + RIGHT_BRACKET;
        String valuesScript = LEFT_BRACKET + properties.toString() + RIGHT_BRACKET;
        String foreachScript = SqlScriptUtils.convertForeach(valuesScript, "list", null, "item", COMMA);

        String keyProperty = null;
        String keyColumn = null;

        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /** 自增主键 */
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }

        // MySQL 使用 INSERT INTO ... VALUES (...), (...), (...) 语法
        String sql = "<script>" +
                "INSERT INTO " + tableInfo.getTableName() + " " + columnScript + " VALUES " + foreachScript +
                "</script>";

        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
                keyProperty, keyColumn);
    }
}