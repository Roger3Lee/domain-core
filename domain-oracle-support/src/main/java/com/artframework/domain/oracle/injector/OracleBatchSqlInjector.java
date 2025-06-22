package com.artframework.domain.oracle.injector;

import com.artframework.domain.core.batch.BatchOperationType;
import com.artframework.domain.oracle.methods.EnhancedOracleBatchMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 增强的 Oracle 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 Oracle 特有的增强批量操作实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 */
public class OracleBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 Oracle 特有的增强批量操作方法
        methodList.addAll(Stream.of(
                new EnhancedOracleBatchMethod(BatchOperationType.INSERT),
                new EnhancedOracleBatchMethod(BatchOperationType.UPDATE)).collect(toList()));

        return methodList;
    }
}