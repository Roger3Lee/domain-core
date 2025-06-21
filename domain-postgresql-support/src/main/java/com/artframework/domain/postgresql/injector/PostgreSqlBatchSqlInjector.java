package com.artframework.domain.postgresql.injector;

import com.artframework.domain.postgresql.methods.PostgreSqlBatchInsert;
import com.artframework.domain.postgresql.methods.PostgreSqlBatchUpdate;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * PostgreSQL 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 PostgreSQL 特有的批量操作实现
 */
public class PostgreSqlBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 PostgreSQL 特有的批量操作方法
        methodList.addAll(Stream.of(
                new PostgreSqlBatchInsert(),
                new PostgreSqlBatchUpdate()).collect(toList()));

        return methodList;
    }
}