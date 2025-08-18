# 领域驱动设计代码生成器 - 接口测试文档

本文档提供了所有REST API接口的测试用例和curl命令示例。

## 项目管理接口

### 1. 分页查询项目

**接口地址**: `GET /api/v1/projects/page`

**参数**:
- `name` (可选): 项目名称，支持模糊查询
- `pageNum` (可选): 页码，默认为1
- `pageSize` (可选): 每页大小，默认为10

**测试用例1**: 查询所有项目
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/page?pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

**测试用例2**: 按名称模糊查询
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/page?name=demo&pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

**期望响应**:
```json
{
  "code": "200",
  "message": "success",
  "success": true,
  "data": {
    "records": [
      {
        "id": 1,
        "name": "示例项目",
        "domainPackage": "com.example.domain",
        "controllerPackage": "com.example.controller",
        "doPackage": "com.example.dataobject",
        "mapperPackage": "com.example.mapper"
      }
    ],
    "total": 1,
    "size": 10,
    "current": 1
  }
}
```

### 2. 新增项目

**接口地址**: `POST /api/v1/projects`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "新项目",
    "domainPackage": "com.newproject.domain",
    "controllerPackage": "com.newproject.controller",
    "doPackage": "com.newproject.dataobject",
    "mapperPackage": "com.newproject.mapper"
  }'
```

**期望响应**:
```json
{
  "code": "200",
  "message": "success",
  "success": true,
  "data": 1
}
```

### 3. 编辑项目

**接口地址**: `PUT /api/v1/projects`

**测试用例**:
```bash
    curl -X PUT "http://localhost:8080/domain-generator/api/v1/projects" \
      -H "Content-Type: application/json" \
      -d '{
        "id": 1,
        "name": "更新的项目名称",
        "domainPackage": "com.updated.domain",
        "controllerPackage": "com.updated.controller",
        "doPackage": "com.updated.dataobject",
        "mapperPackage": "com.updated.mapper"
      }'
```

### 4. 获取项目详情

**接口地址**: `GET /api/v1/projects/{id}`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/1" \
  -H "Content-Type: application/json"
```

### 5. 获取项目详情（包含领域模型）

**接口地址**: `GET /api/v1/projects/{id}/with-domains`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/1/with-domains" \
  -H "Content-Type: application/json"
```

### 6. 获取项目详情（包含数据源表和领域模型）

**接口地址**: `GET /api/v1/projects/{id}/with-domains-and-tables`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/1/with-domains-and-tables" \
  -H "Content-Type: application/json"
```

### 7. 删除项目

**接口地址**: `DELETE /api/v1/projects/{id}`

**测试用例**:
```bash
curl -X DELETE "http://localhost:8080/domain-generator/api/v1/projects/1" \
  -H "Content-Type: application/json"
```

## 领域模型管理接口

### 1. 分页查询领域模型

**接口地址**: `GET /api/v1/domain-configs/page`

**参数**:
- `projectId` (必填): 项目ID
- `domainName` (可选): 领域名称，支持模糊查询
- `folder` (可选): 目录
- `pageNum` (可选): 页码，默认为1
- `pageSize` (可选): 每页大小，默认为10

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/domain-configs/page?projectId=1&pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

### 2. 新增领域模型

**接口地址**: `POST /api/v1/domain-configs`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/domain-configs" \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "domainName": "用户管理",
    "mainTable": "sys_user",
    "folder": "user"
  }'
```

### 3. 编辑领域模型

**接口地址**: `PUT /api/v1/domain-configs`

**测试用例**:
```bash
curl -X PUT "http://localhost:8080/domain-generator/api/v1/domain-configs" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "projectId": 1,
    "domainName": "用户管理模块",
    "mainTable": "sys_user",
    "folder": "user"
  }'
```

### 4. 获取领域模型详情

**接口地址**: `GET /api/v1/domain-configs/{id}`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/domain-configs/1" \
  -H "Content-Type: application/json"
```

### 5. 删除领域模型

**接口地址**: `DELETE /api/v1/domain-configs/{id}`

**测试用例**:
```bash
curl -X DELETE "http://localhost:8080/domain-generator/api/v1/domain-configs/1" \
  -H "Content-Type: application/json"
```

### 6. 生成代码

**接口地址**: `POST /api/v1/domain-configs/{id}/generate-code`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/domain-configs/1/generate-code" \
  -H "Content-Type: application/json"
```

**期望响应示例**:
```json
{
  "code": "200",
  "message": "success", 
  "success": true,
  "data": "代码生成完成！生成的文件结构如下：\n\n📁 controller/\n  📄 UserController.java\n    └─ package com.example.controller;\n📁 domain/\n  📁 user/\n    📁 domain/\n      📄 UserDomain.java\n      📄 UserFindDomain.java\n    📁 service/\n      📄 UserService.java\n      📄 UserServiceImpl.java\n    📁 repository/\n      📄 UserRepository.java\n      📄 UserRepositoryImpl.java\n    📁 convertor/\n      📄 UserConvertor.java\n    📁 lambdaexp/\n      📄 UserLambdaExp.java\n📁 dataobject/\n  📄 UserDO.java\n📁 mapper/\n  📄 UserMapper.java\n\n🎉 代码生成完成！\n📁 生成的代码文件已保存在临时目录：/tmp/domain-code-gen-xxx\n\n📋 生成的代码包含以下内容：\n  • Domain 类：领域聚合根和关联实体\n  • Service 类：领域服务接口和实现\n  • Repository 类：数据仓储接口和实现\n  • Converter 类：对象转换器\n  • Lambda 表达式：类型安全的查询表达式\n  • Controller 类：REST API控制器\n  • DataObject 类：数据库实体对象\n  • Mapper 类：MyBatis-Plus数据访问接口\n\n💡 请根据需要将代码文件复制到项目对应目录中。"
}
```

## ER图管理接口

### 1. 获取领域模型ER图信息

**接口地址**: `GET /api/v1/domain-configs/{id}/er-diagram`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/domain-configs/1/er-diagram" \
  -H "Content-Type: application/json"
```

