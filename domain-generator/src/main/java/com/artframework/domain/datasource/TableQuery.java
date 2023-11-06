package com.artframework.domain.datasource;


import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.query.IDatabaseQuery;
import com.sun.istack.internal.NotNull;

import java.lang.reflect.Constructor;
import java.util.List;

public class TableQuery {
    private final IDatabaseQuery databaseQuery;

    public TableQuery(@NotNull com.baomidou.mybatisplus.generator.config.DataSourceConfig dataSourceConfig) {

        Class<? extends IDatabaseQuery> databaseQueryClass = dataSourceConfig.getDatabaseQueryClass();

        try {
            Constructor<? extends IDatabaseQuery> declaredConstructor = databaseQueryClass.getDeclaredConstructor(this.getClass());
            this.databaseQuery = (IDatabaseQuery) declaredConstructor.newInstance(this);
        } catch (ReflectiveOperationException var9) {
            throw new RuntimeException("创建IDatabaseQuery实例出现错误:", var9);
        }
    }

    public List<TableInfo> queryTables() {
        return databaseQuery.queryTables();
    }
}