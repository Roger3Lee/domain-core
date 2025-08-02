package com.artframework.domain.web.generator.service;

import com.artframework.domain.web.generator.dto.*;

/**
 * 领域模型应用服务接口
 * 
 * @author auto
 * @version v1.0
 */
public interface DomainConfigAppService {

    /**
     * 分页查询领域模型
     * 
     * @param request 分页查询请求
     * @return 分页结果
     */
    PageResult<DomainConfigResponse> page(DomainPageRequest request);

    /**
     * 新增领域模型
     * 
     * @param request 新增领域模型请求
     * @return 领域模型ID
     */
    Integer addDomainConfig(DomainConfigAddRequest request);

    /**
     * 编辑领域模型
     * 
     * @param request 编辑领域模型请求
     * @return 是否成功
     */
    Boolean editDomainConfig(DomainConfigEditRequest request);

    /**
     * 获取领域模型详情
     * 
     * @param id 领域模型ID
     * @return 领域模型详情
     */
    DomainConfigResponse getDomainConfigDetail(Integer id);

    /**
     * 删除领域模型
     * 
     * @param id 领域模型ID
     * @return 是否成功
     */
    Boolean deleteDomainConfig(Integer id);

    /**
     * 生成代码
     * 
     * @param id 领域模型ID
     * @return 生成的代码内容
     */
    String generateCode(Integer id);
} 