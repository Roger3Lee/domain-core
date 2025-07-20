package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.application.ProjectApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理控制器
 */
@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
@Api(tags = "项目管理")
public class ProjectController {

    private final ProjectApplicationService projectApplicationService;

    @GetMapping("/list")
    @ApiOperation("获取项目列表")
    public List<ProjectDomain> list() {
        return projectApplicationService.list();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取项目")
    public ProjectDomain getById(@ApiParam("项目ID") @PathVariable Integer id) {
        return projectApplicationService.getById(id);
    }

    @PostMapping
    @ApiOperation("创建项目")
    public Integer create(@RequestBody ProjectDomain project) {
        return projectApplicationService.create(project);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新项目")
    public Boolean update(@ApiParam("项目ID") @PathVariable Integer id,
            @RequestBody ProjectDomain project) {
        return projectApplicationService.update(id, project);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除项目")
    public Boolean delete(@ApiParam("项目ID") @PathVariable Integer id) {
        return projectApplicationService.delete(id);
    }

    @GetMapping("/{id}/domains")
    @ApiOperation("获取项目的领域模型列表")
    public List<ProjectDomain.DomainConfigDomain> getDomainConfigs(@ApiParam("项目ID") @PathVariable Integer id) {
        return projectApplicationService.getDomainConfigs(id);
    }
}