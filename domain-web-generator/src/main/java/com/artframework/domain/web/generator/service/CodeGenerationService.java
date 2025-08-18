package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.dto.CodeGenerationResult;

public interface CodeGenerationService {
    
    /**
     * 生成代码
     * @param domain 领域模型
     * @return 代码生成结果
     */
    CodeGenerationResult generateCode(DDDDomain domain);
    
    /**
     * 生成代码并返回ZIP压缩包
     * @param domain 领域模型
     * @return ZIP文件字节数组
     */
    byte[] generateCodeAsZip(DDDDomain domain);
    
    /**
     * 根据领域模型ID生成代码
     * @param domainId 领域模型ID
     * @return 代码生成结果
     */
    CodeGenerationResult generateCodeById(Integer domainId);
    
    /**
     * 根据领域模型ID生成代码并返回ZIP压缩包
     * @param domainId 领域模型ID
     * @return ZIP文件字节数组
     */
    byte[] generateCodeAsZipById(Integer domainId);
}