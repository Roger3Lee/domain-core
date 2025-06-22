package com.artframework.domain.postgresql.injector;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.postgresql.methods.EnhancedPostgreSqlBatchMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 增强的 PostgreSQL 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 PostgreSQL 特有的增强批量操作实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 */
public class PostgreSqlBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 PostgreSQL 特有的增强批量操作方法
        methodList.addAll(Stream.of(
                new EnhancedPostgreSqlBatchMethod(BatchOperationType.INSERT),
                new EnhancedPostgreSqlBatchMethod(BatchOperationType.UPDATE)).collect(toList()));

        return methodList;
    }
}