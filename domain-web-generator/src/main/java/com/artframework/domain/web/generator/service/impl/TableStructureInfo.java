package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableColumnDomain;
import lombok.Data;

import java.util.ArrayList;
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

    public TableStructureInfo(){

    }
    /**
     * 构造函数
     */
    public TableStructureInfo(List<DatasourceDomain.DatasourceTableDomain> tables,
                              List<DatasourceDomain.DatasourceTableColumnDomain> columns) {
        this.tables = tables != null ? tables : new ArrayList<>();
        this.columns = columns != null ? columns : new ArrayList<>();
    }


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