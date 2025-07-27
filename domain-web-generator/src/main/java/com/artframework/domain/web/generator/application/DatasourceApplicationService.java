package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.convertor.DatasourceConvertor;
import com.artframework.domain.web.generator.domain.datasource.domain.DatasourceDomain;
import com.artframework.domain.web.generator.domain.datasource.service.DatasourceService;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.domain.PageDomain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据源应用服务
 * 
 * @author auto
 * @version v1.0
 */
@Service
public class DatasourceApplicationService {

  @Autowired
  private DatasourceService datasourceService;

  /**
   * 分页查询数据源
   * 
   * @param request 分页查询请求
   * @return 分页结果
   */
  public PageResult<DatasourceResponse> page(DatasourcePageRequest request) {
    // 构建查询条件
    LambdaQuery<DatasourceDomain> query = LambdaQuery.of(DatasourceDomain.class);

    // 动态添加查询条件
    if (StrUtil.isNotBlank(request.getCode())) {
      query.like(DatasourceDomain::getCode, request.getCode());
    }
    if (StrUtil.isNotBlank(request.getName())) {
      query.like(DatasourceDomain::getName, request.getName());
    }
    if (StrUtil.isNotBlank(request.getDbType())) {
      query.eq(DatasourceDomain::getDbType, request.getDbType());
    }

    // 添加排序
    if (StrUtil.isNotBlank(request.getOrderField())) {
      query.orderBy(request.getOrderField(), Order.getOrder(request.getOrderBy()));
    } else {
      query.orderBy(DatasourceDomain::getCreatedTime, Order.DESC);
    }

    // 构建分页对象
    PageDomain pageDomain = new PageDomain();
    pageDomain.setPageNum(request.getPageNum());
    pageDomain.setPageSize(request.getPageSize());

    // 执行分页查询
    IPage<DatasourceDomain> page = datasourceService.queryPage(DatasourceDomain.class, pageDomain, query);

    // 转换结果
    List<DatasourceResponse> responseList = page.getRecords().stream()
        .map(DatasourceConvertor.INSTANCE::toResponse)
        .collect(Collectors.toList());

    return PageResult.of(responseList, page.getTotal(), page.getSize(), page.getCurrent());
  }

  /**
   * 根据ID获取数据源详情
   * 
   * @param id 数据源ID
   * @return 数据源详情
   */
  public DatasourceResponse getById(Integer id) {
    DatasourceDomain domain = DatasourceDomain.load(id, datasourceService);
    if (domain == null) {
      throw new RuntimeException("数据源不存在");
    }
    return DatasourceConvertor.INSTANCE.toResponse(domain);
  }

  /**
   * 新增数据源
   * 
   * @param request 新增请求
   * @return 数据源ID
   */
  @Transactional
  public Integer add(DatasourceAddRequest request) {
    // 检查编码是否重复
    checkCodeUnique(request.getCode(), null);

    // 转换为领域对象
    DatasourceDomain domain = DatasourceConvertor.INSTANCE.toDomain(request);

    // 插入数据
    return datasourceService.insert(domain);
  }

  /**
   * 编辑数据源
   * 
   * @param request 编辑请求
   * @return 是否成功
   */
  @Transactional
  public Boolean edit(DatasourceEditRequest request) {
    // 获取原始数据
    DatasourceDomain originalDomain = DatasourceDomain.load(request.getId(), datasourceService);
    if (originalDomain == null) {
      throw new RuntimeException("数据源不存在");
    }

    // 检查编码是否重复
    checkCodeUnique(request.getCode(), request.getId());

    // 转换为新的领域对象
    DatasourceDomain newDomain = DatasourceConvertor.INSTANCE.toDomain(request);

    // 执行更新
    return datasourceService.update(newDomain, originalDomain);
  }

  /**
   * 删除数据源
   * 
   * @param id 数据源ID
   * @return 是否成功
   */
  @Transactional
  public Boolean delete(Integer id) {
    // 检查数据源是否存在
    DatasourceDomain domain = DatasourceDomain.load(id, datasourceService);
    if (domain == null) {
      throw new RuntimeException("数据源不存在");
    }

    // 执行删除
    return datasourceService.delete(id);
  }

  /**
   * 测试数据源连接
   * 
   * @param request 数据源配置
   * @return 是否连接成功
   */
  public Boolean testConnection(DatasourceAddRequest request) {
    // TODO: 实现数据源连接测试逻辑
    // 可以使用 JDBC 驱动测试连接
    return true;
  }

  /**
   * 加载数据源表结构
   * 
   * @param id 数据源ID
   * @return 是否成功
   */
  @Transactional
  public Boolean loadTableStructure(Integer id) {
    // 获取数据源信息
    DatasourceDomain domain = DatasourceDomain.load(id, datasourceService);
    if (domain == null) {
      throw new RuntimeException("数据源不存在");
    }

    // TODO: 实现加载表结构逻辑
    // 使用 MyBatis-Plus Generator 功能加载表结构
    return true;
  }

  /**
   * 检查编码唯一性
   * 
   * @param code      编码
   * @param excludeId 排除的ID
   */
  private void checkCodeUnique(String code, Integer excludeId) {
    LambdaQuery<DatasourceDomain> query = LambdaQuery.of(DatasourceDomain.class)
        .eq(DatasourceDomain::getCode, code);

    if (excludeId != null) {
      query.ne(DatasourceDomain::getId, excludeId);
    }

    DatasourceDomain existDomain = datasourceService.queryOne(DatasourceDomain.class, query);
    if (existDomain != null) {
      throw new RuntimeException("数据源编码已存在");
    }
  }
}