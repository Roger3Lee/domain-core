package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ER图保存请求
 * 
 * @author auto
 * @version v1.0
 */
@Data
@ApiModel(value = "ER图保存请求")
public class ERDiagramRequest {

  @ApiModelProperty(value = "领域模型ID", required = true)
  @NotNull(message = "领域模型ID不能为空")
  private Integer domainId;

  @ApiModelProperty(value = "表配置列表")
  @Valid
  private List<DomainConfigTablesDTO> tables;

  @ApiModelProperty(value = "连线配置列表")
  @Valid
  private List<DomainConfigLineDTO> lines;
}
