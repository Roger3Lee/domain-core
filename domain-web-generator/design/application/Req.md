# 表设计

```sql
DROP TABLE IF EXISTS domain_config;
CREATE TABLE domain_config(
    id SERIAL NOT NULL,
    project_id INTEGER,
    revision INTEGER NOT NULL,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    domain_name VARCHAR(255),
    domain_xml VARCHAR(255),
    main_table VARCHAR(255),
    folder VARCHAR(255),
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_config IS '领域模型设置';
COMMENT ON COLUMN domain_config.id IS '主键';
COMMENT ON COLUMN domain_config.project_id IS '项目ID';
COMMENT ON COLUMN domain_config.revision IS '乐观锁';
COMMENT ON COLUMN domain_config.created_by IS '创建人';
COMMENT ON COLUMN domain_config.created_time IS '创建时间';
COMMENT ON COLUMN domain_config.updated_by IS '更新人';
COMMENT ON COLUMN domain_config.updated_time IS '更新时间';
COMMENT ON COLUMN domain_config.domain_name IS '领域名称';
COMMENT ON COLUMN domain_config.domain_xml IS '领域模型XML';
COMMENT ON COLUMN domain_config.main_table IS '主表';
COMMENT ON COLUMN domain_config.folder IS '目录';

DROP TABLE IF EXISTS domain_config_tables;
CREATE TABLE domain_config_tables(
    id SERIAL NOT NULL,
    project_id INTEGER,
    domain_id INTEGER,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    table_name VARCHAR(255),
    x NUMERIC(24,6),
    y NUMERIC(24,6),
    w NUMERIC(24,6),
    h NUMERIC(24,6),
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_config_tables IS '领域模型的表列表';
COMMENT ON COLUMN domain_config_tables.id IS '主键';
COMMENT ON COLUMN domain_config_tables.project_id IS '项目ID';
COMMENT ON COLUMN domain_config_tables.domain_id IS '领域ID';
COMMENT ON COLUMN domain_config_tables.created_by IS '创建人';
COMMENT ON COLUMN domain_config_tables.created_time IS '创建时间';
COMMENT ON COLUMN domain_config_tables.updated_by IS '更新人';
COMMENT ON COLUMN domain_config_tables.updated_time IS '更新时间';
COMMENT ON COLUMN domain_config_tables.table_name IS '表名';
COMMENT ON COLUMN domain_config_tables.x IS '位置X';
COMMENT ON COLUMN domain_config_tables.y IS '位置Y';
COMMENT ON COLUMN domain_config_tables.w IS '宽度';
COMMENT ON COLUMN domain_config_tables.h IS '高度';

DROP TABLE IF EXISTS domain_config_line_config;
CREATE TABLE domain_config_line_config(
    id SERIAL NOT NULL,
    project_id INTEGER,
    domain_id INTEGER,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    line_code VARCHAR(255),
    source_colunm VARCHAR(255),
    source_column_value VARCHAR(255),
    source_column_value_data_type VARCHAR(255),
    target_colunm VARCHAR(255),
    target_column_value VARCHAR(255),
    target_column_value_data_type VARCHAR(255),
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_config_line_config IS '领域模型表关联信息';
COMMENT ON COLUMN domain_config_line_config.id IS '主键';
COMMENT ON COLUMN domain_config_line_config.project_id IS '项目ID';
COMMENT ON COLUMN domain_config_line_config.domain_id IS '领域ID';
COMMENT ON COLUMN domain_config_line_config.created_by IS '创建人';
COMMENT ON COLUMN domain_config_line_config.created_time IS '创建时间';
COMMENT ON COLUMN domain_config_line_config.updated_by IS '更新人';
COMMENT ON COLUMN domain_config_line_config.updated_time IS '更新时间';
COMMENT ON COLUMN domain_config_line_config.line_code IS '连线的编码';
COMMENT ON COLUMN domain_config_line_config.source_colunm IS '源表列';
COMMENT ON COLUMN domain_config_line_config.source_column_value IS '源表列的值（在常量关联时使用）';
COMMENT ON COLUMN domain_config_line_config.source_column_value_data_type IS '源表列值的类型（在常量关联时使用）';
COMMENT ON COLUMN domain_config_line_config.target_colunm IS '目标列';
COMMENT ON COLUMN domain_config_line_config.target_column_value IS '目标表列的值（在常量关联时使用）';
COMMENT ON COLUMN domain_config_line_config.target_column_value_data_type IS '目标列值的类型（在常量关联时使用）';

DROP TABLE IF EXISTS domain_config_line;
CREATE TABLE domain_config_line(
    id SERIAL NOT NULL,
    project_id INTEGER,
    domain_id INTEGER,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    line_code VARCHAR(255),
    line_type VARCHAR(255),
    source_table VARCHAR(255),
    source_colunm VARCHAR(255),
    target_table VARCHAR(255),
    target_colunm VARCHAR(255),
    many CHAR(1),
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_config_line IS '领域模型表之间关联连线配置（用于配置常量关联关系）';
COMMENT ON COLUMN domain_config_line.id IS '主键';
COMMENT ON COLUMN domain_config_line.project_id IS '项目ID';
COMMENT ON COLUMN domain_config_line.domain_id IS '领域ID';
COMMENT ON COLUMN domain_config_line.created_by IS '创建人';
COMMENT ON COLUMN domain_config_line.created_time IS '创建时间';
COMMENT ON COLUMN domain_config_line.updated_by IS '更新人';
COMMENT ON COLUMN domain_config_line.updated_time IS '更新时间';
COMMENT ON COLUMN domain_config_line.line_code IS '连线的编码';
COMMENT ON COLUMN domain_config_line.line_type IS '线条的类型;FK ：外键  REDUNDANCY:冗余';
COMMENT ON COLUMN domain_config_line.source_table IS '源表';
COMMENT ON COLUMN domain_config_line.source_colunm IS '源表列';
COMMENT ON COLUMN domain_config_line.target_table IS '目标表';
COMMENT ON COLUMN domain_config_line.target_colunm IS '目标列';
COMMENT ON COLUMN domain_config_line.many IS '是否一对多';

DROP TABLE IF EXISTS project;
CREATE TABLE project(
    id SERIAL NOT NULL,
    created_by VARCHAR(90),
    created_time TIMESTAMP,
    updated_by VARCHAR(90),
    updated_time TIMESTAMP,
    name VARCHAR(255),
    domain_package VARCHAR(255),
    controller_package VARCHAR(255),
    do_package VARCHAR(255),
    mapper_package VARCHAR(255),
    data_source_id INTEGER,
    PRIMARY KEY (id)
);

COMMENT ON TABLE project IS '领域项目';
COMMENT ON COLUMN project.id IS '主键';
COMMENT ON COLUMN project.created_by IS '创建人';
COMMENT ON COLUMN project.created_time IS '创建时间';
COMMENT ON COLUMN project.updated_by IS '更新人';
COMMENT ON COLUMN project.updated_time IS '更新时间';
COMMENT ON COLUMN project.name IS '项目名称';
COMMENT ON COLUMN project.domain_package IS '领域package';
COMMENT ON COLUMN project.controller_package IS '控制器package';
COMMENT ON COLUMN project.do_package IS 'DO package';
COMMENT ON COLUMN project.mapper_package IS 'Mapper package';


```

