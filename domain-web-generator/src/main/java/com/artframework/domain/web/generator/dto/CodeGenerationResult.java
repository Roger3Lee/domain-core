package com.artframework.domain.web.generator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("代码生成返回结构")
public class CodeGenerationResult {
	@ApiModelProperty("Controller 分组")
	private CodeGenGroup controller;

	@ApiModelProperty("Domain 分组 (包含domain, repository, service, lambdaexp, convertor 子分类)")
	private CodeGenerationResultDomain domain;

	@ApiModelProperty("DataObject 分组")
	private CodeGenGroup dataobject;

	@ApiModelProperty("Mapper 分组")
	private CodeGenGroup mapper;

	@Data
	@ApiModel("Domain分组详情")
	public static class CodeGenerationResultDomain {
		@ApiModelProperty("domain 目录")
		private CodeGenGroup domain;
		@ApiModelProperty("repository 目录")
		private CodeGenGroup repository;
		@ApiModelProperty("service 目录")
		private CodeGenGroup service;
		@ApiModelProperty("lambdaexp 目录")
		private CodeGenGroup lambdaexp;
		@ApiModelProperty("convertor 目录")
		private CodeGenGroup convertor;
	}
}
