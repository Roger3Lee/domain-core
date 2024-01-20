package com.artframework.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.constants.BaseEntityConstants;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TableInfo {
    private String name;
    private Boolean basic = false;
    private String inherit;
    private Boolean inheritBaseEntity;
    private String baseEntity;
    private Boolean keyGenerator = true;
    private List<ColumnMetaInfo> column;

    private String keyType;
    private String keyName;

    public static TableInfo covert(TableMetaInfo tableMetaInfo) {
        TableInfo tableInfo = BeanUtil.copyProperties(tableMetaInfo, TableInfo.class);
        ColumnMetaInfo columnMetaInfo = GlobalSetting.INSTANCE.getTableColumns(tableMetaInfo.getName())
                .stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(new ColumnMetaInfo());
        tableInfo.setKeyType(columnMetaInfo.getType());
        tableInfo.setKeyName(columnMetaInfo.getName());
        tableInfo.inheritBaseEntity = new HashSet<>(tableInfo.getColumn().stream().map(ColumnMetaInfo::getName)
                .collect(Collectors.toList())).containsAll(BaseEntityConstants.FIELDS);
        if (tableInfo.inheritBaseEntity) {
            tableInfo.baseEntity=BaseEntityConstants.BASE_ENTITY;
            tableInfo.column.forEach(x -> {
                if (BaseEntityConstants.FIELDS.contains(x.getName())) {
                    x.setInherit(true);
                }
            });
        }
        return tableInfo;
    }
}
