package com.artframework.domain.datasource;


import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.query.IDatabaseQuery;

import java.lang.reflect.Constructor;
import java.util.List;

public class TableQuery {
    private final IDatabaseQuery databaseQuery;

    public TableQuery(com.baomidou.mybatisplus.generator.config.DataSourceConfig dataSourceConfig) {

        Class<? extends IDatabaseQuery> databaseQueryClass = dataSourceConfig.getDatabaseQueryClass();

        try {
            ConfigBuilder configBuilder=    new ConfigBuilder(null,dataSourceConfig,null,null,null,null);
            Constructor<? extends IDatabaseQuery> declaredConstructor = databaseQueryClass.getDeclaredConstructor(configBuilder.getClass());
            this.databaseQuery = (IDatabaseQuery) declaredConstructor.newInstance(configBuilder);
        } catch (ReflectiveOperationException var9) {
            throw new RuntimeException("创建IDatabaseQuery实例出现错误:", var9);
        }
    }

    public List<TableInfo> queryTables() {
        return databaseQuery.queryTables();
    }
}