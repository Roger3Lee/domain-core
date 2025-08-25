# 角色

你是一个高级java 开发工程师，擅长使用领域模型开发业务系统。

# 上下文

此部分描述***领域模型的XML定义***和***数据库表结构设计***

## 表结构设计

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

## 表结构概览

| 表名                        | 注释        |
| ------------------------- | --------- |
| datasource_config        | 数据源配置     |
| datasource_table         | 数据源表      |
| datasource_table_column  | 数据源表列     |

## 领域模型XML配置

- 数据源领域：主要管理数据源的基本信息及数据源相关的表和表列信息

- 领域模型：基于数据源配置，管理数据源表和表列之间的关系

```xml
 <domain name="datasource" description="数据源领域" main-table="datasource_config">
        <related description="数据表" table="datasource_table" many="true" fk="id:ds_id"/>
        <related description="数据表的列" table="datasource_table_column" many="true" fk="id:ds_id"/>
    </domain>
```

# 任务

以TODO List的方式实现如下接口功能， 每个章节生成一个控制器。

## 数据源管理 :用来配置数据库连接，并从数据库中加载数据库表的结构

- 数据源列表：支持按数据源编码、名称和类型模糊分页查询

- 新增数据源：新增数据源的基本信息，支持不同数据库类型（mysql、postgresql、polardb）

- 编辑数据源：修改数据源的基本信息

- 数据源详情：加载数据源的基本信息（不包含数据源表和数据源表列数据）

- 数据源详情（包含表结构）：加载数据源的基本信息及关联的所有数据表和表列信息

- 删除数据源：如果数据源下有关联的项目则不能删除，抛出BizException

- 测试数据源连接：检查数据源配置的链接的联通性。

- 载入数据库表结构：基于数据源连接加载表结构并存储到数据库表`datasource_table`和`datasource_table_column`中。使用mybatis plus generator提供的功能实现，具体生成代码的方法参考 `domain-generator`工程

# 指令

- 生成接口定义（包括controller和需要的请求响应类型) 后提示如下内容以等下后续指令。
  
  ```textile
  接口定义已经生成，请review代码，并确定是否继续后续？
  确定(Y)  取消(N)
  ```
  
***重要***：当接口定义发生变化时， 重复上述提示，等待确认后再执行后续操作。
    
    

- 所有任务完成后 ， 提示如下内容以确定是否进行总结， 如果需要总结则按照章节`输出`的内容输出总结

# 输出

在当前需求文档的目录下生成需求文档名称+summary.md的文件， 用来记录变更内容。例如 `req01_summary.md`
