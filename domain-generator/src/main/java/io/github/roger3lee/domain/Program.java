package io.github.roger3lee.domain;

import io.github.roger3lee.domain.config.GlobalSetting;
import io.github.roger3lee.domain.customize.CustomPostgreSqlQuery;
import io.github.roger3lee.domain.utils.GenerateUtils;
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
                                "jdbc:postgresql://127.0.0.1:5433/postgres", "postgres", "123456")
                                        .dbQuery(new CustomPostgreSqlQuery())
                                .schema("public")
                                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                                .addConnectionProperty("currentSchema", "domain-generator")
                                .addConnectionProperty("useUnicode", "true")
                                .addConnectionProperty("characterEncoding", "UTF-8")
                                .build();

                GlobalSetting.loadFromDB(dataSourceConfig,
                                new File("D:\\work\\code\\domain\\domain-core\\config\\domain-config.xml"));

                Map<String, String> packageParam = new HashMap<>();
                packageParam.put("tablePackage", "io.github.roger3lee.sample.entities");
                packageParam.put("mapperPackage", "io.github.roger3lee.sample.mappers");
                packageParam.put("domainPackage", "io.github.roger3lee.sample.domains");
                packageParam.put("controllerPackage", "io.github.roger3lee.sample.controllers");
                //
                GenerateUtils.generateTables(
                                "D:\\work\\code\\domain\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\mappers",
                                "D:\\work\\code\\domain\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\entities",
                                GlobalSetting.INSTANCE.getTableList(), packageParam, true, true);
                GenerateUtils.generateDomains(
                                "D:\\work\\code\\domain\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\domains",
                                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);
                GenerateUtils.generateController(
                                "D:\\work\\code\\domain\\domain-core\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\controllers",
                                GlobalSetting.INSTANCE.getDomainList(), packageParam, true);

                // DomainGenerator domainDtoGenerator = new DomainGenerator();
                // domainDtoGenerator.putParam(packageParam);
                // domainDtoGenerator.setTemplateFilePath("schema.ftl");
                // FileUtils.saveFile("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\boot\\src\\main\\resources\\auditlog",
                // "schema.json",
                // domainDtoGenerator.generate(GlobalSetting.INSTANCE.getDomainList().get(0)));
        }
}
