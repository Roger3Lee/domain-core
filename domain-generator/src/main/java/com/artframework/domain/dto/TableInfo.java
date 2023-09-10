package com.artframework.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
    private String name;
    private Boolean basic = false;
    private String inherit;
    private List<ColumnMetaInfo> column;

    private String keyType;
    private String keyName;

    public static TableInfo covert(TableMetaInfo tableMetaInfo) {
        TableInfo tableInfo= BeanUtil.copyProperties(tableMetaInfo, TableInfo.class);
        ColumnMetaInfo columnMetaInfo= GlobalSetting.INSTANCE.getTableColumns(tableMetaInfo.getName())
                .stream().filter(ColumnMetaInfo::getKey).findFirst().orElse(new ColumnMetaInfo());
        tableInfo.setKeyType(columnMetaInfo.getType());
        tableInfo.setKeyName(columnMetaInfo.getName());
        return tableInfo;
    }
}
