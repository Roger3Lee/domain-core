package com.artframework.domain.mysql.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * MySQL 数据库支持自动配置类
 * 当检测到 MySQL 驱动时自动启用
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "com.mysql.cj.jdbc.Driver")
@ConditionalOnProperty(prefix = "domain.mysql", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan(basePackages = "com.artframework.domain.mysql")
public class MySqlDomainConfiguration {

    public MySqlDomainConfiguration() {
        log.info("MySQL Domain Support 已启用");
    }
}