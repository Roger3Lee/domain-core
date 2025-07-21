package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 领域模型分页查询请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "领域模型分页查询请求")
public class DomainPageRequest extends BasePageRequest {

  @ApiModelProperty(value = "项目ID", required = true)
  private Integer projectId;

  @ApiModelProperty(value = "领域名称")
  private String domainName;

  @ApiModelProperty(value = "目录")
  private String folder;
}