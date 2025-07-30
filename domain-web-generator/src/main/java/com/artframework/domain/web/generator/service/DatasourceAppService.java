package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.dto.*;

public interface DatasourceAppService {
  PageResult<DatasourceResponse> page(DatasourcePageRequest request);

  DatasourceResponse getById(Integer id);

  Integer addDatasource(DatasourceAddRequest request);

  Boolean editDatasource(DatasourceEditRequest request);

  Boolean deleteDatasource(Integer id);

  Boolean syncTableStructure(Integer datasourceId);
}