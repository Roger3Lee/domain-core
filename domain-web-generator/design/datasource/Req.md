# 表设计

```sql
DROP TABLE IF EXISTS datasource_config;
CREATE TABLE datasource_config(
    id SERIAL NOT NULL,
    revision INTEGER NOT NULL,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    name VARCHAR(90),
    code VARCHAR(90),
    db_type VARCHAR(90),
    url VARCHAR(900),
    user_name VARCHAR(255),
    password VARCHAR(255),
    schema VARCHAR(255),
    PRIMARY KEY (id)
);

COMMENT ON TABLE datasource_config IS '数据源配置';
COMMENT ON COLUMN datasource_config.id IS '主键';
COMMENT ON COLUMN datasource_config.revision IS '乐观锁';
COMMENT ON COLUMN datasource_config.created_by IS '创建人';
COMMENT ON COLUMN datasource_config.created_time IS '创建时间';
COMMENT ON COLUMN datasource_config.updated_by IS '更新人';
COMMENT ON COLUMN datasource_config.updated_time IS '更新时间';
COMMENT ON COLUMN datasource_config.name IS '数据源名称';
COMMENT ON COLUMN datasource_config.code IS '编码';
COMMENT ON COLUMN datasource_config.db_type IS '数据库类型';
COMMENT ON COLUMN datasource_config.url IS '数据库连接';
COMMENT ON COLUMN datasource_config.user_name IS '数据库用户';
COMMENT ON COLUMN datasource_config.password IS '密码';
COMMENT ON COLUMN datasource_config.schema IS 'schema';

DROP TABLE IF EXISTS datasource_table;
CREATE TABLE datasource_table(
    id SERIAL NOT NULL,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    ds_id INTEGER,
    name VARCHAR(255),
    comment VARCHAR(900),
    PRIMARY KEY (id)
);

COMMENT ON TABLE datasource_table IS '数据源表';
COMMENT ON COLUMN datasource_table.id IS '主键';
COMMENT ON COLUMN datasource_table.created_by IS '创建人';
COMMENT ON COLUMN datasource_table.created_time IS '创建时间';
COMMENT ON COLUMN datasource_table.updated_by IS '更新人';
COMMENT ON COLUMN datasource_table.updated_time IS '更新时间';
COMMENT ON COLUMN datasource_table.ds_id IS '数据源ID';
COMMENT ON COLUMN datasource_table.name IS '表名';
COMMENT ON COLUMN datasource_table.comment IS '表描述';

DROP TABLE IF EXISTS datasource_table_column;
CREATE TABLE datasource_table_column(
    id SERIAL NOT NULL,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    ds_id INTEGER,
    table_name VARCHAR(255),
    name VARCHAR(255),
    type VARCHAR(255),
    comment VARCHAR(900),
    key CHAR(1),
    PRIMARY KEY (id)
);

COMMENT ON TABLE datasource_table_column IS '数据源表列';
COMMENT ON COLUMN datasource_table_column.id IS '主键';
COMMENT ON COLUMN datasource_table_column.created_by IS '创建人';
COMMENT ON COLUMN datasource_table_column.created_time IS '创建时间';
COMMENT ON COLUMN datasource_table_column.updated_by IS '更新人';
COMMENT ON COLUMN datasource_table_column.updated_time IS '更新时间';
COMMENT ON COLUMN datasource_table_column.ds_id IS '数据源ID';
COMMENT ON COLUMN datasource_table_column.table_name IS '表名';
COMMENT ON COLUMN datasource_table_column.name IS '列名';
COMMENT ON COLUMN datasource_table_column.type IS '类型';
COMMENT ON COLUMN datasource_table_column.comment IS '表描述';
COMMENT ON COLUMN datasource_table_column.key IS '是否是主键';
```

# 领域模型设计

```xml
 <domain name="datasource" description="数据源" main-table="datasource_config">
        <related description="数据表" table="datasource_table" many="true" fk="id:ds_id"/>
        <related description="数据表的列" table="datasource_table_column" many="true" fk="id:ds_id"/>
    </domain>
```

# 功能

- 数据源管理 : 用来配置数据库连接，并从数据库中加载数据库表的结构  

# 接口
- 数据源列表：可通过数据源编码，名称和类型查询

- 创建数据源：支持不同数据库的数据源， 包括mysql，postgresql , polardb

- 数据源详情：返回数据源详情，不包含数据源表和数据源表列数据。

- 编辑数据源

- 删除数据源

- 加载数据库表结构 ：基于数据源链接加载表结构：使用mybatis plus generator提供的功能实现， 具体生成代码的方法参考 `domain-generator`工程
