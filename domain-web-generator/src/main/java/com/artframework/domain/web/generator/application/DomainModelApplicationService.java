package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.ddd.service.DDDService;
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
 * 领域模型应用服务
 * 
 * @author auto
 * @version v1.0
 */
@Service
public class DomainModelApplicationService {

  @Autowired
  private DDDService dddService;

  /**
   * 分页查询领域模型
   * 
   * @param request 分页查询请求
   * @return 分页结果
   */
  public PageResult<DomainConfigDTO> page(DomainPageRequest request) {
    // 构建查询条件
    LambdaQuery<DDDDomain> query = LambdaQuery.of(DDDDomain.class)
        .eq(DDDDomain::getProjectId, request.getProjectId());

    // 动态添加查询条件
    if (StrUtil.isNotBlank(request.getDomainName())) {
      query.like(DDDDomain::getDomainName, request.getDomainName());
    }
    if (StrUtil.isNotBlank(request.getFolder())) {
      query.like(DDDDomain::getFolder, request.getFolder());
    }

    // 添加排序
    if (StrUtil.isNotBlank(request.getOrderField())) {
      query.orderBy(request.getOrderField(), Order.getOrder(request.getOrderBy()));
    } else {
      query.orderBy(DDDDomain::getCreatedTime, Order.DESC);
    }

    // 构建分页对象
    PageDomain pageDomain = new PageDomain();
    pageDomain.setPageNum(request.getPageNum());
    pageDomain.setPageSize(request.getPageSize());

    // 执行分页查询
    IPage<DDDDomain> page = dddService.queryPage(DDDDomain.class, pageDomain, query);

    // 转换结果
    List<DomainConfigDTO> responseList = page.getRecords().stream()
        .map(this::toDomainConfigDTO)
        .collect(Collectors.toList());

    return PageResult.of(responseList, page.getTotal(), page.getSize(), page.getCurrent());
  }

  /**
   * 根据ID获取领域模型详情
   * 
   * @param id 领域模型ID
   * @return 领域模型详情
   */
  public DomainConfigDTO getById(Integer id) {
    DDDDomain domain = DDDDomain.load(id, dddService);
    if (domain == null) {
      throw new RuntimeException("领域模型不存在");
    }
    return toDomainConfigDTO(domain);
  }

  /**
   * 新增领域模型
   * 
   * @param request 新增请求
   * @return 领域模型ID
   */
  @Transactional
  public Integer add(DomainConfigDTO request) {
    // 检查领域名称在项目内是否重复
    checkDomainNameUnique(request.getProjectId(), request.getDomainName(), null);

    // 转换为领域对象
    DDDDomain domain = toDDDDomain(request);

    // 插入数据
    return dddService.insert(domain);
  }

  /**
   * 编辑领域模型
   * 
   * @param request 编辑请求
   * @return 是否成功
   */
  @Transactional
  public Boolean edit(DomainConfigDTO request) {
    // 获取原始数据
    DDDDomain originalDomain = DDDDomain.load(request.getId(), dddService);
    if (originalDomain == null) {
      throw new RuntimeException("领域模型不存在");
    }

    // 检查领域名称在项目内是否重复
    checkDomainNameUnique(request.getProjectId(), request.getDomainName(), request.getId());

    // 转换为新的领域对象
    DDDDomain newDomain = toDDDDomain(request);

    // 设置LoadFlag，需要更新关联的表和线配置
    newDomain.setLoadFlag(DDDDomain.LoadFlag.builder()
        .loadDomainConfigTables(true)
        .loadDomainConfigLine(true)
        .loadDomainConfigLineConfig(true)
        .build());

    // 执行更新
    return dddService.update(newDomain, originalDomain, false);
  }

  /**
   * 删除领域模型
   * 
   * @param id 领域模型ID
   * @return 是否成功
   */
  @Transactional
  public Boolean delete(Integer id) {
    // 检查领域模型是否存在
    DDDDomain domain = DDDDomain.load(id, dddService);
    if (domain == null) {
      throw new RuntimeException("领域模型不存在");
    }

    // 执行删除（会级联删除关联的表和线配置）
    return dddService.delete(id);
  }

  /**
   * 生成领域模型代码
   * 
   * @param id 领域模型ID
   * @return 是否成功
   */
  public Boolean generateCode(Integer id) {
    // 获取领域模型详情
    DDDDomain domain = DDDDomain.load(id, dddService);
    if (domain == null) {
      throw new RuntimeException("领域模型不存在");
    }

    // 加载关联数据
    domain.loadRelated(DDDDomain.DomainConfigTablesDomain.class);
    domain.loadRelated(DDDDomain.DomainConfigLineDomain.class, query -> {
      query.orderBy(DDDDomain.DomainConfigLineDomain::getId, Order.ASC);
    });

    // TODO: 实现代码生成逻辑
    // 1. 根据领域模型XML生成领域代码
    // 2. 生成DO、Mapper、Controller等代码
    // 3. 可以调用 domain-generator 模块的生成功能

    return true;
  }

  /**
   * 检查领域名称在项目内唯一性
   * 
   * @param projectId  项目ID
   * @param domainName 领域名称
   * @param excludeId  排除的ID
   */
  private void checkDomainNameUnique(Integer projectId, String domainName, Integer excludeId) {
    LambdaQuery<DDDDomain> query = LambdaQuery.of(DDDDomain.class)
        .eq(DDDDomain::getProjectId, projectId)
        .eq(DDDDomain::getDomainName, domainName);

    if (excludeId != null) {
      query.ne(DDDDomain::getId, excludeId);
    }

    DDDDomain existDomain = dddService.queryOne(DDDDomain.class, query);
    if (existDomain != null) {
      throw new RuntimeException("领域名称在该项目中已存在");
    }
  }

  /**
   * 领域对象转DTO
   */
  private DomainConfigDTO toDomainConfigDTO(DDDDomain domain) {
    DomainConfigDTO dto = new DomainConfigDTO();
    dto.setId(domain.getId());
    dto.setProjectId(domain.getProjectId());
    dto.setRevision(domain.getRevision());
    dto.setCreatedBy(domain.getCreatedBy());
    dto.setCreatedTime(domain.getCreatedTime());
    dto.setUpdatedBy(domain.getUpdatedBy());
    dto.setUpdatedTime(domain.getUpdatedTime());
    dto.setDomainName(domain.getDomainName());
    dto.setDomainXml(domain.getDomainXml());
    dto.setMainTable(domain.getMainTable());
    dto.setFolder(domain.getFolder());
    return dto;
  }

  /**
   * DTO转领域对象
   */
  private DDDDomain toDDDDomain(DomainConfigDTO dto) {
    DDDDomain domain = new DDDDomain();
    domain.setId(dto.getId());
    domain.setProjectId(dto.getProjectId());
    domain.setRevision(dto.getRevision());
    domain.setCreatedBy(dto.getCreatedBy());
    domain.setCreatedTime(dto.getCreatedTime());
    domain.setUpdatedBy(dto.getUpdatedBy());
    domain.setUpdatedTime(dto.getUpdatedTime());
    domain.setDomainName(dto.getDomainName());
    domain.setDomainXml(dto.getDomainXml());
    domain.setMainTable(dto.getMainTable());
    domain.setFolder(dto.getFolder());
    return domain;
  }
}