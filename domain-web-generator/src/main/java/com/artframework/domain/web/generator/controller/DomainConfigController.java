package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.service.DomainConfigAppService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    private static final Logger log = LoggerFactory.getLogger(DomainConfigController.class);

    @Autowired
    private DomainConfigAppService domainConfigAppService;

    @GetMapping("/page")
    @ApiOperation("分页查询领域模型")
    public PageResult<DomainConfigResponse> page(
            @RequestParam Integer projectId,
            @RequestParam(required = false) String domainName,
            @RequestParam(required = false) String folder,
            @RequestParam(defaultValue = "1") Long pageNum,
            @RequestParam(defaultValue = "10") Long pageSize) {
        DomainPageRequest request = new DomainPageRequest();
        request.setProjectId(projectId);
        request.setDomainName(domainName);
        request.setFolder(folder);
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        return domainConfigAppService.page(request);
    }

    @PostMapping
    @ApiOperation("新增领域模型")
    public Integer addDomainConfig(
            @RequestBody @Valid DomainConfigAddRequest request) {
        return domainConfigAppService.addDomainConfig(request);
    }

    @PutMapping
    @ApiOperation("编辑领域模型")
    public Boolean editDomainConfig(
            @RequestBody @Valid DomainConfigEditRequest request) {
        return domainConfigAppService.editDomainConfig(request);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取领域模型详情")
    public DomainConfigResponse getDomainConfigDetail(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        return domainConfigAppService.getDomainConfigDetail(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除领域模型")
    public Boolean deleteDomainConfig(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        return domainConfigAppService.deleteDomainConfig(id);
    }

    @PostMapping("/{id}/generate-code")
    @ApiOperation("生成代码")
    public CodeGenerationResult generateCode(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        return domainConfigAppService.generateCode(id);
    }

    @GetMapping("/{id}/download-code")
    @ApiOperation("下载生成的代码文件压缩包")
    public ResponseEntity<byte[]> downloadGeneratedCode(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        try {
            byte[] zipData = domainConfigAppService.downloadGeneratedCode(id);
            String filename = "generated-code-" + id + ".zip";
            
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .header("Content-Type", "application/zip")
                    .body(zipData);
        } catch (Exception e) {
            log.error("下载代码文件失败，领域ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/er-diagram")
    @ApiOperation("获取领域模型ER图信息")
    public ERDiagramRequest getERDiagram(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        return domainConfigAppService.getERDiagram(id);
    }

    @PostMapping("/{id}/er-diagram")
    @ApiOperation("保存领域模型ER图信息")
    public Boolean saveERDiagram(
            @ApiParam("领域模型ID") @PathVariable Integer id,
            @RequestBody @Valid ERDiagramRequest request) {
        request.setDomainId(id);
        return domainConfigAppService.saveERDiagram(request);
    }

    @PostMapping("/{id}/generate-xml")
    @ApiOperation("基于ER图生成领域模型XML")
    public String generateDomainXml(
            @ApiParam("领域模型ID") @PathVariable Integer id) {
        return domainConfigAppService.generateDomainXml(id);
    }
}