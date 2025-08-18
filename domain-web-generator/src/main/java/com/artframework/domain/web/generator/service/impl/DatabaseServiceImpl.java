package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import com.artframework.domain.web.generator.service.DatabaseService;
import com.artframework.domain.web.generator.domain.datasource.repository.DatasourceTableRepository;
import com.artframework.domain.web.generator.domain.datasource.repository.DatasourceTableColumnRepository;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.customize.CustomPostgreSqlQuery;
import com.artframework.domain.customize.MyPostgreSqlQuery;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.PostgreSqlKeyWordsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作服务实现 - 领域服务层
 * 专注于数据库连接、表结构获取等业务逻辑，不处理数据持久化
 */
@Slf4j
@Service
public class DatabaseServiceImpl implements DatabaseService {

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DatasourceTableRepository datasourceTableRepository;

    @Autowired
    private DatasourceTableColumnRepository datasourceTableColumnRepository;

    @Override
    public Boolean testConnection(DatasourceDomain datasource) {
        try (Connection connection = getConnection(datasource)) {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            log.error("测试数据库连接失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<DatasourceDomain.DatasourceTableDomain> getTableStructures(Integer datasourceId) {
        try {
            // 1. 获取数据源配置
            DatasourceDomain datasource = DatasourceDomain.load(datasourceId, datasourceService);
            if (datasource == null) {
                log.error("数据源不存在: {}", datasourceId);
                return new ArrayList<>();
            }

            // 2. 测试连接
            if (!testConnection(datasource)) {
                log.error("数据库连接失败: {}", datasourceId);
                return new ArrayList<>();
            }

            // 3. 使用MyBatis Plus Generator获取表结构
            List<TableInfo> tableInfos = getTableInfosFromDatabase(datasource);

            // 4. 转换为领域对象（不进行持久化）
            List<DatasourceDomain.DatasourceTableDomain> tableDomains = new ArrayList<>();
            for (TableInfo tableInfo : tableInfos) {
                // 创建表信息领域对象
                DatasourceDomain.DatasourceTableDomain table = new DatasourceDomain.DatasourceTableDomain();
                table.setDsId(datasourceId);
                table.setName(tableInfo.getName());
                table.setComment(tableInfo.getComment());
                tableDomains.add(table);
            }

            log.info("获取表结构成功，数据源ID: {}, 表数量: {}", datasourceId, tableDomains.size());
            return tableDomains;

        } catch (Exception e) {
            log.error("获取表结构失败，数据源ID: {}", datasourceId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public TableStructureInfo getTableStructureInfo(Integer datasourceId) {
        try {
            // 1. 获取数据源配置
            DatasourceDomain datasource = DatasourceDomain.load(datasourceId, datasourceService);
            if (datasource == null) {
                log.error("数据源不存在: {}", datasourceId);
                return new TableStructureInfo();
            }

            // 2. 测试连接
            if (!testConnection(datasource)) {
                log.error("数据库连接失败: {}", datasourceId);
                return new TableStructureInfo();
            }

            // 3. 使用MyBatis Plus Generator获取表结构
            List<TableInfo> tableInfos = getTableInfosFromDatabase(datasource);

            // 4. 转换为领域对象（不进行持久化）
            List<DatasourceDomain.DatasourceTableDomain> tableDomains = new ArrayList<>();
            List<DatasourceDomain.DatasourceTableColumnDomain> columnDomains = new ArrayList<>();

            for (TableInfo tableInfo : tableInfos) {
                // 创建表信息领域对象
                DatasourceDomain.DatasourceTableDomain table = new DatasourceDomain.DatasourceTableDomain();
                table.setDsId(datasourceId);
                table.setName(tableInfo.getName());
                table.setComment(tableInfo.getComment());
                tableDomains.add(table);

                // 处理列信息
                if (tableInfo.getFields() != null && !tableInfo.getFields().isEmpty()) {
                    for (TableField tableField : tableInfo.getFields()) {
                        DatasourceDomain.DatasourceTableColumnDomain column = new DatasourceDomain.DatasourceTableColumnDomain();
                        column.setDsId(datasourceId);
                        column.setTableName(tableInfo.getName());
                        column.setName(tableField.getColumnName());
                        column.setType(tableField.getColumnType().getType());
                        column.setComment(tableField.getComment());
                        column.setKey(tableField.isKeyFlag() ? "Y" : "N");
                        columnDomains.add(column);
                    }
                }
            }

            log.info("获取表结构信息成功，数据源ID: {}, 表数量: {}, 列数量: {}",
                    datasourceId, tableDomains.size(), columnDomains.size());

            return new TableStructureInfo(tableDomains, columnDomains);

        } catch (Exception e) {
            log.error("获取表结构信息失败，数据源ID: {}", datasourceId, e);
            return new TableStructureInfo();
        }
    }

    @Override
    public List<DatasourceDomain.DatasourceTableDomain> getTableList(Integer datasourceId) {
        // 从本地数据库获取表列表
        return datasourceTableRepository.queryList(
                LambdaQuery.of(DatasourceDomain.DatasourceTableDomain.class)
                        .eq(DatasourceDomain.DatasourceTableDomain::getDsId, datasourceId));
    }

    @Override
    public List<DatasourceDomain.DatasourceTableColumnDomain> getColumnList(Integer datasourceId, String tableName) {
        // 从本地数据库获取列列表
        return datasourceTableColumnRepository.queryList(
                LambdaQuery.of(DatasourceDomain.DatasourceTableColumnDomain.class)
                        .eq(DatasourceDomain.DatasourceTableColumnDomain::getDsId, datasourceId)
                        .eq(DatasourceDomain.DatasourceTableColumnDomain::getTableName, tableName));
    }

    /**
     * 获取数据库连接
     */
    private Connection getConnection(DatasourceDomain datasource) throws SQLException {
        return DriverManager.getConnection(
                datasource.getUrl(),
                datasource.getUserName(),
                datasource.getPassword());
    }

    /**
     * 使用MyBatis Plus Generator获取表结构信息
     */
    private List<TableInfo> getTableInfosFromDatabase(DatasourceDomain datasource) {
        try {
            // 构建DataSourceConfig
            DataSourceConfig.Builder builder = new DataSourceConfig.Builder(
                    datasource.getUrl(),
                    datasource.getUserName(),
                    datasource.getPassword());

            // 根据数据库类型配置
            switch (datasource.getDbType().toLowerCase()) {
                case "mysql":
                    builder.dbQuery(new com.baomidou.mybatisplus.generator.config.querys.MySqlQuery())
                            .keyWordsHandler(new MySqlKeyWordsHandler());
                    break;
                case "polardb":
                case "postgresql":
                    builder.dbQuery(new CustomPostgreSqlQuery())
                            .keyWordsHandler(new PostgreSqlKeyWordsHandler());
                    if (datasource.getSchema() != null && !datasource.getSchema().isEmpty()) {
                        builder.schema(datasource.getSchema())
                                .addConnectionProperty("currentSchema", datasource.getSchema());
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("不支持的数据库类型: " + datasource.getDbType());
            }

            // 添加通用连接属性
            builder.addConnectionProperty("useUnicode", "true")
                    .addConnectionProperty("characterEncoding", "UTF-8");

            DataSourceConfig dataSourceConfig = builder.build();

            // 使用TableQuery获取表信息
            com.artframework.domain.datasource.TableQuery tableQuery = new com.artframework.domain.datasource.TableQuery(
                    dataSourceConfig);
            return tableQuery.queryTables();

        } catch (Exception e) {
            log.error("获取表结构信息失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * 根据项目ID获取默认数据源配置
     * 通过项目ID查询项目，然后通过项目的datasourceId查询数据源配置
     */
    @Override
    public DataSourceConfig getDefaultDataSourceConfig(Integer projectId) {
        try {
            // 1. 通过项目ID查询项目信息
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            if (project == null) {
                throw new RuntimeException("项目不存在，项目ID: " + projectId);
            }
            
            if (project.getDatasourceId() == null) {
                throw new RuntimeException("项目未关联数据源配置，项目ID: " + projectId);
            }
            
            // 2. 通过项目的datasourceId查询数据源配置
            DatasourceDomain datasource = DatasourceDomain.load(project.getDatasourceId(), datasourceService);
            if (datasource == null) {
                throw new RuntimeException("数据源配置不存在，数据源ID: " + project.getDatasourceId());
            }
            
            // 3. 构建DataSourceConfig
            DataSourceConfig.Builder builder = new DataSourceConfig.Builder(
                    datasource.getUrl(),
                    datasource.getUserName(),
                    datasource.getPassword());

            // 根据数据库类型配置
            switch (datasource.getDbType().toLowerCase()) {
                case "mysql":
                    builder.dbQuery(new com.baomidou.mybatisplus.generator.config.querys.MySqlQuery())
                            .keyWordsHandler(new MySqlKeyWordsHandler());
                    break;
                case "polardb":
                case "postgresql":
                    builder.dbQuery(new CustomPostgreSqlQuery())
                            .keyWordsHandler(new PostgreSqlKeyWordsHandler());
                    if (datasource.getSchema() != null && !datasource.getSchema().isEmpty()) {
                        builder.schema(datasource.getSchema())
                                .addConnectionProperty("currentSchema", datasource.getSchema());
                    }
                    break;
                default:
                    throw new UnsupportedOperationException("不支持的数据库类型: " + datasource.getDbType());
            }

            // 添加通用连接属性
            builder.addConnectionProperty("useUnicode", "true")
                    .addConnectionProperty("characterEncoding", "UTF-8");

            return builder.build();
            
        } catch (Exception e) {
            log.error("获取项目数据源配置失败，项目ID: {}", projectId, e);
            throw new RuntimeException("获取项目数据源配置失败: " + e.getMessage());
        }
    }
}