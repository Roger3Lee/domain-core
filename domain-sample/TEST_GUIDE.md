# 测试场景说明文档

本文档说明 domain-sample 项目中的测试场景，覆盖家庭领域模型的增删改查操作。

## 测试文件结构

```
domain-sample/src/test/java/
└── com/artframework/sample/
    ├── domains/family/
    │   ├── service/
    │   │   └── FamilyServiceTest.java          # 服务层单元测试
    │   └── domain/
    │       └── FamilyDomainTest.java            # 领域模型测试
    └── controllers/
        └── FamilyControllerTest.java            # 控制器集成测试
```

## 测试覆盖范围

### 1. FamilyServiceTest - 服务层单元测试

**测试类路径**: `com.artframework.sample.domains.family.service.FamilyServiceTest`

#### 查询操作测试（Query Operations）
- ✅ 根据ID查找家庭基本信息（不加载关联数据）
- ✅ 根据ID查找家庭信息（加载住址信息）
- ✅ 根据ID查找家庭信息（加载成员列表）
- ✅ 加载所有关联数据（住址 + 成员）
- ✅ 家庭不存在时返回null
- ✅ 根据业务键查找

#### 新增操作测试（Insert Operations）
- ✅ 仅插入家庭基本信息
- ✅ 插入家庭及住址信息（一对一关联）
- ✅ 插入家庭及多个成员（一对多关联）

#### 更新操作测试（Update Operations）
- ✅ 仅更新家庭基本信息
- ✅ 提供原始domain，不再查询
- ✅ 更新家庭信息及住址
- ✅ 家庭不存在时返回失败

#### 删除操作测试（Delete Operations）
- ✅ 仅删除家庭基本信息
- ✅ 使用LoadFlag控制级联删除范围（删除家庭和住址）
- ✅ 使用LoadFlag控制级联删除范围（删除家庭和成员）
- ✅ 级联删除所有关联数据
- ✅ 家庭不存在时返回失败

#### 边界和异常测试（Edge Cases）
- ✅ 空家庭成员列表不影响查询
- ✅ 住址为null不影响查询
- ✅ LoadFlag为null时不加载关联数据

### 2. FamilyDomainTest - 领域模型测试

**测试类路径**: `com.artframework.sample.domains.family.domain.FamilyDomainTest`

#### 领域模型构造测试（Domain Construction）
- ✅ 无参构造函数创建领域对象
- ✅ 全参构造函数创建领域对象
- ✅ 使用Key和Service构造领域对象

#### 静态加载方法测试（Static Load Methods）
- ✅ 静态load方法 - 根据ID加载领域对象
- ✅ 静态load方法 - ID不存在返回null
- ✅ 静态loadByKey方法 - 根据业务键加载

#### 关联数据加载测试（Related Data Loading）
- ✅ loadRelated - 加载住址信息（一对一关系）
- ✅ loadRelated - 加载成员列表（一对多关系）
- ✅ loadRelated - 带查询条件加载成员

#### 关联对象访问测试（Related Object Access）
- ✅ getFamilyAddress - 访问住址并自动设置_thisDomain
- ✅ getFamilyAddress - 住址为null时不抛异常
- ✅ getFamilyMemberList - 访问成员列表并自动设置_thisDomain
- ✅ getFamilyMemberList - 空列表不抛异常
- ✅ getFamilyMemberList - null时不抛异常

#### LoadFlag测试
- ✅ LoadFlag Builder - 构建加载标志
- ✅ LoadFlag Builder - 加载所有数据
- ✅ LoadFlag merge - 合并两个LoadFlag
- ✅ LoadFlag merge - 各种null情况处理

#### 领域对象拷贝测试（Domain Copy）
- ✅ copy - 浅拷贝领域对象

#### 嵌套领域对象测试（Nested Domain）
- ✅ FamilyAddressDomain - 构造和字段设置
- ✅ FamilyMemberDomain - 构造和字段设置
- ✅ 全参构造函数测试

### 3. FamilyControllerTest - 控制器集成测试

**测试类路径**: `com.artframework.sample.controllers.FamilyControllerTest`

#### 查询API测试（Query API - POST /family/v1/query）
- ✅ 根据ID查找家庭信息（成功）
- ✅ 加载完整信息（包含住址和成员）
- ✅ 家庭不存在（返回null）
- ✅ 请求体格式错误（400错误）

#### 新增API测试（Insert API - PUT /family/v1）
- ✅ 创建家庭基本信息（成功）
- ✅ 创建家庭及住址信息
- ✅ 创建家庭及成员信息
- ✅ 请求体为空处理

#### 更新API测试（Update API - POST /family/v1）
- ✅ 修改家庭基本信息（成功）
- ✅ 修改家庭及住址信息
- ✅ 家庭不存在（返回false）
- ✅ 缺少ID字段处理

#### 删除API测试（Delete API - DELETE /family/v1）
- ✅ 根据ID删除家庭（成功）
- ✅ 家庭不存在（返回false）
- ✅ 缺少key参数（400错误）
- ✅ key参数格式错误（400错误）

#### 集成场景测试（Integration Scenarios）
- ✅ 完整流程 - 创建、查询、更新、删除
- ✅ 复杂场景 - 创建包含完整关联数据的家庭

#### HTTP方法测试
- ✅ 查询接口 - 仅支持POST方法
- ✅ 新增接口 - 仅支持PUT方法
- ✅ 更新接口 - 仅支持POST方法
- ✅ 删除接口 - 仅支持DELETE方法

## 运行测试

### 运行所有测试
```bash
# 在项目根目录运行
./mvnw test

# 仅运行 domain-sample 模块的测试
./mvnw test -pl domain-sample
```

