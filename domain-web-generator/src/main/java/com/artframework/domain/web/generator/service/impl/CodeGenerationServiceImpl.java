package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.service.CodeGenerationService;
import com.artframework.domain.web.generator.service.DatabaseService;
import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
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
    
    @Autowired
    private DatabaseService databaseService;

    @Override
    public String generateCode(DDDDomain domain) {
        log.info("开始生成领域模型代码，领域ID: {}", domain.getId());
        
        try {
            // 验证领域模型XML是否存在
            if (domain.getDomainXml() == null || domain.getDomainXml().trim().isEmpty()) {
                return "错误：领域模型XML为空，请先基于ER图生成XML配置";
            }
            
            // 获取项目信息
            ProjectDomain project = ProjectDomain.load(domain.getProjectId(), projectService);
            if (project == null) {
                return "错误：项目信息不存在，项目ID: " + domain.getProjectId();
            }
            
            // 基于domain-generator工程的方法生成代码
            return generateCodeUsingDomainGenerator(domain, project);
            
        } catch (Exception e) {
            log.error("代码生成失败，领域ID: {}", domain.getId(), e);
            return "代码生成失败: " + e.getMessage();
        }
    }
    
    /**
     * 使用domain-generator工程的方法生成代码
     */
    private String generateCodeUsingDomainGenerator(DDDDomain domain, ProjectDomain project) {
        try {
            // 创建临时XML文件
            String xmlContent = createDomainXmlContent(domain);
            
            // 配置数据源（从数据库服务获取）
            DataSourceConfig dataSourceConfig = databaseService.getDefaultDataSourceConfig(domain.getProjectId());
            
            // 加载XML配置到GlobalSetting
            GlobalSetting.loadFromDB(dataSourceConfig, xmlContent);
            
            // 设置包名参数
            Map<String, String> packageParam = createPackageParameters(project);
            
            // 生成代码到临时目录
            String tempOutputPath = createTempOutputDirectory();
            
            // 生成各种代码文件
            GenerateUtils.generateTables(
                tempOutputPath + "/mapper",
                tempOutputPath + "/dataobject", 
                GlobalSetting.INSTANCE.getTableList(), 
                packageParam, 
                true, 
                true
            );
            
            GenerateUtils.generateDomains(
                tempOutputPath + "/domain",
                GlobalSetting.INSTANCE.getDomainList(),
                packageParam,
                true
            );
            
            GenerateUtils.generateController(
                tempOutputPath + "/controller",
                GlobalSetting.INSTANCE.getDomainList(),
                packageParam,
                true
            );
            
            // 读取生成的代码文件并返回内容
            return readGeneratedCode(tempOutputPath);
            
        } catch (Exception e) {
            log.error("使用domain-generator生成代码失败", e);
            return "代码生成失败: " + e.getMessage();
        }
    }
    
    /**
     * 创建领域模型XML内容
     */
    private String createDomainXmlContent(DDDDomain domain) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<domains>\n");
        xml.append("    ").append(domain.getDomainXml()).append("\n");
        xml.append("</domains>");
        return xml.toString();
    }
    
    /**
     * 创建临时XML文件
     */
    private File createTempXmlFile(String xmlContent) throws IOException {
        Path tempFile = Files.createTempFile("domain-config-", ".xml");
        Files.write(tempFile, xmlContent.getBytes());
        return tempFile.toFile();
    }
    

    
    /**
     * 创建包名参数
     */
    private Map<String, String> createPackageParameters(ProjectDomain project) {
        Map<String, String> packageParam = new HashMap<>();
        packageParam.put("tablePackage", project.getDoPackage());
        packageParam.put("mapperPackage", project.getMapperPackage());
        packageParam.put("domainPackage", project.getDomainPackage());
        packageParam.put("controllerPackage", project.getControllerPackage());
        return packageParam;
    }
    
    /**
     * 创建临时输出目录
     */
    private String createTempOutputDirectory() throws IOException {
        Path tempDir = Files.createTempDirectory("domain-code-gen-");
        return tempDir.toString();
    }
    
    /**
     * 读取生成的代码文件内容
     */
    private String readGeneratedCode(String outputPath) {
        StringBuilder result = new StringBuilder();
        
        try {
            result.append("代码生成完成！生成的文件结构如下：\n\n");
            
            // 读取生成的文件内容
            File outputDir = new File(outputPath);
            if (outputDir.exists() && outputDir.isDirectory()) {
                appendDirectoryContent(result, outputDir, 0);
            }
            
            result.append("\n\n🎉 代码生成完成！");
            result.append("\n📁 生成的代码文件已保存在临时目录：").append(outputPath);
            result.append("\n\n📋 生成的代码包含以下内容：");
            result.append("\n  • Domain 类：领域聚合根和关联实体");
            result.append("\n  • Service 类：领域服务接口和实现");
            result.append("\n  • Repository 类：数据仓储接口和实现");
            result.append("\n  • Converter 类：对象转换器");
            result.append("\n  • Lambda 表达式：类型安全的查询表达式");
            result.append("\n  • Controller 类：REST API控制器");
            result.append("\n  • DataObject 类：数据库实体对象");
            result.append("\n  • Mapper 类：MyBatis-Plus数据访问接口");
            result.append("\n\n💡 请根据需要将代码文件复制到项目对应目录中。");
            
        } catch (Exception e) {
            log.error("读取生成的代码文件失败", e);
            result.append("代码生成完成，但读取文件内容失败: ").append(e.getMessage());
        }
        
        return result.toString();
    }
    
    /**
     * 递归读取目录内容
     */
    private void appendDirectoryContent(StringBuilder result, File dir, int level) {
        File[] files = dir.listFiles();
        if (files == null) return;
        
        StringBuilder indentBuilder = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indentBuilder.append("  ");
        }
        String indent = indentBuilder.toString();
        
        for (File file : files) {
            if (file.isDirectory()) {
                result.append(indent).append("📁 ").append(file.getName()).append("/\n");
                appendDirectoryContent(result, file, level + 1);
            } else {
                result.append(indent).append("📄 ").append(file.getName()).append("\n");
                
                // 如果是Java文件，可以显示文件内容摘要
                if (file.getName().endsWith(".java")) {
                    try {
                        String content = new String(Files.readAllBytes(file.toPath()));
                        String[] lines = content.split("\n");
                        if (lines.length > 0) {
                            result.append(indent).append("   └─ ").append(lines[0].trim()).append("\n");
                        }
                    } catch (IOException e) {
                        // 忽略读取错误
                    }
                }
            }
        }
    }

} 