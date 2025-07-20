package com.artframework.domain.web.generator.application;

import com.artframework.domain.web.generator.domain.ddd.domain.*;
import com.artframework.domain.web.generator.domain.ddd.service.DDDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * DDD领域模型应用服务
 * 依赖domain层，不修改domain层代码
 */
@Service
@RequiredArgsConstructor
public class DDDApplicationService {

    private final DDDService dddService;

    /**
     * 获取所有领域模型列表
     */
    public List<DDDDomain> list() {
        return dddService.list();
    }

    /**
     * 根据ID获取领域模型
     */
    public DDDDomain getById(Integer id) {
        DDDFindDomain request = new DDDFindDomain();
        request.setKey(id);
        return dddService.find(request);
    }

    /**
     * 创建领域模型
     */
    public Integer create(DDDDomain dddDomain) {
        return dddService.insert(dddDomain);
    }

    /**
     * 更新领域模型
     */
    public Boolean update(Integer id, DDDDomain dddDomain) {
        dddDomain.setId(id);
        return dddService.update(dddDomain);
    }

    /**
     * 删除领域模型
     */
    public Boolean delete(Integer id) {
        return dddService.delete(id);
    }

    /**
     * 获取领域模型关联连线
     */
    public List<DDDDomain.DomainConfigLineDomain> getDomainLines(Integer id) {
        DDDFindDomain request = new DDDFindDomain();
        request.setKey(id);
        DDDDomain.LoadFlag loadFlag = DDDDomain.LoadFlag.builder()
                .loadDomainConfigLineDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DDDDomain dddDomain = dddService.find(request);
        return dddDomain != null ? dddDomain.getDomainConfigLineList() : null;
    }

    /**
     * 获取领域模型连线配置
     */
    public List<DDDDomain.DomainConfigLineConfigDomain> getDomainLineConfigs(Integer id) {
        DDDFindDomain request = new DDDFindDomain();
        request.setKey(id);
        DDDDomain.LoadFlag loadFlag = DDDDomain.LoadFlag.builder()
                .loadDomainConfigLineConfigDomain(true)
                .build();
        request.setLoadFlag(loadFlag);
        DDDDomain dddDomain = dddService.find(request);
        return dddDomain != null ? dddDomain.getDomainConfigLineConfigList() : null;
    }

    /**
     * 生成所有代码
     */
    public Boolean generateAllCode(Integer id) {
        // TODO: 实现代码生成逻辑
        return true;
    }

    /**
     * 生成领域模型代码
     */
    public Boolean generateDomainCode(Integer id) {
        // TODO: 实现领域模型代码生成逻辑
        return true;
    }

    /**
     * 生成DO代码
     */
    public Boolean generateDOCode(Integer id) {
        // TODO: 实现DO代码生成逻辑
        return true;
    }

    /**
     * 生成Mapper代码
     */
    public Boolean generateMapperCode(Integer id) {
        // TODO: 实现Mapper代码生成逻辑
        return true;
    }

    /**
     * 生成Controller代码
     */
    public Boolean generateControllerCode(Integer id) {
        // TODO: 实现Controller代码生成逻辑
        return true;
    }
}