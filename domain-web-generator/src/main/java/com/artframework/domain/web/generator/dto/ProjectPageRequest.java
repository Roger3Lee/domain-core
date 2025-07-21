package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目分页查询请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目分页查询请求")
public class ProjectPageRequest extends BasePageRequest {

  @ApiModelProperty(value = "项目名称")
  private String name;
}