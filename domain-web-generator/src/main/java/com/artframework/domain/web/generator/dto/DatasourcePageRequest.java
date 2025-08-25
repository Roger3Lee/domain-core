package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 数据源分页查询请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据源分页查询请求")
public class DatasourcePageRequest extends PageRequest {

    @ApiModelProperty(value = "数据源编码（模糊查询）")
    private String code;

    @ApiModelProperty(value = "数据源名称（模糊查询）")
    private String name;

    @ApiModelProperty(value = "数据库类型（模糊查询）")
    private String dbType;

    public DatasourcePageRequest(Integer pageNum, Integer pageSize, String code, String name, String dbType) {
        super(pageNum, pageSize);
        this.code = code;
        this.name = name;
        this.dbType = dbType;
    }
}
