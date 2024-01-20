package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.utils.GenerateUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String configPath = "C:\\work\\demo\\artframework.domain\\config\\";
        GlobalSetting.load(new File(configPath + "\\table-list.xml"),
                new File(configPath + "\\domain-config.xml"));

        Map<String, String> packageParam = new HashMap<>();
        packageParam.put("tablePackage", "com.artframework.sample.entities");
        packageParam.put("mapperPackage", "com.artframework.sample.mappers");
        packageParam.put("domainPackage", "com.artframework.sample.domains");
        packageParam.put("controllerPackage", "com.artframework.sample.controller");

        GenerateUtils.generateTables("C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\mappers",
                "C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\entities",
                GlobalSetting.INSTANCE.getTableList(), packageParam, false, false);
        GenerateUtils.generateDomains("C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\domains\\",
                GlobalSetting.INSTANCE.getDomainList(), packageParam, false);
    }
}
