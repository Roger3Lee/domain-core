package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * 项目详情（包含领域模型）响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目详情（包含领域模型）响应")
public class ProjectDetailWithDomainsResponse extends ProjectDTO {
    
    @ApiModelProperty(value = "数据源表列表")
    private List<DatasourceTableDTO> datasourceTables;
    
    @ApiModelProperty(value = "领域模型列表")
    private List<DomainConfigDTO> domainConfigList;
}
