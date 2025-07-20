package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;

/**
 * 代码生成服务
 */
public interface CodeGenerationService {

    /**
     * 生成领域代码
     * 
     * @param domainId  领域模型ID
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    Boolean generateDomainCode(Integer domainId, Integer projectId);

    /**
     * 生成DO代码
     * 
     * @param domainId  领域模型ID
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    Boolean generateDOCode(Integer domainId, Integer projectId);

    /**
     * 生成Mapper代码
     * 
     * @param domainId  领域模型ID
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    Boolean generateMapperCode(Integer domainId, Integer projectId);

    /**
     * 生成Controller代码
     * 
     * @param domainId  领域模型ID
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    Boolean generateControllerCode(Integer domainId, Integer projectId);

    /**
     * 生成所有代码
     * 
     * @param domainId  领域模型ID
     * @param projectId 项目ID
     * @return 是否生成成功
     */
    Boolean generateAllCode(Integer domainId, Integer projectId);

    /**
     * 根据领域模型生成代码
     * 
     * @param dddDomain 领域模型
     * @param project   项目配置
     * @return 是否生成成功
     */
    Boolean generateCode(DDDDomain dddDomain, ProjectDomain project);
}