### 2. 保存领域模型ER图信息

**接口地址**: `POST /api/v1/domain-configs/{id}/er-diagram`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/domain-configs/1/er-diagram" \
  -H "Content-Type: application/json" \
  -d '{
    "domainId": 1,
    "tables": [
      {
        "tableName": "sys_user",
        "x": 100,
        "y": 100,
        "w": 200,
        "h": 150
      },
      {
        "tableName": "sys_role",
        "x": 400,
        "y": 100,
        "w": 200,
        "h": 150
      }
    ],
    "lines": [
      {
        "lineCode": "USER_ROLE_FK",
        "lineType": "FK",
        "sourceTable": "sys_user",
        "sourceColunm": "id",
        "targetTable": "sys_user_role",
        "targetColunm": "user_id",
        "many": "1",
        "lineConfigs": [
          {
            "lineCode": "USER_ROLE_FK",
            "sourceColunm": "id",
            "targetColunm": "user_id"
          }
        ]
      }
    ]
  }'
```

### 3. 基于ER图生成领域模型XML

**接口地址**: `POST /api/v1/domain-configs/{id}/generate-xml`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/domain-configs/1/generate-xml" \
  -H "Content-Type: application/json"
```

**期望响应示例**:
```json
{
  "code": "200",
  "message": "success",
  "success": true,
  "data": "<domain name=\"用户管理\" description=\"用户管理\" main-table=\"sys_user\">\n    <related description=\"用户角色\" table=\"sys_user_role\" many=\"true\" fk=\"id:user_id\"/>\n</domain>"
}
```

## 数据源管理接口

### 1. 分页查询数据源

**接口地址**: `GET /api/v1/datasources/page`

**测试用例**:
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/datasources/page?pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

### 2. 新增数据源

**接口地址**: `POST /api/v1/datasources`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/datasources" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "MySQL数据源",
    "code": "mysql_ds",
    "dbType": "MySQL",
    "url": "jdbc:mysql://localhost:3306/test_db",
    "userName": "root",
    "password": "123456",
    "schema": "test_db"
  }'
```

### 3. 测试数据库连接

**接口地址**: `POST /api/v1/datasources/test-connection`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/datasources/test-connection" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试数据源",
    "dbType": "MySQL",
    "url": "jdbc:mysql://localhost:3306/test_db",
    "userName": "root",
    "password": "123456",
    "schema": "test_db"
  }'
```

### 4. 同步表结构

**接口地址**: `POST /api/v1/datasources/{id}/sync-tables`

**测试用例**:
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/datasources/1/sync-tables" \
  -H "Content-Type: application/json"
```

## 错误处理测试

### 1. 测试参数验证

**测试用例**: 新增项目时缺少必填参数
```bash
curl -X POST "http://localhost:8080/domain-generator/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "domainPackage": "com.test.domain"
  }'
```

**期望响应**:
```json
{
  "code": "400",
  "message": "参数验证失败",
  "success": false,
  "data": null
}
```

### 2. 测试资源不存在

**测试用例**: 查询不存在的项目
```bash
curl -X GET "http://localhost:8080/domain-generator/api/v1/projects/99999" \
  -H "Content-Type: application/json"
```

### 3. 测试业务规则

**测试用例**: 删除包含领域模型的项目
```bash
curl -X DELETE "http://localhost:8080/domain-generator/api/v1/projects/1" \
  -H "Content-Type: application/json"
```

**期望响应**:
```json
{
  "code": "BUSINESS_ERROR",
  "message": "项目下存在领域模型，无法删除",
  "success": false,
  "data": null
}
```

## 性能测试

### 批量创建项目

```bash
for i in {1..10}; do
  curl -X POST "http://localhost:8080/domain-generator/api/v1/projects" \
    -H "Content-Type: application/json" \
    -d "{
      \"name\": \"性能测试项目${i}\",
      \"domainPackage\": \"com.perf.test${i}.domain\",
      \"controllerPackage\": \"com.perf.test${i}.controller\",
      \"doPackage\": \"com.perf.test${i}.dataobject\",
      \"mapperPackage\": \"com.perf.test${i}.mapper\"
    }"
done
```

## 环境配置

### 开发环境
- 基础URL: `http://localhost:8080`
- 数据库: MySQL 8.0
- JDK: 1.8+

### 测试环境
- 基础URL: `http://test-server:8080`
- 数据库: PostgreSQL 12+
- JDK: 1.8+

### 生产环境
- 基础URL: `https://api.production.com`
- 数据库: PostgreSQL 12+ (集群)
- JDK: 11+

## 注意事项

1. **认证**: 生产环境需要在请求头中添加认证信息
2. **限流**: 接口有访问频率限制，建议间隔100ms以上
3. **数据格式**: 所有时间字段使用ISO 8601格式
4. **字符编码**: 统一使用UTF-8编码
5. **事务**: 修改操作支持事务回滚

## 常见问题

### Q1: 接口返回500错误
A1: 检查数据库连接和日志文件，确认服务状态

### Q2: 参数验证失败
A2: 检查请求参数格式和必填字段

### Q3: 代码生成失败
A3: 确认领域模型XML配置正确，表结构存在

### Q4: ER图保存失败
A4: 检查连线配置是否正确，外键关系是否存在

---

*文档更新时间: 2024-12-19*
*版本: v1.0*
