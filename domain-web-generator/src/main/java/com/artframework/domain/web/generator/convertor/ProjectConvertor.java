package com.artframework.domain.web.generator.convertor;

import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 项目转换器
 * 
 * @author auto
 * @version v1.0
 */
@Mapper
public interface ProjectConvertor {

  ProjectConvertor INSTANCE = Mappers.getMapper(ProjectConvertor.class);

  /**
   * 领域对象转响应对象
   * 
   * @param domain 领域对象
   * @return 响应对象
   */
  ProjectResponse toResponse(ProjectDomain domain);

  /**
   * 新增请求转领域对象
   * 
   * @param request 新增请求
   * @return 领域对象
   */
  ProjectDomain toDomain(ProjectAddRequest request);

  /**
   * 编辑请求转领域对象
   * 
   * @param request 编辑请求
   * @return 领域对象
   */
  ProjectDomain toDomain(ProjectEditRequest request);

  /**
   * DTO转领域对象
   * 
   * @param dto DTO对象
   * @return 领域对象
   */
  ProjectDomain toDomain(ProjectDTO dto);
}