### 运行特定测试类
```bash
# 运行服务层测试
./mvnw test -Dtest=FamilyServiceTest -pl domain-sample

# 运行领域模型测试
./mvnw test -Dtest=FamilyDomainTest -pl domain-sample

# 运行控制器测试
./mvnw test -Dtest=FamilyControllerTest -pl domain-sample
```

### 运行特定测试方法
```bash
# 运行服务层的查询测试
./mvnw test -Dtest=FamilyServiceTest#QueryTests -pl domain-sample

# 运行控制器的完整CRUD流程测试
./mvnw test -Dtest=FamilyControllerTest#fullCrudFlow_success -pl domain-sample
```

### 生成测试覆盖率报告
```bash
# 运行测试并生成覆盖率报告
./mvnw clean test jacoco:report -pl domain-sample

# 报告位置: domain-sample/target/site/jacoco/index.html
```

## 测试技术栈

- **JUnit 5 (Jupiter)** - 测试框架
- **Mockito** - Mock框架
- **AssertJ** - 流式断言库
- **Spring MockMvc** - Web层测试工具
- **Jackson** - JSON序列化/反序列化

## 测试模式和最佳实践

### AAA模式（Arrange-Act-Assert）
所有测试都遵循AAA模式：

```java
@Test
void testExample() {
    // Arrange - 准备测试数据和环境
    FamilyDomain family = new FamilyDomain();
    family.setName("测试家庭");
    
    // Act - 执行被测试的操作
    Long result = familyService.insert(family);
    
    // Assert - 验证结果
    assertThat(result).isNotNull();
}
```

### @Nested 分组
使用 `@Nested` 注解对相关测试进行逻辑分组，提高可读性：

```java
@Nested
@DisplayName("Query Operations")
class QueryTests {
    // 所有查询相关的测试
}

@Nested
@DisplayName("Insert Operations")
class InsertTests {
    // 所有新增相关的测试
}
```

### @DisplayName 描述
使用中文描述测试用例的业务含义：

```java
@Test
@DisplayName("查询 - 根据ID查找家庭信息（成功）")
void find_byId_success() {
    // 测试代码
}
```

### Mock vs Real
- **单元测试**（Service/Domain）：使用 `@Mock` 模拟依赖
- **集成测试**（Controller）：使用 `@WebMvcTest` + `@MockBean` 模拟服务层

### 边界条件测试
测试覆盖以下边界条件：
- ✅ null值处理
- ✅ 空集合处理
- ✅ 不存在的数据
- ✅ 无效的输入参数
- ✅ 异常情况

## 测试数据准备

所有测试使用统一的测试数据结构：

```java
@BeforeEach
void setUp() {
    // 准备家庭基本信息
    testFamily = new FamilyDomain();
    testFamily.setId(1L);
    testFamily.setName("张家");
    testFamily.setPersonCount(3);
    
    // 准备住址信息
    testAddress = new FamilyDomain.FamilyAddressDomain();
    testAddress.setId(1L);
    testAddress.setFamilyId(1L);
    testAddress.setAddressName("北京市朝阳区");
    
    // 准备成员信息
    testMember = new FamilyDomain.FamilyMemberDomain();
    testMember.setId(1L);
    testMember.setName("张三");
    testMember.setType("父亲");
}
```

## 验证方式

### AssertJ 流式断言
```java
assertThat(result).isNotNull();
assertThat(result.getId()).isEqualTo(1L);
assertThat(result.getName()).isEqualTo("张家");
assertThat(result.getFamilyMemberList()).hasSize(2);
```

### Mockito 验证
```java
verify(familyRepository).insert(any(FamilyDomain.class));
verify(familyService, times(1)).find(any());
verify(familyService, never()).delete(anyLong());
```

### MockMvc JSON路径验证
```java
mockMvc.perform(post("/family/v1/query")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id").value(1))
    .andExpect(jsonPath("$.name").value("张家"))
    .andExpect(jsonPath("$.familyMemberList", hasSize(2)));
```

## 测试覆盖统计

| 测试类 | 测试方法数 | 覆盖场景 |
|--------|-----------|---------|
| FamilyServiceTest | 20+ | 服务层CRUD + 边界条件 |
| FamilyDomainTest | 25+ | 领域模型构造、加载、关联关系 |
| FamilyControllerTest | 20+ | REST API + HTTP方法 + 集成场景 |
| **总计** | **65+** | **全面覆盖增删改查所有场景** |

## 常见问题

### Q: 测试运行失败，提示找不到类？
A: 先执行 `./mvnw clean install` 编译整个项目，确保所有依赖都已安装。

### Q: 如何只运行某一个测试类？
A: 使用 `-Dtest=类名` 参数，例如：`./mvnw test -Dtest=FamilyServiceTest -pl domain-sample`

### Q: 如何跳过测试直接构建？
A: 使用 `-DskipTests` 参数：`./mvnw clean install -DskipTests`

### Q: 测试覆盖率报告在哪里查看？
A: 运行 `./mvnw test jacoco:report` 后，打开 `domain-sample/target/site/jacoco/index.html`

### Q: 如何添加新的测试场景？
A: 按照现有测试类的结构，在对应的 `@Nested` 类中添加新的 `@Test` 方法，遵循AAA模式和命名规范。

## 下一步

1. **持续集成**：将测试集成到CI/CD流程中
2. **性能测试**：添加JMeter或Gatling性能测试
3. **数据库集成测试**：使用Testcontainers进行真实数据库测试
4. **E2E测试**：添加端到端测试覆盖完整业务流程

---

**编写时间**: 2026-04-16  
**测试框架版本**:
- JUnit 5.8.2
- Mockito 4.6.1
- AssertJ 3.23.1
- Spring Boot Test 2.6.15
