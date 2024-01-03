package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.generator.domain.DomainGenerator;
import com.artframework.domain.generator.table.TableGenerator;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.GenerateUtils;
import com.artframework.domain.utils.NameUtils;
import com.artframework.domain.dto.TableInfo;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "C:\\work\\demo\\artframework.domain\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\";
        String configPath = "C:\\work\\demo\\artframework.domain\\config\\";
        GlobalSetting.load(new File(configPath + "\\table-list.xml"),
                new File(configPath + "\\domain-config.xml"));

        Map<String, String> packageParam=new HashMap<>();
        packageParam.put("tablePackage","com.artframework.sample.entities");
        packageParam.put("mapperPackage","com.artframework.sample.mappers");
        packageParam.put("domainPackage","com.artframework.sample.domains");
        packageParam.put("controllerPackage","com.artframework.sample.controller");

        GenerateUtils.generateTables(path,GlobalSetting.INSTANCE.getTableList(),packageParam);
        GenerateUtils.generateDomains(path,GlobalSetting.INSTANCE.getDomainList(),packageParam);
    }
}
