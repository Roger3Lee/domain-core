package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 项目编辑请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目编辑请求")
public class ProjectEditRequest extends ProjectDTO {

  @Override
  @NotNull(message = "ID不能为空")
  @ApiModelProperty(value = "主键", required = true)
  public Integer getId() {
    return super.getId();
  }

  @Override
  @NotBlank(message = "项目名称不能为空")
  @ApiModelProperty(value = "项目名称", required = true)
  public String getName() {
    return super.getName();
  }

  @Override
  @NotBlank(message = "领域package不能为空")
  @ApiModelProperty(value = "领域package", required = true)
  public String getDomainPackage() {
    return super.getDomainPackage();
  }

  @Override
  @NotBlank(message = "控制器package不能为空")
  @ApiModelProperty(value = "控制器package", required = true)
  public String getControllerPackage() {
    return super.getControllerPackage();
  }

  @Override
  @NotBlank(message = "DO package不能为空")
  @ApiModelProperty(value = "DO package", required = true)
  public String getDoPackage() {
    return super.getDoPackage();
  }

  @Override
  @NotBlank(message = "Mapper package不能为空")
  @ApiModelProperty(value = "Mapper package", required = true)
  public String getMapperPackage() {
    return super.getMapperPackage();
  }
}