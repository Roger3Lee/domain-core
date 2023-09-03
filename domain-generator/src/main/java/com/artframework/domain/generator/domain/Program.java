package com.artframework.domain.generator.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.NameUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "D:\\github\\domain-generator\\domain-generator\\src\\main\\java\\com\\artframework\\domain\\demo\\domain\\";
        GlobalSetting.load(new File("D:\\github\\domain-generator\\config\\table-list.xml"),
                new File("D:\\github\\domain-generator\\config\\domain-config.xml"));
        for (DomainMetaInfo domainMetaInfo : GlobalSetting.INSTANCE.getDomainList()) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator("com.artframework.domain.demo", FTLConstants.DTO_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\", domainInfo.nameSuffix("DTO") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.FIND_REQUEST_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("FindRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.PAGE_REQUEST_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("PageRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.UPDATE_REQUEST_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("UpdateRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\", domainInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_PATH);
            FileUtils.saveFile(path + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\impl\\", domainInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo));
        }

    }
}
