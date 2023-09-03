package com.artframework.domain.config;

import com.artframework.domain.meta.domain.DomainCollection;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableCollection;
import com.artframework.domain.meta.table.TableMetaInfo;
import com.artframework.domain.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBException;
import java.util.*;
import java.util.stream.Collectors;

public class GlobalSetting {
    public static GlobalSetting INSTANCE = new GlobalSetting();

    private Map<String, TableMetaInfo> tableMetaInfoMap = new HashMap<>();
    private List<DomainMetaInfo> domainMetaInfoList = new ArrayList<>();


    public List<DomainMetaInfo> getDomainList() {
        return domainMetaInfoList;
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

}
