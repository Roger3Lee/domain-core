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
    key VARCHAR(1),
    PRIMARY KEY (id)
);

COMMENT ON TABLE datasource_table_column IS '数据源表';
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

DROP TABLE IF EXISTS domain_config_line_config;
CREATE TABLE domain_config_line_config(
    id SERIAL NOT NULL,
    project_id INTEGER,
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
    many VARCHAR(1),
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_config_line IS '领域模型表之间关联连线配置（用于配置常量关联关系）';
COMMENT ON COLUMN domain_config_line.id IS '主键';
COMMENT ON COLUMN domain_config_line.project_id IS '项目ID';
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

DROP TABLE IF EXISTS domain_project;
CREATE TABLE domain_project(
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
    PRIMARY KEY (id)
);

COMMENT ON TABLE domain_project IS '领域项目';
COMMENT ON COLUMN domain_project.id IS '主键';
COMMENT ON COLUMN domain_project.created_by IS '创建人';
COMMENT ON COLUMN domain_project.created_time IS '创建时间';
COMMENT ON COLUMN domain_project.updated_by IS '更新人';
COMMENT ON COLUMN domain_project.updated_time IS '更新时间';
COMMENT ON COLUMN domain_project.name IS '项目名称';
COMMENT ON COLUMN domain_project.domain_package IS '领域package';
COMMENT ON COLUMN domain_project.controller_package IS '控制器package';
COMMENT ON COLUMN domain_project.do_package IS 'DO package';
COMMENT ON COLUMN domain_project.mapper_package IS 'Mapper package';

