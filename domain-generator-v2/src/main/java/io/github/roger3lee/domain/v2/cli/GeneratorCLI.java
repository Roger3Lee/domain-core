package io.github.roger3lee.domain.v2.cli;

import io.github.roger3lee.domain.v2.generator.GenerateService;
import io.github.roger3lee.domain.v2.meta.domain.DomainMetaInfo;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@Command(name = "domain-generator-v2",
        mixinStandardHelpOptions = true,
        version = "3.0.0",
        description = "Generate DDD domain model code from domain XML + SQL DDL.")
public class GeneratorCLI implements Callable<Integer> {

    @Option(names = {"-d", "--domain-xml"}, required = true,
            description = "Path to domain configuration XML file")
    private File domainXml;

    @Option(names = {"-s", "--sql-file"}, required = true,
            description = "Path to SQL DDL file (MySQL or PostgreSQL)")
    private File sqlFile;

    @Option(names = {"-o", "--output-dir"}, required = true,
            description = "Output directory for generated code")
    private String outputDir;

    @Option(names = {"-p", "--base-package"}, required = true,
            description = "Base Java package (e.g. com.example.project)")
    private String basePackage;

    @Option(names = {"--dialect"}, defaultValue = "mysql",
            description = "SQL dialect: mysql or postgresql (default: mysql)")
    private String dialect;

    @Option(names = {"--no-overwrite"}, defaultValue = "false",
            description = "Do not overwrite existing files")
    private boolean noOverwrite;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GeneratorCLI()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception {
        log.info("=== Domain Generator V2 ===");
        log.info("Domain XML  : {}", domainXml.getAbsolutePath());
        log.info("SQL File    : {}", sqlFile.getAbsolutePath());
        log.info("Output Dir  : {}", outputDir);
        log.info("Base Package: {}", basePackage);
        log.info("Dialect     : {}", dialect);

        // Validate inputs
        if (!domainXml.exists()) {
            log.error("Domain XML file not found: {}", domainXml.getAbsolutePath());
            return 1;
        }
        if (!sqlFile.exists()) {
            log.error("SQL file not found: {}", sqlFile.getAbsolutePath());
            return 1;
        }

        boolean overwrite = !noOverwrite;

        try {
            GenerateService service = new GenerateService();

            // Step 1: Load table metadata from SQL
            log.info("Step 1: Parsing SQL DDL...");
            service.loadFromSql(sqlFile, dialect);

            // Step 2: Load domain configuration from XML
            log.info("Step 2: Loading domain configuration...");
            List<DomainMetaInfo> domainList = service.loadDomainConfig(domainXml);

            if (domainList.isEmpty()) {
                log.warn("No domain definitions found in XML");
                return 1;
            }

            // Step 3: Generate all code
            log.info("Step 3: Generating code...");
            service.generateAll(outputDir, basePackage, domainList, overwrite);

            log.info("=== Generation completed successfully ===");
            return 0;
        } catch (Exception e) {
            log.error("Code generation failed", e);
            return 1;
        }
    }
}
