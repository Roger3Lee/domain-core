package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.domain.datasource.domain.*;
import com.artframework.domain.web.generator.application.DatasourceApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理控制器
 */
@RestController
@RequestMapping("/api/datasource")
@RequiredArgsConstructor
@Api(tags = "数据源管理")
public class DatasourceController {

    private final DatasourceApplicationService datasourceApplicationService;

    @GetMapping("/list")
    @ApiOperation("获取数据源列表")
    public List<DatasourceDomain> list() {
        return datasourceApplicationService.list();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取数据源")
    public DatasourceDomain getById(@ApiParam("数据源ID") @PathVariable Integer id) {
        return datasourceApplicationService.getById(id);
    }

    @PostMapping
    @ApiOperation("创建数据源")
    public Integer create(@RequestBody DatasourceDomain datasource) {
        return datasourceApplicationService.create(datasource);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新数据源")
    public Boolean update(@ApiParam("数据源ID") @PathVariable Integer id,
            @RequestBody DatasourceDomain datasource) {
        return datasourceApplicationService.update(id, datasource);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除数据源")
    public Boolean delete(@ApiParam("数据源ID") @PathVariable Integer id) {
        return datasourceApplicationService.delete(id);
    }

    @PostMapping("/{id}/test-connection")
    @ApiOperation("测试数据库连接")
    public Boolean testConnection(@ApiParam("数据源ID") @PathVariable Integer id) {
        return datasourceApplicationService.testConnection(id);
    }

    @PostMapping("/{id}/load-tables")
    @ApiOperation("加载数据库表结构")
    public Boolean loadTables(@ApiParam("数据源ID") @PathVariable Integer id) {
        return datasourceApplicationService.loadTableStructure(id);
    }

    @GetMapping("/{id}/tables")
    @ApiOperation("获取数据库表列表")
    public List<DatasourceDomain.DatasourceTableDomain> getTables(@ApiParam("数据源ID") @PathVariable Integer id) {
        return datasourceApplicationService.getTableList(id);
    }

    @GetMapping("/{id}/tables/{tableName}/columns")
    @ApiOperation("获取表字段列表")
    public List<DatasourceDomain.DatasourceTableColumnDomain> getColumns(
            @ApiParam("数据源ID") @PathVariable Integer id,
            @ApiParam("表名") @PathVariable String tableName) {
        return datasourceApplicationService.getColumnList(id, tableName);
    }
}