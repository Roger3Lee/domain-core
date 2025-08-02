package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.service.DomainConfigAppService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 领域模型控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "领域模型管理")
@RestController
@RequestMapping("/api/v1/domain-configs")
public class DomainConfigController {

    @Autowired
    private DomainConfigAppService domainConfigAppService;

    @PostMapping("/page")
    @ApiOperation("分页查询领域模型")
    public ApiResponse<PageResult<DomainConfigResponse>> page(
            @RequestBody @Valid DomainPageRequest request) {
        PageResult<DomainConfigResponse> pageResult = domainConfigAppService.page(request);
        return ApiResponse.success(pageResult);
    }

    @PostMapping
    @ApiOperation("新增领域模型")
    public ApiResponse<Integer> addDomainConfig(
            @RequestBody @Valid DomainConfigAddRequest request) {
        Integer domainId = domainConfigAppService.addDomainConfig(request);
        return ApiResponse.success(domainId);
    }

    @PutMapping
    @ApiOperation("编辑领域模型")
    public ApiResponse<Boolean> editDomainConfig(
            @RequestBody @Valid DomainConfigEditRequest request) {
        Boolean result = domainConfigAppService.editDomainConfig(request);
        return ApiResponse.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取领域模型详情")
    public ApiResponse<DomainConfigResponse> getDomainConfigDetail(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        DomainConfigResponse response = domainConfigAppService.getDomainConfigDetail(id);
        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除领域模型")
    public ApiResponse<Boolean> deleteDomainConfig(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        Boolean result = domainConfigAppService.deleteDomainConfig(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/generate-code")
    @ApiOperation("生成代码")
    public ApiResponse<String> generateCode(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        String code = domainConfigAppService.generateCode(id);
        return ApiResponse.success(code);
    }
} 