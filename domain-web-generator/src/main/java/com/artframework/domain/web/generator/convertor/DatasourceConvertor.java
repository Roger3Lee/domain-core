package com.artframework.domain.web.generator.convertor;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据源转换器
 * 
 * @author auto
 * @version v1.0
 */
@Mapper
public interface DatasourceConvertor {

  DatasourceConvertor INSTANCE = Mappers.getMapper(DatasourceConvertor.class);

  /**
   * 领域对象转响应对象
   * 
   * @param domain 领域对象
   * @return 响应对象
   */
  DatasourceResponse toResponse(DatasourceDomain domain);

  /**
   * 新增请求转领域对象
   * 
   * @param request 新增请求
   * @return 领域对象
   */
  DatasourceDomain toDomain(DatasourceAddRequest request);

  /**
   * 编辑请求转领域对象
   * 
   * @param request 编辑请求
   * @return 领域对象
   */
  DatasourceDomain toDomain(DatasourceEditRequest request);

  /**
   * DTO转领域对象
   * 
   * @param dto DTO对象
   * @return 领域对象
   */
  DatasourceDomain toDomain(DatasourceConfigDTO dto);
}