package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源响应
 * 
 * @author auto
 * @version v1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "数据源响应")
public class DatasourceResponse extends DatasourceConfigDTO {

  @Override
  @ApiModelProperty(value = "密码", hidden = true)
  public String getPassword() {
    // 出于安全考虑，不返回密码
    return "******";
  }
}