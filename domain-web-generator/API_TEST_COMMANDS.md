# API 测试命令文档

## 概述

本文档提供了测试统一返回封装实现的curl命令，用于验证所有API端点是否正确返回统一的响应格式。

## 测试环境

- 基础URL: `http://localhost:8080`
- 项目ID: 假设为 `1`
- 领域模型ID: 假设为 `1`
- 数据源ID: 假设为 `1`

## 项目管理 API 测试

### 1. 分页查询项目

```bash
curl -X GET "http://localhost:8080/api/v1/projects/page?pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": {
        "records": [...],
        "total": 0,
        "size": 10,
        "current": 1
    },
    "success": true
}
```

### 2. 新增项目

```bash
curl -X POST "http://localhost:8080/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试项目",
    "description": "这是一个测试项目",
    "version": "1.0.0"
  }'
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": 1,
    "success": true
}
```

**说明:** 新增接口直接返回新创建的资源ID，由全局响应处理器自动包装为统一格式。

### 3. 获取项目详情

```bash
curl -X GET "http://localhost:8080/api/v1/projects/1" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": {
        "id": 1,
        "name": "测试项目",
        "description": "这是一个测试项目",
        "version": "1.0.0"
    },
    "success": true
}
```

### 4. 编辑项目

```bash
curl -X PUT "http://localhost:8080/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "name": "更新后的项目名称",
    "description": "更新后的项目描述",
    "version": "1.1.0"
  }'
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": true,
    "success": true
}
```

### 5. 删除项目

```bash
curl -X DELETE "http://localhost:8080/api/v1/projects/1" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": true,
    "success": true
}
```

## 领域模型管理 API 测试

### 1. 分页查询领域模型

```bash
curl -X GET "http://localhost:8080/api/v1/domain-configs/page?projectId=1&pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": {
        "records": [...],
        "total": 0,
        "size": 10,
        "current": 1
    },
    "success": true
}
```

### 2. 新增领域模型

```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs" \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "domainName": "用户管理",
    "folder": "user",
    "description": "用户管理领域模型"
  }'
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": 1,
    "success": true
}
```

**说明:** 新增接口直接返回新创建的领域模型ID，由全局响应处理器自动包装为统一格式。

### 3. 获取领域模型详情

```bash
curl -X GET "http://localhost:8080/api/v1/domain-configs/1" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": {
        "id": 1,
        "projectId": 1,
        "domainName": "用户管理",
        "folder": "user",
        "description": "用户管理领域模型"
    },
    "success": true
}
```

### 4. 生成代码

```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs/1/generate-code" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": "代码生成成功",
    "success": true
}
```

## 数据源管理 API 测试

### 1. 分页查询数据源

```bash
curl -X GET "http://localhost:8080/api/v1/datasources/page?pageNum=1&pageSize=10" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": {
        "records": [...],
        "total": 0,
        "size": 10,
        "current": 1
    },
    "success": true
}
```

### 2. 新增数据源

```bash
curl -X POST "http://localhost:8080/api/v1/datasources" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试数据库",
    "code": "TEST_DB",
    "dbType": "MySQL",
    "host": "localhost",
    "port": 3306,
    "database": "test",
    "username": "root",
    "password": "password"
  }'
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": 1,
    "success": true
}
```

**说明:** 新增接口直接返回新创建的数据源ID，由全局响应处理器自动包装为统一格式。

### 3. 测试数据库连接

```bash
curl -X POST "http://localhost:8080/api/v1/datasources/test-connection" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试数据库",
    "code": "TEST_DB",
    "dbType": "MySQL",
    "host": "localhost",
    "port": 3306,
    "database": "test",
    "username": "root",
    "password": "password"
  }'
```

**预期响应格式:**
```json
{
    "code": "0",
    "message": "操作成功",
    "data": true,
    "success": true
}
```

## 异常情况测试

### 1. 参数验证失败

```bash
curl -X POST "http://localhost:8080/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "缺少必填字段name"
  }'
```

**预期响应格式:**
```json
{
    "code": "VALIDATION_ERROR",
    "message": "参数验证失败: name: 项目名称不能为空",
    "data": null,
    "success": false
}
```

### 2. 资源不存在

```bash
curl -X GET "http://localhost:8080/api/v1/projects/999" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "RESOURCE_NOT_FOUND",
    "message": "项目不存在",
    "data": null,
    "success": false
}
```

### 3. 系统异常

```bash
# 触发一个可能导致系统异常的操作
curl -X GET "http://localhost:8080/api/v1/projects/page?pageNum=invalid" \
  -H "Content-Type: application/json"
```

**预期响应格式:**
```json
{
    "code": "SYSTEM_ERROR",
    "message": "系统内部错误，请联系管理员",
    "data": null,
    "success": false
}
```

## 验证要点

1. **响应格式一致性**: 所有成功响应都应该包含 `code`, `message`, `data`, `success` 字段
2. **状态码统一**: 成功响应状态码应该是 "0"
3. **消息统一**: 成功响应消息应该是 "操作成功"
4. **异常处理**: 异常响应应该包含相应的错误码和错误消息
5. **数据包装**: 业务数据应该正确包装在 `data` 字段中

## 测试步骤

1. 启动应用程序
2. 按顺序执行上述测试命令
3. 验证每个响应的格式是否符合预期
4. 检查异常情况下的错误响应格式
5. 确认所有API端点都返回统一的响应格式
