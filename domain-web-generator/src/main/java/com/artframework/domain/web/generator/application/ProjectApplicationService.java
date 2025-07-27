package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.convertor.ProjectConvertor;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
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
 * 项目应用服务
 * 
 * @author auto
 * @version v1.0
 */
@Service
public class ProjectApplicationService {

  @Autowired
  private ProjectService projectService;


  /**
   * 分页查询项目
   * 
   * @param request 分页查询请求
   * @return 分页结果
   */
  public PageResult<ProjectResponse> page(ProjectPageRequest request) {
    // 构建查询条件
    LambdaQuery<ProjectDomain> query = LambdaQuery.of(ProjectDomain.class);

    // 动态添加查询条件
    if (StrUtil.isNotBlank(request.getName())) {
      query.like(ProjectDomain::getName, request.getName());
    }

    // 添加排序
    if (StrUtil.isNotBlank(request.getOrderField())) {
      query.orderBy(request.getOrderField(), Order.getOrder(request.getOrderBy()));
    } else {
      query.orderBy(ProjectDomain::getCreatedTime, Order.DESC);
    }

    // 构建分页对象
    PageDomain pageDomain = new PageDomain();
    pageDomain.setPageNum(request.getPageNum());
    pageDomain.setPageSize(request.getPageSize());

    // 执行分页查询
    IPage<ProjectDomain> page = projectService.queryPage(ProjectDomain.class, pageDomain, query);

    // 转换结果
    List<ProjectResponse> responseList = page.getRecords().stream()
        .map(ProjectConvertor.INSTANCE::toResponse)
        .collect(Collectors.toList());

    return PageResult.of(responseList, page.getTotal(), page.getSize(), page.getCurrent());
  }

  /**
   * 根据ID获取项目详情
   * 
   * @param id 项目ID
   * @return 项目详情
   */
  public ProjectResponse getById(Integer id) {
    ProjectDomain domain = ProjectDomain.load(id, projectService);
    if (domain == null) {
      throw new RuntimeException("项目不存在");
    }
    return ProjectConvertor.INSTANCE.toResponse(domain);
  }

  /**
   * 新增项目
   * 
   * @param request 新增请求
   * @return 项目ID
   */
  @Transactional
  public Integer add(ProjectAddRequest request) {
    // 检查项目名称是否重复
    checkNameUnique(request.getName(), null);

    // 转换为领域对象
    ProjectDomain domain = ProjectConvertor.INSTANCE.toDomain(request);

    // 插入数据
    return projectService.insert(domain);
  }

  /**
   * 编辑项目
   * 
   * @param request 编辑请求
   * @return 是否成功
   */
  @Transactional
  public Boolean edit(ProjectEditRequest request) {
    // 获取原始数据
    ProjectDomain originalDomain = ProjectDomain.load(request.getId(), projectService);
    if (originalDomain == null) {
      throw new RuntimeException("项目不存在");
    }

    // 检查项目名称是否重复
    checkNameUnique(request.getName(), request.getId());

    // 转换为新的领域对象
    ProjectDomain newDomain = ProjectConvertor.INSTANCE.toDomain(request);

    // 执行更新
    return projectService.update(newDomain, originalDomain);
  }

  /**
   * 删除项目
   * 
   * @param id 项目ID
   * @return 是否成功
   */
  @Transactional
  public Boolean delete(Integer id) {
    // 检查项目是否存在
    ProjectDomain domain = ProjectDomain.load(id, projectService);
    if (domain == null) {
      throw new RuntimeException("项目不存在");
    }

    // TODO: 检查项目下是否有领域模型，如果有则不允许删除

    // 执行删除
    return projectService.delete(id);
  }

  /**
   * 检查项目名称唯一性
   * 
   * @param name      项目名称
   * @param excludeId 排除的ID
   */
  private void checkNameUnique(String name, Integer excludeId) {
    LambdaQuery<ProjectDomain> query = LambdaQuery.of(ProjectDomain.class)
        .eq(ProjectDomain::getName, name);

    if (excludeId != null) {
      query.ne(ProjectDomain::getId, excludeId);
    }

    ProjectDomain existDomain = projectService.queryOne(ProjectDomain.class, query);
    if (existDomain != null) {
      throw new RuntimeException("项目名称已存在");
    }
  }
}