# 领域模型设计

- 应用领域：主要管理应用的基本信息及领域模型和应用之间的关系

- 领域模型：基于应用关联的数据源，使用ER图的方式管理领域模型之间的关系， 最终通过ER图生成领域模型XML配置

```xml
 <domain name="project" description="应用" main-table="project">
        <related description="领域模型基本配置" table="domain_config" many="true" fk="id:project_id" />
    </domain>
    <domain name="DDD" description="领域模型" main-table="domain_config">
        <related description="表" table="domain_config_tables" many="true" fk="id:domain_id" redundancy="project_id:project_id"/>
        <related description="领域ER关系连线" table="domain_config_line" many="true" fk="id:domain_id" redundancy="project_id:project_id">
            <ref description="连线" name="line"  many="true" table="domain_config_line_config" fk="line_code:line_code"/>
        </related>
        <related description="领域ER关系连线自定义配置（常量列）" table="domain_config_line_config" many="true" fk="id:domain_id"  redundancy="project_id:project_id"/>
    </domain>
```

# 功能

## 应用管理 :用于配置应用名称及后端java不同层级的包名

- 应用列表：支持按应用名称模糊分页查询

- 新增应用:  新增应用的基本信息

- 编辑应用： 修改应用的基本信息

- 应用详情: 加载应用的基本信息

- 应用详情（包含领域模型）：加载应用的基本信息 ，应用关联的数据源的表和表列信息， 以及所有的领域模型的基本信息

- 删除应用：如果应用下有领域模型则不能删除，抛出BizException

## 领域模型
- 新增领域模型: 基于设计数据库ER图的方式设计领域模型， 并在保存的时候要基于连线生成领域模型的XML

- 编辑领域模型: 修改领域模型的ER图， 并在保存的时候要基于连线生成领域模型的XML

- 删除领域模型：

- 查询领域模型: 查询领域模型相关的数据，另需基于领域模型所属应用的数据源和`domain_config_tables`的表列表 查询`datasource_table`和表`datasource_table_column`表列的数据，用于ER图展示。

- 生成代码：基于领域模型生成领域代码，DO，mapper和domain， 具体生成代码的方法参考 `domain-generator`工程的main方法的样例。 生成的代码需要分层
    - 代码分层
        - controller
        - domain ：按照domain, repository , service, lambdaexp, convertor 等目录分层
        - dataobject
        - mapper