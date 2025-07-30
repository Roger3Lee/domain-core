package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.web.generator.convertor.DatasourceAppConvertor;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatasourceAppServiceImpl implements com.artframework.domain.web.generator.service.DatasourceAppService {

  @Autowired
  private DatasourceService datasourceService;

  @Autowired
  private DatasourceAppConvertor datasourceAppConvertor;

  @Override
  public PageResult<DatasourceResponse> page(DatasourcePageRequest request) {
    LambdaQuery<DatasourceDomain> query = LambdaQuery.of(DatasourceDomain.class);
    if (StrUtil.isNotEmpty(request.getName())) {
      query.like(DatasourceDomain::getName, request.getName());
    }
    if (StrUtil.isNotEmpty(request.getCode())) {
      query.like(DatasourceDomain::getCode, request.getCode());
    }
    if (StrUtil.isNotEmpty(request.getDbType())) {
      query.eq(DatasourceDomain::getDbType, request.getDbType());
    }
    query.orderBy(DatasourceDomain::getCreatedTime, Order.DESC);
    PageDomain pageDomain = PageDomain.builder()
        .pageNum(request.getPageNum())
        .pageSize(request.getPageSize())
        .build();
    IPage<DatasourceDomain> page = datasourceService.queryPage(DatasourceDomain.class, pageDomain, query);
    List<DatasourceResponse> records = page.getRecords().stream()
        .map(datasourceAppConvertor::convertToResponse)
        .collect(Collectors.toList());
    return PageResult.of(records, page.getTotal(), page.getSize(), page.getCurrent());
  }

  @Override
  public DatasourceResponse getById(Integer id) {
    DatasourceDomain domain = DatasourceDomain.load(id, datasourceService);
    if (domain == null) {
      return null;
    }
    domain.setLoadFlag(DatasourceDomain.LoadFlag.builder()
        .loadDatasourceTableDomain(true)
        .loadDatasourceTableColumnDomain(true)
        .build());
    datasourceService.find(domain, domain.getLoadFlag());
    return datasourceAppConvertor.convertToResponse(domain);
  }

  @Override
  @Transactional
  public Integer addDatasource(DatasourceAddRequest request) {
    DatasourceDomain domain = datasourceAppConvertor.convertToDomain(request);
    return datasourceService.insert(domain);
  }

  @Override
  @Transactional
  public Boolean editDatasource(DatasourceEditRequest request) {
    DatasourceDomain originalDomain = DatasourceDomain.load(request.getId(), datasourceService);
    if (originalDomain == null) {
      return false;
    }
    DatasourceDomain newDomain = datasourceAppConvertor.convertToDomain(request);
    newDomain.setLoadFlag(DatasourceDomain.LoadFlag.builder()
        .loadDatasourceTableDomain(false)
        .loadDatasourceTableColumnDomain(false)
        .build());
    return datasourceService.update(newDomain, originalDomain);
  }

  @Override
  @Transactional
  public Boolean deleteDatasource(Integer id) {
    return datasourceService.delete(id);
  }

  @Override
  @Transactional
  public Boolean syncTableStructure(Integer datasourceId) {
    // TODO: 调用数据库服务同步表结构
    return true;
  }
}