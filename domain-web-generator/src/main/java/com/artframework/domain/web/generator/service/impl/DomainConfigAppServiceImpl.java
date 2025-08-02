package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.service.DomainConfigAppService;
import com.artframework.domain.web.generator.service.CodeGenerationService;
import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.ddd.service.DDDService;
import com.artframework.domain.web.generator.service.convert.DomainConfigAppConvertor;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.domain.PageDomain;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.collection.CollUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 领域模型应用服务实现类
 * 
 * @author auto
 * @version v1.0
 */
@Service
public class DomainConfigAppServiceImpl implements DomainConfigAppService {

    @Autowired
    private DDDService dddService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DomainConfigAppConvertor domainConfigAppConvertor;

    @Autowired
    private CodeGenerationService codeGenerationService;

    @Override
    public PageResult<DomainConfigResponse> page(DomainPageRequest request) {
        // 构建查询条件
        LambdaQuery<DDDDomain> query = LambdaQuery.of(DDDDomain.class);
        
        // 按项目ID查询
        if (request.getProjectId() != null) {
            query.eq(DDDDomain::getProjectId, request.getProjectId());
        }
        
        // 按领域名称模糊查询
        if (StrUtil.isNotEmpty(request.getDomainName())) {
            query.like(DDDDomain::getDomainName, request.getDomainName());
        }
        
        // 排序
        query.orderBy(DDDDomain::getId, Order.DESC);
        
        // 分页查询
        PageDomain pageDomain = PageDomain.builder()
                .pageNum(request.getPageNum())
                .pageSize(request.getPageSize())
                .build();
        IPage<DDDDomain> result = dddService.queryPage(DDDDomain.class, pageDomain, query);
        
        // 转换为响应对象
        List<DomainConfigResponse> records = result.getRecords().stream()
                .map(this::toDomainConfigResponse)
                .collect(Collectors.toList());
        
        return PageResult.of(records, result.getTotal(), result.getSize(), result.getCurrent());
    }

    @Override
    @Transactional
    public Integer addDomainConfig(DomainConfigAddRequest request) {
        // 转换为领域对象
        DDDDomain domain = domainConfigAppConvertor.toDomain(request);
        
        // 保存领域模型
        return dddService.insert(domain);
    }

    @Override
    @Transactional
    public Boolean editDomainConfig(DomainConfigEditRequest request) {
        // 获取原始领域模型
        DDDDomain originalDomain = DDDDomain.load(request.getId(), dddService);
        
        // 转换为新的领域对象
        DDDDomain newDomain = domainConfigAppConvertor.toDomain(request);
        
        // 更新领域模型
        return dddService.update(newDomain, originalDomain);
    }

    @Override
    public DomainConfigResponse getDomainConfigDetail(Integer id) {
        // 加载领域模型
        DDDDomain domain = DDDDomain.load(id, dddService);
        
        // 转换为响应对象
        return toDomainConfigResponse(domain);
    }

    @Override
    @Transactional
    public Boolean deleteDomainConfig(Integer id) {
        // 删除领域模型
        return dddService.delete(id);
    }

    @Override
    public String generateCode(Integer id) {
        // 加载领域模型
        DDDDomain domain = DDDDomain.load(id, dddService);
        
        // 生成代码
        return generateCodeFromDomain(domain);
    }

    /**
     * 转换为领域模型响应对象
     */
    private DomainConfigResponse toDomainConfigResponse(DDDDomain domain) {
        DomainConfigResponse response = domainConfigAppConvertor.toResponse(domain);
        
        // 加载项目信息
        if (domain.getProjectId() != null) {
            ProjectDomain project = ProjectDomain.load(domain.getProjectId(), projectService);
            // 使用现有的转换方法，不修改领域驱动代码
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(project.getId());
            projectDTO.setName(project.getName());
            projectDTO.setDomainPackage(project.getDomainPackage());
            projectDTO.setControllerPackage(project.getControllerPackage());
            projectDTO.setDoPackage(project.getDoPackage());
            projectDTO.setMapperPackage(project.getMapperPackage());
            response.setProject(projectDTO);
        }
        
        return response;
    }

    /**
     * 从领域模型生成代码
     */
    private String generateCodeFromDomain(DDDDomain domain) {
        // 调用代码生成服务
        return codeGenerationService.generateCode(domain);
    }
} 