package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源分页查询请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "数据源分页查询请求")
public class DatasourcePageRequest extends BasePageRequest {

  @ApiModelProperty(value = "数据源编码")
  private String code;

  @ApiModelProperty(value = "数据源名称")
  private String name;

  @ApiModelProperty(value = "数据库类型")
  private String dbType;
}