package io.github.roger3lee.domain.mysql.injector;

import io.github.roger3lee.domain.core.batch.BatchOperationType;
import io.github.roger3lee.domain.mysql.methods.EnhancedMySqlBatchMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * 增强的 MySQL 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 MySQL 特有的增强批量操作实现
 * 支持主键自动回填、ignore null策略和逻辑删除字段过滤
 */
public class MySqlBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 MySQL 特有的增强批量操作方法
        methodList.addAll(Stream.of(
                new EnhancedMySqlBatchMethod(BatchOperationType.INSERT),
                new EnhancedMySqlBatchMethod(BatchOperationType.UPDATE)).collect(toList()));

        return methodList;
    }
}