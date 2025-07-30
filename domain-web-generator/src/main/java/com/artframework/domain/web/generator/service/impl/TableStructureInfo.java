package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 表结构信息封装类
 * 用于在领域服务层和应用服务层之间传递表结构数据
 */
@Data
@NoArgsConstructor
public class TableStructureInfo {
    
    /**
     * 表信息列表
     */
    private List<DatasourceDomain.DatasourceTableDomain> tables = new ArrayList<>();
    
    /**
     * 列信息列表
     */
    private List<DatasourceDomain.DatasourceTableColumnDomain> columns = new ArrayList<>();
    
    /**
     * 构造函数
     */
    public TableStructureInfo(List<DatasourceDomain.DatasourceTableDomain> tables, 
                             List<DatasourceDomain.DatasourceTableColumnDomain> columns) {
        this.tables = tables != null ? tables : new ArrayList<>();
        this.columns = columns != null ? columns : new ArrayList<>();
    }
    
    /**
     * 获取表数量
     */
    public int getTableCount() {
        return tables.size();
    }
    
    /**
     * 获取列数量
     */
    public int getColumnCount() {
        return columns.size();
    }
    
    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return tables.isEmpty() && columns.isEmpty();
    }
} 