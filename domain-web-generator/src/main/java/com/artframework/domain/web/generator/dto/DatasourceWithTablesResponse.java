package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * 数据源详情（包含表结构）响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据源详情（包含表结构）响应")
public class DatasourceWithTablesResponse extends DatasourceDetailResponse {

    @ApiModelProperty(value = "数据表列表")
    private List<DatasourceTableResponse> tables;

    @ApiModelProperty(value = "数据表列列表")
    private List<DatasourceTableColumnResponse> columns;
}
