package com.artframework.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.artframework.domain.meta.table.ColumnMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class TableInfo {
    private String name;
    private Boolean basic = false;
    private String inherit;
    private Boolean keyGenerator = false;
    private List<ColumnMetaInfo> column;

    public String nameSuffix(String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.name))) + suffix;
    }

    public String inheritSuffix(String suffix) {
        return StringUtils.capitalize(StrUtil.toCamelCase(StrUtil.format("{}", this.inherit))) + suffix;
    }

    public static TableInfo covert(TableMetaInfo tableMetaInfo) {
        return BeanUtil.copyProperties(tableMetaInfo, TableInfo.class);
    }

}
