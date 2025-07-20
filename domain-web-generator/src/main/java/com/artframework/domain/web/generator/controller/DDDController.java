package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.application.DDDApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DDD领域模型管理控制器
 */
@RestController
@RequestMapping("/api/ddd")
@RequiredArgsConstructor
@Api(tags = "DDD领域模型管理")
public class DDDController {

    private final DDDApplicationService dddApplicationService;

    @GetMapping("/list")
    @ApiOperation("获取领域模型列表")
    public List<DDDDomain> list() {
        return dddApplicationService.list();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取领域模型")
    public DDDDomain getById(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return dddApplicationService.getById(id);
    }

    @PostMapping
    @ApiOperation("创建领域模型")
    public Integer create(@RequestBody DDDDomain dddDomain) {
        return dddApplicationService.create(dddDomain);
    }

    @PutMapping("/{id}")
    @ApiOperation("更新领域模型")
    public Boolean update(@ApiParam("领域模型ID") @PathVariable Integer id,
            @RequestBody DDDDomain dddDomain) {
        return dddApplicationService.update(id, dddDomain);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除领域模型")
    public Boolean delete(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return dddApplicationService.delete(id);
    }

    @PostMapping("/{id}/generate-code")
    @ApiOperation("生成领域代码")
    public Boolean generateCode(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return codeGenerationService.generateAllCode(id, null);
    }

    @PostMapping("/{id}/generate-domain")
    @ApiOperation("生成领域模型代码")
    public Boolean generateDomainCode(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return codeGenerationService.generateDomainCode(id, null);
    }

    @PostMapping("/{id}/generate-do")
    @ApiOperation("生成DO代码")
    public Boolean generateDOCode(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return codeGenerationService.generateDOCode(id, null);
    }

    @PostMapping("/{id}/generate-mapper")
    @ApiOperation("生成Mapper代码")
    public Boolean generateMapperCode(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return codeGenerationService.generateMapperCode(id, null);
    }

    @PostMapping("/{id}/generate-controller")
    @ApiOperation("生成Controller代码")
    public Boolean generateControllerCode(@ApiParam("领域模型ID") @PathVariable Integer id) {
        return codeGenerationService.generateControllerCode(id, null);
    }

    @GetMapping("/{id}/lines")
    @ApiOperation("获取领域模型关联连线")
    public List<DDDDomain.DomainConfigLineDomain> getDomainLines(@ApiParam("领域模型ID") @PathVariable Integer id) {
        DDDFindDomain request = new DDDFindDomain();
        request.setKey(id);
        DDDDomain.LoadFlag loadFlag = DDDDomain.LoadFlag.builder()
                .loadDomainConfigLineDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DDDDomain dddDomain = dddService.find(request);
        return dddDomain != null ? dddDomain.getDomainConfigLineList() : null;
    }

    @GetMapping("/{id}/line-configs")
    @ApiOperation("获取领域模型连线配置")
    public List<DDDDomain.DomainConfigLineConfigDomain> getDomainLineConfigs(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        DDDFindDomain request = new DDDFindDomain();
        request.setKey(id);
        DDDDomain.LoadFlag loadFlag = DDDDomain.LoadFlag.builder()
                .loadDomainConfigLineConfigDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DDDDomain dddDomain = dddService.find(request);
        return dddDomain != null ? dddDomain.getDomainConfigLineConfigList() : null;
    }
}