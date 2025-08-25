package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据源编辑请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据源编辑请求")
public class DatasourceEditRequest {

    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空")
    private Integer id;

    @ApiModelProperty(value = "乐观锁")
    private Integer revision;

    @ApiModelProperty(value = "数据源名称", required = true)
    @NotBlank(message = "数据源名称不能为空")
    private String name;

    @ApiModelProperty(value = "编码", required = true)
    @NotBlank(message = "编码不能为空")
    private String code;

    @ApiModelProperty(value = "数据库类型", required = true, allowableValues = "mysql,postgresql,polardb")
    @NotBlank(message = "数据库类型不能为空")
    private String dbType;

    @ApiModelProperty(value = "数据库连接", required = true)
    @NotBlank(message = "数据库连接不能为空")
    private String url;

    @ApiModelProperty(value = "数据库用户", required = true)
    @NotBlank(message = "数据库用户不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "schema")
    private String schema;
}
