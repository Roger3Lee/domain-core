package com.artframework.domain.polardb.config;

import com.artframework.domain.postgresql.injector.PostgreSqlBatchSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * PolarDB 数据库支持自动配置类
 * 当启用 PolarDB 支持时自动配置
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "domain.polardb", name = "enabled", havingValue = "true", matchIfMissing = false)
@ComponentScan(basePackages = "com.artframework.domain.polardb")
public class PolarDbDomainConfiguration {

    public PolarDbDomainConfiguration() {
        log.info("PolarDB Domain Support 已启用");
    }

    /**
     * PostgreSQL 批量操作 SQL 注入器
     * 当启用 PostgreSQL 支持时注册
     */
    @Bean
    public ISqlInjector postgreSqlBatchSqlInjector() {
        return new PostgreSqlBatchSqlInjector();
    }
}