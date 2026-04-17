# Domain-Core 使用指南

## 📖 目录

- [项目概述](#项目概述)
- [核心概念](#核心概念)
- [快速开始](#快速开始)
- [领域模型定义](#领域模型定义)
- [CRUD 操作](#crud-操作)
- [查询构建](#查询构建)
- [关联数据加载](#关联数据加载)
- [批量操作](#批量操作)
- [最佳实践](#最佳实践)
- [常见问题](#常见问题)

---

## 项目概述

**Domain-Core** 是一个基于领域驱动设计（DDD）的代码生成框架，通过 XML 配置自动生成领域模型、服务接口、仓储接口等代码。

### 核心特性

- 🚀 **自动代码生成** - 基于 XML 配置生成完整的 DDD 代码结构
- 🔗 **关联关系管理** - 支持一对一、一对多、多对多关联
- 🔍 **类型安全查询** - Lambda 表达式构建类型安全的查询条件
- 📦 **批量操作优化** - 数据库特定的批量插入/更新优化
- 🔄 **链式 API** - 流畅的链式调用体验
- 🎯 **懒加载控制** - 精确控制关联数据的加载时机

### 技术栈

- Java 17
- Spring Boot 2.6.7
- MyBatis Plus 3.5.3.1
- MapStruct 1.4.2.Final
- Hutool 5.8.23

---

## 核心概念

### DDD 分层架构

```
Controller (控制层)
    ↓
AppService (应用服务层)
    ↓
DomainService (领域服务层)
    ↓
Repository (仓储层)
    ↓
Mapper (数据访问层)
```

### 核心组件

| 组件 | 说明 | 示例 |
|------|------|------|
| **Domain** | 领域模型，聚合根 | `FamilyDomain` |
| **Service** | 领域服务接口 | `FamilyService` |
| **Repository** | 仓储接口 | `FamilyRepository` |
| **Convertor** | DTO ↔ Domain 转换器 | `FamilyConvertor` |
| **LambdaExp** | Lambda 表达式辅助类 | `FamilyLambdaExp` |
| **LoadFlag** | 关联加载控制标识 | `FamilyDomain.LoadFlag` |

---

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!-- 根据数据库类型选择对应的支持包 -->
<dependency>
    <groupId>com.artframework</groupId>
    <artifactId>domain-postgresql-support</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. 配置数据源

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### 3. 定义领域模型

创建 `domain-config.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<domains>
    <domain name="Family" description="家庭领域模型" main-table="family">
        <!-- 一对一关联 -->
        <related description="家庭住址" table="family_address" many="false" 
                 fk="id:family_id" redundancy="name:family_name"/>
        
        <!-- 一对多关联 -->
        <related description="家庭成员" table="family_member" many="true" 
                 fk="id:family_id" redundancy="name:family_name"/>
    </domain>
</domains>
```

### 4. 运行代码生成器

生成的代码结构：

```
com.example.domain.family
├── domain/
│   ├── FamilyDomain.java
│   ├── FamilyAddressDomain.java
│   └── FamilyMemberDomain.java
├── service/
│   ├── FamilyService.java
│   └── impl/
│       └── FamilyServiceImpl.java
├── repository/
│   └── FamilyRepository.java
├── convertor/
│   └── FamilyConvertor.java
└── lambdaexp/
    └── FamilyLambdaExp.java
```

---

## 领域模型定义

### XML 配置元素

#### Domain 元素（聚合根）

| 属性 | 说明 | 示例 |
|------|------|------|
| `name` | 领域名称 | `Family` |
| `description` | 描述 | `家庭领域模型` |
| `main-table` | 主表名 | `family` |

#### Related 元素（关联实体）

| 属性 | 说明 | 示例 |
|------|------|------|
| `table` | 关联表名 | `family_member` |
| `many` | 是否一对多 | `true` / `false` |
| `fk` | 外键映射 | `id:family_id` |
| `redundancy` | 冗余字段映射 | `name:family_name` |

#### Ref 元素（引用关系）

| 属性 | 说明 | 示例 |
|------|------|------|
| `name` | 引用字段名 | `creator` |
| `table` | 引用表 | `user` |
| `fk` | 引用键映射 | `create_by:id` |

### 配置示例

```xml
<domain name="Order" description="订单领域模型" main-table="order">
    <!-- 订单详情（一对多） -->
    <related description="订单详情" table="order_item" many="true" 
             fk="id:order_id" redundancy="order_no:order_no"/>
    
    <!-- 订单地址（一对一） -->
    <related description="订单地址" table="order_address" many="false" 
             fk="id:order_id"/>
    
    <!-- 创建人引用 -->
    <ref name="creator" table="user" fk="create_by:id"/>
    
    <!-- 审核人引用 -->
    <ref name="auditor" table="user" fk="audit_by:id"/>
</domain>
```

---

## CRUD 操作

### 创建（Insert）

```java
// 创建领域对象
FamilyDomain family = new FamilyDomain();
family.setName("张家");
family.setPersonCount(4);

// 创建关联对象
FamilyAddressDomain address = new FamilyAddressDomain();
address.setProvince("北京市");
address.setCity("北京市");
family.setFamilyAddress(address);

List<FamilyMemberDomain> members = new ArrayList<>();
members.add(new FamilyMemberDomain("张三", 35, "FATHER"));
members.add(new FamilyMemberDomain("李四", 33, "MOTHER"));
family.setFamilyMemberList(members);

// 插入（自动处理外键和冗余字段）
Integer id = familyService.insert(family);
```

**特性：**
- ✅ 自动填充外键字段（`family_id`）
- ✅ 自动填充冗余字段（`family_name`）
- ✅ 级联插入关联实体

### 查询（Query）

#### 根据 ID 查询

```java
// 仅加载主表
FamilyDomain family = FamilyDomain.load(1, familyService);

// 加载指定关联
FamilyDomain family = FamilyDomain.load(1, familyService);
family.loadRelated(FamilyAddressDomain.class);

// 链式加载多个关联
FamilyDomain family = FamilyDomain.load(1, familyService)
    .loadRelated(FamilyAddressDomain.class)
    .loadRelated(FamilyMemberDomain.class);
```

#### 条件查询

```java
// 构建查询条件
LambdaQuery<FamilyDomain> query = LambdaQuery.of(FamilyDomain.class)
    .eq(FamilyDomain::getName, "张家")
    .ge(FamilyDomain::getPersonCount, 3)
    .orderBy(FamilyDomain::getCreateTime, Order.DESC);

// 查询列表
List<FamilyDomain> list = familyService.queryList(FamilyDomain.class, query);

// 分页查询
PageDomain pageDomain = new PageDomain(1, 10);
IPage<FamilyDomain> page = familyService.queryPage(
    FamilyDomain.class, 
    pageDomain, 
    query
);
```

### 更新（Update）

```java
// 加载原始数据
FamilyDomain original = FamilyDomain.load(1, familyService)
    .loadRelated(FamilyMemberDomain.class);

// 修改数据
FamilyDomain updated = FamilyDomain.copy(original);
updated.setName("张家（新）");
updated.setPersonCount(5);

// 添加新成员
FamilyMemberDomain newMember = new FamilyMemberDomain("张五", 8, "SON");
updated.getFamilyMemberList().add(newMember);

// 更新（只更新变化的字段）
familyService.update(updated, original);
```

**特性：**
- ✅ 自动对比差异，只更新变化的字段
- ✅ 级联更新关联实体
- ✅ 自动同步冗余字段

### 删除（Delete）

```java
// 简单删除（不级联）
familyService.delete(1);

// 级联删除（控制范围）
FamilyDomain.LoadFlag loadFlag = FamilyDomain.LoadFlag.builder()
    .loadFamilyAddressDomain(true)
    .loadFamilyMemberDomain(true)
    .build();

familyService.delete(1, loadFlag);
```

---

## 查询构建

### LambdaQuery API

#### 基本条件

```java
LambdaQuery<FamilyDomain> query = LambdaQuery.of(FamilyDomain.class)
    .eq(FamilyDomain::getName, "张家")              // 等于
    .ne(FamilyDomain::getStatus, "DELETED")        // 不等于
    .gt(FamilyDomain::getPersonCount, 2)           // 大于
    .ge(FamilyDomain::getPersonCount, 2)           // 大于等于
    .lt(FamilyDomain::getPersonCount, 10)          // 小于
    .le(FamilyDomain::getPersonCount, 10)          // 小于等于
    .like(FamilyDomain::getName, "张")             // 模糊匹配
    .likeLeft(FamilyDomain::getName, "张")         // 左模糊
    .likeRight(FamilyDomain::getName, "张")        // 右模糊
    .in(FamilyDomain::getStatus, Arrays.asList("ACTIVE", "PENDING"))  // IN
    .notIn(FamilyDomain::getStatus, Arrays.asList("DELETED"))         // NOT IN
    .isNull(FamilyDomain::getDeletedAt)            // IS NULL
    .notNull(FamilyDomain::getCreateTime);         // IS NOT NULL
```

#### 链式 OR（推荐）

**同一字段多个值：**

```java
// 使用 in() 更简洁
query.in(FamilyMemberDomain::getType, Arrays.asList("SON", "DAUGHTER"));
// 生成: type IN (?, ?)

// 或使用链式 OR
query.eq(FamilyMemberDomain::getType, "SON")
     .or().eq(FamilyMemberDomain::getType, "DAUGHTER");
// 生成: (type = ? OR type = ?)
```

**不同字段 OR：**

```java
query.eq(FamilyDomain::getName, "张家")
     .or().eq(FamilyDomain::getPersonCount, 5)
     .or().like(FamilyDomain::getAddress, "北京");
// 生成: (name = ? OR person_count = ? OR address LIKE ?)
```

**OR 多个字段（AND 组）：**

```java
query.eq(FamilyDomain::getStatus, "ACTIVE")
     .orGroup(q -> q.eq(FamilyDomain::getName, "张家")
                    .eq(FamilyDomain::getPersonCount, 5));
// 生成: status = ? OR (name = ? AND person_count = ?)
```

#### 嵌套条件

**AND 组：**

```java
query.eq(FamilyDomain::getStatus, "ACTIVE")
     .and(q -> q.ge(FamilyDomain::getPersonCount, 3)
                .le(FamilyDomain::getPersonCount, 10));
// 生成: status = ? AND (person_count >= ? AND person_count <= ?)
```

**复杂嵌套：**

```java
query.eq(FamilyDomain::getStatus, "ACTIVE")
     .and(q -> q.eq(FamilyDomain::getProvince, "北京")
                .or().eq(FamilyDomain::getProvince, "上海"))
     .ge(FamilyDomain::getPersonCount, 3);
// 生成: status = ? AND (province = ? OR province = ?) AND person_count >= ?
```

#### 排序

```java
query.orderBy(FamilyDomain::getCreateTime, Order.DESC)
     .orderBy(FamilyDomain::getId, Order.ASC);
// 生成: ORDER BY create_time DESC, id ASC
```

### LoadRelated 高级用法

#### 条件过滤

```java
family.loadRelated(FamilyMemberDomain.class, 
    q -> q.eq(FamilyMemberDomain::getType, "SON")
          .ge(FamilyMemberDomain::getAge, 18)
          .orderBy(FamilyMemberDomain::getAge, Order.DESC)
);
// 生成: WHERE family_id = ? AND type = ? AND age >= ? ORDER BY age DESC
```

#### 链式 OR 查询

```java
family.loadRelated(FamilyMemberDomain.class, 
    q -> q.eq(FamilyMemberDomain::getType, "SON")
          .or().eq(FamilyMemberDomain::getType, "DAUGHTER")
);
// 生成: WHERE family_id = ? AND (type = ? OR type = ?)
```

#### 链式加载

```java
FamilyDomain family = FamilyDomain.load(1, familyService)
    .loadRelated(FamilyAddressDomain.class)
    .loadRelated(FamilyMemberDomain.class, 
        q -> q.in(FamilyMemberDomain::getType, Arrays.asList("SON", "DAUGHTER"))
              .orderBy(FamilyMemberDomain::getAge, Order.DESC)
    );

// 访问数据
FamilyAddressDomain address = family.getFamilyAddress();
List<FamilyMemberDomain> children = family.getFamilyMemberList();
```

---

## 批量操作

### 批量插入

```java
List<FamilyDomain> families = new ArrayList<>();
for (int i = 0; i < 100; i++) {
    FamilyDomain family = new FamilyDomain();
    family.setName("家庭" + i);
    family.setPersonCount(3);
    families.add(family);
}

// 批量插入（数据库优化）
familyService.batchInsert(families);
```

**数据库优化：**
- **MySQL**: `INSERT ... VALUES (...), (...), (...)`
- **PostgreSQL**: `COPY` 或 批量 `INSERT`
- **Oracle**: `INSERT ALL`

### 批量更新

```java
List<FamilyDomain> updates = new ArrayList<>();
// ... 构建更新列表

familyService.batchUpdate(updates);
```

### 批量删除

```java
List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5);
familyService.batchDelete(ids);
```

### 性能建议

- ✅ 单批次建议不超过 1000 条记录
- ✅ 大数据量分批处理
- ✅ 使用事务包裹批量操作
- ✅ 批量操作前禁用索引（超大数据量）

```java
// 分批处理示例
int batchSize = 1000;
List<FamilyDomain> allFamilies = ...; // 10000 条数据

for (int i = 0; i < allFamilies.size(); i += batchSize) {
    int end = Math.min(i + batchSize, allFamilies.size());
    List<FamilyDomain> batch = allFamilies.subList(i, end);
    familyService.batchInsert(batch);
}
```

---

## 最佳实践

### 1. LoadFlag 精确控制

```java
// ❌ 避免：加载所有关联
family.setLoadFlag(FamilyDomain.LoadFlag.builder()
    .loadAll(true)
    .build());

// ✅ 推荐：按需加载
family.setLoadFlag(FamilyDomain.LoadFlag.builder()
    .loadFamilyAddressDomain(true)  // 只加载需要的
    .build());
```

### 2. 分页查询

```java
// ✅ 合理的分页大小
PageDomain pageDomain = new PageDomain(1, 20);  // 每页 20 条

// ❌ 避免：过大的分页
PageDomain pageDomain = new PageDomain(1, 1000); // 性能问题
```

### 3. 查询优化

```java
// ✅ 使用 in() 代替多个 OR
query.in(field, Arrays.asList("A", "B", "C"));

// ❌ 避免：多个 OR 条件
query.eq(field, "A")
     .or().eq(field, "B")
     .or().eq(field, "C");
```

### 4. 更新优化

```java
// ✅ 只更新变化的字段
familyService.update(newDomain, originalDomain);

// ❌ 避免：全字段更新
familyService.updateById(newDomain);
```

### 5. 事务管理

```java
@Transactional(rollbackFor = Exception.class)
public void complexOperation() {
    // 插入主表
    Integer familyId = familyService.insert(family);
    
    // 插入关联表
    memberService.batchInsert(members);
    
    // 更新统计
    familyService.updateStatistics(familyId);
}
```

### 6. 异常处理

```java
try {
    familyService.insert(family);
} catch (DuplicateKeyException e) {
    throw new BusinessException("家庭名称已存在");
} catch (DataAccessException e) {
    log.error("数据库操作失败", e);
    throw new BusinessException("操作失败，请稍后重试");
}
```

---

## 常见问题

### Q1: 如何处理循环依赖？

**A**: 使用 `@JsonIgnore` 和 `@ApiModelProperty(hidden = true)` 标记反向引用：

```java
public class FamilyMemberDomain {
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private FamilyDomain family;  // 反向引用
}
```

### Q2: 如何自定义 ID 生成策略？

**A**: 配置 MyBatis Plus 的 ID 生成器：

```java
@Bean
public IdentifierGenerator identifierGenerator() {
    return new CustomIdGenerator();
}
```

### Q3: 如何处理软删除？

**A**: 配置逻辑删除字段：

```yaml
mybatis-plus:
  global-config:
    db-config:
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
```

### Q4: 如何添加自定义查询方法？

**A**: 在 Repository 接口中添加自定义方法：

```java
public interface FamilyRepository extends BaseDomainRepository<FamilyDO> {
    /**
     * 自定义查询方法
     */
    List<FamilyDO> findByProvinceAndCity(String province, String city);
}
```

### Q5: 如何优化 N+1 查询问题？

**A**: 使用 LoadFlag 批量加载关联数据：

```java
// ❌ N+1 问题
List<FamilyDomain> families = familyService.queryList(...);
for (FamilyDomain family : families) {
    family.loadRelated(FamilyMemberDomain.class);  // 每个都查一次
}

// ✅ 批量加载
FamilyDomain.LoadFlag loadFlag = FamilyDomain.LoadFlag.builder()
    .loadFamilyMemberDomain(true)
    .build();
List<FamilyDomain> families = familyService.queryList(..., loadFlag);
```

### Q6: 生成的代码可以修改吗？

**A**: ❌ **不可以**！所有生成的领域模型代码都不应手动修改：
- `*Domain.java`
- `*Service.java`
- `*Repository.java`
- `*LambdaExp.java`

如需修改结构，应更新 `domain-config.xml` 后重新生成。

### Q7: 如何处理复杂的业务逻辑？

**A**: 在应用服务层（AppService）编写业务逻辑，不要在 Domain 或 Service 中：

```java
@Service
public class FamilyAppService {
    
    @Autowired
    private FamilyService familyService;
    
    @Autowired
    private MemberService memberService;
    
    @Transactional
    public void transferMember(Integer fromFamilyId, Integer toFamilyId, Integer memberId) {
        // 复杂业务逻辑
        FamilyDomain fromFamily = FamilyDomain.load(fromFamilyId, familyService);
        FamilyDomain toFamily = FamilyDomain.load(toFamilyId, familyService);
        
        // ... 业务处理
        
        familyService.update(fromFamily);
        familyService.update(toFamily);
    }
}
```

### Q8: 如何调试生成的 SQL？

**A**: 启用 MyBatis Plus 日志：

```yaml
logging:
  level:
    com.artframework.sample.mappers: DEBUG

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

---

## 参考文档

- [CLAUDE.md](CLAUDE.md) - 项目说明和架构
- [CHAINED_OR_API_GUIDE.md](CHAINED_OR_API_GUIDE.md) - 链式 OR 完整指南
- [MIGRATION_TO_CHAINED_OR.md](MIGRATION_TO_CHAINED_OR.md) - 迁移指南
- [API_DEPRECATION_OR_CONSUMER.md](API_DEPRECATION_OR_CONSUMER.md) - API 废弃说明
- [MYBATIS_CONFIGURATION_GUIDE.md](MYBATIS_CONFIGURATION_GUIDE.md) - MyBatis Plus 配置

---

**版本**: 1.0  
**更新日期**: 2026-04-17  
**作者**: Domain-Core Team
