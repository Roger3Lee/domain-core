package com.artframework.domain.postgresql.injector;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.postgresql.methods.EnhancedPostgreSqlBatchMethodV3;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 增强的 PostgreSQL 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 PostgreSQL 特有的增强批量操作实现
 *
 * 支持特性：
 * - 主键自动回填（序列、自增）
 * - FieldStrategy 策略支持（IGNORED, NOT_NULL, NOT_EMPTY, NEVER）
 * - 逻辑删除字段自动过滤
 * - COALESCE SQL 优化
 *
 * @see EnhancedPostgreSqlBatchMethodV3
 * @version 3.0
 */
public class PostgreSqlBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 PostgreSQL 特有的增强批量操作方法（V3 版本：支持 FieldStrategy）
        methodList.addAll(Stream.of(
                new EnhancedPostgreSqlBatchMethodV3(BatchOperationType.INSERT),
                new EnhancedPostgreSqlBatchMethodV3(BatchOperationType.UPDATE)).collect(toList()));

        return methodList;
    }
}