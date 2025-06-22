package com.artframework.domain.core.batch;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 增强的批量操作方法基类
 * 支持MyBatis Plus的主键生成和ignore null策略
 */
public abstract class EnhancedBatchMethod extends AbstractMethod {

    protected final BatchOperationType operationType;

    protected EnhancedBatchMethod(String methodName, BatchOperationType operationType) {
        super(methodName);
        this.operationType = operationType;
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        switch (operationType) {
            case INSERT:
                return createBatchInsertStatement(mapperClass, modelClass, tableInfo);
            case UPDATE:
                return createBatchUpdateStatement(mapperClass, modelClass, tableInfo);
            default:
                throw new UnsupportedOperationException("Unsupported operation: " + operationType);
        }
    }

    /**
     * 创建批量插入语句 - 支持主键回填
     */
    protected MappedStatement createBatchInsertStatement(Class<?> mapperClass, Class<?> modelClass,
            TableInfo tableInfo) {
        String sql = buildBatchInsertSql(tableInfo);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        KeyGenerator keyGenerator = getKeyGenerator(tableInfo);
        String keyProperty = getKeyProperty(tableInfo);
        String keyColumn = getKeyColumn(tableInfo);

        return addInsertMappedStatement(mapperClass, modelClass, methodName, sqlSource,
                keyGenerator, keyProperty, keyColumn);
    }

    /**
     * 创建批量更新语句 - 支持ignore null策略
     */
    protected MappedStatement createBatchUpdateStatement(Class<?> mapperClass, Class<?> modelClass,
            TableInfo tableInfo) {
        String sql = buildBatchUpdateSql(tableInfo);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }

    // ==================== 抽象方法，由子类实现数据库特定SQL ====================

    protected abstract String buildBatchInsertSql(TableInfo tableInfo);

    protected abstract String buildBatchUpdateSql(TableInfo tableInfo);

    // ==================== 工具方法 ====================

    /**
     * 获取有效字段列表（排除逻辑删除字段）
     */
    protected List<TableFieldInfo> getValidFields(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream()
                .filter(field -> !field.isLogicDelete())
                .collect(Collectors.toList());
    }

    /**
     * 构建列名列表
     */
    protected String buildColumnList(TableInfo tableInfo, boolean includeKey, boolean excludeLogicDelete) {
        StringBuilder columns = new StringBuilder();

        if (includeKey && tableInfo.getKeyProperty() != null) {
            columns.append(tableInfo.getKeyColumn());
        }

        List<TableFieldInfo> fields = excludeLogicDelete ? getValidFields(tableInfo) : tableInfo.getFieldList();

        for (TableFieldInfo field : fields) {
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
    protected String buildParameterList(TableInfo tableInfo, boolean includeKey, boolean excludeLogicDelete,
            String itemName) {
        StringBuilder params = new StringBuilder();

        if (includeKey && tableInfo.getKeyProperty() != null) {
            params.append("#{").append(itemName).append(".").append(tableInfo.getKeyProperty()).append("}");
        }

        List<TableFieldInfo> fields = excludeLogicDelete ? getValidFields(tableInfo) : tableInfo.getFieldList();

        for (TableFieldInfo field : fields) {
            if (params.length() > 0) {
                params.append(", ");
            }
            params.append("#{").append(itemName).append(".").append(field.getProperty()).append("}");
        }

        return params.toString();
    }

    /**
     * 获取主键生成器
     */
    private KeyGenerator getKeyGenerator(TableInfo tableInfo) {
        if (tableInfo.getKeyProperty() != null && tableInfo.getIdType() == IdType.AUTO) {
            return Jdbc3KeyGenerator.INSTANCE;
        }
        return NoKeyGenerator.INSTANCE;
    }

    private String getKeyProperty(TableInfo tableInfo) {
        return tableInfo.getKeyProperty();
    }

    private String getKeyColumn(TableInfo tableInfo) {
        return tableInfo.getKeyColumn();
    }
}