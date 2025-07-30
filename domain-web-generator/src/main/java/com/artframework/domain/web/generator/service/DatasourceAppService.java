package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.dto.*;

public interface DatasourceAppService {
  PageResult<DatasourceResponse> page(DatasourcePageRequest request);

  DatasourceResponse getById(Integer id);

  Integer addDatasource(DatasourceAddRequest request);

  Boolean editDatasource(DatasourceEditRequest request);

  Boolean deleteDatasource(Integer id);

  /**
   * 同步表结构数据
   * 获取数据库表结构并保存到本地数据库
   * @param datasourceId 数据源ID
   * @return 同步是否成功
   */
  Boolean syncTableStructure(Integer datasourceId);

  /**
   * 测试数据库连接
   * 
   * @param request 数据源配置
   * @return 连接是否成功
   */
  Boolean testConnection(DatasourceAddRequest request);
}