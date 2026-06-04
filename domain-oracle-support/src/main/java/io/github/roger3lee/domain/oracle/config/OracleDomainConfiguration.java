package io.github.roger3lee.domain.oracle.config;

import io.github.roger3lee.domain.oracle.injector.OracleBatchSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Oracle 数据库支持自动配置类
 * 当检测到 Oracle 驱动时自动启用
 * 必须在 MyBatis-Plus 自动配置之前加载，以确保自定义 SQL 注入器优先生效
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "oracle.jdbc.OracleDriver")
@ConditionalOnProperty(prefix = "domain.oracle", name = "enabled", havingValue = "true", matchIfMissing = false)
@AutoConfigureBefore(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
@ComponentScan(basePackages = "io.github.roger3lee.domain.oracle")
public class OracleDomainConfiguration {

    public OracleDomainConfiguration() {
        log.info("Oracle Domain Support 已启用");
    }

    /**
     * 注册 Oracle 批量操作 SQL 注入器
     */
    @Bean
    public OracleBatchSqlInjector oracleBatchSqlInjector() {
        log.info("注册 Oracle 批量操作 SQL 注入器");
        return new OracleBatchSqlInjector();
    }
}