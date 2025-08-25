package com.artframework.domain.web.generator.controller;

import com.artframework.domain.web.generator.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 数据源管理控制器
 * 
 * @author auto
 * @version v1.0
 */
@Api(tags = "数据源管理")
@RestController
@RequestMapping("/api/v1/datasources")
public class DatasourceController {

    // TODO: 注入应用服务
    // @Autowired
    // private DatasourceApplicationService datasourceApplicationService;

    /**
     * 分页查询数据源列表
     * 支持按数据源编码、名称和类型模糊查询
     */
    @PostMapping("/page")
    @ApiOperation("分页查询数据源列表")
    public ApiResponse<PageResult<DatasourcePageResponse>> page(
            @RequestBody @Valid DatasourcePageRequest request
    ) {
        // TODO: 实现分页查询逻辑
        return ApiResponse.success();
    }

    /**
     * 新增数据源
     * 支持不同数据库类型（mysql、postgresql、polardb）
     */
    @PostMapping
    @ApiOperation("新增数据源")
    public ApiResponse<Integer> add(
            @RequestBody @Valid DatasourceAddRequest request
    ) {
        // TODO: 实现新增逻辑
        return ApiResponse.success();
    }

    /**
     * 编辑数据源
     * 修改数据源的基本信息
     */
    @PutMapping("/{id}")
    @ApiOperation("编辑数据源")
    public ApiResponse<Boolean> edit(
            @ApiParam("数据源ID") @PathVariable Integer id,
            @RequestBody @Valid DatasourceEditRequest request
    ) {
        // 确保请求中的ID与路径参数一致
        request.setId(id);
        // TODO: 实现编辑逻辑
        return ApiResponse.success();
    }

    /**
     * 获取数据源详情
     * 加载数据源的基本信息（不包含数据源表和数据源表列数据）
     */
    @GetMapping("/{id}")
    @ApiOperation("获取数据源详情")
    public ApiResponse<DatasourceDetailResponse> detail(
            @ApiParam("数据源ID") @PathVariable Integer id
    ) {
        // TODO: 实现获取详情逻辑
        return ApiResponse.success();
    }

    /**
     * 获取数据源详情（包含表结构）
     * 加载数据源的基本信息及关联的所有数据表和表列信息
     */
    @GetMapping("/{id}/with-tables")
    @ApiOperation("获取数据源详情（包含表结构）")
    public ApiResponse<DatasourceWithTablesResponse> detailWithTables(
            @ApiParam("数据源ID") @PathVariable Integer id
    ) {
        // TODO: 实现获取详情（包含表结构）逻辑
        return ApiResponse.success();
    }

    /**
     * 删除数据源
     * 如果数据源下有关联的项目则不能删除，抛出BizException
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除数据源")
    public ApiResponse<Boolean> delete(
            @ApiParam("数据源ID") @PathVariable Integer id
    ) {
        // TODO: 实现删除逻辑，需要检查是否有关联项目
        return ApiResponse.success();
    }

    /**
     * 测试数据源连接
     * 检查数据源配置的链接的联通性
     */
    @PostMapping("/test-connection")
    @ApiOperation("测试数据源连接")
    public ApiResponse<Boolean> testConnection(
            @RequestBody @Valid DatasourceTestConnectionRequest request
    ) {
        // TODO: 实现测试连接逻辑
        return ApiResponse.success();
    }

    /**
     * 载入数据库表结构
     * 基于数据源连接加载表结构并存储到数据库表datasource_table和datasource_table_column中
     * 使用mybatis plus generator提供的功能实现，具体参考domain-generator工程
     */
    @PostMapping("/{id}/load-tables")
    @ApiOperation("载入数据库表结构")
    public ApiResponse<Boolean> loadTables(
            @ApiParam("数据源ID") @PathVariable Integer id
    ) {
        // TODO: 实现载入表结构逻辑，加载并存储到datasource_table和datasource_table_column表中，参考domain-generator工程
        return ApiResponse.success();
    }


}
