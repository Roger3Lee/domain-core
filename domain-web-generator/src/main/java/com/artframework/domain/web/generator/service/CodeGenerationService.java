package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.dto.CodeGenerationResult;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;

/**
 * 代码生成服务接口
 * 
 * @author auto
 * @version v1.0
 */
public interface CodeGenerationService {

    /**
     * 根据领域模型生成代码（使用默认数据源配置）
     * 
     * @param domain 领域模型
     * @return 结构化的生成结果
     */
    CodeGenerationResult generateCode(DDDDomain domain);

    /**
     * 根据领域模型和数据源配置生成代码
     * 
     * @param domain 领域模型
     * @param dataSourceConfig 数据源配置
     * @return 结构化的生成结果
     */
    CodeGenerationResult generateCode(DDDDomain domain, DataSourceConfig dataSourceConfig);
}