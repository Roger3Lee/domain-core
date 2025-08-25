package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 数据源测试连接请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "数据源测试连接请求")
public class DatasourceTestConnectionRequest {

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
