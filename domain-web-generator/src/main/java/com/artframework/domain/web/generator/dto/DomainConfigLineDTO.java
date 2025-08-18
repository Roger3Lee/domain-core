package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 领域模型连线配置 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "领域模型连线配置DTO")
public class DomainConfigLineDTO {

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

  @ApiModelProperty(value = "线条类型;FK:外键 REDUNDANCY:冗余")
  private String lineType;

  @ApiModelProperty(value = "源表")
  private String sourceTable;

  @ApiModelProperty(value = "源表列")
  private String sourceColunm;

  @ApiModelProperty(value = "目标表")
  private String targetTable;

  @ApiModelProperty(value = "目标列")
  private String targetColunm;

  @ApiModelProperty(value = "是否一对多")
  private String many;

  @ApiModelProperty(value = "连线配置列表")
  private List<DomainConfigLineConfigDTO> lineConfigs;
}
