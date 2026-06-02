package io.github.roger3lee.domain.v2.dto;

import lombok.Data;

import java.util.List;

/**
 * Table DTO used in FreeMarker template rendering for DO classes.
 */
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
}
