package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

/**
 * 领域模型编辑请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "领域模型编辑请求")
public class DomainConfigEditRequest extends DomainConfigDTO {
    
    @NotNull(message = "ID不能为空")
    @ApiModelProperty(value = "ID", required = true)
    private Integer id;
    
    @NotNull(message = "项目ID不能为空")
    @ApiModelProperty(value = "项目ID", required = true)
    private Integer projectId;
    
    @NotBlank(message = "领域名称不能为空")
    @ApiModelProperty(value = "领域名称", required = true)
    private String domainName;
    
    @NotBlank(message = "主表不能为空")
    @ApiModelProperty(value = "主表", required = true)
    private String mainTable;
} 