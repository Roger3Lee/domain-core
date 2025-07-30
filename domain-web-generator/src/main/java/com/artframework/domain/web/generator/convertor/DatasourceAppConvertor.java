package com.artframework.domain.web.generator.convertor;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据源应用服务转换器
 *
 * @author auto
 * @version v1.0
 */
@Mapper(componentModel = "spring")
@Component
public interface DatasourceAppConvertor {

  DatasourceAppConvertor INSTANCE = Mappers.getMapper(DatasourceAppConvertor.class);

  /**
   * Request DTO 转换为 Domain
   */
  DatasourceDomain convertToDomain(DatasourceAddRequest request);

  /**
   * Request DTO 转换为 Domain
   */
  DatasourceDomain convertToDomain(DatasourceEditRequest request);

  /**
   * Domain 转换为 Response DTO
   */
  DatasourceResponse convertToResponse(DatasourceDomain domain);

  /**
   * Domain列表 转换为 Response DTO列表
   */
  List<DatasourceResponse> convertToResponseList(List<DatasourceDomain> domainList);
}