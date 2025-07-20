# Domain Web Generator

基于DDD领域设计的概念，用于生成领域模型相关代码，改变传统的基于MVC分层的编码方式。

## 功能特性

### 1. 数据源管理
- **数据源列表**: 获取所有配置的数据源
- **创建数据源**: 支持MySQL、PostgreSQL、PolarDB等数据库类型
- **编辑数据源**: 修改数据源配置信息
- **删除数据源**: 删除不需要的数据源
- **测试连接**: 验证数据库连接是否正常
- **加载表结构**: 从数据库中加载表结构信息
- **获取表列表**: 查看数据源中的所有表
- **获取字段列表**: 查看指定表的字段信息

### 2. 项目管理
- **项目列表**: 获取所有项目
- **新增项目**: 创建新项目，配置包名等信息
- **编辑项目**: 修改项目配置
- **删除项目**: 删除项目
- **项目领域模型列表**: 获取项目下的所有领域模型

### 3. 领域模型管理
- **领域模型列表**: 获取所有领域模型
- **新增领域模型**: 基于设计数据库ER图的方式设计领域模型
- **编辑领域模型**: 修改领域模型配置
- **删除领域模型**: 删除领域模型
- **获取关联连线**: 查看领域模型的表关联关系
- **获取连线配置**: 查看领域模型的连线配置信息

### 4. 代码生成
- **生成领域代码**: 生成领域模型相关代码
- **生成DO代码**: 生成数据对象代码
- **生成Mapper代码**: 生成数据访问层代码
- **生成Controller代码**: 生成控制器代码
- **生成所有代码**: 一次性生成所有相关代码

## API接口

### 数据源管理 API
- `GET /api/datasource/list` - 获取数据源列表
- `GET /api/datasource/{id}` - 根据ID获取数据源
- `POST /api/datasource` - 创建数据源
- `PUT /api/datasource/{id}` - 更新数据源
- `DELETE /api/datasource/{id}` - 删除数据源
- `POST /api/datasource/{id}/test-connection` - 测试数据库连接
- `POST /api/datasource/{id}/load-tables` - 加载数据库表结构
- `GET /api/datasource/{id}/tables` - 获取数据库表列表
- `GET /api/datasource/{id}/tables/{tableName}/columns` - 获取表字段列表

### 项目管理 API
- `GET /api/project/list` - 获取项目列表
- `GET /api/project/{id}` - 根据ID获取项目
- `POST /api/project` - 创建项目
- `PUT /api/project/{id}` - 更新项目
- `DELETE /api/project/{id}` - 删除项目
- `GET /api/project/{id}/domains` - 获取项目的领域模型列表

### 领域模型管理 API
- `GET /api/ddd/list` - 获取领域模型列表
- `GET /api/ddd/{id}` - 根据ID获取领域模型
- `POST /api/ddd` - 创建领域模型
- `PUT /api/ddd/{id}` - 更新领域模型
- `DELETE /api/ddd/{id}` - 删除领域模型
- `GET /api/ddd/{id}/lines` - 获取领域模型关联连线
- `GET /api/ddd/{id}/line-configs` - 获取领域模型连线配置

### 代码生成 API
- `POST /api/ddd/{id}/generate-code` - 生成所有代码
- `POST /api/ddd/{id}/generate-domain` - 生成领域模型代码
- `POST /api/ddd/{id}/generate-do` - 生成DO代码
- `POST /api/ddd/{id}/generate-mapper` - 生成Mapper代码
- `POST /api/ddd/{id}/generate-controller` - 生成Controller代码

## 技术架构

### 后端技术栈
- **Spring Boot 2.6.15**: 应用框架
- **MyBatis Plus 3.5.3.1**: 数据访问层
- **PostgreSQL**: 主数据库
- **Swagger 3.0**: API文档
- **Lombok**: 代码简化
- **MapStruct**: 对象映射

### 领域驱动设计 (DDD)
- **聚合根**: 领域模型的核心实体
- **仓储模式**: 数据访问抽象
- **领域服务**: 业务逻辑处理
- **转换器**: 对象映射转换

### 数据库设计
- **datasource_config**: 数据源配置表
- **datasource_table**: 数据源表信息
- **datasource_table_column**: 数据源表字段信息
- **domain_project**: 项目配置表
- **domain_config**: 领域模型配置表
- **domain_config_line**: 领域模型关联连线表
- **domain_config_line_config**: 领域模型连线配置表

## 开发说明

### 项目结构
```
domain-web-generator/
├── src/main/java/com/artframework/domain/web/generator/
│   ├── controller/          # REST控制器
│   ├── service/             # 业务服务
│   ├── domain/              # 领域模型
│   │   ├── datasource/      # 数据源领域
│   │   ├── project/         # 项目领域
│   │   └── ddd/             # DDD领域
│   ├── dataobject/          # 数据对象
│   ├── mapper/              # 数据访问层
│   └── config/              # 配置类
└── domain/                  # 领域模型配置文件
    ├── DDL.sql              # 数据库建表脚本
    └── domain-generator.xml # 领域模型配置
```

### 核心特性
1. **领域驱动设计**: 基于DDD架构，实现业务逻辑与数据访问的分离
2. **代码生成**: 基于领域模型自动生成相关代码
3. **数据库支持**: 支持多种数据库类型
4. **RESTful API**: 提供完整的REST API接口
5. **Swagger文档**: 自动生成API文档

## 使用说明

1. **配置数据源**: 通过API创建和配置数据库连接
2. **加载表结构**: 从数据库中加载表结构信息
3. **创建项目**: 配置项目信息和包名
4. **设计领域模型**: 基于ER图设计领域模型
5. **生成代码**: 根据领域模型生成相关代码

## 部署说明

1. 执行 `domain/DDL.sql` 创建数据库表
2. 配置数据库连接信息
3. 启动Spring Boot应用
4. 访问 `http://localhost:8080/swagger-ui/` 查看API文档 