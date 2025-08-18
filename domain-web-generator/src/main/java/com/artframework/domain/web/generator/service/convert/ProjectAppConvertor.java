package com.artframework.domain.web.generator.service.convert;

import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 项目应用服务转换器
 * 用于处理请求体到领域实体的双向转换
 * 
 * @author auto
 * @version v1.0
 */
@Mapper(componentModel = "spring")
public interface ProjectAppConvertor {
    
    ProjectAppConvertor INSTANCE = Mappers.getMapper(ProjectAppConvertor.class);

    /**
     * 请求DTO转领域对象
     */
    ProjectDomain toDomain(ProjectAddRequest request);
    
    /**
     * 请求DTO转领域对象
     */
    @Mapping(target = "changed", constant = "true")
    ProjectDomain toDomain(ProjectEditRequest request);
    
    /**
     * 领域对象转响应DTO
     */
    ProjectResponse toResponse(ProjectDomain domain);
    
    /**
     * 领域对象列表转响应DTO列表
     */
    List<ProjectResponse> toResponseList(List<ProjectDomain> domainList);
    
    /**
     * 领域配置对象转DTO
     */
    DomainConfigDTO toDomainConfigDTO(ProjectDomain.DomainConfigDomain domain);
    
    /**
     * 领域配置对象列表转DTO列表
     */
    List<DomainConfigDTO> toDomainConfigDTOList(List<ProjectDomain.DomainConfigDomain> domainList);
} 