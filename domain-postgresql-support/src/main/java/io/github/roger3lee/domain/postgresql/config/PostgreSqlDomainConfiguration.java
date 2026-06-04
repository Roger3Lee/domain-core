package io.github.roger3lee.domain.postgresql.config;

import io.github.roger3lee.domain.postgresql.injector.PostgreSqlBatchSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * PostgreSQL 数据库支持自动配置类
 * 当检测到 PostgreSQL 驱动时自动启用
 * 必须在 MyBatis-Plus 自动配置之前加载，以确保自定义 SQL 注入器优先生效
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.postgresql.Driver")
@ConditionalOnProperty(prefix = "domain.postgresql", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
@ComponentScan(basePackages = "io.github.roger3lee.domain.postgresql")
public class PostgreSqlDomainConfiguration {

    public PostgreSqlDomainConfiguration() {
        log.info("PostgreSQL Domain Support 已启用");
    }

    /**
     * 注册 PostgreSQL 批量操作 SQL 注入器
     * 替代 MyBatis-Plus 默认的 DefaultSqlInjector，提供批量插入/更新能力
     */
    @Bean
    public PostgreSqlBatchSqlInjector postgreSqlBatchSqlInjector() {
        log.info("注册 PostgreSQL 批量操作 SQL 注入器");
        return new PostgreSqlBatchSqlInjector();
    }
}