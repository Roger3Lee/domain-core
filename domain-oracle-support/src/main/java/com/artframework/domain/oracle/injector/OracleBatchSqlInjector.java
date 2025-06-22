package com.artframework.domain.oracle.injector;

import com.artframework.domain.oracle.methods.OracleBatchInsert;
import com.artframework.domain.oracle.methods.OracleBatchUpdate;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Oracle 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 Oracle 特有的批量操作实现
 */
public class OracleBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 添加 Oracle 特有的批量操作方法
        methodList.addAll(Stream.of(
                new OracleBatchInsert(),
                new OracleBatchUpdate()).collect(toList()));

        return methodList;
    }
}