package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableDomain;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain.DatasourceTableColumnDomain;
import com.artframework.domain.web.generator.service.impl.TableStructureInfo;

import java.util.List;

/**
 * 数据库操作服务 - 领域服务层
 * 专注于数据库连接、表结构获取等业务逻辑
 */
public interface DatabaseService {

    /**
     * 测试数据库连接
     * @param datasource 数据源配置
     * @return 连接是否成功
     */
    Boolean testConnection(DatasourceDomain datasource);

    /**
     * 获取数据库表结构信息（不包含持久化操作）
     * @param datasourceId 数据源ID
     * @return 表结构信息列表
     */
    List<DatasourceTableDomain> getTableStructures(Integer datasourceId);

    /**
     * 获取完整的表结构信息（包含表和列信息）
     * @param datasourceId 数据源ID
     * @return 表结构信息封装对象
     */
    TableStructureInfo getTableStructureInfo(Integer datasourceId);

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