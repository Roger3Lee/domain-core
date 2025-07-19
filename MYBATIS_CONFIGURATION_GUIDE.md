# MyBatis Plus 配置功能指南

本文档介绍 domain-core 项目中新增的 MyBatis Plus 配置功能，包括分页插件和自定义 ID 生成器的使用方法。

## 🛠️ 配置概览

### 1. 核心配置类

- **`domain-core/src/main/java/com/artframework/domain/core/config/MybatisConfiguration.java`** - 完整的 MyBatis 配置类（推荐用于生产环境）
- **`domain-sample/src/main/java/com/artframework/sample/config/SampleMybatisConfiguration.java`** - 简化的示例配置类

### 2. 工具类

- **`domain-core/src/main/java/com/artframework/domain/core/utils/PageUtils.java`** - 分页工具类

## 🔧 功能特性

### 1️⃣ 分页插件 (PaginationInnerInterceptor)

- ✅ **自动检测数据库类型**：支持 MySQL、PostgreSQL、Oracle、SQL Server 等
- ✅ **自动添加分页 SQL**：无需手动编写 LIMIT/OFFSET 语句
- ✅ **自动执行 COUNT 查询**：获取总记录数
- ✅ **防止超大分页**：默认最大限制 10000 条记录
- ✅ **分页溢出处理**：可配置超出总页数后的处理策略

```java
// 基本分页查询
Page<User> page = new Page<>(1, 10); // 第1页，每页10条
IPage<User> result = userMapper.selectPage(page, null);

// 带条件的分页查询
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.like(User::getName, "张");
IPage<User> result = userMapper.selectPage(page, wrapper);
```

### 2️⃣ 自定义 ID 生成器

#### 完整版 (SnowflakeIdentifierGenerator)
- ✅ **雪花算法**：基于时间戳的分布式 ID 生成
- ✅ **时钟回拨处理**：异常情况下的容错机制
- ✅ **数据中心ID**：基于 MAC 地址自动生成
- ✅ **机器ID**：基于进程 ID 和时间戳生成

#### 简化版 (SimpleIdentifierGenerator)
- ✅ **时间戳基础**：确保 ID 递增趋势
- ✅ **机器标识**：支持简单的分布式环境
- ✅ **序列号机制**：同一时间戳内保证唯一性
- ✅ **自动回填**：插入后实体对象自动包含生成的 ID

```java
// 实体类配置
@TableId(value = "id", type = IdType.ASSIGN_ID)
private Long id;

// 插入数据时自动生成ID
User user = new User();
user.setName("张三");
userMapper.insert(user); // ID自动生成并回填到user对象
```

### 3️⃣ 乐观锁插件 (OptimisticLockerInnerInterceptor)

- ✅ **自动处理 @Version 字段**：无需手动管理版本号
- ✅ **防止并发更新**：避免数据丢失问题

```java
@Version
private Integer version;

// 更新时会自动加上版本号条件
userMapper.updateById(user); // WHERE id = ? AND version = ?
```

### 4️⃣ 防全表操作插件 (BlockAttackInnerInterceptor)

- ✅ **防止无条件 UPDATE**：避免误操作更新全表
- ✅ **防止无条件 DELETE**：避免误操作删除全表
- ✅ **保护数据安全**：生产环境必备

### 5️⃣ 增强批量操作

- ✅ **批量插入**：支持主键回填和数据库特定优化
- ✅ **批量更新**：支持 ignore null 策略
- ✅ **逻辑删除过滤**：自动跳过逻辑删除字段

```java
// 批量插入
List<User> users = Arrays.asList(user1, user2, user3);
int count = userMapper.batchInsert(users); // 所有user对象的ID都会被回填

// 批量更新（ignore null策略）
int count = userMapper.batchUpdate(users); // null字段不会被更新
```

## 📍 测试端点

在 `domain-sample` 项目中提供了完整的测试端点：

### 分页功能测试
- **GET** `/family/v1/page?current=1&size=10` - 基本分页查询
- **GET** `/family/v1/page?current=1&size=5&name=张` - 带条件分页查询
- **GET** `/family/v1/test-pagination` - 分页插件功能演示

### ID生成器测试
- **GET** `/family/v1/test-id-generator` - ID 生成器功能测试

### 批量操作测试
- **POST** `/family/v1/batch-insert` - 批量插入测试
- **POST** `/family/v1/batch-update` - 批量更新测试

