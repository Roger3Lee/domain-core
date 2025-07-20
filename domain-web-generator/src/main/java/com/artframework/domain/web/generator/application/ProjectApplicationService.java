package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.domain.project.domain.*;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目应用服务
 * 依赖domain层，不修改domain层代码
 */
@Service
@RequiredArgsConstructor
public class ProjectApplicationService {

    private final ProjectService projectService;

    /**
     * 获取所有项目列表
     */
    public List<ProjectDomain> list() {
        return projectService.list();
    }

    /**
     * 根据ID获取项目
     */
    public ProjectDomain getById(Integer id) {
        ProjectFindDomain request = new ProjectFindDomain();
        request.setKey(id);
        return projectService.find(request);
    }

    /**
     * 创建项目
     */
    public Integer create(ProjectDomain project) {
        return projectService.insert(project);
    }

    /**
     * 更新项目
     */
    public Boolean update(Integer id, ProjectDomain project) {
        project.setId(id);
        return projectService.update(project);
    }

    /**
     * 删除项目
     */
    public Boolean delete(Integer id) {
        return projectService.delete(id);
    }

    /**
     * 获取项目的领域模型列表
     */
    public List<ProjectDomain.DomainConfigDomain> getDomainConfigs(Integer id) {
        ProjectFindDomain request = new ProjectFindDomain();
        request.setKey(id);
        ProjectDomain.LoadFlag loadFlag = ProjectDomain.LoadFlag.builder()
                .loadDomainConfigDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        ProjectDomain project = projectService.find(request);
        return project != null ? project.getDomainConfigList() : null;
    }
}