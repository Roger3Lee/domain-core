package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import com.artframework.domain.web.generator.domain.datasource.repository.DatasourceTableRepository;
import com.artframework.domain.web.generator.domain.datasource.repository.DatasourceTableColumnRepository;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.web.generator.convertor.DatasourceAppConvertor;
import com.artframework.domain.web.generator.service.DatabaseService;
import com.artframework.domain.web.generator.service.impl.TableStructureInfo;
import com.artframework.domain.core.domain.PageDomain;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DatasourceAppServiceImpl implements com.artframework.domain.web.generator.service.DatasourceAppService {

  @Autowired
  private DatasourceService datasourceService;

  @Autowired
  private DatasourceAppConvertor datasourceAppConvertor;

  @Autowired
  private DatabaseService databaseService;

  @Autowired
  private DatasourceTableRepository datasourceTableRepository;

  @Autowired
  private DatasourceTableColumnRepository datasourceTableColumnRepository;

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
    // 根据需求，数据源详情不包含数据源表和数据源表列数据
    // 不设置LoadFlag，只返回基本信息
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
    try {
      // 1. 加载原始数据源聚合根（包含现有的表结构数据）
      DatasourceDomain originalDomain = DatasourceDomain.load(datasourceId, datasourceService);
      if (originalDomain == null) {
        log.error("数据源不存在: {}", datasourceId);
        return false;
      }

      // 2. 加载关联的表和列数据
      originalDomain.setLoadFlag(DatasourceDomain.LoadFlag.builder()
          .loadDatasourceTableDomain(true)
          .loadDatasourceTableColumnDomain(true)
          .build());

      // 重新加载包含关联数据的聚合根
      originalDomain = datasourceService.find(originalDomain, originalDomain.getLoadFlag());

      // 3. 使用领域服务获取新的表结构信息
      TableStructureInfo tableStructureInfo = databaseService.getTableStructureInfo(datasourceId);
      if (tableStructureInfo.isEmpty()) {
        log.warn("未获取到表结构信息，数据源ID: {}", datasourceId);
        return false;
      }

      // 4. 业务逻辑验证
      if (!validateTableStructure(tableStructureInfo)) {
        log.error("表结构数据验证失败，数据源ID: {}", datasourceId);
        return false;
      }

      // 5. 创建新的聚合根，包含新的表结构数据
      DatasourceDomain newDomain = DatasourceDomain.copy(originalDomain);

      // 设置新的表结构数据
      newDomain.setDatasourceTableList(tableStructureInfo.getTables());
      newDomain.setDatasourceTableColumnList(tableStructureInfo.getColumns());

      // 设置LoadFlag，指定需要更新的关联实体
      newDomain.setLoadFlag(DatasourceDomain.LoadFlag.builder()
          .loadDatasourceTableDomain(true)
          .loadDatasourceTableColumnDomain(true)
          .build());

      // 6. 使用聚合根的update方法更新整个聚合（包括表和列）
      Boolean result = datasourceService.update(newDomain, originalDomain);

      if (result) {
        log.info("同步表结构成功，数据源ID: {}, 表数量: {}, 列数量: {}",
            datasourceId, tableStructureInfo.getTableCount(), tableStructureInfo.getColumnCount());
      } else {
        log.error("同步表结构失败，数据源ID: {}", datasourceId);
      }

      return result;

    } catch (Exception e) {
      log.error("同步表结构失败，数据源ID: {}", datasourceId, e);
      return false;
    }
  }

  /**
   * 验证表结构数据的业务逻辑
   */
  private Boolean validateTableStructure(TableStructureInfo tableStructureInfo) {
    try {
      // 验证表数据
      if (tableStructureInfo.getTables() != null) {
        for (DatasourceDomain.DatasourceTableDomain table : tableStructureInfo.getTables()) {
          if (table.getName() == null || table.getName().trim().isEmpty()) {
            log.error("表名不能为空");
            return false;
          }
        }
      }

      // 验证列数据
      if (tableStructureInfo.getColumns() != null) {
        for (DatasourceDomain.DatasourceTableColumnDomain column : tableStructureInfo.getColumns()) {
          if (column.getName() == null || column.getName().trim().isEmpty()) {
            log.error("列名不能为空");
            return false;
          }
          if (column.getTableName() == null || column.getTableName().trim().isEmpty()) {
            log.error("列所属表名不能为空");
            return false;
          }
          if (column.getType() == null || column.getType().trim().isEmpty()) {
            log.error("列类型不能为空");
            return false;
          }
        }
      }

      return true;
    } catch (Exception e) {
      log.error("验证表结构数据时发生异常", e);
      return false;
    }
  }

  @Override
  public Boolean testConnection(DatasourceAddRequest request) {
    // 将Request转换为Domain进行测试
    DatasourceDomain domain = datasourceAppConvertor.convertToDomain(request);
    return databaseService.testConnection(domain);
  }
}