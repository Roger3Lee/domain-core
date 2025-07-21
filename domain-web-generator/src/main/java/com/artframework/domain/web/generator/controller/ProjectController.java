package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.application.ProjectApplicationService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 项目管理控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "项目管理")
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    @Autowired
    private ProjectApplicationService applicationService;

    @PostMapping("/page")
    @ApiOperation("分页查询项目")
    public ApiResponse<PageResult<ProjectResponse>> page(@RequestBody @Valid ProjectPageRequest request) {
        PageResult<ProjectResponse> pageResult = applicationService.page(request);
        return ApiResponse.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取项目详情")
    public ApiResponse<ProjectResponse> detail(@PathVariable Integer id) {
        ProjectResponse response = applicationService.getById(id);
        return ApiResponse.success(response);
    }

    @PostMapping("/add")
    @ApiOperation("新增项目")
    public ApiResponse<Integer> add(@RequestBody @Valid ProjectAddRequest request) {
        Integer id = applicationService.add(request);
        return ApiResponse.success(id);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑项目")
    public ApiResponse<Boolean> edit(@RequestBody @Valid ProjectEditRequest request) {
        Boolean result = applicationService.edit(request);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除项目")
    public ApiResponse<Boolean> delete(@PathVariable Integer id) {
        Boolean result = applicationService.delete(id);
        return ApiResponse.success(result);
    }
}
