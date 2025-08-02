package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.dto.*;

/**
 * 项目应用服务接口
 * 
 * @author auto
 * @version v1.0
 */
public interface ProjectAppService {

    /**
     * 分页查询项目
     * 
     * @param request 分页查询请求
     * @return 分页结果
     */
    PageResult<ProjectResponse> page(ProjectPageRequest request);

    /**
     * 新增项目
     * 
     * @param request 新增项目请求
     * @return 项目ID
     */
    Integer addProject(ProjectAddRequest request);

    /**
     * 编辑项目
     * 
     * @param request 编辑项目请求
     * @return 是否成功
     */
    Boolean editProject(ProjectEditRequest request);

    /**
     * 获取项目详情
     * 
     * @param id 项目ID
     * @return 项目详情
     */
    ProjectResponse getProjectDetail(Integer id);

    /**
     * 获取项目详情（包含领域模型）
     * 
     * @param id 项目ID
     * @return 项目详情（包含领域模型）
     */
    ProjectResponse getProjectDetailWithDomains(Integer id);

    /**
     * 删除项目
     * 
     * @param id 项目ID
     * @return 是否成功
     */
    Boolean deleteProject(Integer id);
} 