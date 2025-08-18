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
import com.artframework.domain.web.generator.exception.BusinessException;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目应用服务实现类
 * 
 * @author auto
 * @version v1.0
 */
@Slf4j
@Service
public class ProjectAppServiceImpl implements ProjectAppService {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectAppConvertor projectAppConvertor;

    @Autowired
    private DatasourceService datasourceService;

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
            throw new BusinessException("PROJECT_HAS_DOMAINS", "项目下存在领域模型，无法删除");
        }
        
        // 删除项目
        return projectService.delete(id);
    }

    @Override
    public ProjectDetailWithDomainsResponse getProjectDetailWithDomainsAndTables(Integer id) {
        // 加载项目
        ProjectDomain domain = ProjectDomain.load(id, projectService);
        
        // 加载关联的领域模型
        domain.loadRelated(ProjectDomain.DomainConfigDomain.class);
        
        // 转换为响应对象
        ProjectDetailWithDomainsResponse response = new ProjectDetailWithDomainsResponse();
        response.setId(domain.getId());
        response.setName(domain.getName());
        response.setDomainPackage(domain.getDomainPackage());
        response.setControllerPackage(domain.getControllerPackage());
        response.setDoPackage(domain.getDoPackage());
        response.setMapperPackage(domain.getMapperPackage());
        response.setCreatedBy(domain.getCreatedBy());
        response.setCreatedTime(domain.getCreatedTime());
        response.setUpdatedBy(domain.getUpdatedBy());
        response.setUpdatedTime(domain.getUpdatedTime());
        
        // 设置领域模型列表
        if (CollUtil.isNotEmpty(domain.getDomainConfigList())) {
            List<DomainConfigDTO> domainConfigList = domain.getDomainConfigList().stream()
                    .map(projectAppConvertor::toDomainConfigDTO)
                    .collect(Collectors.toList());
            response.setDomainConfigList(domainConfigList);
        }
        
        // 根据新需求：基于领域模型所属应用的数据源来查询表数据
        List<DatasourceTableDTO> datasourceTables = loadDatasourceTables(id);
        response.setDatasourceTables(datasourceTables);
        
        return response;
    }

    /**
     * 根据项目ID加载数据源表信息（新需求：基于领域模型所属应用的数据源）
     */
    private List<DatasourceTableDTO> loadDatasourceTables(Integer projectId) {
        List<DatasourceTableDTO> allTables = new java.util.ArrayList<>();
        
        try {
            // 根据项目获取关联的数据源
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            if (project == null) {
                log.warn("项目不存在，项目ID: {}", projectId);
                return allTables;
            }
            
            // TODO: 需要在ProjectDomain中添加datasourceId字段
            // 目前使用第一个可用数据源作为示例
            LambdaQuery<DatasourceDomain> datasourceQuery = LambdaQuery.of(DatasourceDomain.class);
            List<DatasourceDomain> datasources = datasourceService.queryList(DatasourceDomain.class, datasourceQuery);
            
            if (CollUtil.isEmpty(datasources)) {
                log.warn("没有可用的数据源");
                return allTables;
            }
            
            DatasourceDomain datasource = datasources.get(0);
            
            // 加载数据源的表和列信息
            datasource.loadRelated(DatasourceDomain.DatasourceTableDomain.class);
            datasource.loadRelated(DatasourceDomain.DatasourceTableColumnDomain.class);
            
            if (CollUtil.isNotEmpty(datasource.getDatasourceTableList())) {
                for (DatasourceDomain.DatasourceTableDomain table : datasource.getDatasourceTableList()) {
                    DatasourceTableDTO tableDTO = new DatasourceTableDTO();
                    tableDTO.setId(table.getId());
                    tableDTO.setDsId(table.getDsId());
                    tableDTO.setName(table.getName());
                    tableDTO.setComment(table.getComment());
                    tableDTO.setCreatedBy(table.getCreatedBy());
                    tableDTO.setCreatedTime(table.getCreatedTime());
                    tableDTO.setUpdatedBy(table.getUpdatedBy());
                    tableDTO.setUpdatedTime(table.getUpdatedTime());
                    
                    // 从数据源关联的列信息中筛选该表的列
                    if (CollUtil.isNotEmpty(datasource.getDatasourceTableColumnList())) {
                        List<DatasourceTableColumnDTO> columns = datasource.getDatasourceTableColumnList().stream()
                                .filter(column -> table.getName().equals(column.getTableName()))
                                .map(this::toDatasourceTableColumnDTO)
                                .collect(Collectors.toList());
                        tableDTO.setColumns(columns);
                    }
                    
                    allTables.add(tableDTO);
                }
            }
            
        } catch (Exception e) {
            log.error("加载项目数据源表信息失败，项目ID: {}", projectId, e);
        }
        
        return allTables;
    }
    
    /**
     * 转换表列为DTO
     */
    private DatasourceTableColumnDTO toDatasourceTableColumnDTO(DatasourceDomain.DatasourceTableColumnDomain column) {
        DatasourceTableColumnDTO dto = new DatasourceTableColumnDTO();
        dto.setId(column.getId());
        dto.setDsId(column.getDsId());
        dto.setTableName(column.getTableName());
        dto.setName(column.getName());
        dto.setType(column.getType());
        dto.setComment(column.getComment());
        dto.setKey(column.getKey());
        dto.setCreatedBy(column.getCreatedBy());
        dto.setCreatedTime(column.getCreatedTime());
        dto.setUpdatedBy(column.getUpdatedBy());
        dto.setUpdatedTime(column.getUpdatedTime());
        return dto;
    }
} 