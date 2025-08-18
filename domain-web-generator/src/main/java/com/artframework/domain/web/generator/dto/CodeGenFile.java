package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("生成文件信息")
public class CodeGenFile {
	@ApiModelProperty("文件名")
	private String name;

	@ApiModelProperty("文件内容")
	private String content;
}
