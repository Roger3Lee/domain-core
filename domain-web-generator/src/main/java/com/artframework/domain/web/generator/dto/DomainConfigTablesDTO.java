package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 领域模型表配置 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "领域模型表配置DTO")
public class DomainConfigTablesDTO {

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

  @ApiModelProperty(value = "表名")
  private String tableName;

  @ApiModelProperty(value = "位置X")
  private BigDecimal x;

  @ApiModelProperty(value = "位置Y")
  private BigDecimal y;

  @ApiModelProperty(value = "宽度")
  private BigDecimal w;

  @ApiModelProperty(value = "高度")
  private BigDecimal h;
}
