package com.artframework.domain.mysql.injector;

import com.artframework.domain.mysql.methods.MySqlBatchInsert;
import com.artframework.domain.mysql.methods.MySqlBatchUpdate;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * MySQL 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 MySQL 特有的批量操作实现
 */
public class MySqlBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 MySQL 特有的批量操作方法
        methodList.addAll(Stream.of(
                new MySqlBatchInsert(),
                new MySqlBatchUpdate()).collect(toList()));

        return methodList;
    }
}