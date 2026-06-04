package io.github.roger3lee.domain.mysql.config;

import io.github.roger3lee.domain.mysql.injector.MySqlBatchSqlInjector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MySQL 数据库支持自动配置类
 * 当检测到 MySQL 驱动时自动启用
 * 必须在 MyBatis-Plus 自动配置之前加载，以确保自定义 SQL 注入器优先生效
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "domain.mysql", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureBefore(name = "com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration")
public class MySqlDomainConfiguration {

    public MySqlDomainConfiguration() {
        log.info("MySQL Domain Support 已启用");
    }

    /**
     * 注册 MySQL 批量操作 SQL 注入器
     */
    @Bean
    public MySqlBatchSqlInjector mySqlBatchSqlInjector() {
        log.info("注册 MySQL 批量操作 SQL 注入器");
        return new MySqlBatchSqlInjector();
    }
}