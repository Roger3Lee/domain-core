package com.artframework.domain.customize;

import com.baomidou.mybatisplus.generator.config.querys.AbstractDbQuery;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/1/4
 **/
public class MyPostgreSqlQuery extends AbstractDbQuery {
    private String schema;

    public MyPostgreSqlQuery(String schema) {
        this.schema = schema;
    }

    public String tablesSql() {
        return "SELECT A.tablename, obj_description(relfilenode, 'pg_class') AS comments FROM pg_tables A, pg_class B, pg_namespace C \n WHERE A.schemaname='%s' AND A.tablename = B.relname and C.nspname=A.schemaname and C.oid=B.relnamespace";
    }

    public String tableFieldsSql() {
        return "SELECT\n   A.attname AS name,format_type (A.atttypid,A.atttypmod) AS type,col_description (A.attrelid,A.attnum) AS comment,\n\t D.column_default,\n   CASE WHEN length(B.attname) > 0 THEN 'PRI' ELSE '' END AS key\nFROM\n   pg_attribute A\nLEFT JOIN (\n    SELECT\n        pg_attribute.attname\n    FROM\n        pg_index,\n        pg_class,\n        pg_attribute\n    WHERE\n        pg_class.oid ='\"%s\"' :: regclass\n    AND pg_index.indrelid = pg_class.oid\n    AND pg_attribute.attrelid = pg_class.oid\n    AND pg_attribute.attnum = ANY (pg_index.indkey)\n AND pg_index.indisprimary=1 \n) B ON A.attname = b.attname\nINNER JOIN pg_class C on A.attrelid = C.oid\nINNER JOIN information_schema.columns D on A.attname = D.column_name\nWHERE A.attrelid ='\"%s\"' :: regclass AND A.attnum> 0 AND NOT A.attisdropped AND D.table_name = '%s'\n AND D.table_schema='"+this.schema+"'\nORDER BY A.attnum;";
    }

    public String tableName() {
        return "tablename";
    }

    public String tableComment() {
        return "comments";
    }

    public String fieldName() {
        return "name";
    }

    public String fieldType() {
        return "type";
    }

    public String fieldComment() {
        return "comment";
    }

    public String fieldKey() {
        return "key";
    }

    public boolean isKeyIdentity(ResultSet results) throws SQLException {
        return StringUtils.isNotBlank(results.getString("column_default")) && results.getString("column_default").contains("nextval");
    }
}
