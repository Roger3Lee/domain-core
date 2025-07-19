package com.artframework.domain.web.generator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * MyBatis Plus 统一配置类
 * 提供分页插件、ID生成器等核心功能
 */
@Slf4j
@Configuration
public class MybatisConfiguration {

    /**
     * MyBatis Plus 拦截器配置
     * 包含分页、乐观锁、防全表更新删除等插件
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(DataSource dataSource) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 1. 分页插件
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();

        // 自动检测数据库类型
        DbType dbType = detectDbType(dataSource);
        paginationInterceptor.setDbType(dbType);

        // 分页配置
        paginationInterceptor.setMaxLimit(10000L); // 单页最大限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setOverflow(false);   // 溢出总页数后是否进行处理，默认不处理

        interceptor.addInnerInterceptor(paginationInterceptor);
        log.info("分页插件已启用，数据库类型: {}", dbType);

        // 2. 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        log.info("乐观锁插件已启用");

        // 3. 防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        log.info("防全表更新删除插件已启用");

        return interceptor;
    }

    /**
     * 自定义 ID 生成器
     * 基于雪花算法的分布式ID生成器，适用于分布式环境
     */
    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator();
    }

    /**
     * 自动检测数据库类型
     */
    private DbType detectDbType(DataSource dataSource) {
        try {
            String url = dataSource.getConnection().getMetaData().getURL().toLowerCase();

            if (url.contains("mysql") || url.contains("mariadb")) {
                return DbType.MYSQL;
            } else if (url.contains("postgresql")) {
                return DbType.POSTGRE_SQL;
            } else if (url.contains("oracle")) {
                return DbType.ORACLE;
            } else if (url.contains("sqlserver")) {
                return DbType.SQL_SERVER;
            } else if (url.contains("sqlite")) {
                return DbType.SQLITE;
            } else if (url.contains("h2")) {
                return DbType.H2;
            }

            log.warn("无法自动检测数据库类型，URL: {}，将使用 MySQL 作为默认类型", url);
            return DbType.MYSQL;

        } catch (Exception e) {
            log.warn("检测数据库类型失败，将使用 MySQL 作为默认类型", e);
            return DbType.MYSQL;
        }
    }
}