### 功能总览
- **GET** `/family/v1/config-overview` - 所有配置功能的总览说明

## 📋 使用建议

### 1. 分页查询最佳实践

```java
// ❌ 避免：直接使用大的分页参数
Page<User> page = new Page<>(1, 50000); // 可能被限制

// ✅ 推荐：合理的分页大小
Page<User> page = new Page<>(1, 20);

// ✅ 推荐：使用工具类
IPage<User> page = PageUtils.createPage(1, 20, 1000); // 带最大限制
```

### 2. ID生成器配置

```java
// ✅ 推荐：使用ASSIGN_ID让自定义生成器生效
@TableId(value = "id", type = IdType.ASSIGN_ID)
private Long id;

// ❌ 避免：使用AUTO时自定义生成器不会生效
@TableId(value = "id", type = IdType.AUTO)
private Long id;
```

### 3. 实体类配置

```java
@TableName("user")
public class User {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @TableField("name")
    private String name;
    
    @Version // 启用乐观锁
    private Integer version;
    
    @TableLogic // 逻辑删除字段会被批量操作自动过滤
    private Integer deleted;
}
```

### 4. 批量操作建议

```java
// ✅ 推荐：使用增强的批量方法
int count = mapper.batchInsert(users);    // 支持主键回填
int count = mapper.batchUpdate(users);    // 支持ignore null

// ❌ 避免：逐个操作
for (User user : users) {
    mapper.insert(user); // 性能较差
}
```

## 🔨 快速开始

### 1. 引入配置类

在你的 Spring Boot 项目中添加配置类：

```java
@Configuration
@Import(MybatisConfiguration.class) // 导入完整配置
public class YourConfig {
}
```

或者复制 `SampleMybatisConfiguration` 到你的项目中并根据需要调整。

### 2. 修改实体类

确保实体类使用正确的注解：

```java
@TableId(value = "id", type = IdType.ASSIGN_ID) // 启用自定义ID生成器
private Long id;
```

### 3. 使用分页功能

```java
@RestController
public class UserController {
    
    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/users")
    public IPage<User> getUsers(
        @RequestParam(defaultValue = "1") Long current,
        @RequestParam(defaultValue = "10") Long size) {
        
        Page<User> page = new Page<>(current, size);
        return userMapper.selectPage(page, null);
    }
}
```

### 4. 使用批量操作

确保你的 Mapper 继承了 `BatchBaseMapper`：

```java
@Mapper
public interface UserMapper extends BatchBaseMapper<User> {
    // 自动拥有 batchInsert 和 batchUpdate 方法
}
```

## ⚙️ 高级配置

### 自定义分页限制

```java
@Bean
public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    
    PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
    paginationInterceptor.setMaxLimit(5000L); // 自定义最大限制
    paginationInterceptor.setOverflow(true);   // 允许分页溢出
    
    interceptor.addInnerInterceptor(paginationInterceptor);
    return interceptor;
}
```

### 自定义ID生成器

```java
@Bean
public IdentifierGenerator customIdentifierGenerator() {
    return new IdentifierGenerator() {
        @Override
        public Long nextId(Object entity) {
            // 你的自定义ID生成逻辑
            return generateYourCustomId();
        }
    };
}
```

## 🎯 注意事项

1. **数据库兼容性**：分页插件会自动检测数据库类型，但建议在配置中明确指定
2. **ID生成器性能**：雪花算法适合高并发场景，简化版适合单机或低并发场景
3. **批量操作限制**：建议单次批量操作的记录数控制在 1000 条以内
4. **乐观锁字段**：使用 `@Version` 注解的字段会被自动管理，不要手动修改
5. **逻辑删除**：使用 `@TableLogic` 的字段在批量更新时会被自动过滤

## 🐛 常见问题

### Q: 分页插件不生效？
A: 检查是否正确配置了 `MybatisPlusInterceptor` Bean，并且使用了 `IPage` 参数。

### Q: ID生成器不工作？
A: 确保实体类的主键字段使用了 `IdType.ASSIGN_ID`，而不是 `IdType.AUTO`。

### Q: 批量操作方法不存在？
A: 确保 Mapper 接口继承了 `BatchBaseMapper` 而不是普通的 `BaseMapper`。

### Q: 乐观锁更新失败？
A: 检查实体对象的 version 字段是否有值，以及数据库中的版本号是否匹配。

---

🎉 **祝使用愉快！** 如有问题，请查看示例项目中的测试端点进行参考。 