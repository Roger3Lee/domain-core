package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.ddd.service.DDDService;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.web.generator.service.DomainConfigAppService;
import com.artframework.domain.web.generator.service.CodeGenerationService;
import com.artframework.domain.web.generator.service.DatabaseService;
import com.artframework.domain.web.generator.dto.*;
import com.artframework.domain.web.generator.service.convert.DomainConfigAppConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.artframework.domain.core.lambda.query.LambdaQuery;
import com.artframework.domain.core.constants.Order;
import com.artframework.domain.core.domain.PageDomain;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
@Slf4j
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

    @Autowired
    private DatabaseService databaseService;

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
    public CodeGenerationResult generateCode(Integer id) {
        return codeGenerationService.generateCodeById(id);
    }

    @Override
    public ERDiagramRequest getERDiagram(Integer id) {
        // 加载领域模型
        DDDDomain domain = DDDDomain.load(id, dddService);
        
        // 加载关联的表和连线信息
        domain.loadRelated(DDDDomain.DomainConfigTablesDomain.class);
        domain.loadRelated(DDDDomain.DomainConfigLineDomain.class);
        domain.loadRelated(DDDDomain.DomainConfigLineConfigDomain.class);
        
        ERDiagramRequest response = new ERDiagramRequest();
        response.setDomainId(id);
        
        // 转换表配置
        if (CollUtil.isNotEmpty(domain.getDomainConfigTablesList())) {
            List<DomainConfigTablesDTO> tables = domain.getDomainConfigTablesList().stream()
                    .map(this::toDomainConfigTablesDTO)
                    .collect(Collectors.toList());
            response.setTables(tables);
        }
        
        // 转换连线配置
        if (CollUtil.isNotEmpty(domain.getDomainConfigLineList())) {
            List<DomainConfigLineDTO> lines = domain.getDomainConfigLineList().stream()
                    .map(line -> toDomainConfigLineDTO(line, domain.getDomainConfigLineConfigList()))
                    .collect(Collectors.toList());
            response.setLines(lines);
        }
        
        return response;
    }

    @Override
    @Transactional
    public Boolean saveERDiagram(ERDiagramRequest request) {
        // 加载原始领域模型
        DDDDomain originalDomain = DDDDomain.load(request.getDomainId(), dddService);
        originalDomain.loadRelated(DDDDomain.DomainConfigTablesDomain.class);
        originalDomain.loadRelated(DDDDomain.DomainConfigLineDomain.class);
        originalDomain.loadRelated(DDDDomain.DomainConfigLineConfigDomain.class);
        
        // 创建新的领域模型用于更新
        DDDDomain newDomain = new DDDDomain();
        newDomain.setId(request.getDomainId());
        newDomain.setProjectId(originalDomain.getProjectId());
        newDomain.setDomainName(originalDomain.getDomainName());
        newDomain.setMainTable(originalDomain.getMainTable());
        newDomain.setFolder(originalDomain.getFolder());
        
        // 转换表配置
        if (CollUtil.isNotEmpty(request.getTables())) {
            List<DDDDomain.DomainConfigTablesDomain> tables = request.getTables().stream()
                    .map(this::fromDomainConfigTablesDTO)
                    .collect(Collectors.toList());
            newDomain.setDomainConfigTablesList(tables);
        }
        
        // 转换连线配置
        if (CollUtil.isNotEmpty(request.getLines())) {
            List<DDDDomain.DomainConfigLineDomain> lines = new java.util.ArrayList<>();
            List<DDDDomain.DomainConfigLineConfigDomain> lineConfigs = new java.util.ArrayList<>();
            
            for (DomainConfigLineDTO lineDTO : request.getLines()) {
                DDDDomain.DomainConfigLineDomain line = fromDomainConfigLineDTO(lineDTO);
                lines.add(line);
                
                if (CollUtil.isNotEmpty(lineDTO.getLineConfigs())) {
                    List<DDDDomain.DomainConfigLineConfigDomain> configs = lineDTO.getLineConfigs().stream()
                            .map(this::fromDomainConfigLineConfigDTO)
                            .collect(Collectors.toList());
                    lineConfigs.addAll(configs);
                }
            }
            
            newDomain.setDomainConfigLineList(lines);
            newDomain.setDomainConfigLineConfigList(lineConfigs);
        }
        
        // 设置LoadFlag，指定需要更新的关联实体
        newDomain.setLoadFlag(DDDDomain.LoadFlag.builder()
                .loadDomainConfigTablesDomain(true)
                .loadDomainConfigLineDomain(true)
                .loadDomainConfigLineConfigDomain(true)
                .build());
        
        // 执行更新
        return dddService.update(newDomain, originalDomain);
    }

    @Override
    public String generateDomainXml(Integer id) {
        // 加载领域模型完整信息
        DDDDomain domain = DDDDomain.load(id, dddService);
        domain.loadRelated(DDDDomain.DomainConfigTablesDomain.class);
        domain.loadRelated(DDDDomain.DomainConfigLineDomain.class);
        domain.loadRelated(DDDDomain.DomainConfigLineConfigDomain.class);
        
        // 生成XML
        String xml = generateDomainXmlFromERDiagram(domain);
        
        // 更新领域模型的XML字段
        DDDDomain updateDomain = new DDDDomain();
        updateDomain.setId(id);
        updateDomain.setDomainXml(xml);
        
        DDDDomain originalDomain = DDDDomain.load(id, dddService);
        dddService.update(updateDomain, originalDomain);
        
        return xml;
    }

    @Override
    public byte[] downloadGeneratedCode(Integer id) {
        return codeGenerationService.generateCodeAsZipById(id);
    }

    /**
     * 转换为领域模型响应对象
     */
    private DomainConfigResponse toDomainConfigResponse(DDDDomain domain) {
        DomainConfigResponse response = new DomainConfigResponse();
        
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
        
        response.setId(domain.getId());
        response.setProjectId(domain.getProjectId());
        response.setDomainName(domain.getDomainName());
        response.setDomainXml(domain.getDomainXml());
        response.setMainTable(domain.getMainTable());
        response.setFolder(domain.getFolder());
        response.setCreatedBy(domain.getCreatedBy());
        response.setCreatedTime(domain.getCreatedTime());
        response.setUpdatedBy(domain.getUpdatedBy());
        response.setUpdatedTime(domain.getUpdatedTime());
        
        return response;
    }

    /**
     * 转换表配置为DTO
     */
    private DomainConfigTablesDTO toDomainConfigTablesDTO(DDDDomain.DomainConfigTablesDomain table) {
        DomainConfigTablesDTO dto = new DomainConfigTablesDTO();
        dto.setId(table.getId());
        dto.setProjectId(table.getProjectId());
        dto.setDomainId(table.getDomainId());
        dto.setTableName(table.getTableName());
        dto.setX(table.getX());
        dto.setY(table.getY());
        dto.setW(table.getW());
        dto.setH(table.getH());
        dto.setCreatedBy(table.getCreatedBy());
        dto.setCreatedTime(table.getCreatedTime());
        dto.setUpdatedBy(table.getUpdatedBy());
        dto.setUpdatedTime(table.getUpdatedTime());
        return dto;
    }

    /**
     * 转换连线配置为DTO
     */
    private DomainConfigLineDTO toDomainConfigLineDTO(DDDDomain.DomainConfigLineDomain line, 
            List<DDDDomain.DomainConfigLineConfigDomain> allConfigs) {
        DomainConfigLineDTO dto = new DomainConfigLineDTO();
        dto.setId(line.getId());
        dto.setProjectId(line.getProjectId());
        dto.setDomainId(line.getDomainId());
        dto.setLineCode(line.getLineCode());
        dto.setLineType(line.getLineType());
        dto.setSourceTable(line.getSourceTable());
        dto.setSourceColunm(line.getSourceColunm());
        dto.setTargetTable(line.getTargetTable());
        dto.setTargetColunm(line.getTargetColunm());
        dto.setMany(line.getMany());
        dto.setCreatedBy(line.getCreatedBy());
        dto.setCreatedTime(line.getCreatedTime());
        dto.setUpdatedBy(line.getUpdatedBy());
        dto.setUpdatedTime(line.getUpdatedTime());
        
        // 加载连线的详细配置
        if (CollUtil.isNotEmpty(allConfigs)) {
            List<DomainConfigLineConfigDTO> configs = allConfigs.stream()
                    .filter(config -> line.getLineCode().equals(config.getLineCode()))
                    .map(this::toDomainConfigLineConfigDTO)
                    .collect(Collectors.toList());
            dto.setLineConfigs(configs);
        }
        
        return dto;
    }

    /**
     * 转换连线详细配置为DTO
     */
    private DomainConfigLineConfigDTO toDomainConfigLineConfigDTO(DDDDomain.DomainConfigLineConfigDomain config) {
        DomainConfigLineConfigDTO dto = new DomainConfigLineConfigDTO();
        dto.setId(config.getId());
        dto.setProjectId(config.getProjectId());
        dto.setDomainId(config.getDomainId());
        dto.setLineCode(config.getLineCode());
        dto.setSourceColunm(config.getSourceColunm());
        dto.setSourceColumnValue(config.getSourceColumnValue());
        dto.setSourceColumnValueDataType(config.getSourceColumnValueDataType());
        dto.setTargetColunm(config.getTargetColunm());
        dto.setTargetColumnValue(config.getTargetColumnValue());
        dto.setTargetColumnValueDataType(config.getTargetColumnValueDataType());
        dto.setCreatedBy(config.getCreatedBy());
        dto.setCreatedTime(config.getCreatedTime());
        dto.setUpdatedBy(config.getUpdatedBy());
        dto.setUpdatedTime(config.getUpdatedTime());
        return dto;
    }

    /**
     * 从DTO转换表配置
     */
    private DDDDomain.DomainConfigTablesDomain fromDomainConfigTablesDTO(DomainConfigTablesDTO dto) {
        DDDDomain.DomainConfigTablesDomain table = new DDDDomain.DomainConfigTablesDomain();
        table.setId(dto.getId());
        table.setProjectId(dto.getProjectId());
        table.setDomainId(dto.getDomainId());
        table.setTableName(dto.getTableName());
        table.setX(dto.getX());
        table.setY(dto.getY());
        table.setW(dto.getW());
        table.setH(dto.getH());
        return table;
    }

    /**
     * 从DTO转换连线配置
     */
    private DDDDomain.DomainConfigLineDomain fromDomainConfigLineDTO(DomainConfigLineDTO dto) {
        DDDDomain.DomainConfigLineDomain line = new DDDDomain.DomainConfigLineDomain();
        line.setId(dto.getId());
        line.setProjectId(dto.getProjectId());
        line.setDomainId(dto.getDomainId());
        line.setLineCode(dto.getLineCode());
        line.setLineType(dto.getLineType());
        line.setSourceTable(dto.getSourceTable());
        line.setSourceColunm(dto.getSourceColunm());
        line.setTargetTable(dto.getTargetTable());
        line.setTargetColunm(dto.getTargetColunm());
        line.setMany(dto.getMany());
        return line;
    }

    /**
     * 从DTO转换连线详细配置
     */
    private DDDDomain.DomainConfigLineConfigDomain fromDomainConfigLineConfigDTO(DomainConfigLineConfigDTO dto) {
        DDDDomain.DomainConfigLineConfigDomain config = new DDDDomain.DomainConfigLineConfigDomain();
        config.setId(dto.getId());
        config.setProjectId(dto.getProjectId());
        config.setDomainId(dto.getDomainId());
        config.setLineCode(dto.getLineCode());
        config.setSourceColunm(dto.getSourceColunm());
        config.setSourceColumnValue(dto.getSourceColumnValue());
        config.setSourceColumnValueDataType(dto.getSourceColumnValueDataType());
        config.setTargetColunm(dto.getTargetColunm());
        config.setTargetColumnValue(dto.getTargetColumnValue());
        config.setTargetColumnValueDataType(dto.getTargetColumnValueDataType());
        return config;
    }

    /**
     * 基于ER图生成领域模型XML
     */
    private String generateDomainXmlFromERDiagram(DDDDomain domain) {
        StringBuilder xml = new StringBuilder();
        xml.append("<domain name=\"").append(domain.getDomainName()).append("\"");
        xml.append(" description=\"").append(domain.getDomainName()).append("\"");
        xml.append(" main-table=\"").append(domain.getMainTable()).append("\">\n");
        
        // 根据连线配置生成related元素
        if (CollUtil.isNotEmpty(domain.getDomainConfigLineList())) {
            for (DDDDomain.DomainConfigLineDomain line : domain.getDomainConfigLineList()) {
                if ("FK".equals(line.getLineType())) {
                    // 外键关系
                    xml.append("    <related description=\"").append(getTableDescription(line.getTargetTable()))
                       .append("\" table=\"").append(line.getTargetTable()).append("\"");
                    
                    if ("1".equals(line.getMany())) {
                        xml.append(" many=\"true\"");
                    }
                    
                    xml.append(" fk=\"").append(line.getSourceColunm()).append(":").append(line.getTargetColunm()).append("\"");
                    
                    // 添加冗余字段
                    String redundancy = generateRedundancyFromLineConfigs(line.getLineCode(), domain.getDomainConfigLineConfigList());
                    if (StrUtil.isNotEmpty(redundancy)) {
                        xml.append(" redundancy=\"").append(redundancy).append("\"");
                    }
                    
                    xml.append("/>\n");
                }
            }
        }
        
        xml.append("</domain>");
        return xml.toString();
    }

    /**
     * 获取表描述（简化实现）
     */
    private String getTableDescription(String tableName) {
        return tableName;
    }

    /**
     * 从连线配置生成冗余字段配置
     */
    private String generateRedundancyFromLineConfigs(String lineCode, List<DDDDomain.DomainConfigLineConfigDomain> configs) {
        if (CollUtil.isEmpty(configs)) {
            return "";
        }
        
        return configs.stream()
                .filter(config -> lineCode.equals(config.getLineCode()))
                .map(config -> config.getSourceColunm() + ":" + config.getTargetColunm())
                .collect(Collectors.joining(","));
    }
} 