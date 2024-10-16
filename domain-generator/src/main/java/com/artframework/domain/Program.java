package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.customize.MyPostgreSqlQuery;
import com.artframework.domain.customize.MyPostgreSqlTypeConvert;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String configPath = "D:\\github\\domain-core\\config\\domain-config.xml";
        DataSourceConfig.Builder builder = new DataSourceConfig
                .Builder("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres",
                "123456");
            builder.dbQuery(new MyPostgreSqlQuery("public"))
                    .schema("public")
                    .typeConvert(new MyPostgreSqlTypeConvert())
                    .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                    .addConnectionProperty("currentSchema","public")
                    .databaseQueryClass(SQLQuery.class);

        DataSourceConfig dataSourceConfig = builder.build();
        GlobalSetting.loadFromDB(dataSourceConfig ,new File(configPath));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","com.artframework.sample.entities");
        packageParam.put("mapperPackage","com.artframework.sample.mappers");
        packageParam.put("domainPackage","com.artframework.sample.domains");
        packageParam.put("controllerPackage","com.artframework.sample.controller");

        GenerateUtils.generateTables("C:\\work\\domain-core\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\mappers",
                "C:\\work\\domain-core\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\entities",
                GlobalSetting.INSTANCE.getTableList(), packageParam,true,true);
        GenerateUtils.generateDomains("C:\\work\\domain-core\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\domains\\",
                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);
    }
}
