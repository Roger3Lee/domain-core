package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.web.generator.service.DatasourceAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "数据源管理")
@RestController
@RequestMapping("/api/v1/datasources")
@Validated
public class DatasourceController {

  @Autowired
  private DatasourceAppService datasourceAppService;

  @GetMapping("/page")
  @ApiOperation("分页查询数据源")
  public PageResult<DatasourceResponse> page(
          @RequestParam(required = false) String name,
          @RequestParam(required = false) String code,
          @RequestParam(required = false) String dbType,
          @RequestParam(defaultValue = "1") Long pageNum,
          @RequestParam(defaultValue = "10") Long pageSize) {
    DatasourcePageRequest request = new DatasourcePageRequest();
    request.setName(name);
    request.setCode(code);
    request.setDbType(dbType);
    request.setPageNum(pageNum);
    request.setPageSize(pageSize);
    return datasourceAppService.page(request);
  }

  @GetMapping("/{id}")
  @ApiOperation("获取数据源详情")
  public DatasourceResponse detail(@PathVariable Integer id) {
    return datasourceAppService.getById(id);
  }

  @PostMapping
  @ApiOperation("新增数据源")
  public Integer add(@RequestBody @Valid DatasourceAddRequest request) {
    return datasourceAppService.addDatasource(request);
  }

  @PutMapping
  @ApiOperation("编辑数据源")
  public Boolean edit(@RequestBody @Valid DatasourceEditRequest request) {
    return datasourceAppService.editDatasource(request);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("删除数据源")
  public Boolean delete(@PathVariable Integer id) {
    return datasourceAppService.deleteDatasource(id);
  }

  @PostMapping("/{id}/sync")
  @ApiOperation("同步表结构")
  public Boolean syncTable(@PathVariable Integer id) {
    return datasourceAppService.syncTableStructure(id);
  }

  @PostMapping("/test-connection")
  @ApiOperation("测试数据库连接")
  public Boolean testConnection(@RequestBody @Valid DatasourceAddRequest request) {
    return datasourceAppService.testConnection(request);
  }
}