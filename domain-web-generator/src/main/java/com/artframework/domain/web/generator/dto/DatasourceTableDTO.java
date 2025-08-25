package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 数据表DTO
 * 
 * @author auto
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据表DTO")
public class DatasourceTableDTO {

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

    @ApiModelProperty(value = "数据源ID")
    private Integer dsId;

    @ApiModelProperty(value = "表名")
    private String name;

    @ApiModelProperty(value = "表描述")
    private String comment;
}
