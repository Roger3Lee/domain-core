package com.artframework.domain;

import com.artframework.domain.datasource.TableQuery;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import java.util.List;

public class MPProgram {

    public static void main(String[] args) {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/domain","root","123456")
                .dbQuery(new MySqlQuery())
                .schema("domain")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build();

        TableQuery tableQuery=new TableQuery(dataSourceConfig);
        List<TableInfo> tableInfos=  tableQuery.queryTables();

    }
}
