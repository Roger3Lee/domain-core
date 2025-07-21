package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.application.DatasourceApplicationService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 数据源管理控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "数据源管理")
@RestController
@RequestMapping("/api/v1/datasource")
public class DatasourceController {

    @Autowired
    private DatasourceApplicationService applicationService;

    @PostMapping("/page")
    @ApiOperation("分页查询数据源")
    public ApiResponse<PageResult<DatasourceResponse>> page(@RequestBody @Valid DatasourcePageRequest request) {
        PageResult<DatasourceResponse> pageResult = applicationService.page(request);
        return ApiResponse.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取数据源详情")
    public ApiResponse<DatasourceResponse> detail(@PathVariable Integer id) {
        DatasourceResponse response = applicationService.getById(id);
        return ApiResponse.success(response);
    }

    @PostMapping("/add")
    @ApiOperation("新增数据源")
    public ApiResponse<Integer> add(@RequestBody @Valid DatasourceAddRequest request) {
        Integer id = applicationService.add(request);
        return ApiResponse.success(id);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑数据源")
    public ApiResponse<Boolean> edit(@RequestBody @Valid DatasourceEditRequest request) {
        Boolean result = applicationService.edit(request);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除数据源")
    public ApiResponse<Boolean> delete(@PathVariable Integer id) {
        Boolean result = applicationService.delete(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/test")
    @ApiOperation("测试数据源连接")
    public ApiResponse<Boolean> testConnection(@RequestBody @Valid DatasourceAddRequest request) {
        Boolean result = applicationService.testConnection(request);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/load-tables")
    @ApiOperation("加载数据源表结构")
    public ApiResponse<Boolean> loadTableStructure(@PathVariable Integer id) {
        Boolean result = applicationService.loadTableStructure(id);
        return ApiResponse.success(result);
    }
}
