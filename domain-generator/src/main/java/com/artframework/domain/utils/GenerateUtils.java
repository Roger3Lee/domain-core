package com.artframework.domain.utils;

import com.artframework.domain.constants.FTLConstants;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.generator.domain.DomainGenerator;
import com.artframework.domain.generator.table.TableGenerator;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GenerateUtils {

    private static String getDomainFolderName(DomainInfo domainInfo) {
        return domainInfo.getFolderPath();
    }

    public static void generateDomains(String path, List<DomainMetaInfo> domainMetaInfoList, Map<String, String> packageParam, Boolean overWrite) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainMetaInfoList) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator();
            domainDtoGenerator.putParam(packageParam);
            domainDtoGenerator.setTemplateFilePath(FTLConstants.DTO_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "domain" + File.separator, domainInfo.nameSuffix("Domain") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.FIND_REQUEST_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "domain" + File.separator, domainInfo.nameSuffix("FindDomain") + ".java", domainDtoGenerator.generate(domainInfo));

//            domainDtoGenerator.setTemplateFilePath(FTLConstants.PAGE_REQUEST_PATH);
//            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "domain"+File.separator, domainInfo.nameSuffix("PageDomain") + ".java", domainDtoGenerator.generate(domainInfo));


//            domainDtoGenerator.setTemplateFilePath(FTLConstants.UPDATE_REQUEST_PATH);
//            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "domain"+File.separator, domainInfo.nameSuffix("UpdateDomain") + ".java", domainDtoGenerator.generate(domainInfo));
//
//            domainDtoGenerator.setTemplateFilePath(FTLConstants.CREATE_REQUEST_PATH);
//            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "domain"+File.separator, domainInfo.nameSuffix("CreateDomain") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository" + File.separator, domainInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);

            domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository"+File.separator+"impl" + File.separator, domainInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);

            for (DomainInfo.RelateTableInfo relateTableInfo : domainInfo.getRelatedTableDistinct()) {
                domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_RELATED_PATH);
                domainDtoGenerator.putParam("table", relateTableInfo);
                FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository" + File.separator, relateTableInfo.nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);

                domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_RELATED_PATH);
                FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository"+File.separator+"impl" + File.separator, relateTableInfo.nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);
            }
            if (domainInfo.getAggregate() != null) {
                domainDtoGenerator.putParam("table", domainInfo.getAggregate());
                domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_RELATED_PATH);
                FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository" + File.separator, domainInfo.getAggregate().nameSuffix("Repository") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);

                domainDtoGenerator.setTemplateFilePath(FTLConstants.REPOSITORY_IMPL_RELATED_PATH);
                FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "repository"+File.separator+"impl" + File.separator, domainInfo.getAggregate().nameSuffix("RepositoryImpl") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);
            }

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "service" + File.separator, domainInfo.nameSuffix("Service") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_SERVICE_IMPL_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "service"+File.separator+"impl" + File.separator, domainInfo.nameSuffix("ServiceImpl") + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_LAMBDA_EXP_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "lambdaexp" + File.separator, NameUtils.lambdaExpName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo));


            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "convertor" + File.separator, domainInfo.nameSuffix("Convertor") + ".java", domainDtoGenerator.generate(domainInfo));

            domainDtoGenerator.setTemplateFilePath(FTLConstants.DOMAIN_CONVERTOR_DECORATOR_PATH);
            FileUtils.saveFile(path + File.separator + getDomainFolderName(domainInfo) + File.separator + "convertor" + File.separator, domainInfo.nameSuffix("ConvertorDecorator") + ".java", domainDtoGenerator.generate(domainInfo), null != overWrite ? overWrite : false);
        }
    }

    public static void generateController(String controllerPath, List<DomainMetaInfo> domainMetaInfoList, Map<String, String> packageParam, Boolean overWrite) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainMetaInfoList) {
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            DomainGenerator domainDtoGenerator = new DomainGenerator();
            domainDtoGenerator.putParam(packageParam);
            domainDtoGenerator.setTemplateFilePath(FTLConstants.CONTROLLER_PATH);
            FileUtils.saveFile(controllerPath, NameUtils.controllerName(domainInfo.getName()) + ".java", domainDtoGenerator.generate(domainInfo), overWrite);
        }
    }

    public static void generateTables(String daoPath, String doPath, List<TableMetaInfo> tableCollection, Map<String, String> packageParam, boolean overWriteDO, boolean overWriteDao) throws IOException {
        for (TableMetaInfo table : tableCollection) {
            TableGenerator generator = new TableGenerator();
            generator.putParam(packageParam);
            TableInfo tableMetaInfo = TableInfo.covert(table);
            generator.setTemplateFilePath(FTLConstants.TABLE_DO_PATH);
            FileUtils.saveFile(doPath, NameUtils.dataObjectName(tableMetaInfo.getName()) + ".java", generator.generate(tableMetaInfo), overWriteDO);
            if (!tableMetaInfo.getBasic()) {
                generator.setTemplateFilePath(FTLConstants.TABLE_MAPPER_PATH);
                FileUtils.saveFile(daoPath, NameUtils.mapperName(tableMetaInfo.getName()) + ".java", generator.generate(tableMetaInfo), overWriteDao);
            }
        }
    }
}
