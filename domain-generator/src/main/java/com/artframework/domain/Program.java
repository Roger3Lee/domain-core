package com.artframework.domain;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.generator.domain.DomainGenerator;
import com.artframework.domain.generator.table.TableGenerator;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.FileUtils;
import com.artframework.domain.utils.NameUtils;
import com.artframework.domain.dto.TableInfo;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Program {
    public static void main(String[] args) throws JAXBException, IOException {
        String path = "C:\\work\\demo\\artframework.domain-master\\artframework.domain-master\\domain-sample\\src\\main\\java\\com\\artframework\\sample\\";
        String configPath = "C:\\work\\demo\\artframework.domain-master\\artframework.domain-master\\config\\";
        GlobalSetting.load(new File(configPath + "\\table-list.xml"),
                new File(configPath + "\\domain-config.xml"));

        List<TableMetaInfo> tableCollection = GlobalSetting.INSTANCE.getTableList();
        for (TableMetaInfo table : tableCollection) {
            TableGenerator generator = new TableGenerator("com.artframework.sample");
            TableInfo tableMetaInfo = TableInfo.covert(table);
            generator.setTemplateFilePath(FTLConstants.TABLE_DO_PATH);
            FileUtils.saveFile(path + "entities\\", NameUtils.dataObjectName(tableMetaInfo.getName())+ ".java", generator.generate(tableMetaInfo));
            if(!tableMetaInfo.getBasic()){
                generator.setTemplateFilePath(FTLConstants.TABLE_MAPPER_PATH);
                FileUtils.saveFile(path + "mappers\\", NameUtils.mapperName(tableMetaInfo.getName()) + ".java", generator.generate(tableMetaInfo));
            }
        }

        for (DomainMetaInfo domainMetaInfo : GlobalSetting.INSTANCE.getDomainList()) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator("com.artframework.sample", FTLConstants.DTO_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\", domainInfo.nameSuffix("DTO") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.FIND_REQUEST_PATH);
            FileUtils.saveFile(path+ "domain\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("FindRequest") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.PAGE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domain\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("PageRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.UPDATE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("UpdateRequest") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.CREATE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "dto\\request\\", domainInfo.nameSuffix("CreateRequest") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\", domainInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\impl\\", domainInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "service\\", domainInfo.nameSuffix("Service") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_IMPL_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "service\\impl\\", domainInfo.nameSuffix("ServiceImpl") + ".java", domainDtoGenerator.generate(domainInfo));



            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_LAMBDA_EXP_PATH);
            FileUtils.saveFile(path+ "domain\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "lambdaexp\\", NameUtils.lambdaExpName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_PATH);
            FileUtils.saveFile(path+ "domain\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "convertor\\", domainInfo.nameSuffix("Convertor") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_DECORATOR_PATH);
            FileUtils.saveFile(path+ "domain\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "convertor\\", domainInfo.nameSuffix("ConvertorDecorator") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.CONTROLLER_PATH);
            FileUtils.saveFile(path + "controller\\", NameUtils.controllerName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo));
        }

    }
}
