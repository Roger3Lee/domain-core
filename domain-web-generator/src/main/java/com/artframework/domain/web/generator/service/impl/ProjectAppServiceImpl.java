package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.service.ProjectAppService;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.web.generator.service.convert.ProjectAppConvertor;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.artframework.domain.core.domain.PageDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目应用服务实现类
 * 
 * @author auto
 * @version v1.0
 */
@Service
public class ProjectAppServiceImpl implements ProjectAppService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectAppConvertor projectAppConvertor;

    @Override
    public PageResult<ProjectResponse> page(ProjectPageRequest request) {
        // 构建查询条件
        LambdaQuery<ProjectDomain> query = LambdaQuery.of(ProjectDomain.class);
        
        // 按项目名称模糊查询
        if (StrUtil.isNotEmpty(request.getName())) {
            query.like(ProjectDomain::getName, request.getName());
        }
        
        // 排序
        query.orderBy(ProjectDomain::getId, Order.DESC);
        
        // 分页查询
        PageDomain pageDomain = PageDomain.builder()
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .build();
        IPage<ProjectDomain> result = projectService.queryPage(ProjectDomain.class, pageDomain, query);
        
        // 转换为响应对象
        List<ProjectResponse> records = result.getRecords().stream()
                .map(projectAppConvertor::toResponse)
                .collect(Collectors.toList());
        
        return PageResult.of(records, result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public Integer addProject(ProjectAddRequest request) {
        // 转换为领域对象
        ProjectDomain domain = projectAppConvertor.toDomain(request);
        
        // 保存项目
        return projectService.insert(domain);
    }

    @Override
    @Transactional
    public Boolean editProject(ProjectEditRequest request) {
        // 获取原始项目
        ProjectDomain originalDomain = ProjectDomain.load(request.getId(), projectService);
        
        // 转换为新的领域对象
        ProjectDomain newDomain = projectAppConvertor.toDomain(request);
        
        // 更新项目
        return projectService.update(newDomain, originalDomain);
    }

    @Override
    public ProjectResponse getProjectDetail(Integer id) {
        // 加载项目
        ProjectDomain domain = ProjectDomain.load(id, projectService);
        
        // 转换为响应对象
        return projectAppConvertor.toResponse(domain);
    }

    @Override
    public ProjectResponse getProjectDetailWithDomains(Integer id) {
        // 加载项目
        ProjectDomain domain = ProjectDomain.load(id, projectService);
        
        // 加载关联的领域模型
        domain.loadRelated(ProjectDomain.DomainConfigDomain.class);
        
        // 转换为响应对象
        ProjectResponse response = projectAppConvertor.toResponse(domain);
        
        // 设置领域模型列表
        if (CollUtil.isNotEmpty(domain.getDomainConfigList())) {
            List<DomainConfigDTO> domainConfigList = domain.getDomainConfigList().stream()
                    .map(projectAppConvertor::toDomainConfigDTO)
                    .collect(Collectors.toList());
            response.setDomainConfigList(domainConfigList);
        }
        
        return response;
    }

    @Override
    @Transactional
    public Boolean deleteProject(Integer id) {
        // 检查是否有领域模型
        ProjectDomain domain = ProjectDomain.load(id, projectService);
        domain.loadRelated(ProjectDomain.DomainConfigDomain.class);
        
        if (CollUtil.isNotEmpty(domain.getDomainConfigList())) {
            throw new RuntimeException("项目下存在领域模型，无法删除");
        }
        
        // 删除项目
        return projectService.delete(id);
    }
} 