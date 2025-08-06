package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;

/**
 * 代码生成服务接口
 * 
 * @author auto
 * @version v1.0
 */
public interface CodeGenerationService {

    /**
     * 根据领域模型生成代码
     * 
     * @param domain 领域模型
     * @return 生成的代码内容
     */
    String generateCode(DDDDomain domain);
}