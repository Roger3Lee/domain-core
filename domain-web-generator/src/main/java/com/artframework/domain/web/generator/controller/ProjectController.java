package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.service.ProjectAppService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public PageResult<ProjectResponse> page(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        ProjectPageRequest request = new ProjectPageRequest();
        request.setName(name);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return projectAppService.page(request);
    }

    @PostMapping
    @ApiOperation("新增项目")
    public Integer addProject(
            @RequestBody @Valid ProjectAddRequest request) {
        return projectAppService.addProject(request);
    }

    @PutMapping
    @ApiOperation("编辑项目")
    public Boolean editProject(
            @RequestBody @Valid ProjectEditRequest request) {
        return projectAppService.editProject(request);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取项目详情")
    public ProjectResponse getProjectDetail(
            @ApiParam("项目ID") @PathVariable Integer id) {
        return projectAppService.getProjectDetail(id);
    }

    @GetMapping("/{id}/with-domains")
    @ApiOperation("获取项目详情（包含领域模型）")
    public ProjectResponse getProjectDetailWithDomains(
            @ApiParam("项目ID") @PathVariable Integer id) {
        return projectAppService.getProjectDetailWithDomains(id);
    }

    @GetMapping("/{id}/with-domains-and-tables")
    @ApiOperation("获取项目详情（包含数据源表和领域模型）")
    public ProjectDetailWithDomainsResponse getProjectDetailWithDomainsAndTables(
            @ApiParam("项目ID") @PathVariable Integer id) {
        return projectAppService.getProjectDetailWithDomainsAndTables(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除项目")
    public Boolean deleteProject(
            @ApiParam("项目ID") @PathVariable Integer id) {
        return projectAppService.deleteProject(id);
    }
} 