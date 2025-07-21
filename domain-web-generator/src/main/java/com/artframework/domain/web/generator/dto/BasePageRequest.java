package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 基础分页请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "基础分页请求")
public class BasePageRequest {

  @ApiModelProperty(value = "页码", example = "1")
  private Integer pageNum = 1;

  @ApiModelProperty(value = "每页大小", example = "10")
  private Integer pageSize = 10;

  @ApiModelProperty(value = "排序字段")
  private String orderField;

  @ApiModelProperty(value = "排序方向", example = "asc")
  private String orderBy = "asc";
}