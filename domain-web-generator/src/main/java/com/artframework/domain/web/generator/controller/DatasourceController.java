package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.web.generator.service.DatasourceAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ApiResponse<PageResult<DatasourceResponse>> page(
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
    PageResult<DatasourceResponse> pageResult = datasourceAppService.page(request);
    return ApiResponse.success(pageResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("获取数据源详情")
  public ApiResponse<DatasourceResponse> detail(@PathVariable Integer id) {
    DatasourceResponse response = datasourceAppService.getById(id);
    return ApiResponse.success(response);
  }

  @PostMapping
  @ApiOperation("新增数据源")
  public ResponseEntity<ApiResponse<Integer>> add(@RequestBody @Valid DatasourceAddRequest request) {
    Integer id = datasourceAppService.addDatasource(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .header("Location", "/api/v1/datasources/" + id)
            .body(ApiResponse.success(id));
  }

  @PutMapping
  @ApiOperation("编辑数据源")
  public ApiResponse<Boolean> edit(@RequestBody @Valid DatasourceEditRequest request) {
    Boolean result = datasourceAppService.editDatasource(request);
    return ApiResponse.success(result);
  }

  @DeleteMapping("/{id}")
  @ApiOperation("删除数据源")
  public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable Integer id) {
    Boolean result = datasourceAppService.deleteDatasource(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success(result));
  }

  @PostMapping("/{id}/sync")
  @ApiOperation("同步表结构")
  public ApiResponse<Boolean> syncTable(@PathVariable Integer id) {
    Boolean result = datasourceAppService.syncTableStructure(id);
    return ApiResponse.success(result);
  }

  @PostMapping("/test-connection")
  @ApiOperation("测试数据库连接")
  public ApiResponse<Boolean> testConnection(@RequestBody @Valid DatasourceAddRequest request) {
    Boolean result = datasourceAppService.testConnection(request);
    return ApiResponse.success(result);
  }
}