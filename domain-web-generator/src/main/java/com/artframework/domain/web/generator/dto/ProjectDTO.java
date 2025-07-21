package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "项目DTO")
public class ProjectDTO {

  @ApiModelProperty(value = "主键")
  private Integer id;

  @ApiModelProperty(value = "创建人")
  private String createdBy;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createdTime;

  @ApiModelProperty(value = "更新人")
  private String updatedBy;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updatedTime;

  @ApiModelProperty(value = "项目名称")
  private String name;

  @ApiModelProperty(value = "领域package")
  private String domainPackage;

  @ApiModelProperty(value = "控制器package")
  private String controllerPackage;

  @ApiModelProperty(value = "DO package")
  private String doPackage;

  @ApiModelProperty(value = "Mapper package")
  private String mapperPackage;
}