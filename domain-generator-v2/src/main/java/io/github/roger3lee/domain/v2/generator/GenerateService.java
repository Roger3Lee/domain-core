package io.github.roger3lee.domain.v2.generator;

import cn.hutool.core.bean.BeanUtil;
import io.github.roger3lee.domain.v2.dto.DomainInfo;
import io.github.roger3lee.domain.v2.dto.TableInfo;
import io.github.roger3lee.domain.v2.meta.domain.DomainCollection;
import io.github.roger3lee.domain.v2.meta.domain.DomainMetaInfo;
import io.github.roger3lee.domain.v2.meta.table.ColumnMetaInfo;
import io.github.roger3lee.domain.v2.meta.table.TableMetaInfo;
import io.github.roger3lee.domain.v2.utils.FileUtils;
import io.github.roger3lee.domain.v2.utils.FreeMakerTplUtil;
import io.github.roger3lee.domain.v2.utils.NameUtils;
import io.github.roger3lee.domain.v2.utils.XmlUtils;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GenerateService {

    private static final String CORE_PACKAGE = "io.github.roger3lee.domain.core";
    private static final String BASE_MAPPER_CLASS = "com.baomidou.mybatisplus.core.mapper.BaseMapper";

    private Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();

    /**
     * Load table metadata from SQL DDL file.
     */
    public void loadFromSql(File sqlFile, String dialect) throws IOException {
        String sql = new String(Files.readAllBytes(sqlFile.toPath()), "UTF-8");
        loadFromSql(sql, dialect);
    }

    /**
     * Load table metadata from SQL DDL string.
     */
    public void loadFromSql(String sql, String dialect) {
        List<TableMetaInfo> tables = io.github.roger3lee.domain.v2.parser.SqlDdlParser.parse(sql, dialect);
        if ("postgresql".equalsIgnoreCase(dialect)) {
            tables.forEach(x -> x.setKeyGenerator(false));
        }
        tableMetaInfoMap = tables.stream()
                .collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));
        log.info("Loaded {} tables from SQL", tables.size());
    }

    /**
     * Load domain configuration from XML file.
     */
    public List<DomainMetaInfo> loadDomainConfig(File domainXmlFile) throws IOException, JAXBException {
        String xml = new String(Files.readAllBytes(domainXmlFile.toPath()), "UTF-8");
        return loadDomainConfig(xml);
    }

    /**
     * Load domain configuration from XML string.
     */
    public List<DomainMetaInfo> loadDomainConfig(String domainXml) throws JAXBException {
        DomainCollection collection = XmlUtils.xmlToBean(domainXml, DomainCollection.class);
        if (collection != null && collection.getDomain() != null) {
            log.info("Loaded {} domain definitions", collection.getDomain().size());
            return collection.getDomain();
        }
        return Collections.emptyList();
    }

    /**
     * Get all tables referenced by domain definitions.
     */
    public List<TableMetaInfo> getReferencedTables(List<DomainMetaInfo> domainList) {
        List<TableMetaInfo> result = new ArrayList<>();
        for (DomainMetaInfo domainMetaInfo : domainList) {
            if (tableMetaInfoMap.containsKey(domainMetaInfo.getMainTable())) {
                result.add(tableMetaInfoMap.get(domainMetaInfo.getMainTable()));
            }
            if (domainMetaInfo.getAggregate() != null && tableMetaInfoMap.containsKey(domainMetaInfo.getAggregate().getTable())) {
                result.add(tableMetaInfoMap.get(domainMetaInfo.getAggregate().getTable()));
            }
            domainMetaInfo.getRelatedList().forEach(x -> {
                if (tableMetaInfoMap.containsKey(x.getTable())) {
                    result.add(tableMetaInfoMap.get(x.getTable()));
                }
            });
        }
        return result;
    }

    /**
     * Get all columns for a table (including inherited).
     */
    public List<ColumnMetaInfo> getTableColumns(String tableName) {
        if (tableMetaInfoMap.containsKey(tableName)) {
            return new ArrayList<>(tableMetaInfoMap.get(tableName).getColumn());
        }
        return Collections.emptyList();
    }

    /**
     * Generate all code: tables (DO + Mapper) + domains + controllers.
     */
    public void generateAll(String outputPath, String basePackage,
                            List<DomainMetaInfo> domainList, boolean overwrite) throws IOException {
        String packagePath = outputPath + "/" + basePackage.replace(".", "/");
        Map<String, String> packageParam = buildPackageParam(basePackage);

        // Build DomainInfo list first to extract redundancy column information
        List<DomainInfo> domainInfoList = new ArrayList<>();
        for (DomainMetaInfo domainMetaInfo : domainList) {
            domainInfoList.add(DomainInfo.convert(domainMetaInfo, tableMetaInfoMap));
        }

        // Collect redundancy columns per table for DO generation
        Map<String, List<io.github.roger3lee.domain.v2.dto.ColumnMetaInfo>> redundancyColumnsMap = new HashMap<>();
        for (DomainInfo domainInfo : domainInfoList) {
            for (DomainInfo.RelateTableInfo relateTable : domainInfo.getRelatedTable()) {
                String tableName = relateTable.getTableName();
                for (DomainInfo.TableFK fk : relateTable.getRedundancyList()) {
                    // Find the target column name and type
                    String targetColName = fk.getFkTargetColumn() != null ? fk.getFkTargetColumn().toString() : "";
                    String targetColType = fk.getFkTargetColumnType() != null ? fk.getFkTargetColumnType() : "";
                    if (!targetColName.isEmpty() && !targetColType.isEmpty()) {
                        redundancyColumnsMap.computeIfAbsent(tableName, k -> new ArrayList<>());
                        // Check if already added
                        boolean exists = redundancyColumnsMap.get(tableName).stream()
                                .anyMatch(c -> c.getName().equalsIgnoreCase(targetColName));
                        if (!exists) {
                            io.github.roger3lee.domain.v2.dto.ColumnMetaInfo col = new io.github.roger3lee.domain.v2.dto.ColumnMetaInfo();
                            col.setName(targetColName);
                            col.setType(targetColType);
                            col.setKey(false);
                            col.setInherit(false);
                            redundancyColumnsMap.get(tableName).add(col);
                        }
                    }
                }
            }
        }

        // 1. Generate table entities (DO) and mappers
        List<TableMetaInfo> tableList = getReferencedTables(domainList);
        generateTables(packagePath + "/mappers", packagePath + "/entities", tableList, redundancyColumnsMap, packageParam, overwrite);

        // 2. Generate domain layers
        generateDomains(packagePath + "/domains", domainList, packageParam, overwrite);

        // 3. Generate application layer
        generateApplications(packagePath + "/applications", domainList, packageParam, overwrite);

        // 4. Generate controllers
        generateControllers(packagePath + "/controllers", domainList, packageParam, overwrite);

        log.info("Code generation completed. Output: {}", outputPath);
    }

    public void generateTables(String mapperPath, String entityPath,
                               List<TableMetaInfo> tableList,
                               Map<String, List<io.github.roger3lee.domain.v2.dto.ColumnMetaInfo>> redundancyColumnsMap,
                               Map<String, String> packageParam,
                               boolean overwrite) throws IOException {
        for (TableMetaInfo table : tableList) {
            TableInfo tableInfo = convertTableInfo(table);

            // Add redundancy columns to the DO class
            if (redundancyColumnsMap.containsKey(table.getName())) {
                List<io.github.roger3lee.domain.v2.dto.ColumnMetaInfo> existingColumns = tableInfo.getColumn();
                for (io.github.roger3lee.domain.v2.dto.ColumnMetaInfo redundancyCol : redundancyColumnsMap.get(table.getName())) {
                    boolean exists = existingColumns.stream()
                            .anyMatch(c -> c.getName().equalsIgnoreCase(redundancyCol.getName()));
                    if (!exists) {
                        existingColumns.add(redundancyCol);
                    }
                }
            }
            Map<String, Object> params = buildTemplateParams(packageParam);
            params.put("source", tableInfo);

            // Generate DO
            String doCode = processTemplate("table/template-table.ftl", params);
            FileUtils.saveFile(entityPath, NameUtils.dataObjectName(tableInfo.getName()) + ".java", doCode, overwrite);

            // Generate Mapper (skip for basic tables)
            if (!tableInfo.getBasic()) {
                String mapperCode = processTemplate("table/mapper/template-mapper.ftl", params);
                FileUtils.saveFile(mapperPath, NameUtils.mapperName(tableInfo.getName()) + ".java", mapperCode, overwrite);
            }
        }
    }

    public void generateDomains(String domainPath, List<DomainMetaInfo> domainList,
                                Map<String, String> packageParam, boolean overwrite) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainList) {
            DomainInfo domainInfo = DomainInfo.convert(domainMetaInfo, tableMetaInfoMap);
            Map<String, Object> params = buildTemplateParams(packageParam);
            params.put("source", domainInfo);

            String folder = domainInfo.getFolderPath();

            // Domain DTO
            String dtoCode = processTemplate("domain/domain/template-dto.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/domain",
                    domainInfo.nameSuffix("Domain") + ".java", dtoCode, overwrite);

            // FindDomain
            String findCode = processTemplate("domain/domain/template-find-request.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/domain",
                    NameUtils.getName(domainInfo.getName()) + "FindDomain.java", findCode, overwrite);

            // LambdaExp
            String lambdaCode = processTemplate("domain/lambda-exp/template-lambda-exp.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/lambdaexp",
                    NameUtils.lambdaExpName(domainInfo.getName()) + ".java", lambdaCode, overwrite);

            // Convertor
            String convertorCode = processTemplate("domain/convertor/template-convertor.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/convertor",
                    NameUtils.covertName(domainInfo.getName()) + ".java", convertorCode, overwrite);

            // ConvertorDecorator
            String decoratorCode = processTemplate("domain/convertor/template-convertor-decorator.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/convertor",
                    NameUtils.covertDecoratorName(domainInfo.getName()) + ".java", decoratorCode, overwrite);

            // Main Repository
            String repoCode = processTemplate("domain/repository/template-repository.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/repository",
                    NameUtils.repositoryName(domainInfo.getName()) + ".java", repoCode, overwrite);

            // Main RepositoryImpl
            String repoImplCode = processTemplate("domain/repository/template-repository-impl.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/repository/impl",
                    NameUtils.repositoryImplName(domainInfo.getName()) + ".java", repoImplCode, overwrite);

            // Related repositories
            for (DomainInfo.RelateTableInfo relateTableInfo : domainInfo.getRelatedTable()) {
                Map<String, Object> relatedParams = new HashMap<>(params);
                relatedParams.put("table", relateTableInfo);

                String relatedRepoCode = processTemplate("domain/repository/template-repository-related.ftl", relatedParams);
                FileUtils.saveFile(domainPath + "/" + folder + "/repository",
                        NameUtils.repositoryName(relateTableInfo.getName()) + ".java", relatedRepoCode, overwrite);

                String relatedRepoImplCode = processTemplate("domain/repository/template-repository-impl-related.ftl", relatedParams);
                FileUtils.saveFile(domainPath + "/" + folder + "/repository/impl",
                        NameUtils.repositoryImplName(relateTableInfo.getName()) + ".java", relatedRepoImplCode, overwrite);
            }

            // Service
            String serviceCode = processTemplate("domain/service/template-service.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/service",
                    NameUtils.serviceName(domainInfo.getName()) + ".java", serviceCode, overwrite);

            // ServiceImpl
            String serviceImplCode = processTemplate("domain/service/template-service-impl.ftl", params);
            FileUtils.saveFile(domainPath + "/" + folder + "/service/impl",
                    NameUtils.serviceImplName(domainInfo.getName()) + ".java", serviceImplCode, overwrite);
        }
    }

    public void generateApplications(String applicationPath, List<DomainMetaInfo> domainList,
                                     Map<String, String> packageParam, boolean overwrite) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainList) {
            DomainInfo domainInfo = DomainInfo.convert(domainMetaInfo, tableMetaInfoMap);
            Map<String, Object> params = buildTemplateParams(packageParam);
            params.put("source", domainInfo);

            // AppService interface
            String appServiceCode = processTemplate("application/template-application.ftl", params);
            FileUtils.saveFile(applicationPath,
                    NameUtils.appServiceName(domainInfo.getName()) + ".java", appServiceCode, overwrite);

            // AppServiceImpl
            String appServiceImplCode = processTemplate("application/template-application-impl.ftl", params);
            FileUtils.saveFile(applicationPath + "/impl",
                    NameUtils.appServiceImplName(domainInfo.getName()) + ".java", appServiceImplCode, overwrite);
        }
    }

    public void generateControllers(String controllerPath, List<DomainMetaInfo> domainList,
                                    Map<String, String> packageParam, boolean overwrite) throws IOException {
        for (DomainMetaInfo domainMetaInfo : domainList) {
            DomainInfo domainInfo = DomainInfo.convert(domainMetaInfo, tableMetaInfoMap);
            Map<String, Object> params = buildTemplateParams(packageParam);
            params.put("source", domainInfo);

            String controllerCode = processTemplate("controller/template-controller.ftl", params);
            FileUtils.saveFile(controllerPath,
                    NameUtils.controllerName(domainInfo.getName()) + ".java", controllerCode, overwrite);
        }
    }

    // ---- Helper methods ----

    private TableInfo convertTableInfo(TableMetaInfo tableMetaInfo) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(tableMetaInfo.getName());
        tableInfo.setBasic(tableMetaInfo.getBasic());
        tableInfo.setInherit(tableMetaInfo.getInherit());
        tableInfo.setKeyGenerator(tableMetaInfo.getKeyGenerator());

        // Convert meta columns to DTO columns
        List<io.github.roger3lee.domain.v2.dto.ColumnMetaInfo> columns = tableMetaInfo.getColumn().stream()
                .map(this::convertColumn)
                .collect(Collectors.toList());
        tableInfo.setColumn(columns);

        // Find key column
        io.github.roger3lee.domain.v2.dto.ColumnMetaInfo keyCol = columns.stream()
                .filter(io.github.roger3lee.domain.v2.dto.ColumnMetaInfo::getKey)
                .findFirst().orElse(null);
        if (keyCol != null) {
            tableInfo.setKeyType(keyCol.getType());
            tableInfo.setKeyName(NameUtils.getFieldName(keyCol.getName()));
        }
        return tableInfo;
    }

    private io.github.roger3lee.domain.v2.dto.ColumnMetaInfo convertColumn(ColumnMetaInfo meta) {
        io.github.roger3lee.domain.v2.dto.ColumnMetaInfo dto = new io.github.roger3lee.domain.v2.dto.ColumnMetaInfo();
        dto.setName(meta.getName());
        dto.setType(meta.getType());
        dto.setComment(meta.getComment());
        dto.setKey(meta.getKey());
        dto.setInherit(meta.getInherit());
        return dto;
    }

    private Map<String, String> buildPackageParam(String basePackage) {
        Map<String, String> param = new HashMap<>();
        param.put("tablePackage", basePackage + ".entities");
        param.put("mapperPackage", basePackage + ".mappers");
        param.put("domainPackage", basePackage + ".domains");
        param.put("applicationPackage", basePackage + ".applications");
        param.put("controllerPackage", basePackage + ".controllers");
        return param;
    }

    private Map<String, Object> buildTemplateParams(Map<String, String> packageParam) {
        Map<String, Object> params = new HashMap<>();
        params.putAll(packageParam);
        params.put("corePackage", CORE_PACKAGE);
        params.put("baseMapperClass", BASE_MAPPER_CLASS);
        return params;
    }

    private String processTemplate(String templatePath, Map<String, Object> params) {
        try (InputStream is = GenerateService.class.getClassLoader().getResourceAsStream(templatePath)) {
            if (is == null) {
                throw new IOException("Template not found: " + templatePath);
            }
            String template = readStream(is);
            return FreeMakerTplUtil.process(template, params);
        } catch (IOException e) {
            log.error("Failed to process template: {}", templatePath, e);
            return "";
        }
    }

    private static String readStream(InputStream is) throws IOException {
        java.io.ByteArrayOutputStream result = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
