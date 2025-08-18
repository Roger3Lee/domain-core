package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 领域模型连线详细配置 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "领域模型连线详细配置DTO")
public class DomainConfigLineConfigDTO {

  @ApiModelProperty(value = "主键")
  private Integer id;

  @ApiModelProperty(value = "项目ID")
  private Integer projectId;

  @ApiModelProperty(value = "领域ID")
  private Integer domainId;

  @ApiModelProperty(value = "创建人")
  private String createdBy;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createdTime;

  @ApiModelProperty(value = "更新人")
  private String updatedBy;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updatedTime;

  @ApiModelProperty(value = "连线编码")
  private String lineCode;

  @ApiModelProperty(value = "源表列")
  private String sourceColunm;

  @ApiModelProperty(value = "源表列的值（在常量关联时使用）")
  private String sourceColumnValue;

  @ApiModelProperty(value = "源表列值的类型（在常量关联时使用）")
  private String sourceColumnValueDataType;

  @ApiModelProperty(value = "目标列")
  private String targetColunm;

  @ApiModelProperty(value = "目标表列的值（在常量关联时使用）")
  private String targetColumnValue;

  @ApiModelProperty(value = "目标列值的类型（在常量关联时使用）")
  private String targetColumnValueDataType;
}
