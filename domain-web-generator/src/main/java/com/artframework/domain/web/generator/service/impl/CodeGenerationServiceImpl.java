package com.artframework.domain.web.generator.service.impl;

import com.artframework.domain.web.generator.domain.ddd.domain.DDDDomain;
import com.artframework.domain.web.generator.domain.ddd.service.DDDService;
import com.artframework.domain.web.generator.domain.project.domain.ProjectDomain;
import com.artframework.domain.web.generator.domain.project.service.ProjectService;
import com.artframework.domain.web.generator.dto.CodeGenFile;
import com.artframework.domain.web.generator.dto.CodeGenGroup;
import com.artframework.domain.web.generator.dto.CodeGenerationResult;
import com.artframework.domain.web.generator.service.CodeGenerationService;
import com.artframework.domain.web.generator.service.DatabaseService;
import com.artframework.domain.config.GlobalSetting;
import com.artframework.domain.utils.GenerateUtils;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {

    @Autowired
    private DDDService dddService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DatabaseService databaseService;

    @Override
    public CodeGenerationResult generateCode(DDDDomain domain) {
        try {
            // 生成代码到临时目录
            String tempOutputPath = createTempOutputDirectory();
            String xmlContent = createDomainXmlContent(domain);
            DataSourceConfig dataSourceConfig = databaseService.getDefaultDataSourceConfig(domain.getProjectId());
            
            GlobalSetting.loadFromDB(dataSourceConfig, xmlContent);
            Map<String, String> packageParam = createPackageParameters(domain.getProjectId());
            
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
            
            // 读取生成的文件并构建结果
            return buildCodeGenerationResult(tempOutputPath);
            
        } catch (Exception e) {
            log.error("代码生成失败，领域ID: {}", domain.getId(), e);
            throw new RuntimeException("代码生成失败: " + e.getMessage());
        }
    }

    @Override
    public byte[] generateCodeAsZip(DDDDomain domain) {
        try {
            // 生成代码到临时目录
            String tempOutputPath = createTempOutputDirectory();
            String xmlContent = createDomainXmlContent(domain);
            DataSourceConfig dataSourceConfig = databaseService.getDefaultDataSourceConfig(domain.getProjectId());
            
            GlobalSetting.loadFromDB(dataSourceConfig, xmlContent);
            Map<String, String> packageParam = createPackageParameters(domain.getProjectId());
            
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
            
            // 创建ZIP文件
            return createZipFromDirectory(tempOutputPath);
            
        } catch (Exception e) {
            log.error("生成代码压缩包失败，领域ID: {}", domain.getId(), e);
            throw new RuntimeException("生成代码压缩包失败: " + e.getMessage());
        }
    }

    @Override
    public CodeGenerationResult generateCodeById(Integer domainId) {
        DDDDomain domain = DDDDomain.load(domainId, dddService);
        if (domain == null) {
            throw new RuntimeException("领域模型不存在，ID: " + domainId);
        }
        return generateCode(domain);
    }

    @Override
    public byte[] generateCodeAsZipById(Integer domainId) {
        DDDDomain domain = DDDDomain.load(domainId, dddService);
        if (domain == null) {
            throw new RuntimeException("领域模型不存在，ID: " + domainId);
        }
        return generateCodeAsZip(domain);
    }

    /**
     * 创建临时输出目录
     */
    private String createTempOutputDirectory() {
        try {
            Path tempDir = Files.createTempDirectory("domain-code-gen");
            return tempDir.toAbsolutePath().toString();
        } catch (IOException e) {
            log.error("创建临时输出目录失败", e);
            throw new RuntimeException("创建临时输出目录失败: " + e.getMessage());
        }
    }

    /**
     * 创建领域模型XML内容
     */
    private String createDomainXmlContent(DDDDomain domain) {
        StringBuilder xml = new StringBuilder();
        xml.append("<domain name=\"").append(domain.getDomainName()).append("\"");
        xml.append(" description=\"").append(domain.getDomainName()).append("\"");
        xml.append(" main-table=\"").append(domain.getMainTable()).append("\">\n");
        
        // 根据连线配置生成related元素
        if (domain.getDomainConfigLineList() != null && !domain.getDomainConfigLineList().isEmpty()) {
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
                    if (redundancy != null && !redundancy.isEmpty()) {
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
     * 创建包参数
     */
    private Map<String, String> createPackageParameters(Integer projectId) {
        ProjectDomain project = ProjectDomain.load(projectId, projectService);
        if (project == null) {
            throw new RuntimeException("项目不存在，项目ID: " + projectId);
        }
        
        Map<String, String> packageParam = new HashMap<>();
        packageParam.put("projectName", project.getName());
        packageParam.put("projectPackage", project.getDomainPackage());
        packageParam.put("controllerPackage", project.getControllerPackage());
        packageParam.put("doPackage", project.getDoPackage());
        packageParam.put("mapperPackage", project.getMapperPackage());
        return packageParam;
    }

    /**
     * 构建代码生成结果
     */
    private CodeGenerationResult buildCodeGenerationResult(String outputPath) {
        CodeGenerationResult result = new CodeGenerationResult();
        
        try {
            File rootDir = new File(outputPath);
            if (rootDir.exists() && rootDir.isDirectory()) {
                // 构建Controller分组
                result.setController(readGroup(new File(rootDir, "controller"), "controller"));
                
                // 构建Domain分组（包含子分组）
                CodeGenerationResult.CodeGenerationResultDomain domainGroup = new CodeGenerationResult.CodeGenerationResultDomain();
                File domainRoot = new File(rootDir, "domain");
                if (domainRoot.exists() && domainRoot.isDirectory()) {
                    domainGroup.setDomain(readGroup(new File(domainRoot, "domain"), "domain"));
                    domainGroup.setRepository(readGroup(new File(domainRoot, "repository"), "repository"));
                    domainGroup.setService(readGroup(new File(domainRoot, "service"), "service"));
                    domainGroup.setLambdaexp(readGroup(new File(domainRoot, "lambdaexp"), "lambdaexp"));
                    domainGroup.setConvertor(readGroup(new File(domainRoot, "convertor"), "convertor"));
                }
                result.setDomain(domainGroup);
                
                // 构建DataObject分组
                result.setDataobject(readGroup(new File(rootDir, "dataobject"), "dataobject"));
                
                // 构建Mapper分组
                result.setMapper(readGroup(new File(rootDir, "mapper"), "mapper"));
            }
        } catch (Exception e) {
            log.error("扫描生成的文件失败", e);
        }
        
        return result;
    }
    
    /**
     * 读取目录中的文件并构建分组
     */
    private CodeGenGroup readGroup(File dir, String name) {
        CodeGenGroup group = new CodeGenGroup();
        group.setName(name);
        
        if (!dir.exists() || !dir.isDirectory()) {
            return group;
        }
        
        File[] files = dir.listFiles((f) -> f.isFile() && f.getName().endsWith(".java"));
        if (files != null) {
            for (File file : files) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    group.getFiles().add(new CodeGenFile(file.getName(), content));
                } catch (IOException ignored) {
                    // 忽略读取失败的文件
                }
            }
        }
        
        return group;
    }

    /**
     * 创建ZIP文件
     */
    private byte[] createZipFromDirectory(String outputPath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            File rootDir = new File(outputPath);
            if (rootDir.exists() && rootDir.isDirectory()) {
                addDirectoryToZip(rootDir, zos, "");
            }
        } catch (IOException e) {
            log.error("创建ZIP文件失败", e);
            throw new RuntimeException("创建ZIP文件失败: " + e.getMessage());
        }
        return baos.toByteArray();
    }

    /**
     * 递归添加目录到ZIP
     */
    private void addDirectoryToZip(File file, ZipOutputStream zos, String parentPath) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    addDirectoryToZip(f, zos, parentPath + "/" + file.getName());
                }
            }
        } else {
            ZipEntry entry = new ZipEntry(parentPath + "/" + file.getName());
            zos.putNextEntry(entry);
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
        }
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
    private String generateRedundancyFromLineConfigs(String lineCode, java.util.List<DDDDomain.DomainConfigLineConfigDomain> configs) {
        if (configs == null || configs.isEmpty()) {
            return "";
        }
        
        return configs.stream()
                .filter(config -> lineCode.equals(config.getLineCode()))
                .map(config -> config.getSourceColunm() + ":" + config.getTargetColunm())
                .collect(java.util.stream.Collectors.joining(","));
    }
} 