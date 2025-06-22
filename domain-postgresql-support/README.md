# Domain PostgreSQL Support

PostgreSQL 数据库支持模块，提供 PostgreSQL 特有的增强批量操作实现。

## 功能特性

- **高性能批量插入**：基于 VALUES 语法的批量插入
- **主键自动回填**：支持 SERIAL/SEQUENCE 主键自动回填
- **Ignore Null 策略**：只更新非空字段，空字段使用默认值
- **逻辑删除支持**：自动排除逻辑删除字段
- **RETURNING 子句**：支持主键回填和结果返回
- **自动配置检测**：零配置自动检测 PostgreSQL 数据库

## 快速开始

### 1. 添加依赖

```xml
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
    url: jdbc:postgresql://localhost:5432/test
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
```

### 3. 创建 Mapper

```java
@Mapper
public interface UserMapper extends BatchBaseMapper<UserDO> {
    // 继承增强的批量操作方法
}
```

### 4. 使用增强批量操作

```java
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public void batchOperations(List<UserDO> users) {
        // 增强批量插入 - 支持主键回填
        userMapper.batchInsert(users);
        // 执行后，users 列表中的每个对象都会填充生成的主键值
        
        // 增强批量更新 - 支持 ignore null 策略
        userMapper.batchUpdate(users);
        // 只更新非空字段，空字段保持数据库原值不变
    }
}
```

## PostgreSQL 增强功能详解

### 1. 主键自动回填
```java
List<UserDO> users = Arrays.asList(
    new UserDO().setName("张三").setEmail("zhang@test.com"),
    new UserDO().setName("李四").setEmail("li@test.com")
);

// 批量插入后，主键会自动回填到原对象中
userMapper.batchInsert(users);

// 现在可以直接获取生成的主键
Long userId1 = users.get(0).getId();  // 自动生成的主键值
Long userId2 = users.get(1).getId();  // 自动生成的主键值
```

### 2. Ignore Null 策略
```java
List<UserDO> users = Arrays.asList(
    new UserDO().setId(1L).setName("新名字").setEmail(null),  // email为null
    new UserDO().setId(2L).setName(null).setAge(25)        // name为null
);

// 批量更新时，null字段不会被更新
userMapper.batchUpdate(users);
// 结果：第一条记录只更新name，email保持原值
//      第二条记录只更新age，name保持原值
```

### 3. 逻辑删除字段自动排除
```java
// 如果实体中有逻辑删除字段（如@TableLogic），会自动排除
@TableName("user")
public class UserDO {
    private Long id;
    private String name;
    
    @TableLogic
    private Integer deleted;  // 这个字段在批量操作中会被自动排除
}
```

### 4. SEQUENCE 主键支持
```java
// 支持 PostgreSQL 的 SEQUENCE 主键
@TableName("user")
public class UserDO {
    @TableId(type = IdType.INPUT)
    @KeySequence("user_id_seq")  // 指定序列名
    private Long id;
    
    // 批量插入时会自动使用 nextval('user_id_seq')
}
```

### 5. 高效的批量操作SQL
```sql
-- 批量插入生成的SQL（支持主键回填）
INSERT INTO user (name, email) VALUES 
('张三', 'zhang@test.com'),
('李四', 'li@test.com')
RETURNING id;

-- 批量更新生成的SQL（支持ignore null）
UPDATE user SET 
    name = CASE WHEN v.name IS NOT NULL THEN v.name ELSE user.name END,
    email = CASE WHEN v.email IS NOT NULL THEN v.email ELSE user.email END
FROM (VALUES 
    (1, '新名字', NULL),
    (2, NULL, 'new@test.com')
) AS v(id, name, email)
WHERE user.id = v.id;
```

## 性能优化建议

1. **合理的批量大小**：建议每批 1000-5000 条记录
2. **利用主键回填**：避免插入后再次查询主键
3. **使用 ignore null 策略**：减少不必要的字段更新
4. **预编译语句**：MyBatis Plus 自动使用预编译语句提升性能
5. **索引优化**：为批量更新的WHERE条件字段建立索引

## 配置选项

```yaml
# 自动配置，无需额外配置
# 系统会自动检测 PostgreSQL 数据库并启用相应功能
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/test
    # ... 其他配置
```

## 版本兼容性

- **PostgreSQL 9.5+**：完全支持所有增强功能
- **SEQUENCE 支持**：PostgreSQL 8.1+
- **RETURNING 子句**：PostgreSQL 8.2+
- **VALUES 语法**：PostgreSQL 8.2+ 