package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("生成文件分组")
public class CodeGenGroup {
	@ApiModelProperty("分组名称")
	private String name;

	@ApiModelProperty("文件列表")
	private List<CodeGenFile> files = new ArrayList<>();
}
