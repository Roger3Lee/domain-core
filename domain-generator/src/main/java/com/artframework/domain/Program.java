package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.customize.CustomPostgreSqlQuery;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
        public static void main(String[] args) throws JAXBException, IOException {
                // 数据源配置
                DataSourceConfig dataSourceConfig = new DataSourceConfig.Builder(
                                "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "123456")
                                        .dbQuery(new CustomPostgreSqlQuery())
                                .schema("family")
                                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                                .addConnectionProperty("currentSchema", "family")
                                .addConnectionProperty("useUnicode", "true")
                                .addConnectionProperty("characterEncoding", "UTF-8")
                                .build();

                GlobalSetting.loadFromDB(dataSourceConfig,
                                new File("E:\\github\\domain-core\\config\\domain-config.xml"));

                Map<String, String> packageParam = new HashMap<>();
                packageParam.put("tablePackage", "com.artframework.sample.entities");
                packageParam.put("mapperPackage", "com.artframework.sample.mappers");
                packageParam.put("domainPackage", "com.artframework.sample.domains");
                packageParam.put("controllerPackage", "com.artframework.sample.controllers");
                //
                GenerateUtils.generateTables(
                                "E:\\github\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\mappers",
                                "E:\\github\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\entities",
                                GlobalSetting.INSTANCE.getTableList(), packageParam, true, true);
                GenerateUtils.generateDomains(
                                "E:\\github\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\domains",
                                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);
                GenerateUtils.generateController(
                                "E:\\github\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\controllers",
                                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);

                // DomainGenerator domainDtoGenerator = new DomainGenerator();
                // domainDtoGenerator.putParam(packageParam);
                // domainDtoGenerator.setTemplateFilePath("schema.ftl");
                // FileUtils.saveFile("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\boot\\src\\main\\resources\\auditlog",
                // "schema.json",
                // domainDtoGenerator.generate(GlobalSetting.INSTANCE.getDomainList().get(0)));
        }
}
