package com.artframework.domain.utils;

import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.generator.domain.DomainGenerator;
import com.artframework.domain.generator.table.TableGenerator;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenerateUtils {

    public static void generateDomains(String path, List<DomainMetaInfo> domainMetaInfoList , Map<String, String> packageParam) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainMetaInfoList) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator();
            domainDtoGenerator.putParam(packageParam);
            domainDtoGenerator.setTemplateFilePath(FTLConstants.DTO_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "domain\\", domainInfo.nameSuffix("Domain") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.FIND_REQUEST_PATH);
            FileUtils.saveFile(path+ "domains\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "domain\\", domainInfo.nameSuffix("FindDomain") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.PAGE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domains\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "domain\\", domainInfo.nameSuffix("PageDomain") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.UPDATE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "domain\\", domainInfo.nameSuffix("UpdateDomain") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.CREATE_REQUEST_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "domain\\", domainInfo.nameSuffix("CreateDomain") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\", domainInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "repository\\impl\\", domainInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "service\\", domainInfo.nameSuffix("Service") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_IMPL_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "service\\impl\\", domainInfo.nameSuffix("ServiceImpl") + ".java", domainDtoGenerator.generate(domainInfo));



            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_LAMBDA_EXP_PATH);
            FileUtils.saveFile(path+ "domains\\" + NameUtils.packageName(domainInfo.getName()) + "\\" + "lambdaexp\\", NameUtils.lambdaExpName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_PATH);
            FileUtils.saveFile(path+ "domains\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "convertor\\", domainInfo.nameSuffix("Convertor") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_DECORATOR_PATH);
            FileUtils.saveFile(path+ "domains\\"+  NameUtils.packageName(domainInfo.getName()) + "\\" + "convertor\\", domainInfo.nameSuffix("ConvertorDecorator") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.CONTROLLER_PATH);
            FileUtils.saveFile(path + "controllers\\", NameUtils.controllerName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo));
        }

    }

    public static void generateTables(String path, List<TableMetaInfo> tableCollection, Map<String, String> packageParam) throws IOException {
        for (TableMetaInfo table : tableCollection) {
            TableGenerator generator = new TableGenerator();
            generator.putParam(packageParam);
            TableInfo tableMetaInfo = TableInfo.covert(table);
            generator.setTemplateFilePath(FTLConstants.TABLE_DO_PATH);
            FileUtils.saveFile(path + "entities\\", NameUtils.dataObjectName(tableMetaInfo.getName())+ ".java", generator.generate(tableMetaInfo));
            if(!tableMetaInfo.getBasic()){
                generator.setTemplateFilePath(FTLConstants.TABLE_MAPPER_PATH);
                FileUtils.saveFile(path + "mappers\\", NameUtils.mapperName(tableMetaInfo.getName()) + ".java", generator.generate(tableMetaInfo));
            }
        }
    }
}
