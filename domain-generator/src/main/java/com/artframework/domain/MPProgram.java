package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.datasource.TableQuery;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MPProgram {

    public static void main(String[] args) throws IOException, JAXBException {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:mysql://127.0.0.1:3306/domain","root","123456")
                .dbQuery(new MySqlQuery())
                .schema("domain")
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build();

        String path = "C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\";
        String configPath = "C:\\work\\demo\\artframework.domain\\config\\";
        GlobalSetting.loadFromDB(dataSourceConfig,
                new File(configPath + "\\domain-config.xml"));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","com.artframework.sample.entities");
        packageParam.put("mapperPackage","com.artframework.sample.mappers");
        packageParam.put("domainPackage","com.artframework.sample.domain");
        packageParam.put("controllerPackage","com.artframework.sample.controller");

        GenerateUtils.generateTables(path,GlobalSetting.INSTANCE.getTableList(),packageParam);
        GenerateUtils.generateDomains(path,GlobalSetting.INSTANCE.getDomainList(),packageParam);
    }
}
