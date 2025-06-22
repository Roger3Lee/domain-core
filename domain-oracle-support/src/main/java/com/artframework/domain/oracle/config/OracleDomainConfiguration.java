package com.artframework.domain.oracle.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Oracle 数据库支持自动配置类
 * 当检测到 Oracle 驱动时自动启用
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "oracle.jdbc.OracleDriver")
@ConditionalOnProperty(prefix = "domain.oracle", name = "enabled", havingValue = "true", matchIfMissing = false)
@ComponentScan(basePackages = "com.artframework.domain.oracle")
public class OracleDomainConfiguration {

    public OracleDomainConfiguration() {
        log.info("Oracle Domain Support 已启用");
    }
}