package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.application.DomainModelApplicationService;
import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 领域模型管理控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "领域模型管理")
@RestController
@RequestMapping("/api/v1/domain")
public class DDDController {

    @Autowired
    private DomainModelApplicationService applicationService;

    @PostMapping("/page")
    @ApiOperation("分页查询领域模型")
    public ApiResponse<PageResult<DomainConfigDTO>> page(@RequestBody @Validated DomainPageRequest request) {
        PageResult<DomainConfigDTO> pageResult = applicationService.page(request);
        return ApiResponse.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取领域模型详情")
    public ApiResponse<DomainConfigDTO> detail(@PathVariable Integer id) {
        DomainConfigDTO response = applicationService.getById(id);
        return ApiResponse.success(response);
    }

    @PostMapping("/add")
    @ApiOperation("新增领域模型")
    public ApiResponse<Integer> add(@RequestBody @Validated DomainConfigDTO request) {
        Integer id = applicationService.add(request);
        return ApiResponse.success(id);
    }

    @PutMapping("/edit")
    @ApiOperation("编辑领域模型")
    public ApiResponse<Boolean> edit(@RequestBody @Validated DomainConfigDTO request) {
        Boolean result = applicationService.edit(request);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除领域模型")
    public ApiResponse<Boolean> delete(@PathVariable Integer id) {
        Boolean result = applicationService.delete(id);
        return ApiResponse.success(result);
    }

    @PostMapping("/{id}/generate")
    @ApiOperation("生成领域模型代码")
    public ApiResponse<Boolean> generateCode(@PathVariable Integer id) {
        Boolean result = applicationService.generateCode(id);
        return ApiResponse.success(result);
    }


    public static void main(String[] args) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        Boolean result = pathMatcher.match("/notarial-dev/system/notarial-regulars/v1/{regularsId}/group/{groupId}", "/notarial-dev/system/notarial-regulars/v1/104/group/batch/R_GROUP_JJLGxjhWM1Vq_GSHDa7bm");
    }

}
