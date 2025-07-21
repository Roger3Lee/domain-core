package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 领域模型配置 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "领域模型配置DTO")
public class DomainConfigDTO {

  @ApiModelProperty(value = "主键")
  private Integer id;

  @ApiModelProperty(value = "项目ID")
  private Integer projectId;

  @ApiModelProperty(value = "乐观锁")
  private Integer revision;

  @ApiModelProperty(value = "创建人")
  private String createdBy;

  @ApiModelProperty(value = "创建时间")
  private LocalDateTime createdTime;

  @ApiModelProperty(value = "更新人")
  private String updatedBy;

  @ApiModelProperty(value = "更新时间")
  private LocalDateTime updatedTime;

  @ApiModelProperty(value = "领域名称")
  private String domainName;

  @ApiModelProperty(value = "领域模型XML")
  private String domainXml;

  @ApiModelProperty(value = "主表")
  private String mainTable;

  @ApiModelProperty(value = "目录")
  private String folder;
}