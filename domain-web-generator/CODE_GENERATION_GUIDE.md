# 代码生成功能使用指南

## 概述

本指南介绍如何使用领域驱动设计代码生成器来生成完整的DDD代码结构。代码生成器基于`domain-generator`工程，能够根据领域模型XML配置自动生成各层代码文件。

## 代码生成流程

### 1. 前置条件

1. **创建项目**：首先需要在系统中创建一个项目，配置好各层的包名
2. **配置数据源**：确保有可用的数据源配置
3. **设计ER图**：使用ER图设计工具设计领域模型
4. **生成XML**：基于ER图连线生成领域模型XML配置

### 2. 生成步骤

#### 步骤1：创建项目
```bash
curl -X POST "http://localhost:8080/api/v1/projects" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "用户管理系统",
    "domainPackage": "com.example.domain",
    "controllerPackage": "com.example.controller",
    "doPackage": "com.example.dataobject",
    "mapperPackage": "com.example.mapper"
  }'
```

#### 步骤2：创建领域模型
```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs" \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "domainName": "User",
    "mainTable": "sys_user",
    "folder": "user"
  }'
```

#### 步骤3：设计ER图
```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs/1/er-diagram" \
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
        "tableName": "sys_user_role",
        "x": 400,
        "y": 100,
        "w": 200,
        "h": 120
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
        "many": "1"
      }
    ]
  }'
```

#### 步骤4：生成XML配置
```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs/1/generate-xml" \
  -H "Content-Type: application/json"
```

#### 步骤5：生成代码
```bash
curl -X POST "http://localhost:8080/api/v1/domain-configs/1/generate-code" \
  -H "Content-Type: application/json"
```

## 生成的代码结构

### 目录结构
```
generated-code/
├── controller/                 # 控制器层
│   └── UserController.java
├── domain/                     # 领域层
│   └── user/
│       ├── domain/             # 领域模型
│       │   ├── UserDomain.java
│       │   └── UserFindDomain.java
│       ├── service/            # 领域服务
│       │   ├── UserService.java
│       │   └── impl/
│       │       └── UserServiceImpl.java
│       ├── repository/         # 数据仓储
│       │   ├── UserRepository.java
│       │   └── impl/
│       │       └── UserRepositoryImpl.java
│       ├── convertor/          # 对象转换器
│       │   ├── UserConvertor.java
│       │   └── UserConvertorDecorator.java
│       └── lambdaexp/          # Lambda表达式
│           └── UserLambdaExp.java
├── dataobject/                 # 数据对象
│   ├── SysUserDO.java
│   └── SysUserRoleDO.java
└── mapper/                     # 数据访问层
    ├── SysUserMapper.java
    └── SysUserRoleMapper.java
```

### 代码文件说明

#### 1. Domain 类（领域聚合根）
- **UserDomain.java**：主聚合根，包含业务逻辑和关联实体
- **UserFindDomain.java**：查询请求对象

#### 2. Service 类（领域服务）
- **UserService.java**：领域服务接口，定义业务操作
- **UserServiceImpl.java**：领域服务实现，处理业务逻辑

#### 3. Repository 类（数据仓储）
- **UserRepository.java**：仓储接口，定义数据访问方法
- **UserRepositoryImpl.java**：仓储实现，处理数据持久化

#### 4. Converter 类（对象转换器）
- **UserConvertor.java**：基于MapStruct的对象转换器
- **UserConvertorDecorator.java**：转换器装饰器，可自定义转换逻辑

#### 5. Lambda 表达式类
- **UserLambdaExp.java**：类型安全的查询表达式，用于构建查询条件

#### 6. Controller 类（控制器）
- **UserController.java**：REST API控制器，提供HTTP接口

#### 7. DataObject 类（数据对象）
- **SysUserDO.java**：用户表对应的数据对象
- **SysUserRoleDO.java**：用户角色表对应的数据对象

#### 8. Mapper 类（数据访问）
- **SysUserMapper.java**：用户表的MyBatis-Plus Mapper
- **SysUserRoleMapper.java**：用户角色表的MyBatis-Plus Mapper

## 代码特性

### 1. DDD架构
- 严格遵循领域驱动设计原则
- 清晰的分层架构
- 聚合根管理一致性边界

### 2. 类型安全
- 使用Lambda表达式构建类型安全的查询
- 编译期检查，避免字段名错误

### 3. 自动化处理
- 自动处理外键关系
- 自动设置冗余字段
- 自动生成CRUD操作

### 4. 性能优化
- 按需加载关联数据
- 缓存机制减少数据库访问
- 内存查询优化

## 使用示例

### 基本CRUD操作

```java
// 创建用户
UserDomain user = new UserDomain();
user.setUserName("john");
user.setEmail("john@example.com");
Integer userId = userService.insert(user);

// 查询用户
UserDomain loadedUser = UserDomain.load(userId, userService);

// 加载关联数据
loadedUser.loadRelated(UserDomain.SysUserRoleDomain.class);

// 更新用户
UserDomain updatedUser = new UserDomain();
updatedUser.setId(userId);
updatedUser.setEmail("john.doe@example.com");
userService.update(updatedUser, loadedUser);

// 删除用户
userService.delete(userId);
```

### 复杂查询

```java
// 使用Lambda表达式构建查询
LambdaQuery<UserDomain> query = LambdaQuery.of(UserDomain.class)
    .like(UserDomain::getUserName, "john")
    .ge(UserDomain::getCreateTime, startTime)
    .orderBy(UserDomain::getCreateTime, Order.DESC);

// 分页查询
IPage<UserDomain> page = userService.queryPage(
    UserDomain.class, 
    pageDomain, 
    query
);
```

## 注意事项

1. **生成前检查**：确保XML配置正确，数据源连接正常
2. **文件覆盖**：生成的代码可能覆盖现有文件，请备份重要修改
3. **包名配置**：确保项目中的包名配置正确
4. **数据库权限**：确保数据源用户有足够的查询权限
5. **依赖检查**：确保项目已引入必要的依赖包

## 故障排除

### 常见问题

1. **XML配置错误**
   - 检查表名是否存在
   - 检查外键关系是否正确
   - 验证XML语法

2. **数据库连接失败**
   - 检查数据源配置
   - 验证网络连接
   - 确认用户权限

3. **代码生成失败**
   - 查看日志文件
   - 检查模板文件
   - 验证输出目录权限

### 调试建议

1. 查看应用日志获取详细错误信息
2. 使用测试数据源验证配置
3. 逐步验证每个生成步骤
4. 检查临时文件内容

---

*更新时间: 2024-12-19*
*版本: v2.0*
