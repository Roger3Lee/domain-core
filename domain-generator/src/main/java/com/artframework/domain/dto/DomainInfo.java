package com.artframework.domain.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.domain.RefTableMetaInfo;
import com.artframework.domain.meta.domain.RelatedTableMetaInfo;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.utils.NameUtils;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Builder
public class DomainInfo {
    /**
     * 文件夾
     */
    private String folder;
    private String name;
    private String description;
    private String implement;

    private TableInfo mainTable;

    private List<RelateTableInfo> relatedTable;
    private RelateTableInfo aggregate;

    public String getFolder() {
        return StringUtils.isNotEmpty(this.folder) ? StrUtil.replace(this.folder, "/", ".") : NameUtils.packageName(this.getName());
    }

    public String getFolderPath() {
        return StringUtils.isNotEmpty(this.folder) ? this.folder: NameUtils.packageName(this.getName());
    }

    public String nameSuffix(String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.name))) + suffix;
    }

    public static DomainInfo covert(DomainMetaInfo domainMetaInfo) {
        TableInfo tableInfo = TableInfo.convert(domainMetaInfo.getMainTable());
        return DomainInfo
                .builder()
                .folder(domainMetaInfo.getFolder())
                .implement(domainMetaInfo.getImplement())
                .name(domainMetaInfo.getName())
                .description(domainMetaInfo.getDescription())
                .mainTable(tableInfo)
                .relatedTable(domainMetaInfo.getRelatedList().stream().map(x -> RelateTableInfo.convert(x, tableInfo)).collect(Collectors.toList()))
                .aggregate(RelateTableInfo.convert(domainMetaInfo.getAggregate(), tableInfo))
                .build();
    }

    public List<RelateTableInfo> getRelatedTableDistinct() {
        if(CollectionUtil.isEmpty(relatedTable)){
            return relatedTable;
        }

        return relatedTable.stream().filter(distinctByKey(TableInfo::getTableName)).collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class RelateTableInfo extends TableInfo {
        private Boolean many;
        private String fkSourceColumnType;
        private String fkSourceColumn;
        private String fkTargetColumnType;
        private String fkTargetColumn;
        private List<TableFK> otherFkList = new ArrayList<>();
        /**
         * 關聯表列表
         */
        private List<RefTable> refTableList;

        public static RelateTableInfo convert(RelatedTableMetaInfo relatedTableMetaInfo, TableInfo mainTable) {
            if (ObjectUtil.isNull(relatedTableMetaInfo)) {
                return null;
            }
            RelateTableInfo tableInfo = new RelateTableInfo();
            tableInfo.setImplement(relatedTableMetaInfo.getImplement());
            tableInfo.setDeletable(relatedTableMetaInfo.getDeletable());
            tableInfo.setName(StrUtil.isEmpty(relatedTableMetaInfo.getName()) ? relatedTableMetaInfo.getTable() : relatedTableMetaInfo.getName());
            tableInfo.setTableName(relatedTableMetaInfo.getTable());
            tableInfo.setColumn(GlobalSetting.INSTANCE.getTableColumns(relatedTableMetaInfo.getTable()));
            ColumnMetaInfo keyColumn = tableInfo.getColumn().stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(null);
            if (keyColumn != null) {
                tableInfo.setKeyType(keyColumn.getType());
                tableInfo.setKeyColName(keyColumn.getName());
            }

            tableInfo.setMany(relatedTableMetaInfo.getMany());
            String[] strings = relatedTableMetaInfo.getFk().split(":");
            tableInfo.setFkSourceColumn(strings[0]);
            tableInfo.setFkSourceColumnType(mainTable.getColumn().stream().filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), strings[0]))
                    .map(ColumnMetaInfo::getType).findFirst().orElse(""));
            tableInfo.setFkTargetColumn(strings[1]);
            tableInfo.setFkTargetColumnType(tableInfo.getColumn().stream().filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), strings[1]))
                    .map(ColumnMetaInfo::getType).findFirst().orElse(""));

            if (StringUtils.isNotEmpty(relatedTableMetaInfo.getOtherFk())) {
                tableInfo.setOtherFkList(getFK(relatedTableMetaInfo.getOtherFk(), tableInfo.getColumn(), mainTable.getColumn()));
            }

            if (CollectionUtil.isNotEmpty(relatedTableMetaInfo.getRefList())) {
                tableInfo.setRefTableList(relatedTableMetaInfo.getRefList().stream().map(x -> RelateTableInfo.convertRef(x, tableInfo))
                        .collect(Collectors.toList()));
            }
            return tableInfo;
        }

        public static RefTable convertRef(RefTableMetaInfo relatedTableMetaInfo, TableInfo mainTable) {
            RefTable tableInfo = RefTable.builder()
                    .name(StrUtil.isEmpty(relatedTableMetaInfo.getName()) ? relatedTableMetaInfo.getTable() : relatedTableMetaInfo.getName())
                    .tableName(relatedTableMetaInfo.getTable())
                    .many(relatedTableMetaInfo.getMany())
                    .build();
            tableInfo.setColumn(GlobalSetting.INSTANCE.getTableColumns(relatedTableMetaInfo.getTable()));
            tableInfo.setFkList(getFK(relatedTableMetaInfo.getFk(), tableInfo.getColumn(), mainTable.getColumn()));
            return tableInfo;
        }

        private static List<TableFK> getFK(String fkString, List<ColumnMetaInfo> targetColumns, List<ColumnMetaInfo> mainColumns) {
            String[] strings = fkString.split("\\|");
            List<TableFK> fkList = new ArrayList<>();
            for (String fk : strings) {
                String[] fkMap = fk.split(":");
                if(fkMap.length!=2){
                    continue;
                }
                Tuple target = getColumnInfo(fkMap[1]);
                Tuple source = getColumnInfo(fkMap[0]);
                //目標字段類型
                String targetType = targetColumns.stream().filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), target.get(0).toString()))
                        .map(ColumnMetaInfo::getType).findFirst().orElse("");

                TableFK refTableFK = new TableFK();
                ColumnMetaInfo sourceColumn = mainColumns.stream().filter(x -> StringUtils.equalsAnyIgnoreCase(x.getName(), source.get(0).toString())).findFirst().orElse(null);
                if (null != sourceColumn) {
                    refTableFK.setFkSourceColumn(source.get(0));
                    refTableFK.setFkSourceColumnType(sourceColumn.getType());
                } else {
                    //沒有找到此列常量處理, 如果包含.則認為是使用的代碼中定義的常量
                    if(source.get(0).toString().contains(".") ||targetType.equals("String")){
                        refTableFK.setSourceValue(source.get(0));
                    }else{
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

        /**
         * 轉換方法
         * @param value
         * @return
         */
        public static Tuple getColumnInfo(String value) {
            // 使用Pattern和Matcher进行匹配
            Pattern p = Pattern.compile("\\(([^)]+)\\)");
            Matcher m = p.matcher(value);

            // 查找整个字符串中是否存在一个完整且匹配的括号对序列
            if (m.find()) {
                String insideBrackets = m.group(1);
                return new Tuple(value.substring(0, value.indexOf("(")), insideBrackets);
            }
            return new Tuple(value, "");
        }

        public String nameSuffix(String suffix) {
            return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.getTableName()))) + suffix;
        }

        public static void main(String[] args) {
            getColumnInfo("id:1(String.valueOf)");
        }
    }

    @Data
    @Builder
    public static class RefTable{
        private String name;
        private String tableName;
        private Boolean many;
        private List<TableFK> fkList;
        private List<ColumnMetaInfo> column;
    }



    @Data
    public static class TableInfo {
        private String name;
        private String tableName;
        private String implement;
        private Boolean deletable;
        private List<ColumnMetaInfo> column;
        private String keyType;
        private String keyColName;

        public static TableInfo convert(String tableName) {
            TableInfo tableInfo = new TableInfo();
            tableInfo.setName(tableName);
            tableInfo.setColumn(GlobalSetting.INSTANCE.getTableColumns(tableName));
            ColumnMetaInfo keyColumn = tableInfo.column.stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(null);
            if (keyColumn != null) {
                tableInfo.setKeyType(keyColumn.getType());
                tableInfo.setKeyColName(keyColumn.getName());
            }

            return tableInfo;
        }
    }


    @Data
    public static class TableFK{
        private String fkSourceColumnType;
        private String fkSourceColumn;
        private String fkSourceConvertMethod;
        private String fkTargetColumnType;
        private String fkTargetColumn;
        private String fkTargetConvertMethod;
        private String sourceValue; //為常量是，存儲常量的值， 常量定義為常量
    }
}
