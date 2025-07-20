package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源应用服务
 * 依赖domain层，不修改domain层代码
 */
@Service
@RequiredArgsConstructor
public class DatasourceApplicationService {

    private final DatasourceService datasourceService;

    /**
     * 获取所有数据源列表
     */
    public List<DatasourceDomain> list() {
        return datasourceService.list();
    }

    /**
     * 根据ID获取数据源
     */
    public DatasourceDomain getById(Integer id) {
        DatasourceFindDomain request = new DatasourceFindDomain();
        request.setKey(id);
        return datasourceService.find(request);
    }

    /**
     * 创建数据源
     */
    public Integer create(DatasourceDomain datasource) {
        return datasourceService.insert(datasource);
    }

    /**
     * 更新数据源
     */
    public Boolean update(Integer id, DatasourceDomain datasource) {
        datasource.setId(id);
        return datasourceService.update(datasource);
    }

    /**
     * 删除数据源
     */
    public Boolean delete(Integer id) {
        return datasourceService.delete(id);
    }

    /**
     * 测试数据库连接
     */
    public Boolean testConnection(Integer id) {
        DatasourceFindDomain request = new DatasourceFindDomain();
        request.setKey(id);
        DatasourceDomain datasource = datasourceService.find(request);
        if (datasource == null) {
            return false;
        }
        // TODO: 实现数据库连接测试逻辑
        return true;
    }

    /**
     * 加载数据库表结构
     */
    public Boolean loadTableStructure(Integer id) {
        // TODO: 实现加载数据库表结构逻辑
        return true;
    }

    /**
     * 获取数据库表列表
     */
    public List<DatasourceDomain.DatasourceTableDomain> getTableList(Integer id) {
        DatasourceFindDomain request = new DatasourceFindDomain();
        request.setKey(id);
        DatasourceDomain.LoadFlag loadFlag = DatasourceDomain.LoadFlag.builder()
                .loadDatasourceTableDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DatasourceDomain datasource = datasourceService.find(request);
        return datasource != null ? datasource.getDatasourceTableList() : null;
    }

    /**
     * 获取表字段列表
     */
    public List<DatasourceDomain.DatasourceTableColumnDomain> getColumnList(Integer id, String tableName) {
        DatasourceFindDomain request = new DatasourceFindDomain();
        request.setKey(id);
        DatasourceDomain.LoadFlag loadFlag = DatasourceDomain.LoadFlag.builder()
                .loadDatasourceTableColumnDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DatasourceDomain datasource = datasourceService.find(request);
        if (datasource == null || datasource.getDatasourceTableColumnList() == null) {
            return null;
        }
        // 根据表名过滤字段
        return datasource.getDatasourceTableColumnList().stream()
                .filter(column -> tableName.equals(column.getTableName()))
                .collect(java.util.stream.Collectors.toList());
    }
}