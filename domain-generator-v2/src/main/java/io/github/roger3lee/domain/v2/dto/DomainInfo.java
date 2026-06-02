package io.github.roger3lee.domain.v2.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.github.roger3lee.domain.v2.meta.domain.DomainMetaInfo;
import io.github.roger3lee.domain.v2.meta.domain.RefTableMetaInfo;
import io.github.roger3lee.domain.v2.meta.domain.RelatedTableMetaInfo;
import io.github.roger3lee.domain.v2.meta.table.ColumnMetaInfo;
import io.github.roger3lee.domain.v2.meta.table.TableMetaInfo;
import io.github.roger3lee.domain.v2.utils.NameUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Builder
public class DomainInfo {
    private String folder;
    private String name;
    private String description;
    private String implement;
    private TableInfo mainTable;
    private List<RelateTableInfo> relatedTable;

    public String getFolder() {
        return StringUtils.isNotEmpty(this.folder) ? StrUtil.replace(this.folder, "/", ".") : NameUtils.packageName(this.getName());
    }

    public String getFolderPath() {
        return StringUtils.isNotEmpty(this.folder) ? this.folder : NameUtils.packageName(this.getName());
    }

    public String nameSuffix(String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.name))) + suffix;
    }

    public static DomainInfo convert(DomainMetaInfo domainMetaInfo, Map<String, TableMetaInfo> tableMetaInfoMap) {
        TableInfo tableInfo = TableInfo.convert(domainMetaInfo.getMainTable(), tableMetaInfoMap);
        return DomainInfo.builder()
                .folder(domainMetaInfo.getFolder())
                .implement(domainMetaInfo.getImplement())
                .name(domainMetaInfo.getName())
                .description(domainMetaInfo.getDescription())
                .mainTable(tableInfo)
                .relatedTable(domainMetaInfo.getRelatedList().stream()
                        .map(x -> RelateTableInfo.convert(x, tableInfo, tableMetaInfoMap))
                        .collect(Collectors.toList()))
                .build();
    }

    public List<RelateTableInfo> getRelatedTableDistinct() {
        if (CollectionUtil.isEmpty(relatedTable)) {
            return relatedTable;
        }
        return relatedTable.stream().filter(distinctByKey(TableInfo::getTableName)).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RelateTableInfo extends TableInfo {
        private Boolean many;
        private List<TableFK> fkList = new ArrayList<>();
        private List<TableFK> redundancyList = new ArrayList<>();
        private List<RefTable> refTableList;

        public static RelateTableInfo convert(RelatedTableMetaInfo relatedTableMetaInfo, TableInfo mainTable, Map<String, TableMetaInfo> tableMetaInfoMap) {
            if (ObjectUtil.isNull(relatedTableMetaInfo)) {
                return null;
            }
            TableMetaInfo table = tableMetaInfoMap.get(relatedTableMetaInfo.getTable());
            RelateTableInfo tableInfo = new RelateTableInfo();
            tableInfo.setImplement(relatedTableMetaInfo.getImplement());
            tableInfo.setDeletable(relatedTableMetaInfo.getDeletable());
            tableInfo.setName(StrUtil.isEmpty(relatedTableMetaInfo.getName()) ? table.getName() : relatedTableMetaInfo.getName());
            tableInfo.setComment(table.getComment());
            tableInfo.setTableName(relatedTableMetaInfo.getTable());
            tableInfo.setColumn(convertColumns(tableMetaInfoMap, relatedTableMetaInfo.getTable()));
            // Add redundancy columns to the column list
            if (StringUtils.isNotEmpty(relatedTableMetaInfo.getRedundancy())) {
                addRedundancyColumns(relatedTableMetaInfo.getRedundancy(), tableInfo.getColumn(), mainTable.getColumn());
            }
            ColumnMetaInfo keyColumn = tableInfo.getColumn().stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(null);
            if (keyColumn != null) {
                tableInfo.setKeyType(keyColumn.getType());
                tableInfo.setKeyColName(NameUtils.getFieldName(keyColumn.getName()));
            }

            tableInfo.setMany(relatedTableMetaInfo.getMany());
            if (StringUtils.isNotEmpty(relatedTableMetaInfo.getFk())) {
                tableInfo.setFkList(getFK(relatedTableMetaInfo.getFk(), tableInfo.getColumn(), mainTable.getColumn()));
            }
            if (StringUtils.isNotEmpty(relatedTableMetaInfo.getRedundancy())) {
                tableInfo.setRedundancyList(getFK(relatedTableMetaInfo.getRedundancy(), tableInfo.getColumn(), mainTable.getColumn()));
            }
            if (CollectionUtil.isNotEmpty(relatedTableMetaInfo.getRefList())) {
                tableInfo.setRefTableList(relatedTableMetaInfo.getRefList().stream()
                        .map(x -> convertRef(x, tableInfo, tableMetaInfoMap))
                        .collect(Collectors.toList()));
            }
            return tableInfo;
        }

        public static RefTable convertRef(RefTableMetaInfo refMeta, TableInfo mainTable, Map<String, TableMetaInfo> tableMetaInfoMap) {
            RefTable refTable = RefTable.builder()
                    .name(StrUtil.isEmpty(refMeta.getName()) ? refMeta.getTable() : refMeta.getName())
                    .tableName(refMeta.getTable())
                    .many(refMeta.getMany())
                    .build();
                refTable.setColumn(convertColumns(tableMetaInfoMap, refMeta.getTable()));
                // Add redundancy columns to the ref table column list
                if (StringUtils.isNotEmpty(refMeta.getRedundancy())) {
                    addRedundancyColumns(refMeta.getRedundancy(), refTable.getColumn(), mainTable.getColumn());
                }
                refTable.setFkList(getFK(refMeta.getFk(), refTable.getColumn(), mainTable.getColumn()));
            if (StringUtils.isNotEmpty(refMeta.getRedundancy())) {
                refTable.setRedundancyList(getFK(refMeta.getRedundancy(), refTable.getColumn(), mainTable.getColumn()));
            }
            return refTable;
        }

        private static List<ColumnMetaInfo> convertColumns(Map<String, TableMetaInfo> tableMetaInfoMap, String tableName) {
            if (!tableMetaInfoMap.containsKey(tableName)) {
                return Collections.emptyList();
            }
            TableMetaInfo table = tableMetaInfoMap.get(tableName);
            List<ColumnMetaInfo> result = new ArrayList<>();
            for (io.github.roger3lee.domain.v2.meta.table.ColumnMetaInfo col : table.getColumn()) {
                ColumnMetaInfo dto = new ColumnMetaInfo();
                dto.setName(col.getName());
                dto.setType(col.getType());
                dto.setComment(col.getComment());
                dto.setKey(col.getKey());
                dto.setInherit(col.getInherit());
                result.add(dto);
            }
            return result;
        }

        /**
         * Add redundancy columns from the domain XML to the related table's column list.
         * Redundancy format: name:family_name (source:target)
         * The target column gets the type from the source column.
         */
        private static void addRedundancyColumns(String redundancyString, List<ColumnMetaInfo> targetColumns, List<ColumnMetaInfo> mainColumns) {
            if (StringUtils.isEmpty(redundancyString)) return;
            String[] strings = redundancyString.split("\\|");
            for (String redundancy : strings) {
                String[] parts = redundancy.split(":");
                if (parts.length != 2) continue;
                String sourceColName = parts[0];
                String targetColName = parts[1];

                // Check if target column already exists
                boolean exists = targetColumns.stream()
                        .anyMatch(x -> StringUtils.equalsIgnoreCase(x.getName(), targetColName));
                if (exists) continue;

                // Find source column type from main table
                ColumnMetaInfo sourceColumn = mainColumns.stream()
                        .filter(x -> StringUtils.equalsIgnoreCase(x.getName(), sourceColName))
                        .findFirst().orElse(null);
                if (sourceColumn == null) continue;

                ColumnMetaInfo redundancyCol = new ColumnMetaInfo();
                redundancyCol.setName(targetColName);
                redundancyCol.setType(sourceColumn.getType());
                redundancyCol.setComment(sourceColumn.getComment());
                redundancyCol.setKey(false);
                redundancyCol.setInherit(false);
                targetColumns.add(redundancyCol);
            }
        }

        private static List<TableFK> getFK(String fkString, List<ColumnMetaInfo> targetColumns, List<ColumnMetaInfo> mainColumns) {
            if (StringUtils.isEmpty(fkString)) return new ArrayList<>();
            String[] strings = fkString.split("\\|");
            List<TableFK> fkList = new ArrayList<>();
            for (String fk : strings) {
                String[] fkMap = fk.split(":");
                if (fkMap.length != 2) continue;
                Tuple target = getColumnInfo(fkMap[1]);
                Tuple source = getColumnInfo(fkMap[0]);

                String targetType = targetColumns.stream()
                        .filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), target.get(0).toString()))
                        .map(ColumnMetaInfo::getType).findFirst().orElse(null);

                TableFK refTableFK = new TableFK();
                ColumnMetaInfo sourceColumn = mainColumns.stream()
                        .filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), source.get(0).toString()))
                        .findFirst().orElse(null);

                // For redundancy columns, if target column doesn't exist in table, use source column type
                if (targetType == null) {
                    targetType = sourceColumn != null ? sourceColumn.getType() : "";
                }
                if (null != sourceColumn) {
                    refTableFK.setFkSourceColumn(source.get(0));
                    refTableFK.setFkSourceColumnType(sourceColumn.getType());
                } else {
                    if (source.get(0).toString().contains(".") || targetType.equals("String")) {
                        refTableFK.setSourceValue(source.get(0));
                    } else {
                        continue;
                    }
                }
                refTableFK.setFkTargetColumn(target.get(0));
                refTableFK.setFkTargetColumnType(targetType);
                refTableFK.setFkSourceConvertMethod(source.get(1));
                refTableFK.setFkTargetConvertMethod(target.get(1));
                fkList.add(refTableFK);
            }
            return fkList;
        }

        public static Tuple getColumnInfo(String value) {
            Pattern p = Pattern.compile("\\(([^)]+)\\)");
            Matcher m = p.matcher(value);
            if (m.find()) {
                String insideBrackets = m.group(1);
                return new Tuple(value.substring(0, value.indexOf("(")), insideBrackets);
            }
            return new Tuple(value, "");
        }

        public String nameSuffix(String suffix) {
            return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.getName()))) + suffix;
        }
    }

    @Data
    @Builder
    public static class RefTable {
        private String name;
        private String tableName;
        private Boolean many;
        private List<TableFK> fkList;
        private List<TableFK> redundancyList = new ArrayList<>();
        private List<ColumnMetaInfo> column;
    }

    @Data
    public static class TableInfo {
        private String name;
        private String comment;
        private String tableName;
        private String implement;
        private Boolean deletable;
        private List<ColumnMetaInfo> column;
        private String keyType;
        private String keyColName;

        public static TableInfo convert(String tableName, Map<String, TableMetaInfo> tableMetaInfoMap) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName(tableName);
            if (tableMetaInfoMap.containsKey(tableName)) {
                TableMetaInfo meta = tableMetaInfoMap.get(tableName);
                tableInfo.setColumn(RelateTableInfo.convertColumns(tableMetaInfoMap, tableName));
                tableInfo.setComment(meta.getComment());
                ColumnMetaInfo keyColumn = tableInfo.column.stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(null);
                if (keyColumn != null) {
                    tableInfo.setKeyType(keyColumn.getType());
                    tableInfo.setKeyColName(NameUtils.getFieldName(keyColumn.getName()));
                }
            }
            return tableInfo;
        }
    }

    @Data
    public static class TableFK {
        private String fkSourceColumnType;
        private String fkSourceColumn;
        private String fkSourceConvertMethod;
        private String fkTargetColumnType;
        private String fkTargetColumn;
        private String fkTargetConvertMethod;
        private String sourceValue;
    }
}
