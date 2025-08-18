package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据源表列 DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "数据源表列DTO")
public class DatasourceTableColumnDTO {

  @ApiModelProperty(value = "主键")
  private Integer id;

  @ApiModelProperty(value = "数据源ID")
  private Integer dsId;

  @ApiModelProperty(value = "表ID")
  private Integer tableId;

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

  @ApiModelProperty(value = "表名")
  private String tableName;

  @ApiModelProperty(value = "列名")
  private String name;

  @ApiModelProperty(value = "数据类型")
  private String type;

  @ApiModelProperty(value = "列备注")
  private String comment;

  @ApiModelProperty(value = "是否主键")
  private String key;
}
