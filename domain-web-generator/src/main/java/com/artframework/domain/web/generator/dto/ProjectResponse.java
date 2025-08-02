package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 项目响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目响应")
public class ProjectResponse extends ProjectDTO {
    
    @ApiModelProperty(value = "领域模型列表")
    private List<DomainConfigDTO> domainConfigList;
}