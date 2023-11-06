package com.artframework.domain;

import com.artframework.domain.datasource.TableQuery;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

public class MPProgram {

    public static void main(String[] args) {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/mybatis-plus","root","123456")
                .dbQuery(new PostgreSqlQuery())
                .schema("mybatis-plus")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build();

        TableQuery tableQuery=new TableQuery(dataSourceConfig);
        tableQuery.queryTables();
    }
}
