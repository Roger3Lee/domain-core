package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 数据源新增请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "数据源新增请求")
public class DatasourceAddRequest extends DatasourceConfigDTO {

  @Override
  @NotBlank(message = "数据源名称不能为空")
  @ApiModelProperty(value = "数据源名称", required = true)
  public String getName() {
    return super.getName();
  }

  @Override
  @NotBlank(message = "编码不能为空")
  @ApiModelProperty(value = "编码", required = true)
  public String getCode() {
    return super.getCode();
  }

  @Override
  @NotBlank(message = "数据库类型不能为空")
  @ApiModelProperty(value = "数据库类型", required = true)
  public String getDbType() {
    return super.getDbType();
  }

  @Override
  @NotBlank(message = "数据库连接不能为空")
  @ApiModelProperty(value = "数据库连接", required = true)
  public String getUrl() {
    return super.getUrl();
  }

  @Override
  @NotBlank(message = "数据库用户不能为空")
  @ApiModelProperty(value = "数据库用户", required = true)
  public String getUserName() {
    return super.getUserName();
  }

  @Override
  @NotBlank(message = "密码不能为空")
  @ApiModelProperty(value = "密码", required = true)
  public String getPassword() {
    return super.getPassword();
  }
}