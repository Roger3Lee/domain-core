package com.artframework.domain.customize;

import com.baomidou.mybatisplus.generator.config.querys.PostgreSqlQuery;

/**
 * 自定义 PostgreSQL 查询类，修复 schema 过滤问题
 * 
 * MyBatis Plus Generator 在处理 PostgreSQL 的 schema 时存在缺陷：
 * 原始 SQL 没有正确关联 pg_class 和 pg_namespace，导致可能查询到其他 schema 的同名表
 * 
 * 本类通过重写 tablesSql() 方法，使用正确的 JOIN 条件来确保只查询指定 schema 的表
 * 
 * @author domain-generator
 * @since MyBatis Plus 3.5.7
 */
public class CustomPostgreSqlQuery extends PostgreSqlQuery {

    @Override
    public String tablesSql() {
        // 修正后的 SQL：通过 JOIN pg_namespace 和 pg_class 确保表属于指定的 schema
        // 1. pg_tables.schemaname = '%s' 确保从 pg_tables 中只查询指定 schema 的表
        // 2. JOIN pg_namespace 获取 schema 的 OID
        // 3. JOIN pg_class 时同时匹配表名和 namespace OID，确保获取正确的表注释
        return "SELECT A.tablename, obj_description(C.oid, 'pg_class') AS comments " +
                "FROM pg_tables A " +
                "INNER JOIN pg_namespace B ON A.schemaname = B.nspname " +
                "INNER JOIN pg_class C ON A.tablename = C.relname AND C.relnamespace = B.oid " +
                "WHERE A.schemaname = '%s'";
    }
}