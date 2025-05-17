package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.customize.MyPostgreSqlQuery;
import com.artframework.domain.customize.MyPostgreSqlTypeConvert;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:postgresql://127.0.0.1:5432/postgres","postgres","123456")
                .dbQuery(new PostgreSqlQuery())
                .schema("domain")
                .typeConvert(new PostgreSqlTypeConvert())
                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .addConnectionProperty("currentSchema","domain")
                .addConnectionProperty("useUnicode","true")
                .addConnectionProperty("characterEncoding","UTF-8")
                .build();

        GlobalSetting.loadFromDB(dataSourceConfig,
                new File("D:\\日常\\dev-partform\\artframework-domain\\config\\domain-config.xml"));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","com.artframework.sample.entities");
        packageParam.put("mapperPackage","com.artframework.sample.mappers");
        packageParam.put("domainPackage","com.artframework.sample.domains");
        packageParam.put("controllerPackage","com.artframework.sample.controllers");
//
        GenerateUtils.generateTables("D:\\日常\\dev-partform\\artframework-domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\mappers"
                ,"D:\\日常\\dev-partform\\artframework-domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\entities",
                GlobalSetting.INSTANCE.getTableList(),packageParam, true, true);
        GenerateUtils.generateDomains("D:\\日常\\dev-partform\\artframework-domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\domains",
                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);
        GenerateUtils.generateController("D:\\日常\\dev-partform\\artframework-domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\controllers",
                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);

//        DomainGenerator domainDtoGenerator = new DomainGenerator();
//        domainDtoGenerator.putParam(packageParam);
//        domainDtoGenerator.setTemplateFilePath("schema.ftl");
//        FileUtils.saveFile("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\boot\\src\\main\\resources\\auditlog", "schema.json",
//                domainDtoGenerator.generate(GlobalSetting.INSTANCE.getDomainList().get(0)));
    }
}
