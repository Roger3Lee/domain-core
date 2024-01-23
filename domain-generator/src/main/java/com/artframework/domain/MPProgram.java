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

public class MPProgram {

    public static void main(String[] args) throws IOException, JAXBException {
        // 数据源配置
        DataSourceConfig dataSourceConfig= new DataSourceConfig.Builder("jdbc:postgresql://polar02.o.polardb.rds.aliyuncs.com:1521/general_module_dev","dev","Whalecloud!!!")
                .dbQuery(new MyPostgreSqlQuery("public"))
                .schema("public")
                .typeConvert(new MyPostgreSqlTypeConvert())
                .keyWordsHandler(new PostgreSqlKeyWordsHandler())
                .databaseQueryClass(SQLQuery.class)
                .build();

        GlobalSetting.loadFromDB(dataSourceConfig,
                new File("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\1-domain\\domain-dynamic.xml"));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","mo.gov.dsaj.notarial.dal.dataobject");
        packageParam.put("mapperPackage","mo.gov.dsaj.notarial.dal.dao");
        packageParam.put("domainPackage","mo.gov.dsaj.notarial.core.application");
        packageParam.put("controllerPackage","mo.gov.dsaj.notarial.server.controller");

        GenerateUtils.generateTables("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\infra\\dal\\src\\main\\java\\mo\\gov\\dsaj\\notarial\\dal\\dao\\"
                ,"C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\infra\\dal\\src\\main\\java\\mo\\gov\\dsaj\\notarial\\dal\\dataobject\\",
                GlobalSetting.INSTANCE.getTableList(),packageParam, true, false);
        GenerateUtils.generateDomains("C:\\work\\Coding\\FW\\public-notarial\\public-notarial-be\\business\\core\\src\\main\\java\\mo\\gov\\dsaj\\notarial\\core\\application\\",
                GlobalSetting.INSTANCE.getDomainList(),packageParam);
    }
}
