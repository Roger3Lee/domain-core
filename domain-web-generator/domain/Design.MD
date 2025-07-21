# 项目介绍
这个项目是一个用来基于ER图生成领域模型设计文件并使用 domain-generator里面的代码生成的项目
现阶段项目已经生成了领域模型文件 domain-generator.xml

整体功能分为以下两部分
- 数据源管理 : 用来配置数据库连接，并从数据库中加载数据库表的结构
    - 数据源列表：可通过数据源编码，名称和类型查询
    - 创建数据源：支持不同数据库的数据源， 包括mysql，postgresql , polardb等
    - 编辑数据源
    - 删除数据源
    - 基于数据源链接加载表结构：使用mybatis plus generator提供的功能实现
- 应用管理： 用于配置应用名称及后端java不同层级的包名
    - 应用列表：支持应用名称查询
    - 新增应用
    - 编辑应用
    - 删除应用
- 领域模型
    - 应用领域模型列表
    - 新增领域模型: 基于设计数据库ER图的方式设计领域模型
    - 编辑领域模型
    - 删除领域模型
    - 基于领域模型生成领域代码，DO，mapper和controller

需要在 web-generator目录下生成基于若伊框架的前端代码
需要在 domain-web-generator后端项目中生成以上功能的代码

后端代码需要基于领域模型设计，已经生成了相关的领域模型代码
领域模型配置文件为
<domains>
    <domain name="datasource" description="数据源" main-table="datasource_config">
        <related description="数据表" table="datasource_table" many="true" fk="id:ds_id"/>
        <related description="数据表的列" table="datasource_table_column" many="true" fk="id:ds_id"/>
    </domain>
    <domain name="project" description="项目" main-table="project">
        <related description="领域模型基本配置" table="domain_config" many="true" fk="id:project_id" />
    </domain>
    <domain name="DDD" description="项目" main-table="domain_config">
        <related description="领域ER关系连线" table="domain_config_line" many="true" fk="id:domain_id" redundancy="project_id:project_id">
            <ref description="连线" name="line"  many="true" table="domain_config_line_config" fk="line_code:line_code"/>
        </related>
        <related description="领域ER关系连线自定义配置（常量列）" table="domain_config_line_config" many="true" fk="id:domain_id"  redundancy="project_id:project_id"/>
    </domain>
</domains>