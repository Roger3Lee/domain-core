package com.artframework.domain.polardb.injector;

import com.artframework.domain.polardb.methods.PolarDbBatchInsert;
import com.artframework.domain.polardb.methods.PolarDbBatchUpdate;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * PolarDB 批量操作 SQL 注入器
 * 为 BatchBaseMapper 接口提供 PolarDB 特有的批量操作实现
 */
public class PolarDbBatchSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        
        // 添加 PolarDB 特有的批量操作方法
        methodList.addAll(Stream.of(
            new PolarDbBatchInsert(),
            new PolarDbBatchUpdate()
        ).collect(toList()));
        
        return methodList;
    }
} 