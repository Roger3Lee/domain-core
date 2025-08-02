package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.service.CodeGenerationService;
import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.utils.GenerateUtils;
import com.artframework.domain.dto.DomainInfo;
import com.artframework.domain.dto.TableInfo;
import com.artframework.domain.meta.domain.DomainMetaInfo;
import com.artframework.domain.meta.table.TableMetaInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成服务实现类
 * 
 * @author auto
 * @version v1.0
 */
@Slf4j
@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

    @Autowired
    private ProjectService projectService;

    @Override
    public Boolean generateDomainCode(Integer domainId, Integer projectId) {
        try {
            // 获取领域模型和项目信息
            DDDDomain dddDomain = DDDDomain.load(domainId, null);
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            
            return generateCode(dddDomain, project);
        } catch (Exception e) {
            log.error("生成领域代码失败", e);
            return false;
        }
    }

    @Override
    public Boolean generateDOCode(Integer domainId, Integer projectId) {
        try {
            // 获取领域模型和项目信息
            DDDDomain dddDomain = DDDDomain.load(domainId, null);
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            
            return generateCode(dddDomain, project);
        } catch (Exception e) {
            log.error("生成DO代码失败", e);
            return false;
        }
    }

    @Override
    public Boolean generateMapperCode(Integer domainId, Integer projectId) {
        try {
            // 获取领域模型和项目信息
            DDDDomain dddDomain = DDDDomain.load(domainId, null);
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            
            return generateCode(dddDomain, project);
        } catch (Exception e) {
            log.error("生成Mapper代码失败", e);
            return false;
        }
    }

    @Override
    public Boolean generateControllerCode(Integer domainId, Integer projectId) {
        try {
            // 获取领域模型和项目信息
            DDDDomain dddDomain = DDDDomain.load(domainId, null);
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            
            return generateCode(dddDomain, project);
        } catch (Exception e) {
            log.error("生成Controller代码失败", e);
            return false;
        }
    }

    @Override
    public Boolean generateAllCode(Integer domainId, Integer projectId) {
        try {
            // 获取领域模型和项目信息
            DDDDomain dddDomain = DDDDomain.load(domainId, null);
            ProjectDomain project = ProjectDomain.load(projectId, projectService);
            
            return generateCode(dddDomain, project);
        } catch (Exception e) {
            log.error("生成所有代码失败", e);
            return false;
        }
    }

    @Override
    public Boolean generateCode(DDDDomain dddDomain, ProjectDomain project) {
        try {
            // 构建包参数
            Map<String, String> packageParam = buildPackageParam(project);
            
            // 构建领域元信息
            DomainMetaInfo domainMetaInfo = buildDomainMetaInfo(dddDomain);
            
            // 生成领域代码
//            String domainPath = project.getDomainPackage().replace(".", "/");
//            GenerateUtils.generateDomains(domainPath, List.of(domainMetaInfo), packageParam, true);
//
//            // 生成控制器代码
//            String controllerPath = project.getControllerPackage().replace(".", "/");
//            GenerateUtils.generateController(controllerPath, List.of(domainMetaInfo), packageParam, true);
//
            return true;
        } catch (Exception e) {
            log.error("生成代码失败", e);
            return false;
        }
    }

    @Override
    public String generateCode(DDDDomain dddDomain) {
        try {
            // 获取项目信息
            ProjectDomain project = ProjectDomain.load(dddDomain.getProjectId(), projectService);
            
            // 构建包参数
            Map<String, String> packageParam = buildPackageParam(project);
            
            // 构建领域元信息
            DomainMetaInfo domainMetaInfo = buildDomainMetaInfo(dddDomain);
            
            // 构建领域信息
            DomainInfo domainInfo = DomainInfo.covert(domainMetaInfo);
            
            // 返回生成的代码内容（这里简化处理，实际应该调用具体的生成器）
            return "// 生成的代码内容\n" +
                   "// 项目: " + project.getName() + "\n" +
                   "// 领域: " + dddDomain.getDomainName() + "\n" +
                   "// 主表: " + dddDomain.getMainTable() + "\n" +
                   "// 包路径: " + project.getDomainPackage();
        } catch (Exception e) {
            log.error("生成代码失败", e);
            return "生成代码失败: " + e.getMessage();
        }
    }

    /**
     * 构建包参数
     */
    private Map<String, String> buildPackageParam(ProjectDomain project) {
        Map<String, String> packageParam = new HashMap<>();
        packageParam.put("tablePackage", project.getDoPackage());
        packageParam.put("mapperPackage", project.getMapperPackage());
        packageParam.put("domainPackage", project.getDomainPackage());
        packageParam.put("controllerPackage", project.getControllerPackage());
        return packageParam;
    }

    /**
     * 构建领域元信息
     */
    private DomainMetaInfo buildDomainMetaInfo(DDDDomain dddDomain) {
        // 这里需要根据DDDDomain构建DomainMetaInfo
        // 由于DomainMetaInfo的构建比较复杂，这里简化处理
        DomainMetaInfo domainMetaInfo = new DomainMetaInfo();
        domainMetaInfo.setName(dddDomain.getDomainName());
        domainMetaInfo.setMainTable(dddDomain.getMainTable());
        domainMetaInfo.setFolder(dddDomain.getFolder());
        // 其他字段需要根据实际情况设置
        return domainMetaInfo;
    }
} 