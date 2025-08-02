package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 领域模型响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "领域模型响应")
public class DomainConfigResponse extends DomainConfigDTO {
    
    @ApiModelProperty(value = "项目信息")
    private ProjectDTO project;
} 