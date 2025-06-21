package com.artframework.domain.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.artframework.domain.datasource.TableQuery;
import com.artframework.domain.meta.domain.DomainCollection;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableCollection;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.StreamUtils;
import com.artframework.domain.utils.XmlUtils;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class GlobalSetting {
    public static GlobalSetting INSTANCE = new GlobalSetting();

    public Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();
    private List<DomainMetaInfo> domainMetaInfoList = new ArrayList<>();

    public List<DomainMetaInfo> getDomainList() {
        return domainMetaInfoList;
    }

    public List<TableMetaInfo> getTableList() {
        if (INSTANCE.getDomainList().size() > 0) {
            List<TableMetaInfo> tableMetaInfos = new ArrayList<>();
            for (DomainMetaInfo domainMetaInfo : INSTANCE.getDomainList()) {
                if (INSTANCE.tableMetaInfoMap.containsKey(domainMetaInfo.getMainTable())) {
                    tableMetaInfos.add(INSTANCE.tableMetaInfoMap.get(domainMetaInfo.getMainTable()));
                }
                if (null != domainMetaInfo.getAggregate()
                        && INSTANCE.tableMetaInfoMap.containsKey(domainMetaInfo.getAggregate().getTable())) {
                    tableMetaInfos.add(INSTANCE.tableMetaInfoMap.get(domainMetaInfo.getAggregate().getTable()));
                }

                domainMetaInfo.getRelatedList().forEach(x -> {
                    if (INSTANCE.tableMetaInfoMap.containsKey(x.getTable())) {
                        tableMetaInfos.add(INSTANCE.tableMetaInfoMap.get(x.getTable()));
                    }
                });
            }
            return tableMetaInfos;
        } else {
            return new ArrayList<>(INSTANCE.tableMetaInfoMap.values());
        }
    }

    public List<ColumnMetaInfo> getTableColumns(String tableName) {
        if (INSTANCE.tableMetaInfoMap.containsKey(tableName)) {
            TableMetaInfo tableMetaInfo = INSTANCE.tableMetaInfoMap.get(tableName);
            List<ColumnMetaInfo> columnList = new ArrayList<>(tableMetaInfo.getColumn());
            Map<String, ColumnMetaInfo> columns = tableMetaInfo.getColumn().stream()
                    .collect(Collectors.toMap(ColumnMetaInfo::getName, x -> x, (x, y) -> x));

            // 有继承关系
            if (StrUtil.isNotBlank(tableMetaInfo.getInherit())) {
                List<ColumnMetaInfo> columnMetaInfos = getTableColumns(tableMetaInfo.getInherit());
                for (ColumnMetaInfo item : columnMetaInfos) {
                    if (!columns.containsKey(item.getName())) {
                        columnList.add(item);
                    }
                }
            }
            return columnList;
        }
        return Collections.emptyList();
    }

    public void setTableInfos(List<TableInfo> tableInfos) {
        List<TableMetaInfo> tableMetaInfos = tableInfos.stream().map(GlobalSetting::convert)
                .collect(Collectors.toList());
        INSTANCE.tableMetaInfoMap = tableMetaInfos.stream()
                .collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));
    }

    public static void loadDomainConfig(File domainFile) throws JAXBException, IOException {
        DomainCollection domainCollection = XmlUtils
                .xmlToBean(StreamUtils.readAsString(Files.newInputStream(domainFile.toPath())), DomainCollection.class);
        if (ObjectUtil.isNotEmpty(domainCollection)) {
            INSTANCE.domainMetaInfoList = domainCollection.getDomain();
        }
    }

    public static void load(String tableXml, String domainXml) throws JAXBException {
        INSTANCE = new GlobalSetting();
        TableCollection tableCollection = XmlUtils.xmlToBean(tableXml, TableCollection.class);
        DomainCollection domainCollection = XmlUtils.xmlToBean(domainXml, DomainCollection.class);

        INSTANCE.domainMetaInfoList = domainCollection.getDomain();
        INSTANCE.tableMetaInfoMap = tableCollection.getTables().stream()
                .collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));
    }

    public static void load(File tableFile, File domainFile) throws IOException, JAXBException {
        load(StreamUtils.readAsString(Files.newInputStream(tableFile.toPath())),
                StreamUtils.readAsString(Files.newInputStream(domainFile.toPath())));
    }

    public static void loadFromDB(DataSourceConfig dataSourceConfig, File domainFile)
            throws IOException, JAXBException {

        TableQuery tableQuery = new TableQuery(dataSourceConfig);
        List<TableInfo> tableInfos = tableQuery.queryTables();
        List<TableMetaInfo> tableMetaInfos = tableInfos.stream().map(GlobalSetting::convert)
                .collect(Collectors.toList());
        if (dataSourceConfig.getDbType().equals(DbType.POSTGRE_SQL)
                || dataSourceConfig.getDbType().equals(DbType.ORACLE)) {
            tableMetaInfos.forEach(x -> x.setKeyGenerator(false));
        }
        INSTANCE.tableMetaInfoMap = tableMetaInfos.stream()
                .collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));
        DomainCollection domainCollection = XmlUtils
                .xmlToBean(StreamUtils.readAsString(Files.newInputStream(domainFile.toPath())), DomainCollection.class);
        if (ObjectUtil.isNotEmpty(domainCollection)) {
            INSTANCE.domainMetaInfoList = domainCollection.getDomain();
        }
    }

    public static TableMetaInfo convert(TableInfo tableInfo) {
        TableMetaInfo table = new TableMetaInfo();
        table.setName(tableInfo.getName());
        List<ColumnMetaInfo> columnMetaInfos = new ArrayList<>();

        Set<String> colset = new HashSet<>();
        for (TableField tableField : tableInfo.getFields()) {
            if (colset.contains(tableField.getName())) {
                continue;
            } else {
                colset.add(tableField.getName());
            }
            ColumnMetaInfo column = new ColumnMetaInfo();
            column.setKey(tableField.isKeyFlag());
            column.setComment(tableField.getComment());
            column.setName(tableField.getName());
            if (StrUtil.isNotBlank(tableField.getColumnType().getPkg())) {
                column.setType(tableField.getColumnType().getPkg());
            } else {
                column.setType(tableField.getColumnType().getType());
            }

            columnMetaInfos.add(column);
        }
        table.setColumn(columnMetaInfos);
        return table;
    }
}
