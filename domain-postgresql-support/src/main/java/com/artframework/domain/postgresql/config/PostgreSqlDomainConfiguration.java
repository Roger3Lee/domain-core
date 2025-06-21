package com.artframework.domain.postgresql.config;

import com.artframework.domain.postgresql.injector.PostgreSqlBatchSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * PostgreSQL 数据库支持自动配置类
 * 当检测到 PostgreSQL 驱动时自动启用
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.postgresql.Driver")
@ComponentScan(basePackages = "com.artframework.domain.postgresql")
public class PostgreSqlDomainConfiguration {

    public PostgreSqlDomainConfiguration() {
        log.info("PostgreSQL Domain Support 已启用");
    }

    /**
     * 注册 PostgreSQL 批量操作 SQL 注入器
     */
    @Bean
    public PostgreSqlBatchSqlInjector postgreSqlBatchSqlInjector() {
        log.info("注册 PostgreSQL 批量操作 SQL 注入器");
        return new PostgreSqlBatchSqlInjector();
    }
}