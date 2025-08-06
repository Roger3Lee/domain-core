package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableColumnDomain;
import lombok.Data;

import java.util.List;

/**
 * 表结构信息封装类
 * 
 * @author auto
 * @version v1.0
 */
@Data
public class TableStructureInfo {
    
    private List<DatasourceTableDomain> tables;
    private List<DatasourceTableColumnDomain> columns;
    
    public boolean isEmpty() {
        return (tables == null || tables.isEmpty()) && (columns == null || columns.isEmpty());
    }
    
    public int getTableCount() {
        return tables != null ? tables.size() : 0;
    }
    
    public int getColumnCount() {
        return columns != null ? columns.size() : 0;
    }
} 