package com.artframework.domain.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.artframework.domain.datasource.TableQuery;
import com.artframework.domain.meta.domain.DomainCollection;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableCollection;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.StreamUtils;
import com.artframework.domain.utils.XmlUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class GlobalSetting {
    public static GlobalSetting INSTANCE = new GlobalSetting();

    private Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();
    private List<DomainMetaInfo> domainMetaInfoList = new ArrayList<>();


    public List<DomainMetaInfo> getDomainList() {
        return domainMetaInfoList;
    }

    public List<TableMetaInfo> getTableList() {
        return new ArrayList<>(tableMetaInfoMap.values());
    }

    public List<ColumnMetaInfo> getTableColumns(String tableName) {
        if (INSTANCE.tableMetaInfoMap.containsKey(tableName)) {
            TableMetaInfo tableMetaInfo = INSTANCE.tableMetaInfoMap.get(tableName);
            List<ColumnMetaInfo> columnList = new ArrayList<>(tableMetaInfo.getColumn());
            Map<String, ColumnMetaInfo> columns = tableMetaInfo.getColumn().stream()
                    .collect(Collectors.toMap(ColumnMetaInfo::getName, x -> x, (x, y) -> x));

            //有继承关系
            if (StringUtils.isNoneBlank(tableMetaInfo.getInherit())) {
                List<ColumnMetaInfo> columnMetaInfos = getTableColumns(tableMetaInfo.getInherit());
                for (ColumnMetaInfo item:columnMetaInfos) {
                    if(!columns.containsKey(item.getName())){
                        columnList.add(item);
                    }
                }
            }
            return columnList;
        }
        return Collections.emptyList();
    }

    public static void load(String tableXml, String domainXml) throws JAXBException {
        INSTANCE = new GlobalSetting();
        TableCollection tableCollection = XmlUtils.xmlToBean(tableXml, TableCollection.class);
        DomainCollection domainCollection = XmlUtils.xmlToBean(domainXml, DomainCollection.class);

        INSTANCE.domainMetaInfoList = domainCollection.getDomain();
        INSTANCE.tableMetaInfoMap = tableCollection.getTables().stream().collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));
    }

    public static void load(File tableFile, File domainFile) throws IOException, JAXBException {
        load(StreamUtils.readAsString(Files.newInputStream(tableFile.toPath())),StreamUtils.readAsString(Files.newInputStream(domainFile.toPath())));
    }

    public static void loadFromDB(DataSourceConfig dataSourceConfig, File domainFile) throws IOException, JAXBException {

        TableQuery tableQuery = new TableQuery(dataSourceConfig);
        List<TableInfo> tableInfos = tableQuery.queryTables();
        INSTANCE.tableMetaInfoMap = tableInfos.stream().map(GlobalSetting::convert).collect(Collectors.toMap(TableMetaInfo::getName, x -> x, (x, y) -> x));

        DomainCollection domainCollection = XmlUtils.xmlToBean(StreamUtils.readAsString(Files.newInputStream(domainFile.toPath())), DomainCollection.class);
       if(ObjectUtil.isNotEmpty(domainCollection)){
           INSTANCE.domainMetaInfoList = domainCollection.getDomain();
       }
    }

    public static TableMetaInfo convert(TableInfo tableInfo) {
        TableMetaInfo table = new TableMetaInfo();
        table.setName(tableInfo.getName());
        List<ColumnMetaInfo> columnMetaInfos = new ArrayList<>();
        for (TableField tableField : tableInfo.getFields()) {
            ColumnMetaInfo column = new ColumnMetaInfo();
            column.setKey(tableField.isKeyFlag());
            column.setComment(tableField.getComment());
            column.setName(tableField.getName());
            if(StringUtils.isNoneBlank(tableField.getColumnType().getPkg())){
                if(tableField.getColumnType().getPkg().equals("java.time.LocalDateTime")){
                    column.setType("java.util.Date");
                }else{
                    column.setType(tableField.getColumnType().getPkg());
                }
            }else{
                column.setType(tableField.getColumnType().getType());
            }
            if(tableField.isKeyFlag()){
                column.setKeyGenerator(true);
            }
            columnMetaInfos.add(column);
        }
        table.setColumn(columnMetaInfos);
        return table;
    }
}
