package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableColumnDomain;

import java.util.List;

/**
 * 数据库操作服务
 */
public interface DatabaseService {

    /**
     * 测试数据库连接
     * @param datasource 数据源配置
     * @return 连接是否成功
     */
    Boolean testConnection(DatasourceDomain datasource);

    /**
     * 加载数据库表结构
     * @param datasourceId 数据源ID
     * @return 是否加载成功
     */
    Boolean loadTableStructure(Integer datasourceId);

    /**
     * 获取数据库表列表
     * @param datasourceId 数据源ID
     * @return 表列表
     */
    List<DatasourceTableDomain> getTableList(Integer datasourceId);

    /**
     * 获取表字段列表
     * @param datasourceId 数据源ID
     * @param tableName 表名
     * @return 字段列表
     */
    List<DatasourceTableColumnDomain> getColumnList(Integer datasourceId, String tableName);
} 