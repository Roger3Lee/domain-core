package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.service.ProjectAppService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 项目控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "项目管理")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectAppService projectAppService;

    @GetMapping("/page")
    @ApiOperation("分页查询项目")
    public ApiResponse<PageResult<ProjectResponse>> page(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        ProjectPageRequest request = new ProjectPageRequest();
        request.setName(name);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        PageResult<ProjectResponse> pageResult = projectAppService.page(request);
        return ApiResponse.success(pageResult);
    }

    @PostMapping
    @ApiOperation("新增项目")
    public ResponseEntity<ApiResponse<Integer>> addProject(
            @RequestBody @Valid ProjectAddRequest request) {
        Integer projectId = projectAppService.addProject(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/v1/projects/" + projectId)
                .body(ApiResponse.success(projectId));
    }

    @PutMapping
    @ApiOperation("编辑项目")
    public ApiResponse<Boolean> editProject(
            @RequestBody @Valid ProjectEditRequest request) {
        Boolean result = projectAppService.editProject(request);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取项目详情")
    public ApiResponse<ProjectResponse> getProjectDetail(
            @ApiParam("项目ID") @PathVariable Integer id) {
        ProjectResponse response = projectAppService.getProjectDetail(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/with-domains")
    @ApiOperation("获取项目详情（包含领域模型）")
    public ApiResponse<ProjectResponse> getProjectDetailWithDomains(
            @ApiParam("项目ID") @PathVariable Integer id) {
        ProjectResponse response = projectAppService.getProjectDetailWithDomains(id);
        return ApiResponse.success(response);
    }

    @GetMapping("/{id}/with-domains-and-tables")
    @ApiOperation("获取项目详情（包含数据源表和领域模型）")
    public ApiResponse<ProjectDetailWithDomainsResponse> getProjectDetailWithDomainsAndTables(
            @ApiParam("项目ID") @PathVariable Integer id) {
        ProjectDetailWithDomainsResponse response = projectAppService.getProjectDetailWithDomainsAndTables(id);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除项目")
    public ResponseEntity<ApiResponse<Boolean>> deleteProject(
            @ApiParam("项目ID") @PathVariable Integer id) {
        Boolean result = projectAppService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ApiResponse.success(result));
    }
} 