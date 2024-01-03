package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.typeconverts.MyPostgreSqlTypeConvert;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MPProgram {

    public static void main(String[] args) throws IOException, JAXBException {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:postgresql://127.0.0.1:5432/postgres","postgres","123456")
                .dbQuery(new PostgreSqlQuery())
                .schema("public")
                .typeConvert(new MyPostgreSqlTypeConvert())
                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build();

        String path = "C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample";
        GlobalSetting.loadFromDB(dataSourceConfig,
                new File("C:\\work\\demo\\artframework.domain\\config\\domain-config.xml"));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","com.artframework.servicemesh.entities");
        packageParam.put("mapperPackage","com.artframework.servicemesh.mappers");
        packageParam.put("domainPackage","com.artframework.servicemesh.domains");
        packageParam.put("controllerPackage","com.artframework.servicemesh.controllers");

        GenerateUtils.generateTables(path,GlobalSetting.INSTANCE.getTableList(),packageParam);
        GenerateUtils.generateDomains(path,GlobalSetting.INSTANCE.getDomainList(),packageParam);
    }
}
